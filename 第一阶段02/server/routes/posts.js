const express = require('express');
const router = express.Router();
const pool = require('../config/db');
const auth = require('../middleware/auth');
const postModel = require('../models/Post');
const multer = require('multer');
const path = require('path');

router.use(auth);

const storage = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, 'uploads/posts');
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
}).fields([
    { name: 'images', maxCount: 3 },
    { name: 'content', maxCount: 1 }
]);

router.get('/', async (req, res) => {
    try {
        const posts = await postModel.getPosts();
        res.json({ success: true, posts });
    } catch (error) {
        console.error('获取动态错误:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.get('/user/:userId', async (req, res) => {
    try {
        const posts = await postModel.getUserPosts(req.params.userId);
        res.json({ success: true, posts });
    } catch (error) {
        console.error('获取用户动态错误:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.get('/:postId', async (req, res) => {
    try {
        const [posts] = await pool.query(
            `SELECT p.*, u.name, u.avatar,
             (SELECT COUNT(*) FROM post_likes pl WHERE pl.post_id = p.id) AS likes_count,
             (SELECT COUNT(*) FROM post_comments pc WHERE pc.post_id = p.id) AS comments_count
             FROM posts p JOIN users u ON p.user_id = u.id WHERE p.id = ?`,
            [req.params.postId]
        );
        if (posts.length === 0) {
            return res.status(404).json({ success: false, message: '动态不存在' });
        }
        posts[0].images = posts[0].images ? JSON.parse(posts[0].images) : [];
        res.json({ success: true, post: posts[0] });
    } catch (error) {
        console.error('获取单个动态错误:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.post('/', upload, async (req, res) => {
    try {
        const { content } = req.body;
        const files = req.files && req.files['images'] ? req.files['images'] : [];
        
        console.log('收到请求数据:', { content, files });
        if (!content) {
            return res.status(400).json({ success: false, message: '内容不能为空' });
        }
        
        const imagePaths = files.map(file => `/uploads/posts/${file.filename}`) || [];
        const result = await postModel.createPost({ 
            user_id: req.user.id, 
            content, 
            images: imagePaths.length ? JSON.stringify(imagePaths) : null 
        });
        
        const [newPost] = await pool.query(
            `SELECT p.*, u.name, u.avatar,
             (SELECT COUNT(*) FROM post_likes pl WHERE pl.post_id = p.id) AS likes_count,
             (SELECT COUNT(*) FROM post_comments pc WHERE pc.post_id = p.id) AS comments_count
             FROM posts p JOIN users u ON p.user_id = u.id WHERE p.id = ?`,
            [result.insertId]
        );
        
        console.log('数据库返回数据:', newPost[0]);
        newPost[0].images = newPost[0].images ? JSON.parse(newPost[0].images) : [];
        res.json({ success: true, post: newPost[0] });
    } catch (error) {
        console.error('发布动态错误:', error);
        res.status(500).json({ success: false, message: '服务器错误: ' + error.message });
    }
});

router.post('/:postId/like', async (req, res) => {
    try {
        const [post] = await pool.query('SELECT * FROM posts WHERE id = ?', [req.params.postId]);
        if (!post.length) {
            return res.status(404).json({ success: false, message: '动态不存在' });
        }
        
        const [existingLikes] = await pool.query(
            'SELECT * FROM post_likes WHERE post_id = ? AND user_id = ?',
            [req.params.postId, req.user.id]
        );
        if (existingLikes.length > 0) {
            await postModel.unlikePost(req.params.postId, req.user.id);
            const likesCount = (await pool.query('SELECT COUNT(*) AS count FROM post_likes WHERE post_id = ?', [req.params.postId]))[0][0].count;
            console.log(`取消点赞后，postId: ${req.params.postId}, likesCount: ${likesCount}`);
            res.json({ success: true, liked: false, likesCount });
        } else {
            await postModel.likePost(req.params.postId, req.user.id);
            const likesCount = (await pool.query('SELECT COUNT(*) AS count FROM post_likes WHERE post_id = ?', [req.params.postId]))[0][0].count;
            console.log(`点赞后，postId: ${req.params.postId}, likesCount: ${likesCount}`);
            res.json({ success: true, liked: true, likesCount });
        }
    } catch (error) {
        console.error('点赞错误:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.post('/:postId/comment', async (req, res) => {
    try {
        const { text } = req.body;
        if (!text) {
            return res.status(400).json({ success: false, message: '评论内容不能为空' });
        }
        
        const [post] = await pool.query('SELECT * FROM posts WHERE id = ?', [req.params.postId]);
        if (!post.length) {
            return res.status(404).json({ success: false, message: '动态不存在' });
        }
        
        await postModel.commentPost(req.params.postId, req.user.id, text);
        const commentsCount = (await pool.query('SELECT COUNT(*) AS count FROM post_comments WHERE post_id = ?', [req.params.postId]))[0][0].count;
        res.json({ success: true, commentsCount });
    } catch (error) {
        console.error('评论错误:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.get('/:postId/comments', async (req, res) => {
    try {
        const comments = await postModel.getComments(req.params.postId);
        res.json({ success: true, comments });
    } catch (error) {
        console.error('获取评论错误:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.get('/liked/:userId', async (req, res) => {
    try {
        const posts = await postModel.getLikedPosts(req.params.userId);
        res.json({ success: true, posts });
    } catch (error) {
        console.error('获取点赞帖子错误:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

router.get('/commented/:userId', async (req, res) => {
    try {
        const posts = await postModel.getCommentedPosts(req.params.userId);
        res.json({ success: true, posts });
    } catch (error) {
        console.error('获取评论帖子错误:', error);
        res.status(500).json({ success: false, message: '服务器错误' });
    }
});

module.exports = router;