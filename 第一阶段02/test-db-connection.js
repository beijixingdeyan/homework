const mysql = require('mysql2/promise');

async function testDatabaseConnection() {
    const pool = mysql.createPool({
        host: 'localhost', // 数据库主机名
        user: 'root', // 数据库用户名
        password: 'qweasd123456', // 数据库密码
        database: 'social_app', // 数据库名称
        waitForConnections: true,
        connectionLimit: 10,
        queueLimit: 0
    });

    try {
        // 获取数据库连接
        const connection = await pool.getConnection();
        console.log('数据库连接成功！');
        // 释放连接
        connection.release();
    } catch (error) {
        console.error('数据库连接失败:', error);
    }
}

testDatabaseConnection();