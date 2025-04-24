<template>
  <div class="comment-window">
    <button class="comment-window-close" @click="$emit('close')">×</button>
    <div class="comment-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <img :src="comment.avatar || '/images/default-avatar.jpg'" alt="评论者头像" class="comment-avatar">
        <div class="comment-details">
          <span class="comment-username">{{ comment.name }}</span>
          <p class="comment-text">{{ comment.text }}</p>
          <span class="comment-time">{{ formatTime(comment.created_at) }}</span>
          <el-button size="small" @click="startReply(comment.id)">回复</el-button>
          <div v-if="comment.replies && comment.replies.length" class="replies">
            <div v-for="reply in comment.replies" :key="reply.id" class="comment-item reply">
              <img :src="reply.avatar || '/images/default-avatar.jpg'" alt="评论者头像" class="comment-avatar">
              <div class="comment-details">
                <span class="comment-username">{{ reply.name }}</span>
                <p class="comment-text">{{ reply.text }}</p>
                <span class="comment-time">{{ formatTime(reply.created_at) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="comment-input-container">
      <el-input
          v-model="newComment"
          :placeholder="replyTo ? '回复 ' + replyTo.name + '...' : '写下你的评论...'"></el-input>
      <el-button @click="submitComment" :disabled="!newComment.trim()">发布</el-button>
      <el-button v-if="replyTo" @click="cancelReply" size="small">取消回复</el-button>
    </div>
  </div>
</template>

<script>
import api from '../api'

export default {
  props: ['postId'],
  data() {
    return {
      comments: [],
      newComment: '',
      replyTo: null
    }
  },
  async created() {
    await this.loadComments()
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
    async loadComments() {
      try {
        const response = await api.get(`/posts/${this.postId}/comments`)
        if (response.data.success) {
          this.comments = response.data.comments
          this.$emit('update:comments_count', this.comments.length)
        }
      } catch (error) {
        this.$message.error('加载评论失败')
      }
    },
    async submitComment() {
      if (!this.newComment.trim()) {
        this.$message.warning('评论不能为空')
        return
      }
      try {
        const data = { text: this.newComment }
        if (this.replyTo) {
          data.parentCommentId = this.replyTo.id
        }
        const response = await api.post(`/posts/${this.postId}/comments`, data)
        if (response.data.success) {
          this.newComment = ''
          this.replyTo = null
          await this.loadComments()
        }
      } catch (error) {
        this.$message.error('评论失败')
      }
    },
    startReply(commentId) {
      const comment = this.comments.find(c => c.id === commentId) ||
          this.comments.flatMap(c => c.replies || []).find(r => r.id === commentId)
      if (comment) {
        this.replyTo = comment
      }
    },
    cancelReply() {
      this.replyTo = null
      this.newComment = ''
    }
  }
}
</script>

<style scoped>
.replies {
  margin-left: 20px;
  border-left: 2px solid #eee;
  padding-left: 10px;
}
.reply {
  margin-top: 10px;
}
</style>