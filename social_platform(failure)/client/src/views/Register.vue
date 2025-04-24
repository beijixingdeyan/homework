<template>
  <div class="register-container">
    <h2>注册</h2>
    <el-form :model="form" @submit.prevent="register">
      <el-form-item label="用户名">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" type="email"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" type="password"></el-input>
      </el-form-item>
      <el-form-item label="简介">
        <el-input v-model="form.bio" type="textarea"></el-input>
      </el-form-item>
      <el-button type="primary" native-type="submit">注册</el-button>
    </el-form>
  </div>
</template>

<script>
import api from '../api'

export default {
  data() {
    return {
      form: {
        name: '',
        email: '',
        password: '',
        bio: ''
      }
    }
  },
  methods: {
    async register() {
      try {
        const response = await api.post('/users/register', this.form)
        if (response.data.success) {
          this.$message.success('注册成功，请登录')
          this.$router.push('/login')
        } else {
          this.$message.error(response.data.message)
        }
      } catch (error) {
        this.$message.error('注册失败')
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}
</style>