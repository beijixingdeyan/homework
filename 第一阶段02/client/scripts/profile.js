document.addEventListener('DOMContentLoaded', async function() {
    console.log('Profile 页面加载，检查 Token:', localStorage.getItem('token'));
    
    if (!API.isLoggedIn()) {
        console.log('未检测到 token，重定向到登录页');
        window.location.replace('login.html');
        return;
    }
    
    const urlParams = new URLSearchParams(window.location.search);
    const targetUserId = urlParams.get('userId');
    const currentUserId = API.getUserId();
    const userId = targetUserId || currentUserId;
    
    const profileName = document.querySelector('.profile-name');
    const profileBio = document.querySelector('.profile-bio');
    const postCount = document.getElementById('post-count');
    const followerCount = document.getElementById('follower-count');
    const followingCount = document.getElementById('following-count');
    const userPostsContainer = document.querySelector('.user-posts');
    const editProfileBtn = document.getElementById('edit-profile-btn');
    const followBtn = document.getElementById('follow-btn');
    const editProfileModal = document.getElementById('edit-profile-modal');
    const closeBtn = document.querySelector('.close');
    const editProfileForm = document.getElementById('edit-profile-form');
    const editAvatar = document.getElementById('edit-avatar');
    const previewContainer = document.querySelector('.preview-container');
    const avatarImg = document.querySelector('.profile-avatar');
    const logoutBtn = document.getElementById('logout-btn');

    if (!avatarImg) {
        console.error('未找到 .profile-avatar 元素');
    }

    try {
        console.log('获取用户信息，userId:', userId);
        const userResponse = await API.get(`users/${userId}`);
        console.log('用户信息响应:', userResponse);
        if (userResponse.success) {
            const user = userResponse.user;
            profileName.textContent = user.name;
            profileBio.textContent = user.bio || '该用户很神秘，什么也没有留下';
            postCount.textContent = user.postsCount || 0;
            followerCount.textContent = user.followersCount || 0;
            followingCount.textContent = user.followingCount || 0;
            if (avatarImg && user.avatar) avatarImg.src = user.avatar;
            
            editProfileBtn.style.display = (user.id === currentUserId) ? 'inline-block' : 'none';
            
            if (user.id !== currentUserId) {
                followBtn.style.display = 'inline-block';
                const followStatus = await API.get(`users/${userId}/follow-status`);
                console.log('关注状态:', followStatus);
                followBtn.textContent = followStatus.isFollowing ? '取消关注' : '关注';
                followBtn.dataset.following = followStatus.isFollowing ? 'true' : 'false';
            }
        } else {
            throw new Error(userResponse.message || '获取用户信息失败');
        }
    } catch (error) {
        console.error('获取用户信息失败:', error);
    }

    try {
        const postsResponse = await API.get(`posts/user/${userId}`);
        console.log('用户动态响应:', postsResponse);
        if (postsResponse.success) {
            renderUserPosts(postsResponse.posts);
        }
    } catch (error) {
        console.error('获取动态失败:', error);
    }

    editProfileBtn.addEventListener('click', function() {
        editProfileModal.style.display = 'block';
        document.getElementById('edit-name').value = profileName.textContent;
        document.getElementById('edit-bio').value = profileBio.textContent === '该用户很神秘，什么也没有留下' ? '' : profileBio.textContent;
    });

    closeBtn.addEventListener('click', function() {
        editProfileModal.style.display = 'none';
    });

    window.addEventListener('click', function(event) {
        if (event.target === editProfileModal) {
            editProfileModal.style.display = 'none';
        }
    });

    editAvatar.addEventListener('change', function() {
        const file = this.files[0];
        if (file && file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                img.alt = '预览图片';
                img.className = 'preview-image';
                previewContainer.innerHTML = '';
                previewContainer.appendChild(img);
            };
            reader.readAsDataURL(file);
        }
    });

    editProfileForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const name = document.getElementById('edit-name').value.trim();
        const bio = document.getElementById('edit-bio').value.trim();
        const avatar = document.getElementById('edit-avatar').files[0];
        
        if (!name) {
            alert('昵称不能为空');
            return;
        }
        
        try {
            const userData = { name, bio };
            if (avatar) {
                const formData = new FormData();
                formData.append('avatar', avatar);
                const avatarResponse = await API.post(`users/${currentUserId}/avatar`, formData, true);
                console.log('头像上传响应:', avatarResponse);
                if (avatarResponse.success) {
                    userData.avatar = avatarResponse.avatarUrl;
                } else {
                    throw new Error(avatarResponse.message || '头像上传失败');
                }
            }
            
            const response = await API.put(`users/${currentUserId}`, userData);
            console.log('资料更新响应:', response);
            if (response.success) {
                profileName.textContent = name;
                profileBio.textContent = bio || '该用户很神秘，什么也没有留下';
                if (userData.avatar && avatarImg) avatarImg.src = userData.avatar;
                editProfileModal.style.display = 'none';
                alert('个人资料更新成功');
            } else {
                throw new Error(response.message || '更新失败');
            }
        } catch (error) {
            console.error('更新个人资料失败:', error);
            alert(`更新失败：${error.message}`);
        }
    });

    followBtn.addEventListener('click', async function() {
        const isFollowing = followBtn.dataset.following === 'true';
        try {
            if (isFollowing) {
                await API.post(`users/${userId}/unfollow`, {});
                followBtn.textContent = '关注';
                followBtn.dataset.following = 'false';
                followerCount.textContent = parseInt(followerCount.textContent) - 1;
            } else {
                await API.post(`users/${userId}/follow`, {});
                followBtn.textContent = '取消关注';
                followBtn.dataset.following = 'true';
                followerCount.textContent = parseInt(followerCount.textContent) + 1;
            }
        } catch (error) {
            console.error('关注/取消关注失败:', error);
            alert(`操作失败：${error.message}`);
        }
    });

    const profileTabs = document.querySelectorAll('.profile-tab');
    profileTabs.forEach(tab => {
        tab.addEventListener('click', async function() {
            profileTabs.forEach(t => t.classList.remove('active'));
            this.classList.add('active');
            
            const tabType = this.dataset.tab;
            try {
                if (tabType === 'posts') {
                    const postsResponse = await API.get(`posts/user/${userId}`);
                    if (postsResponse.success) {
                        renderUserPosts(postsResponse.posts);
                    }
                } else if (tabType === 'photos') {
                    const likedPostsResponse = await API.get(`posts/liked/${userId}`);
                    if (likedPostsResponse.success) {
                        renderUserPosts(likedPostsResponse.posts);
                    }
                } else if (tabType === 'about') {
                    const commentedPostsResponse = await API.get(`posts/commented/${userId}`);
                    if (commentedPostsResponse.success) {
                        renderUserPosts(commentedPostsResponse.posts);
                    }
                }
            } catch (error) {
                console.error(`加载 ${tabType} 数据失败:`, error);
                userPostsContainer.innerHTML = `<p>加载失败：${error.message}</p>`;
            }
        });
    });

    logoutBtn.addEventListener('click', function() {
        console.log('退出登录按钮点击');
        API.removeToken();
        console.log('Token 已清除，重定向到登录页');
        window.location.replace('login.html');
    });

    function renderUserPosts(postsData) {
        userPostsContainer.innerHTML = '';
        if (postsData.length === 0) {
            userPostsContainer.innerHTML = '<p>暂无内容</p>';
            return;
        }
        postsData.forEach(post => {
            const postElement = document.createElement('section');
            postElement.className = 'post fade-in';
            postElement.dataset.id = post.id;
            
            let imagesHTML = post.images && post.images.length ? `
                <div class="post-images">
                    ${post.images.map(img => `<img src="${img}" alt="动态图片" class="post-image">`).join('')}
                </div>
            ` : '';
            
            postElement.innerHTML = `
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
            `;
            userPostsContainer.appendChild(postElement);
    
            const likeBtn = postElement.querySelector('.like-btn');
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
    
            const commentSubmitBtn = postElement.querySelector('.comment-submit-btn');
            const commentInput = postElement.querySelector('.comment-input');
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
                        const commentCountEl = postElement.querySelector('.comment-btn');
                        const currentCount = parseInt(commentCountEl.textContent.match(/\d+/)[0]);
                        commentCountEl.innerHTML = `<i class="icon-comment">💬</i> 评论 (${currentCount + 1})`;
                        loadComments(post.id, postElement.querySelector('.comment-window'));
                    } else {
                        throw new Error(response.message || '评论失败');
                    }
                } catch (error) {
                    console.error('评论失败:', error);
                    alert(`评论失败：${error.message}`);
                }
            });
    
            const commentBtn = postElement.querySelector('.comment-btn');
            const commentWindow = postElement.querySelector('.comment-window');
            commentBtn.addEventListener('click', async function() {
                if (commentWindow.style.display === 'none' || commentWindow.style.display === '') {
                    loadComments(post.id, commentWindow);
                    commentWindow.style.display = 'block';
                } else {
                    commentWindow.style.display = 'none';
                }
            });
    
            // 使用事件委托绑定关闭按钮
            const commentsContainer = postElement.querySelector('.comments');
            commentsContainer.addEventListener('click', function(event) {
                if (event.target.classList.contains('comment-window-close')) {
                    console.log('关闭评论悬浮窗，postId:', post.id);
                    commentWindow.style.display = 'none';
                }
            });
    
            const shareBtn = postElement.querySelector('.share-btn');
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
});