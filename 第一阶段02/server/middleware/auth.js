const jwt = require('jsonwebtoken');
const pool = require('../config/db');

module.exports = async (req, res, next) => {
    const token = req.header('Authorization');
    if (!token) {
        return res.status(401).json({ success: false, message: '未授权，请登录' });
    }
    
    try {
        if (!process.env.JWT_SECRET) {
            throw new Error('JWT_SECRET 未定义');
        }
        const decoded = jwt.verify(token.split(' ')[1], process.env.JWT_SECRET);
        console.log('Token 解码:', decoded); // 调试
        if (typeof pool.query !== 'function') {
            throw new Error('数据库连接池未正确初始化');
        }
        const [user] = await pool.query('SELECT * FROM users WHERE id = ?', [decoded.user.id]);
        console.log('查询用户结果:', user); // 调试
        if (!user.length) {
            return res.status(401).json({ success: false, message: '用户未找到' });
        }
        req.user = user[0];
        next();
    } catch (error) {
        console.error('认证错误:', error);
        if (error.name === 'TokenExpiredError') {
            return res.status(401).json({ success: false, message: '令牌已过期，请重新登录' });
        }
        res.status(401).json({ success: false, message: '令牌无效: ' + error.message });
    }
};