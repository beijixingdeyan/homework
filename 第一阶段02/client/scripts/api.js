const API = {
    isLoggedIn: () => {
        const token = localStorage.getItem('token');
        console.log('API.isLoggedIn 检查 Token:', token);
        return !!token;
    },
    setToken: (token) => localStorage.setItem('token', token),
    getToken: () => localStorage.getItem('token'),
    removeToken: () => localStorage.removeItem('token'),
    
    getUserId: () => {
        const token = API.getToken();
        if (!token) return null;
        try {
            const payload = token.split('.')[1];
            const decoded = atob(payload);
            const userData = JSON.parse(decoded);
            return userData.user.id;
        } catch (error) {
            console.error('解析 Token 失败:', error);
            return null;
        }
    },
    
    get: async (endpoint) => {
        const token = API.getToken();
        console.log('GET 请求:', endpoint, 'Token:', token);
        const response = await fetch(`http://localhost:5000/api/${endpoint}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        const data = await response.json();
        console.log('GET 响应:', data);
        if (!response.ok) {
            if (response.status === 401) {
                console.log('401 未授权，重定向到登录页');
                API.removeToken();
                window.location.replace('login.html');
            }
            throw new Error(data.message || `HTTP error! status: ${response.status}`);
        }
        return data;
    },
    
    post: async (endpoint, data, isFile = false) => {
        const token = API.getToken();
        console.log('POST 请求:', endpoint, '数据:', data, 'Token:', token);
        const headers = { 'Authorization': `Bearer ${token}` };
        let body;
        if (isFile) {
            body = data;
        } else {
            headers['Content-Type'] = 'application/json';
            body = JSON.stringify(data);
        }
        
        const response = await fetch(`http://localhost:5000/api/${endpoint}`, {
            method: 'POST',
            headers,
            body
        });
        const result = await response.json();
        console.log('POST 响应:', result);
        if (!response.ok) {
            if (response.status === 401) {
                console.log('401 未授权，重定向到登录页');
                API.removeToken();
                window.location.replace('login.html');
            }
            throw new Error(result.message || `HTTP error! status: ${response.status}`);
        }
        return result;
    },
    
    put: async (endpoint, data) => {
        const token = API.getToken();
        console.log('PUT 请求:', endpoint, '数据:', data, 'Token:', token);
        const response = await fetch(`http://localhost:5000/api/${endpoint}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(data)
        });
        const result = await response.json();
        console.log('PUT 响应:', result);
        if (!response.ok) {
            if (response.status === 401) {
                console.log('401 未授权，重定向到登录页');
                API.removeToken();
                window.location.replace('login.html');
            }
            throw new Error(result.message || `HTTP error! status: ${response.status}`);
        }
        return result;
    },
    
    delete: async (endpoint) => {
        const token = API.getToken();
        console.log('DELETE 请求:', endpoint, 'Token:', token);
        const response = await fetch(`http://localhost:5000/api/${endpoint}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        const result = await response.json();
        console.log('DELETE 响应:', result);
        if (!response.ok) {
            if (response.status === 401) {
                console.log('401 未授权，重定向到登录页');
                API.removeToken();
                window.location.replace('login.html');
            }
            throw new Error(result.message || `HTTP error! status: ${response.status}`);
        }
        return result;
    }
};