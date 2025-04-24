<template>
  <section class="post fade-in">
    <div class="post-header">
      <img :src="post.avatar || '/images/default-avatar.jpg'" alt="用户头像" class="profile-avatar">
      <div class="post-info">
        <router-link :to="'/profile/' + post.user_id" class="username">{{ post.name }}</router-link>
        <span class="post-time">{{ formatTime(post.created_at) }}</span>
      </div>
    </div>
    <p class="post-content">{{ post.content }}</p>
    <div class="post-images" v-if="post.images && JSON.parse(post.images).length">
      <img v-for="img in JSON.parse(post.images)" :key="img" :src="img" alt="动态图片" class="post-image">
    </div>
    <div class="post-actions">
      <el-button :class="{ liked: post.liked }" @click="likePost">
        <i class="icon-like">❤</i> 喜欢 ({{ post.likes_count }})
      </el-button>
      <el-button @click="toggleCommentWindow">
        <i class="icon-comment">💬</i> 评论 ({{ post.comments_count }})
      </el-button>
      <el-button @click="sharePost">
        <i class="icon-share">🔄</i> 分享
      </el-button>
    </div>
    <comment-window
        v-if="showCommentWindow"
        :post-id="post.id"
        @close="toggleCommentWindow"
        @update:comments_count="updateCommentsCount"/>
  </section>
</template>

<script>
import api from '../api'
import CommentWindow from './CommentWindow.vue'

export default {
  components: { CommentWindow },
  props: ['post'],
  data() {
    return {
      showCommentWindow: false
    }
  },
  methods: {
    formatTime(timestamp) {
      const now = new Date()
      const postTime = new Date(timestamp)
      const diff = now - postTime
      const minute = 60 * 1000
      const hour = 60 * minute
      const day = 24 * hour
      if (diff < minute) return '刚刚'
      if (diff < hour) return Math.floor(diff / minute) + '分钟前'
      if (diff < day) return Math.floor(diff / hour) + '小时前'
      return Math.floor(diff / day) + '天前'
    },
    async likePost() {
      try {
        const response = await api.post(`/posts/${this.post.id}/like`)
        if (response.data.success) {
          this.$emit('update:post', {
            ...this.post,
            likes_count: response.data.likesCount,
            liked: response.data.liked
          })
        }
      } catch (error) {
        this.$message.error('点赞失败')
      }
    },
    toggleCommentWindow() {
      this.showCommentWindow = !this.showCommentWindow
    },
    updateCommentsCount(count) {
      this.$emit('update:post', {
        ...this.post,
        comments_count: count
      })
    },
    async sharePost() {
      const shareUrl = `${window.location.origin}/post/${this.post.id}`
      try {
        if (navigator.share) {
          await navigator.share({
            title: `来自 ${this.post.name} 的动态`,
            text: this.post.content,
            url: shareUrl
          })
        } else {
          await navigator.clipboard.writeText(shareUrl)
          this.$message.success('链接已复制到剪贴板')
        }
      } catch (error) {
        this.$message.error('分享失败')
      }
    }
  }
}
</script>

<style scoped>
.liked {
  color: #ff4d4f;
}
</style>