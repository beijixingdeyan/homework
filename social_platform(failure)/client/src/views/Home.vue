<template>
  <div class="feed-container">
    <header>
      <nav>
        <div class="logo">社交平台</div>
        <ul class="nav-links">
          <li><router-link to="/" class="active">首页</router-link></li>
          <li><router-link to="/profile/me">个人主页</router-link></li>
        </ul>
        <el-button @click="logout">退出登录</el-button>
      </nav>
    </header>
    <el-form class="post-form" @submit.prevent="createPost">
      <el-input v-model="newPost.content" placeholder="发布动态..." type="textarea" rows="3"></el-input>
      <el-upload
          :file-list="newPost.images"
          :on-change="handleImageChange"
          :auto-upload="false"
          multiple
          accept="image/*">
        <el-button>上传图片</el-button>
      </el-upload>
      <el-button type="primary" native-type="submit">发布</el-button>
    </el-form>
    <post-item
        v-for="post in posts"
        :key="post.id"
        :post="post"
        @update:post="updatePost"/>
  </div>
</template>

<script>
import api from '../api'
import PostItem from '../components/PostItem.vue'

export default {
  components: { PostItem },
  data() {
    return {
      posts: [],
      newPost: {
        content: '',
        images: []
      }
    }
  },
  async created() {
    await this.loadPosts()
  },
  methods: {
    async loadPosts() {
      try {
        const response = await api.get('/posts')
        if (response.data.success) {
          this.posts = response.data.posts
        }
      } catch (error) {
        this.$message.error('加载动态失败')
      }
    },
    async createPost() {
      try {
        const formData = new FormData()
        formData.append('content', this.newPost.content)
        this.newPost.images.forEach(file => {
          formData.append('images', file.raw)
        })
        const response = await api.post('/posts', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        })
        if (response.data.success) {
          this.newPost.content = ''
          this.newPost.images = []
          await this.loadPosts()
          this.$message.success('发布成功')
        }
      } catch (error) {
        this.$message.error('发布失败')
      }
    },
    handleImageChange(file) {
      this.newPost.images.push(file)
    },
    updatePost(updatedPost) {
      const index = this.posts.findIndex(p => p.id === updatedPost.id)
      if (index !== -1) {
        this.posts[index] = updatedPost
      }
    },
    logout() {
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.feed-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
.post-form {
  margin-bottom: 20px;
}
header {
  margin-bottom: 20px;
}
nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.logo {
  font-size: 24px;
  font-weight: bold;
}
.nav-links {
  list-style: none;
  display: flex;
  gap: 20px;
}
.nav-links a {
  text-decoration: none;
  color: #333;
}
.nav-links a.active {
  font-weight: bold;
  color: #007bff;
}
</style>