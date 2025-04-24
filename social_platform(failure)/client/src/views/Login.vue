<template>
  <div class="login-container">
    <h2>登录</h2>
    <el-form :model="form" @submit.prevent="login">
      <el-form-item label="用户名">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" type="password"></el-input>
      </el-form-item>
      <el-button type="primary" native-type="submit">登录</el-button>
    </el-form>
  </div>
</template>

<script>
import api from '../api'

export default {
  data() {
    return {
      form: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    async login() {
      try {
        const response = await api.post('/users/login', this.form)
        if (response.data.success) {
          localStorage.setItem('token', response.data.token)
          localStorage.setItem('userId', response.data.userId)
          this.$router.push('/')
        } else {
          this.$message.error(response.data.message)
        }
      } catch (error) {
        this.$message.error('登录失败')
      }
    }
  }
}
</script>