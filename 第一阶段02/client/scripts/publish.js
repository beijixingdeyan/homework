document.addEventListener('DOMContentLoaded', async function() {
    console.log('Publish 页面加载，检查 Token:', localStorage.getItem('token'));
    
    if (!API.isLoggedIn()) {
        console.log('未检测到 token，重定向到登录页');
        window.location.replace('login.html');
        return;
    }
    
    const postForm = document.getElementById('post-form');
    const postContent = document.getElementById('post-content');
    const postImages = document.getElementById('post-images');
    const postImagesPreview = document.querySelector('.preview-container');
    
    postForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        if (postContent.value.trim() === '') {
            alert('内容不能为空');
            return;
        }
        
        const files = postImages.files;
        if (files.length > 3) {
            alert('最多上传 3 张图片');
            return;
        }
        
        const formData = new FormData();
        formData.append('content', postContent.value);
        for (let i = 0; i < files.length; i++) {
            formData.append('images', files[i]);
        }
        
        try {
            console.log('发布动态请求: FormData'); // 无法直接打印 FormData 内容
            const response = await API.post('posts', formData, true); // true 表示文件上传
            console.log('发布动态响应:', response);
            if (response.success) {
                alert('发布成功！');
                window.location.replace('index.html');
            } else {
                throw new Error(response.message || '发布失败');
            }
        } catch (error) {
            console.error('发布动态失败:', error);
            alert(`发布失败：${error.message}`);
        }
    });
    
    postImages.addEventListener('change', function() {
        const files = this.files;
        if (files.length > 3) {
            alert('最多上传 3 张图片');
            this.value = '';
            return;
        }
        
        postImagesPreview.innerHTML = '';
        for (let i = 0; i < files.length; i++) {
            if (files[i].type.startsWith('image/')) {
                const img = document.createElement('img');
                img.src = URL.createObjectURL(files[i]);
                img.alt = '预览图片';
                img.className = 'preview-image';
                postImagesPreview.appendChild(img);
            }
        }
    });
});