document.addEventListener('DOMContentLoaded', function() {
    const API = {
        post: async (endpoint, data) => {
            const response = await fetch(`http://localhost:5000/api/${endpoint}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            const result = await response.json();
            if (!response.ok) throw new Error(result.message || '请求失败');
            return result;
        },
        isLoggedIn: () => !!localStorage.getItem('token')
    };

    // 注册表单处理
    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const name = document.getElementById('name').value.trim();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirm-password').value;
            
            if (!name || !email || !password || !confirmPassword) {
                alert('所有字段都是必填的');
                return;
            }
            if (password !== confirmPassword) {
                alert('密码和确认密码不一致');
                return;
            }
            if (password.length < 8) {
                alert('密码长度至少8位');
                return;
            }
            
            try {
                const response = await API.post('auth/register', { name, email, password });
                if (response.success) {
                    alert('注册成功！请登录');
                    window.location.replace('login.html'); // 使用 replace 避免历史记录堆叠
                } else {
                    alert(response.message || '注册失败');
                }
            } catch (error) {
                alert(`注册失败：${error.message}`);
            }
        });
    }
    
    // 登录表单处理
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;
            
            if (!email || !password) {
                alert('请输入邮箱和密码');
                return;
            }
            
            try {
                const response = await API.post('auth/login', { email, password });
                console.log('登录响应:', response);
                if (response.success && response.token) {
                    localStorage.setItem('token', response.token);
                    console.log('Token 已存储:', localStorage.getItem('token'));
                    window.location.replace('index.html'); // 使用 replace 确保跳转
                } else {
                    alert(response.message || '登录失败');
                }
            } catch (error) {
                alert(`登录失败：${error.message}`);
            }
        });
    }
    
    // 退出登录
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function() {
            localStorage.removeItem('token');
            window.location.replace('login.html');
        });
    }

    // 只在非登录/注册页面检查登录状态
    const currentPage = window.location.pathname.split('/').pop() || 'index.html';
    if (!['login.html', 'register.html'].includes(currentPage) && !API.isLoggedIn()) {
        console.log('未登录，重定向到登录页面');
        window.location.replace('login.html');
    }
});