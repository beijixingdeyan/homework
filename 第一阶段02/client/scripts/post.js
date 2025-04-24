document.addEventListener('DOMContentLoaded', async function() {
    console.log('Post 页面加载，检查 Token:', localStorage.getItem('token'));
    
    if (!API.isLoggedIn()) {
        console.log('未检测到 token，重定向到登录页');
        window.location.replace('login.html');
        return;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const postId = urlParams.get('postId');
    
    if (!postId) {
        document.querySelector('.post-container').innerHTML = '<p>无效的动态 ID</p>';
        return;
    }

    async function loadPost() {
        try {
            const response = await API.get(`posts/${postId}`);
            console.log('动态响应:', response);
            if (response.success && response.post) {
                renderPost(response.post);
            } else {
                throw new Error(response.message || '获取动态失败');
            }
        } catch (error) {
            console.error('获取动态失败:', error);
            document.querySelector('.post-container').innerHTML = `<p>加载失败：${error.message}</p>`;
        }
    }

    function renderPost(post) {
        const postContainer = document.querySelector('.post-container');
        let imagesHTML = post.images && post.images.length ? `
            <div class="post-images">
                ${post.images.map(img => `<img src="${img}" alt="动态图片" class="post-image">`).join('')}
            </div>
        ` : '';
    
        postContainer.innerHTML = `
            <section class="post fade-in" data-id="${post.id}">
                <div class="post-header">
                    <img src="${post.avatar || 'assets/images/default-avatar.jpg'}" alt="用户头像" class="profile-avatar">
                    <div class="post-info">
                        <a href="profile.html?userId=${post.user_id}" class="username">${post.name}</a>
                        <span class="post-time">${formatTime(post.created_at)}</span>
                    </div>
                </div>
                <p class="post-content">${post.content}</p>
                ${imagesHTML}
                <div class="post-actions">
                    <button class="like-btn"><i class="icon-like">❤</i> 喜欢 (${post.likes_count || 0})</button>
                    <button class="comment-btn"><i class="icon-comment">💬</i> 评论 (${post.comments_count || 0})</button>
                    <button class="share-btn"><i class="icon-share">🔄</i> 分享</button>
                </div>
                <div class="comments">
                    <div class="comment-window">
                        <button class="comment-window-close">×</button>
                    </div>
                    <div class="comment-input-container">
                        <input type="text" class="comment-input" placeholder="写下你的评论...">
                        <button class="comment-submit-btn">发布</button>
                    </div>
                </div>
            </section>
        `;
    
        const likeBtn = postContainer.querySelector('.like-btn');
        likeBtn.addEventListener('click', async function() {
            try {
                const response = await API.post(`posts/${post.id}/like`, {});
                console.log('点赞响应:', response);
                if (response.success) {
                    likeBtn.innerHTML = `<i class="icon-like">❤</i> 喜欢 (${response.likesCount})`;
                    likeBtn.classList.toggle('liked', response.liked);
                } else {
                    throw new Error(response.message || '点赞失败');
                }
            } catch (error) {
                console.error('点赞失败:', error);
                alert(`点赞失败：${error.message}`);
            }
        });
    
        const commentSubmitBtn = postContainer.querySelector('.comment-submit-btn');
        const commentInput = postContainer.querySelector('.comment-input');
        commentSubmitBtn.addEventListener('click', async function() {
            const text = commentInput.value.trim();
            if (!text) {
                alert('评论内容不能为空');
                return;
            }
            try {
                const response = await API.post(`posts/${post.id}/comment`, { text });
                console.log('评论响应:', response);
                if (response.success) {
                    commentInput.value = '';
                    const commentCountEl = postContainer.querySelector('.comment-btn');
                    commentCountEl.innerHTML = `<i class="icon-comment">💬</i> 评论 (${response.commentsCount})`;
                    loadComments(post.id, postContainer.querySelector('.comment-window'));
                } else {
                    throw new Error(response.message || '评论失败');
                }
            } catch (error) {
                console.error('评论失败:', error);
                alert(`评论失败：${error.message}`);
            }
        });
    
        const commentBtn = postContainer.querySelector('.comment-btn');
        const commentWindow = postContainer.querySelector('.comment-window');
        commentBtn.addEventListener('click', async function() {
            if (commentWindow.style.display === 'none' || commentWindow.style.display === '') {
                loadComments(post.id, commentWindow);
                commentWindow.style.display = 'block';
            } else {
                commentWindow.style.display = 'none';
            }
        });
    
        // 使用事件委托绑定关闭按钮
        const commentsContainer = postContainer.querySelector('.comments');
        commentsContainer.addEventListener('click', function(event) {
            if (event.target.classList.contains('comment-window-close')) {
                console.log('关闭评论悬浮窗，postId:', post.id);
                commentWindow.style.display = 'none';
            }
        });
    
        const shareBtn = postContainer.querySelector('.share-btn');
        shareBtn.addEventListener('click', async function() {
            const shareUrl = `${window.location.origin}/post.html?postId=${post.id}`;
            try {
                if (navigator.share) {
                    await navigator.share({
                        title: `来自 ${post.name} 的动态`,
                        text: post.content,
                        url: shareUrl
                    });
                    console.log('分享成功');
                } else {
                    await navigator.clipboard.writeText(shareUrl);
                    alert('帖子链接已复制到剪贴板！');
                }
            } catch (error) {
                console.error('分享失败:', error);
                try {
                    await navigator.clipboard.writeText(shareUrl);
                    alert('帖子链接已复制到剪贴板！');
                } catch (copyError) {
                    console.error('复制链接失败:', copyError);
                    alert('分享失败，请手动复制链接：' + shareUrl);
                }
            }
        });
    }
    
    async function loadComments(postId, commentWindowEl) {
        try {
            const response = await API.get(`posts/${postId}/comments`);
            console.log('评论列表响应:', response);
            if (response.success) {
                commentWindowEl.innerHTML = `
                    <button class="comment-window-close">×</button>
                    <div class="comment-list">
                        ${response.comments.map(comment => `
                            <div class="comment-item">
                                <img src="${comment.avatar || 'assets/images/default-avatar.jpg'}" alt="评论者头像" class="comment-avatar">
                                <div class="comment-details">
                                    <span class="comment-username">${comment.name}</span>
                                    <p class="comment-text">${comment.text}</p>
                                    <span class="comment-time">${formatTime(comment.created_at)}</span>
                                </div>
                            </div>
                        `).join('')}
                    </div>
                `;
            } else {
                throw new Error(response.message || '获取评论失败');
            }
        } catch (error) {
            console.error('获取评论失败:', error);
            commentWindowEl.innerHTML = `
                <button class="comment-window-close">×</button>
                <p>加载评论失败：${error.message}</p>
            `;
        }
    }

    function formatTime(timestamp) {
        const now = new Date();
        const postTime = new Date(timestamp);
        const diff = now - postTime;
        const minute = 60 * 1000;
        const hour = 60 * minute;
        const day = 24 * hour;
        
        if (diff < minute) return '刚刚';
        if (diff < hour) return Math.floor(diff / minute) + '分钟前';
        if (diff < day) return Math.floor(diff / hour) + '小时前';
        return Math.floor(diff / day) + '天前';
    }

    loadPost();
});