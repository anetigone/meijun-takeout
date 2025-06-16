<script lang="ts">
import {defineComponent} from 'vue'
import { getAdmins, getAdminsByPage, getEmployees, getOrderOverview, getSales, getSalesTotal, getTraffic, getWorkspaceData, removeAdmin, removeEmployee } from '../api/admin';
import { checkAuthExpired } from '../utils/authStorage.ts';

export default defineComponent({
  name: "DashboardAdm"
})
</script>

<template>
  <el-header height="48px" class="sub-header">
    <div class="user-info">
      <span>当前用户：{{ currentAdmin.username || '未登录' }}</span>
      <el-button size="small" type="danger" @click="logout">退出登录</el-button>
      <el-button size="small" type="primary" @click="refreshToken">刷新Token</el-button>
    </div>
  </el-header>
  <el-container>
    <el-aside width="200px" class="dashboard-aside">
      <el-menu :default-active="activeMenu" class="el-menu-vertical-demo" @select="handleMenuSelect">
        <el-menu-item index="workspace">工作台</el-menu-item>
        <el-menu-item index="admin">管理员管理</el-menu-item>
        <el-menu-item index="employee">员工管理</el-menu-item>
        <el-menu-item index="stat">销售统计</el-menu-item>
        <el-menu-item index="chat">聊天</el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <div v-if="activeMenu === 'workspace'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <div style="font-weight: bold; margin-bottom: 12px;">工作台数据</div>
              <div>销售总额：<span style="color:#409EFF;font-size:1.2em;">￥{{ workspaceData?.turnover ?? '-' }}</span></div>
              <div>有效订单数：{{ workspaceData?.validOrderCount ?? '-' }}</div>
              <div>订单完成率：{{ workspaceData?.orderCompletionRate != null ? (workspaceData.orderCompletionRate * 100).toFixed(2) + '%' : '-' }}</div>
              <div>平均客单价：{{ workspaceData?.unitPrice ?? '-' }}</div>
              <div>新增用户数：{{ workspaceData?.newUsers ?? '-' }}</div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div style="font-weight: bold; margin-bottom: 12px;">订单概览</div>
              <div>待处理订单：{{ orderOverview?.waitingOrders ?? '-' }}</div>
              <div>待派送订单：{{ orderOverview?.deliveredOrders ?? '-' }}</div>
              <div>已完成订单：{{ orderOverview?.completedOrders ?? '-' }}</div>
              <div>已取消订单：{{ orderOverview?.cancelledOrders ?? '-' }}</div>
              <div>全部订单：{{ orderOverview?.allOrders ?? '-' }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <!-- 管理员管理 -->
      <div v-else-if="activeMenu === 'admin'">
        <el-card>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-weight: bold;">管理员列表</span>
            <div>
              <el-button type="primary" @click="fetchAdmins()">刷新</el-button>
              <el-button type="success" @click="handleAddAdmin()" style="margin-left: 8px;">添加管理员</el-button>
            </div>
          </div>
          <el-table :data="adminList" style="width: 100%; margin-top: 16px;">
            <el-table-column prop="id" label="ID" width="60"/>
            <el-table-column prop="uuid" label="UUID" width="240"/>
            <el-table-column prop="username" label="用户名" width="120"/>
            <el-table-column prop="password" label="密码" width="120">
              <template #default="scope">
                <span>{{ scope.row.password ? '******' : '未设置' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="email" label="邮箱" width="240">
              <template #default="scope">
                <span>{{ scope.row.email || '未设置' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间"/>
            <el-table-column prop="updateTime" label="更新时间"/>
            <el-table-column prop="role" label="角色">
              <template #default="scope">
                <span>{{ scope.row.role === 'ADMIN' ? '超级管理员' : '普通管理员' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" type="info" @click="handleViewAdmin(scope.row)">查看</el-button>
                <el-button size="small" type="primary" @click="handleEditAdmin(scope.row)">更新</el-button>
                <el-button size="small" type="danger" @click="deleteAdmin(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
              style="margin-top: 16px; text-align: right"
              background
              layout="prev, pager, next, sizes, total"
              :current-page="adminPage"
              :page-size="adminPageSize"
              :total="adminTotal"
              @current-change="onAdminPageChange"
              @size-change="onAdminSizeChange"
            />
        </el-card>
      </div>
      <!-- 员工管理 -->
      <div v-else-if="activeMenu === 'employee'">
        <el-card>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-weight: bold;">员工列表</span>
            <div>
              <el-button type="primary" @click="fetchEmployees">刷新</el-button>
              <el-button type="success" @click="handleAddEmployee" style="margin-left: 8px;">添加员工</el-button>
            </div>
          </div>
          <el-table :data="employeeList" style="width: 100%; margin-top: 16px;">
            <el-table-column prop="id" label="ID" width="60"/>
            <el-table-column prop="username" label="用户名" width="120"/>
            <el-table-column prop="password" label="密码" width="120">
              <template #default="scope">
                <span>{{ scope.row.password ? '******' : '未设置' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="姓名"/>
            <el-table-column prop="phone" label="电话"/>
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <span>{{ scope.row.status === 'active' ? '启用' : '禁用' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间"/>
            <el-table-column prop="updateTime" label="更新时间"/>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button size="small" type="info" @click="handleViewEmployee(scope.row)">查看</el-button>
                <el-button size="small" type="primary" @click="handleEditEmployee(scope.row)">更新</el-button>
                <el-button size="small" type="danger" @click="deleteEmployee(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
              style="margin-top: 16px; text-align: right"
              background
              layout="prev, pager, next, sizes, total"
              :current-page="employeePage"
              :page-size="employeePageSize"
              :total="employeeTotal"
              @current-change="onEmployeePageChange"
              @size-change="onEmployeeSizeChange"
           />
        </el-card>
      </div>
      <!-- 销售统计 -->
      <div v-else-if="activeMenu === 'stat'">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card>
              <div>销售总额</div>
              <div style="font-size: 2em; color: #409EFF;">${{ salesTotal }}</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <div>今日流量</div>
              <div style="font-size: 2em; color: #67C23A;">{{ traffic }}</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <div>热销商品</div>
              <el-table :data="salesList" size="default" style="margin-top: 8px;">
                <el-table-column prop="name" label="商品"/>
                <el-table-column prop="sales" label="销量"/>
              </el-table>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-main>
  </el-container>

  <!-- 管理员查看/编辑弹窗 -->
  <el-dialog v-model="adminDialogVisible" :title="adminDialogMode === 'view' ? '查看管理员' : '更新管理员'" width="400px">
    <el-form v-if="adminDialogMode === 'edit'" :model="adminForm" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="adminForm.username" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="adminForm.email" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="adminForm.password" type="password" show-password />
      </el-form-item>
    </el-form>
    <div v-else>
      <p><b>ID:</b>{{ adminForm.id }}</p>
      <p><b>UUID:</b>{{ adminForm.uuid }}</p>
      <p><b>用户名:</b>{{ adminForm.username }}</p>
      <p><b>密码:</b>{{ adminForm.password }}</p>
      <p><b>邮箱:</b>{{ adminForm.email }}</p>
      <p><b>角色:</b>{{ adminForm.role === 'ADMIN' ? '超级管理员' : '普通管理员' }}</p>
      <p><b>创建时间:</b>{{ adminForm.createTime }}</p>
      <p><b>更新时间:</b>{{ adminForm.updateTime }}</p>
    </div>
    <template #footer>
      <el-button v-if="adminDialogMode === 'view'" @click="adminDialogMode = 'edit'">修改</el-button>
      <el-button v-if="adminDialogMode === 'edit'" type="primary" @click="submitAdminUpdate">确认</el-button>
      <el-button @click="adminDialogVisible = false">取消</el-button>
    </template>
  </el-dialog>
  <el-dialog v-model="adminSaveDialogVisible" :title="'添加管理员'" width="400px">
    <el-form :model="adminSaveForm" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="adminSaveForm.username" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="adminSaveForm.email" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="adminSaveForm.password" type="password" show-password />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="submitAdminSave">确认</el-button>
      <el-button @click="adminSaveDialogVisible = false">取消</el-button>
    </template>
  </el-dialog>
  <!-- 员工查看/编辑弹窗 -->
  <el-dialog v-model="employeeDialogVisible" :title="employeeDialogMode === 'view' ? '查看员工' : '更新员工'" width="400px">
    <el-form v-if="employeeDialogMode === 'edit'" :model="employeeForm" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="employeeForm.username" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select  v-model="employeeForm.status" placeholder="请选择状态">
          <el-option label="启用" value="active" />
          <el-option label="禁用" value="inactive" />
        </el-select>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="employeeForm.password" type="password" show-password />
      </el-form-item>
    </el-form>
    <div v-else>
      <p><b>ID:</b>{{ employeeForm.id }}</p>
      <p><b>UUID:</b>{{ employeeForm.uuid }}</p>
      <p><b>用户名:</b>{{ employeeForm.username }}</p>
      <p><b>创建时间:</b>{{ employeeForm.createTime }}</p>
      <p><b>更新时间:</b>{{ employeeForm.updateTime }}</p>
    </div>
    <template #footer>
      <el-button v-if="employeeDialogMode === 'view'" @click="employeeDialogMode = 'edit'">修改</el-button>
      <el-button v-if="employeeDialogMode === 'edit'" type="primary" @click="submitEmployeeUpdate">确认</el-button>
      <el-button @click="employeeDialogVisible = false">取消</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {upgradeAdmin, getAdminById, getEmployeeById, upgradeEmployee, getEmployeesByPage, addAdmin} from '../api/admin'
import type {Admin, AdminSaveDTO, AdminUpdateDTO, Employee, Product} from '../api/types'
import router from "../router";
import {logoutApi, refreshTokenApi} from "../api/auth.ts";

const activeMenu = ref('workspace')
const workspaceData = ref()
const orderOverview = ref()
const adminList = ref<Admin[]>([])
const employeeList = ref<Employee[]>([])
const salesList = ref<Product[]>([])
const salesTotal = ref(0)
const traffic = ref(0)
const adminDialogVisible = ref(false)
const adminSaveDialogVisible = ref(false)
const adminDialogMode = ref<'view' | 'edit'>('view')
const adminForm = ref<Partial<Admin>>({})
const adminSaveForm = ref<Partial<AdminSaveDTO>>({})
const employeeDialogVisible = ref(false)
const employeeDialogMode = ref<'view' | 'edit'>('view')
const employeeForm = ref<Partial<Employee>>({})
const adminPage = ref(1)
const adminPageSize = ref(10)
const adminTotal = ref(0)
const employeePage = ref(1)
const employeePageSize = ref(10)
const employeeTotal = ref(0)
const currentAdmin = ref<Partial<Admin>>({})
const adminUpdateForm = ref<Partial<AdminUpdateDTO>>({})

const fetchWorkspaceData = async () => {
// 工作台数据
  const res = await getWorkspaceData()
  workspaceData.value = res.data.data
}
// 订单概览
const fetchOrderOverview = async () => {
  const res = await getOrderOverview()
  orderOverview.value = res.data.data
}
// 管理员列表
const fetchAdmins = async () => {
  const res = await getAdmins()
  adminList.value = res.data.data
}
// 分页获取管理员
const fetchAdminPage = async (page:number, size:number) => {
  const dto = {
    pageNum: page,
    pageSize: size
  }
  const res = await getAdminsByPage(dto)
  adminTotal.value = res.data.total
  adminPage.value = res.data.current
  adminPageSize.value = res.data.size
  adminList.value = res.data.records
}
const onAdminPageChange = async (page: number) => {
  adminPage.value = page
  await fetchAdminPage(page, adminPageSize.value)
}
const onAdminSizeChange = async (size: number) => {
  adminPageSize.value = size
  await fetchAdminPage(adminPage.value, size)
}
// 删除管理员
const deleteAdmin = async (id: number) => {
  ElMessageBox.confirm('确定删除该管理员吗？', '提示', { type: 'warning' })
    .then(async () => {
      if( id === 1) {
        ElMessage.error('不能删除超级管理员')
        return
      }
      await removeAdmin(id)
      ElMessage.success('删除成功')
      await fetchAdmins()
    })
}
// 员工列表
const fetchEmployees = async () => {
  await fetchEmployeePage(employeePage.value, employeePageSize.value)
}
// 员工分页
const fetchEmployeePage = async (page: number, size: number) => {
  const dto = { page, size }
  const res = await getEmployeesByPage(dto)
  employeeList.value = res.data.records
  employeeTotal.value = res.data.total
  employeePage.value = res.data.current
  employeePageSize.value = res.data.size
}
const onEmployeePageChange = (page: number) => {
  fetchEmployeePage(page, employeePageSize.value)
}
const onEmployeeSizeChange = (size: number) => {
  fetchEmployeePage(1, size)
}
// 删除员工
const deleteEmployee = async (id: number) => {
  ElMessageBox.confirm('确定删除该员工吗？', '提示', { type: 'warning' })
    .then(async () => {
      await removeEmployee(id)
      ElMessage.success('删除成功')
      await fetchEmployeePage(employeePage.value, employeePageSize.value)
    })
}
// 销售统计
const fetchStats = async () => {
  const [salesRes, totalRes, trafficRes] = await Promise.all([
    getSales(),
    getSalesTotal(),
    getTraffic()
  ])
  salesList.value = salesRes.data.data
  salesTotal.value = totalRes.data.data
  traffic.value = trafficRes.data.data
}

// 菜单切换时加载第一页
const handleMenuSelect = (key: string) => {
  activeMenu.value = key
  if (key === 'workspace') {
    fetchWorkspaceData()
    fetchOrderOverview()
  }
  if (key === 'admin') fetchAdminPage(1, adminPageSize.value)
  if (key === 'employee') fetchEmployeePage(1, employeePageSize.value)
  if (key === 'stat') fetchStats()
  if( key === 'chat') {
    router.push('/chat')
  }
}

// 查看
const handleViewAdmin = async (row: Admin) => {
  const res = await getAdminById(row.id!)
  adminForm.value = { ...res.data.data }
  adminDialogMode.value = 'view'
  adminDialogVisible.value = true
}

// 编辑
const handleEditAdmin = (row: Admin) => {
  adminForm.value = { ...row }
  adminDialogMode.value = 'edit'
  adminDialogVisible.value = true
}
// 添加管理员
const handleAddAdmin = () => {
  adminSaveForm.value = {};
  adminSaveDialogVisible.value = true;
};

// 提交更新
const submitAdminUpdate = async () => {
  try {
    console.log(adminForm.value)
    adminUpdateForm.value = {
      id: adminForm.value.id,
      username: adminForm.value.username,
      password: adminForm.value.password,
      email: adminForm.value.email,
      role: adminForm.value.role
    }
    await upgradeAdmin(adminUpdateForm.value)
    ElMessage.success('更新成功')
    adminDialogVisible.value = false
    await fetchAdmins()
  } catch (e) {
    ElMessage.error('更新失败')
  }
}

const submitAdminSave = async () => {
  try {
    console.log(adminSaveForm.value)
    await addAdmin(adminSaveForm.value as AdminSaveDTO)
    ElMessage.success('添加成功')
    adminSaveDialogVisible.value = false
    await fetchAdmins()
  } catch (e) {
    ElMessage.error('添加失败')
  }
};

const handleViewEmployee = async (row: Employee) => {
  const res = await getEmployeeById(row.id!)
  employeeForm.value = { ...res.data.data }
  employeeDialogMode.value = 'view'
  employeeDialogVisible.value = true
}

const handleEditEmployee = (row: Employee) => {
  employeeForm.value = { ...row }
  employeeDialogMode.value = 'edit'
  employeeDialogVisible.value = true
}
// 添加员工
const handleAddEmployee = () => {
  employeeForm.value = {};
  employeeDialogMode.value = 'edit';
  employeeDialogVisible.value = true;
};

const submitEmployeeUpdate = async () => {
  try {
    await upgradeEmployee(employeeForm.value)
    ElMessage.success('更新成功')
    employeeDialogVisible.value = false
    await fetchEmployeePage(employeePage.value, employeePageSize.value)
  } catch (e) {
    ElMessage.error('更新失败')
  }
}

const refreshToken = async () => {
  try {
    const res = await refreshTokenApi()
    const newToken = res.data.data
    localStorage.setItem('token', newToken)
    ElMessage.success('登录状态已刷新')
  } catch (e) {
    ElMessage.error('刷新失败，请重新登录')
    await router.push('/login')
  }
}
// ...

const logout = () => {
  ElMessageBox.confirm('确定退出登录吗？', '提示', { type: 'warning' })
    .then(async () => {
      await logoutApi()
      ElMessage.success('退出成功')
      localStorage.removeItem('token')
      localStorage.removeItem('uuid')
      localStorage.removeItem('username')
      localStorage.removeItem('userType')
      await router.push('/index')
    })
}

onMounted(() => {
  if(!checkAuthExpired) {
    ElMessage.error('登录已过期，请重新登录')
    router.push('/login')
    return
  }
  const storedUsername = localStorage.getItem('username');
  if (storedUsername) {
    currentAdmin.value.username = storedUsername;
  }
  console.log(currentAdmin.value.username)
  fetchWorkspaceData()
  fetchOrderOverview()
  // 初始加载管理员和员工数据
  fetchAdminPage(1, adminPageSize.value)
  fetchEmployeePage(1, employeePageSize.value)
})
</script>

<style scoped>
.dashboard-aside {
  background: #f5f7fa;
  min-height: 100vh;
  border-right: 1px solid #ebeef5;
}

.el-main {
  background: #f9f9f9;
}

.sub-header {
  background-color: #f5f7fa;
  padding: 10px 20px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  border-bottom: 1px solid #ebeef5;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

</style>