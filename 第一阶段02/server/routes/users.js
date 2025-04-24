const express = require('express');
const router = express.Router();
const pool = require('../config/db');
const multer = require('multer');
const path = require('path');
const auth = require('../middleware/auth');

router.use(auth);

const storage = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, 'uploads/avatars');
    },
    filename: function(req, file, cb) {
        cb(null, Date.now() + path.extname(file.originalname));
    }
});
const upload = multer({
    storage: storage,
    fileFilter: (req, file, cb) => {
        if (!file.mimetype.startsWith('image/')) {
            return cb(new Error('只允许上传图片'));
        }
        cb(null, true);
    }
});

router.get('/:userId', async (req, res) => {
    try {
        const [user] = await pool.query('SELECT * FROM users WHERE id = ?', [req.params.userId]);
        if (!user.length) {
            return res.status(404).json({ success: false, message: '用户不存在' });
        }
        
        const [postsCount] = await pool.query('SELECT COUNT(*) AS count FROM posts WHERE user_id = ?', [req.params.userId]);
        const [followersCount] = await pool.query('SELECT COUNT(*) AS count FROM user_follows WHERE followed_user_id = ?', [req.params.userId]);
        const [followingCount] = await pool.query('SELECT COUNT(*) AS count FROM user_follows WHERE follower_user_id = ?', [req.params.userId]);
        
        res.json({
            success: true,
            user: {
                ...user[0],
                postsCount: postsCount[0].count,
                followersCount: followersCount[0].count,
                followingCount: followingCount[0].count
            }
        });
    } catch (error) {
        console.error('获取用户信息失败:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.put('/:userId', async (req, res) => {
    try {
        const { name, bio, avatar } = req.body;
        if (!name) {
            return res.status(400).json({ success: false, message: '昵称不能为空' });
        }
        if (req.user.id !== parseInt(req.params.userId)) {
            return res.status(403).json({ success: false, message: '无权限修改他人资料' });
        }
        
        await pool.query(
            'UPDATE users SET name = ?, bio = ?, avatar = ? WHERE id = ?',
            [name, bio, avatar || null, req.params.userId]
        );
        res.json({ success: true, message: '个人资料更新成功' });
    } catch (error) {
        console.error('更新个人资料失败:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.post('/:userId/avatar', upload.single('avatar'), async (req, res) => {
    try {
        if (!req.file) {
            return res.status(400).json({ success: false, message: '没有上传文件' });
        }
        if (req.user.id !== parseInt(req.params.userId)) {
            return res.status(403).json({ success: false, message: '无权限修改他人头像' });
        }
        
        const avatarUrl = `/uploads/avatars/${req.file.filename}`;
        await pool.query(
            'UPDATE users SET avatar = ? WHERE id = ?',
            [avatarUrl, req.params.userId]
        );
        console.log('头像更新成功，URL:', avatarUrl);
        res.json({ success: true, avatarUrl });
    } catch (error) {
        console.error('上传头像失败:', error);
        res.status(500).json({ success: false, message: '服务器错误: ' + error.message });
    }
});

router.post('/:userId/follow', async (req, res) => {
    try {
        const followerId = req.user.id;
        const followedId = parseInt(req.params.userId);
        
        if (followerId === followedId) {
            return res.status(400).json({ success: false, message: '不能关注自己' });
        }
        
        const [existing] = await pool.query(
            'SELECT * FROM user_follows WHERE follower_user_id = ? AND followed_user_id = ?',
            [followerId, followedId]
        );
        if (existing.length > 0) {
            return res.status(400).json({ success: false, message: '已关注该用户' });
        }
        
        await pool.query(
            'INSERT INTO user_follows (follower_user_id, followed_user_id) VALUES (?, ?)',
            [followerId, followedId]
        );
        res.json({ success: true, message: '关注成功' });
    } catch (error) {
        console.error('关注用户失败:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.post('/:userId/unfollow', async (req, res) => {
    try {
        const followerId = req.user.id;
        const followedId = parseInt(req.params.userId);
        
        const [existing] = await pool.query(
            'SELECT * FROM user_follows WHERE follower_user_id = ? AND followed_user_id = ?',
            [followerId, followedId]
        );
        if (!existing.length) {
            return res.status(400).json({ success: false, message: '未关注该用户' });
        }
        
        await pool.query(
            'DELETE FROM user_follows WHERE follower_user_id = ? AND followed_user_id = ?',
            [followerId, followedId]
        );
        res.json({ success: true, message: '取消关注成功' });
    } catch (error) {
        console.error('取消关注失败:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.get('/:userId/follow-status', async (req, res) => {
    try {
        const followerId = req.user.id;
        const followedId = parseInt(req.params.userId);
        
        const [existing] = await pool.query(
            'SELECT * FROM user_follows WHERE follower_user_id = ? AND followed_user_id = ?',
            [followerId, followedId]
        );
        res.json({ success: true, isFollowing: existing.length > 0 });
    } catch (error) {
        console.error('检查关注状态失败:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

module.exports = router;