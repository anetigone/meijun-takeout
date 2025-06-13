<template>
  <el-container class="login-bg">
    <el-card class="login-card">
      <h2 class="login-title">登录</h2>
      <el-form @submit.prevent="handleLogin" :model="form" :rules="rules" ref="formRef" class="login-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" size="large" clearable />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password size="large" clearable />
        </el-form-item>
        <el-form-item label="验证码" prop="captcha">
          <div class="captcha-row">
            <el-input v-model="form.code" size="large" clearable />
            <img
              :src="captchaUrl"
              alt="验证码"
              class="captcha-img"
              @click="fetchCaptcha"
            />
          </div>
        </el-form-item>
        <el-form-item label="身份" prop="role">
          <el-select v-model="form.identity" placeholder="请选择身份" size="large">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="商户" value="MERCHANT" />
            <el-option label="员工" value="EMPLOYEE" />
          </el-select>
        </el-form-item>
        <el-button type="primary" native-type="submit" class="login-btn" size="large" round>登录</el-button>
      </el-form>
    </el-card>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login, getCaptchaBase64 } from '../api/auth';
import { ElMessage } from 'element-plus';
import type {AuthLoginDTO} from "../api/types.ts";
import { setAuth } from '../utils/authStorage.ts';

const router = useRouter();
const captchaUrl = ref('');
const kaptchaUuid = ref('');
const form = ref<AuthLoginDTO>({
  username: '',
  password: '',
  code: '',
  identity: '',
  uuid: ''
});
const formRef = ref();

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
  identity: [{ required: true, message: '请选择身份', trigger: 'change' }]
};

const fetchCaptcha = async () => {
  try {
    const res = await getCaptchaBase64();
    const data = res.data.data;
    captchaUrl.value = data.code;
    kaptchaUuid.value = data.uuid;
    console.log(kaptchaUuid.value);
    form.value.uuid = kaptchaUuid.value; // 更新表单中的kaptchaUuid
  } catch (error) {
    ElMessage.error('获取验证码失败');
  }
};

const handleLogin = async () => {
  try {
    form.value.uuid = kaptchaUuid.value; // 确保kaptchaUuid在登录时是最新的
    console.log(form.value);
    const res = await login(form.value);
    const { token, uuid } = res.data.data;
    // 根据身份跳转到不同的页面
    if (form.value.identity.toLowerCase() === 'admin') {
      await router.push('/adm-dashboard');
    } else if (form.value.identity.toLowerCase() === 'merchant') {
      await router.push('/mer-dashboard');
    } else if (form.value.identity.toLowerCase() === 'employee') {
      await router.push('/emp-dashboard');
    }
    setAuth({
      token,
      uuid,
      username: form.value.username,
      userType: form.value.identity,
      expireTime: 3600*1000
    });
    ElMessage.success('登录成功');
  } catch (error) {
    ElMessage.error('登录失败，请检查用户名或密码');
  }
};

fetchCaptcha();
</script>

<style scoped>
.login-bg {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

.login-card {
  width: 400px;
  border-radius: 18px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.18);
  padding: 32px 28px 24px 28px;
  background: rgba(255,255,255,0.95);
}

.login-title {
  text-align: center;
  margin-bottom: 28px;
  font-weight: 600;
  font-size: 26px;
  color: #333;
  letter-spacing: 2px;
}

.login-form .el-form-item {
  margin-bottom: 22px;
}

.captcha-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.captcha-img {
  height: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: box-shadow 0.2s;
}
.captcha-img:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.18);
}

.login-btn {
  width: 100%;
  margin-top: 10px;
  font-size: 18px;
  letter-spacing: 2px;
}
</style>