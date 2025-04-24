<template>
  <div class="post-container">
    <post-item :post="post" />
  </div>
</template>

<script>
import api from '../api'
import PostItem from '../components/PostItem.vue'

export default {
  components: { PostItem },
  data() {
    return {
      post: {}
    }
  },
  async created() {
    await this.loadPost()
  },
  methods: {
    async loadPost() {
      try {
        const response = await api.get(`/posts/${this.$route.params.postId}`)
        if (response.data.success) {
          this.post = response.data.post
        }
      } catch (error) {
        this.$message.error('加载动态失败')
      }
    }
  }
}
</script>