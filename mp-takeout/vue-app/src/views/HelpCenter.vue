<template>
  <div class="help-center-container">
    <!-- 标题 -->
    <h2 class="help-title">帮助中心</h2>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input v-model="searchQuery" placeholder="请输入关键词搜索问题..." clearable style="width: 400px;" />
    </div>

    <!-- 分类导航 -->
    <div class="category-nav">
      <el-button
        v-for="category in categories"
        :key="category"
        :type="selectedCategory === category ? 'primary' : 'default'"
        @click="selectCategory(category)"
        style="margin-right: 12px; margin-bottom: 12px;"
      >
        {{ category }}
      </el-button>
    </div>

    <!-- 问题列表 -->
    <div class="faq-list">
      <el-collapse v-model="activeNames">
        <el-collapse-item
          v-for="(faq, index) in filteredFaqs"
          :key="index"
          :title="faq.question"
          :name="index"
        >
          <div v-html="faq.answer"></div>
        </el-collapse-item>
      </el-collapse>
    </div>

    <!-- 联系客服 -->
    <div class="contact-support">
      <el-button type="success" plain @click="contactSupport">联系客服</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 数据定义
const searchQuery = ref('')
const selectedCategory = ref('全部')
const activeNames = ref<Array<number>>([])

// 所有分类
const categories = ['全部', '登录相关', '支付相关', '订单相关', '账户管理', '常见错误']

// 示例 FAQ 数据
const faqData = [
  {
    question: '如何找回密码？',
    answer: '点击登录页的“忘记密码”，按照提示操作即可。你也可以通过邮箱或手机号重置密码。',
    category: '账户管理'
  },
  {
    question: '支付失败怎么办？',
    answer: '请检查银行卡余额、网络连接或稍后尝试重新支付。若问题持续，请联系客服处理。',
    category: '支付相关'
  },
  {
    question: '如何修改个人信息？',
    answer: '进入“我的账户” -> “个人设置” -> 修改信息并保存即可。',
    category: '账户管理'
  },
  {
    question: '订单状态显示“已取消”是什么意思？',
    answer: '该订单因用户主动取消或超时未付款被系统自动取消。',
    category: '订单相关'
  },
  {
    question: '为什么无法登录？',
    answer: '可能原因：<br>1. 用户名或密码错误<br>2. 账户被锁定<br>3. 网络异常<br>建议尝试重试或联系客服。',
    category: '登录相关'
  },
  {
    question: '如何申请退款？',
    answer: '在“我的订单”中找到对应订单，点击“申请退款”，填写原因并提交。商家审核后会进行退款操作。',
    category: '订单相关'
  }
]

// 过滤数据：按分类和搜索内容过滤
const filteredFaqs = computed(() => {
  return faqData.filter(faq => {
    const matchesCategory = selectedCategory.value === '全部' || faq.category === selectedCategory.value
    const matchesSearch = !searchQuery.value || faq.question.includes(searchQuery.value) || faq.answer.includes(searchQuery.value)
    return matchesCategory && matchesSearch
  })
})

// 切换分类
function selectCategory(category: string) {
  selectedCategory.value = category
}

// 联系客服模拟
function contactSupport() {
  ElMessage.success('请联系客服邮箱：support@meijun.com 或拨打热线：400-123-4567')
}
</script>

<style scoped>
.help-center-container {
  padding: 40px;
  max-width: 900px;
  margin: auto;
  background-color: #f9f9f9;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.help-title {
  text-align: center;
  color: #333;
  font-size: 2rem;
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.category-nav {
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.faq-list {
  margin-bottom: 30px;
}

.contact-support {
  text-align: center;
}
</style>
