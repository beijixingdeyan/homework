const pool = require('../config/db');

// 获取用户
exports.getUser = async (userId) => {
    const [user] = await pool.query('SELECT * FROM users WHERE id = ?', [userId]);
    return user[0];
};

// 获取用户列表
exports.getUsers = async () => {
    const [users] = await pool.query('SELECT * FROM users');
    return users;
};

// 创建用户
exports.createUser = async (userData) => {
    const [result] = await pool.query(
        'INSERT INTO users (name, email, password) VALUES (?, ?, ?)',
        [userData.name, userData.email, userData.password]
    );
    return result;
};

// 更新用户
exports.updateUser = async (userId, userData) => {
    const [result] = await pool.query(
        'UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?',
        [userData.name, userData.email, userData.password, userId]
    );
    return result;
};

// 删除用户
exports.deleteUser = async (userId) => {
    const [result] = await pool.query('DELETE FROM users WHERE id = ?', [userId]);
    return result;
};