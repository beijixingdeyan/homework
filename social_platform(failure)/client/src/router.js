import { createRouter, createWebHistory } from 'vue-router'
import Home from './views/Home.vue'
import Profile from './views/Profile.vue'
import Post from './views/Post.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'

const routes = [
    { path: '/', component: Home },
    { path: '/profile/:userId', component: Profile },
    { path: '/post/:postId', component: Post },
    { path: '/login', component: Login },
    { path: '/register', component: Register }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.path !== '/login' && to.path !== '/register' && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router