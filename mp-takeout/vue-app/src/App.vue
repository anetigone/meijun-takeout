<!-- src/App.vue -->
<script setup lang="ts">
import { RouterView } from 'vue-router'
import { ElContainer, ElHeader, ElMain, ElAside, ElMenu, ElMenuItem, ElPagination } from 'element-plus'
</script>

<template>
  <el-container style="height: 100vh; width: 100%;">
    <!-- 顶部导航栏 -->
    <el-header style="background-color: #409EFF; color: white; display: flex; align-items: center; justify-content: space-between;">
      <h1 style="margin: 0;" @click="$router.push('/')">MeiJun 在线点餐系统</h1>
      <div>
        <el-menu
          default-active="$route.path"
          mode="horizontal"
          background-color="#409EFF"
          text-color="white"
          active-text-color="#ffd04b"
          router
        >
          <el-menu-item index="/index">首页</el-menu-item>
          <el-menu-item index="/login">登录</el-menu-item>
          <el-menu-item index="/dashboard">管理面板</el-menu-item>
          <el-menu-item index="/about">关于我们</el-menu-item>
          <el-menu-item index="/contact">联系我们</el-menu-item>
          <el-menu-item index="/help">帮助中心</el-menu-item>
          <el-menu-item index="/feedback">用户反馈</el-menu-item>
          <el-menu-item index="/settings">设置</el-menu-item>
          <el-menu-item index="/logout">退出登录</el-menu-item>
        </el-menu>
      </div>
    </el-header>

    <!-- 页面主体内容 -->
    <el-main style="padding: 0px;">
      <router-view />
    </el-main>
  </el-container>
</template>

<style>
body {
  margin: 0;
  font-family: Arial, sans-serif;
}
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
</style>
