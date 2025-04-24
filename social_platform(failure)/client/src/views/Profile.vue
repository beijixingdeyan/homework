<template>
  <div class="profile-container">
    <header>
      <nav>
        <div class="logo">社交平台</div>
        <ul class="nav-links">
          <li><router-link to="/">首页</router-link></li>
          <li><router-link :to="'/profile/' + user.id" class="active">个人主页</router-link></li>
        </ul>
        <el-button @click="logout">退出登录</el-button>
      </nav>
    </header>
    <div class="profile-header">
      <img :src="user.avatar || '/images/default-avatar.jpg'" alt="头像" class="profile-avatar">
      <h2>{{ user.name }}</h2>
      <p>{{ user.bio || '暂无简介' }}</p>
      <p>关注: {{ followingCount }} | 粉丝: {{ followerCount }}</p>
      <el-button
          v-if="user.id !== currentUserId"
          :type="isFollowing ? 'info' : 'primary'"
          @click="toggleFollow">
        {{ isFollowing ? '取消关注' : '关注' }}
      </el-button>
    </div>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="动态" name="posts">
        <post-item
            v-for="post in posts"
            :key="post.id"
            :post="post"
            @update:post="updatePost"/>
      </el-tab-pane>
      <el-tab-pane label="评论" name="comments">
        <p>暂未实现用户评论列表</p>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import api from '../api'
import PostItem from '../components/PostItem.vue'

export default {
  components: {PostItem},
  data() {
    return {
      user: {},
      posts: [],
      activeTab: 'posts',
      isFollowing: false,
      followerCount: 0,
      followingCount: 0,
      currentUserId: parseInt(localStorage.getItem('userId'))
    }
  },
  async created() {
    await this.loadUser()
    await this.loadPosts()
    if (this.user.id !== this.currentUserId) {
      await this.loadFollowStatus()
    }
  },
  methods: {
    async loadUser() {
      try {
        const response = await api.get(`/users/${this.$route.params.userId}`)
        if (response.data.success) {
          this.user = response.data
        }
      } catch (error) {
        this.$message.error('加载用户信息失败')
      }
    },
    async loadPosts() {
      try {
        const response = await api.get(`/posts?userId=${this.$route.params.userId}`)
        if (response.data.success) {
          this.posts = response.data.posts
        }
      } catch (error) {
        this.$message.error('加载动态失败')
      }
    },
    async loadFollowStatus() {
      try {
        const response = await api.get(`/users/${this.$route.params.userId}/follow/status`)
        if (response.data.success) {
          this.isFollowing = response.data.isFollowing
          this.followerCount = response.data.followerCount
          this.followingCount = response.data.followingCount
        }
      } catch (error) {
        this.$message.error('加载关注状态失败')
      }
    },
    async toggleFollow() {
      try {
        const response = await api.post(`/users/${this.$route.params.userId}/follow`)
        if (response.data.success) {
          this.isFollowing = response.data.isFollowing
          this.followerCount = response.data.followerCount
          this.$message.success(this.isFollowing ? '关注成功' : '取消关注成功')
        }
      } catch (error) {
        this.$message.error('操作失败')
      }
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
.profile-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.profile-header {
  text-align: center;
  margin-bottom: 20px;
}

.profile-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 10px;
}
</style>