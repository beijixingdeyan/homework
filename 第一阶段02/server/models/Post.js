const pool = require('../config/db');

exports.getPosts = async () => {
    const [posts] = await pool.query(
        `SELECT p.*, u.name, u.avatar,
         (SELECT COUNT(*) FROM post_likes pl WHERE pl.post_id = p.id) AS likes_count,
         (SELECT COUNT(*) FROM post_comments pc WHERE pc.post_id = p.id) AS comments_count
         FROM posts p JOIN users u ON p.user_id = u.id ORDER BY p.created_at DESC`
    );
    posts.forEach(post => {
        post.images = post.images ? JSON.parse(post.images) : [];
    });
    return posts;
};

exports.getUserPosts = async (userId) => {
    const [posts] = await pool.query(
        `SELECT p.*, u.name, u.avatar,
         (SELECT COUNT(*) FROM post_likes pl WHERE pl.post_id = p.id) AS likes_count,
         (SELECT COUNT(*) FROM post_comments pc WHERE pc.post_id = p.id) AS comments_count
         FROM posts p JOIN users u ON p.user_id = u.id WHERE p.user_id = ? ORDER BY p.created_at DESC`,
        [userId]
    );
    posts.forEach(post => {
        post.images = post.images ? JSON.parse(post.images) : [];
    });
    return posts;
};

exports.createPost = async (postData) => {
    console.log('createPost 接收的数据:', postData);
    const images = postData.images ? JSON.stringify(postData.images) : null;
    const [result] = await pool.query(
        'INSERT INTO posts (user_id, content, images) VALUES (?, ?, ?)',
        [postData.user_id, postData.content, images]
    );
    return result;
};

exports.updatePost = async (postId, postData) => {
    const images = postData.images ? JSON.stringify(postData.images) : null;
    const [result] = await pool.query(
        'UPDATE posts SET content = ?, images = ? WHERE id = ?',
        [postData.content, images, postId]
    );
    return result;
};

exports.deletePost = async (postId, userId) => {
    const [result] = await pool.query(
        'DELETE FROM posts WHERE id = ? AND user_id = ?',
        [postId, userId]
    );
    return result;
};

exports.likePost = async (postId, userId) => {
    const [result] = await pool.query(
        'INSERT INTO post_likes (post_id, user_id) VALUES (?, ?)',
        [postId, userId]
    );
    return result;
};

exports.unlikePost = async (postId, userId) => {
    const [result] = await pool.query(
        'DELETE FROM post_likes WHERE post_id = ? AND user_id = ?',
        [postId, userId]
    );
    return result;
};

exports.commentPost = async (postId, userId, commentText) => {
    const [result] = await pool.query(
        'INSERT INTO post_comments (post_id, user_id, text) VALUES (?, ?, ?)',
        [postId, userId, commentText]
    );
    return result;
};

exports.getComments = async (postId) => {
    const [comments] = await pool.query(
        `SELECT pc.*, u.name, u.avatar 
         FROM post_comments pc 
         JOIN users u ON pc.user_id = u.id 
         WHERE pc.post_id = ? 
         ORDER BY pc.created_at ASC`,
        [postId]
    );
    return comments;
};

exports.getLikedPosts = async (userId) => {
    const [posts] = await pool.query(
        `SELECT p.*, u.name, u.avatar,
         (SELECT COUNT(*) FROM post_likes pl WHERE pl.post_id = p.id) AS likes_count,
         (SELECT COUNT(*) FROM post_comments pc WHERE pc.post_id = p.id) AS comments_count
         FROM posts p 
         JOIN users u ON p.user_id = u.id 
         JOIN post_likes pl ON p.id = pl.post_id 
         WHERE pl.user_id = ? 
         ORDER BY pl.created_at DESC`,
        [userId]
    );
    posts.forEach(post => {
        post.images = post.images ? JSON.parse(post.images) : [];
    });
    return posts;
};

exports.getCommentedPosts = async (userId) => {
    const [posts] = await pool.query(
        `SELECT p.*, u.name, u.avatar, 
         (SELECT COUNT(*) FROM post_likes pl WHERE pl.post_id = p.id) AS likes_count,
         (SELECT COUNT(*) FROM post_comments pc2 WHERE pc2.post_id = p.id) AS comments_count,
         (SELECT MAX(pc.created_at) FROM post_comments pc WHERE pc.post_id = p.id AND pc.user_id = ?) AS latest_comment_at
         FROM posts p 
         JOIN users u ON p.user_id = u.id 
         JOIN post_comments pc ON p.id = pc.post_id 
         WHERE pc.user_id = ? 
         GROUP BY p.id
         ORDER BY latest_comment_at DESC`,
        [userId, userId]
    );
    posts.forEach(post => {
        post.images = post.images ? JSON.parse(post.images) : [];
    });
    return posts;
};