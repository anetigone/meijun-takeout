<template>
  <el-header height="48px" class="sub-header">
    <div class="user-info">
      <span>当前用户：{{ currentEmployee.username || '未登录' }}</span>
      <el-button size="small" type="danger" @click="logout">退出登录</el-button>
    </div>
  </el-header>
  <el-container>
    <el-aside width="200px" class="dashboard-aside">
      <el-menu :default-active="activeMenu" @select="handleMenuSelect">
        <el-menu-item index="orders">订单管理</el-menu-item>
        <el-menu-item index="staff">员工管理</el-menu-item>
        <el-menu-item index="stores">门店管理</el-menu-item>
        <el-menu-item index="users">用户管理</el-menu-item>
        <el-menu-item index="dishes">菜品管理</el-menu-item>
        <el-menu-item index="coupons">优惠券</el-menu-item>
        <el-menu-item index="promotions">促销活动</el-menu-item>
        <el-menu-item index="stat">销售统计</el-menu-item>
        <el-menu-item index="chat">聊天</el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <!-- 订单管理 -->
      <div v-if="activeMenu === 'orders'">
        <el-card>
          <div style="display: flex; align-items: center; gap: 10px;">
            <el-input v-model.number="orderId" placeholder="输入订单ID查询" style="width: 200px;" />
            <el-button type="primary" @click="fetchOrderById">查询订单</el-button>
            <el-button v-if="isOrderSearchMode" @click="exitOrderSearch" type="info" size="small" style="margin-left: 10px;">
              取消
            </el-button>
            <el-button type="success" @click="fetchOrder" style="margin-right: 10px;">刷新订单列表</el-button>
          </div>
          <el-table :data="orderList" style="margin-top: 16px;">
            <el-table-column prop="id" label="订单ID" />
            <el-table-column prop="status" label="状态" />
            <el-table-column prop="payStatus" label="支付状态" />
            <el-table-column prop="orderTime" label="下单时间" />
            <el-table-column prop="total" label="总价" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="handleViewOrder(scope.row)">查看</el-button>
                <el-button
                  v-if="scope.row.status === 'pending'"
                  size="small"
                  type="primary"
                  @click="showConfirmDialog(scope.row, '是否接单？', 'accept')"
                >接单</el-button>
                <el-button
                  v-if="scope.row.status === 'pending'"
                  size="small"
                  type="danger"
                  @click="showConfirmDialog(scope.row, '是否拒单？', 'reject')"
                >拒单</el-button>
                <el-button
                  v-if="scope.row.status === 'confirmed'"
                  size="small"
                  type="success"
                  @click="confirmStartDelivery(scope.row)"
                >开始派送</el-button>
                <el-button
                  v-if="scope.row.status === 'delivering'"
                  size="small"
                  type="success"
                  @click="confirmCompleteOrder(scope.row)"
                >完成</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页控件 -->
          <el-pagination
            style="margin-top: 16px; text-align: right"
              background
              layout="prev, pager, next, sizes, total"
              :current-page="orderPage"
              :page-size="orderPageSize"
              :total="orderTotal"
              @current-change="onOrderPageChange"
              @size-change="onOrderSizeChange"
          />
        </el-card>
      </div>
      <!-- 员工管理 -->
      <div v-else-if="activeMenu === 'staff'">
        <el-card>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span>员工列表</span>
            <div>
              <el-button type="primary" @click="fetchStaff">刷新</el-button>
              <el-button type="success" @click="handleAddEmployee" style="margin-left: 8px;">添加员工</el-button>
            </div>
          </div>
          <el-table :data="staffList" style="margin-top: 16px;">
            <el-table-column prop="id" label="ID" width="60"/>
            <el-table-column prop="uuid" label="UUID" width="240"/>
            <el-table-column prop="username" label="用户名" width="240"/>
            <el-table-column prop="name" label="姓名" width="240"/>
            <el-table-column prop="phone" label="电话" width="240"/>
            <el-table-column prop="status" label="状态" width="180">
              <template #default="scope">
                <span>{{ scope.row.status === 'active' ? '启用' : '禁用' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="360"/>
            <el-table-column prop="updateTime" label="更新时间" width="360"/>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button size="small" type="info" @click="handleViewEmployee(scope.row)" style="margin-left: 8px;">查看</el-button>
                <el-button size="small" type="primary" @click="handleEditEmployee(scope.row)" style="margin-left: 8px;">更新</el-button>
                <el-button size="small" type="danger" @click="deleteEmployeeFn(scope.row.id)" style="margin-left: 8px;">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            layout="prev, pager, next, sizes, total"
            :total="employeeTotal"
            :page-size="employeePageSize"
            :current-page="employeePage"
            @current-change="onEmployeePageChange"
            @size-change="onEmployeeSizeChange"
            style="margin-top: 16px;"
          />
        </el-card>
      </div>
      <!-- 门店管理 -->
      <div v-else-if="activeMenu === 'stores'">
        <el-card>
          <span>门店列表</span>
          <el-table :data="storeList" style="margin-top: 16px;">
            <el-table-column prop="id" label="ID" width="60"/>
            <el-table-column prop="name" label="门店名"/>
            <el-table-column prop="address" label="地址"/>
          </el-table>
        </el-card>
      </div>
      <!-- 用户管理 -->
      <div v-else-if="activeMenu === 'users'">
        <el-card>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span>用户列表</span>
            <el-button type="primary" @click="fetchUsers">刷新</el-button>
          </div>
          <el-table :data="userList" style="margin-top: 16px;">
            <el-table-column prop="id" label="ID" width="60"/>
            <el-table-column prop="uuid" label="UUID" width="240"/>
            <el-table-column prop="username" label="用户名" width="240"/>
            <el-table-column prop="password" label="密码" width="240">
              <template #default="scope">
                <span>{{ scope.row.password?'******' : '未设置' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间"/>
            <el-table-column prop="updateTime" label="更新时间"/>
            <el-table-column prop="phone" label="电话" width="240"/>
            <el-table-column label="操作" width="240">
              <template #default="scope">
                <el-button size="small" type="info" @click="handleViewUser(scope.row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            layout="prev, pager, next, sizes, total"
            :total="userTotal"
            :page-size="userPageSize"
            :current-page="userPage"
            @current-change="onUserPageChange"
            @size-change="onUserSizeChange"
            style="margin-top: 16px;"
          />
        </el-card>
      </div>
      <div v-else-if="activeMenu === 'dishes'">
        <el-card>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
              <el-input v-model="dishSearchName" placeholder="输入菜品名称搜索" style="width: 200px; margin-right: 8px;" />
              <el-button type="primary" @click="fetchDishes">搜索</el-button>
              <el-button @click="resetDishSearch" style="margin-left: 8px;">重置</el-button>
            </div>
            <el-button type="success" @click="showDishDialog('add')">添加菜品</el-button>
          </div>
          <el-table :data="dishList" style="margin-top: 16px;">
            <el-table-column prop="id" label="ID" width="60"/>
            <el-table-column prop="name" label="菜品名称"/>
            <el-table-column prop="categoryId" label="分类ID"/>
            <el-table-column prop="price" label="价格"/>
            <el-table-column prop="image" label="图片">
              <template #default="scope">
                <el-image v-if="scope.row.image" :src="scope.row.image" style="width: 40px; height: 40px;" fit="cover"/>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="描述"/>
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
                  {{ scope.row.status === 1 ? '起售' : '停售' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="sale" label="销量"/>
            <el-table-column label="操作" width="180">
              <template #default="scope">
                <el-button size="small" type="info" @click="showDishDialog('view', scope.row)">查看</el-button>
                <el-button size="small" type="primary" @click="showDishDialog('edit', scope.row)">编辑</el-button>
                <!-- 可扩展删除功能 -->
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            layout="prev, pager, next, sizes, total"
            :total="dishTotal"
            :page-size="dishPageSize"
            :current-page="dishPage"
            @current-change="onDishPageChange"
            @size-change="onDishSizeChange"
            style="margin-top: 16px;"
          />
        </el-card>
        <!-- 菜品查看/编辑/新增弹窗 -->
        <el-dialog v-model="dishDialogVisible" :title="dishDialogTitle" width="500px">
          <el-form v-if="dishDialogMode !== 'view'" :model="dishForm" label-width="80px">
            <el-form-item label="菜品名称">
              <el-input v-model="dishForm.name" />
            </el-form-item>
            <el-form-item label="分类">
              <el-select v-model="dishForm.categoryId" placeholder="请选择分类">
                <el-option v-for="cat in dishCategories" :key="cat.id" :label="cat.name" :value="cat.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="价格">
              <el-input v-model="dishForm.price" type="number" />
            </el-form-item>
            <el-form-item label="图片">
              <el-input v-model="dishForm.image" placeholder="图片URL" />
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="dishForm.description" type="textarea" />
            </el-form-item>
            <el-form-item label="状态">
              <el-switch v-model="dishForm.status" :active-value="1" :inactive-value="0" active-text="起售" inactive-text="停售"/>
            </el-form-item>
          </el-form>
          <div v-else>
            <p><b>ID:</b> {{ dishForm.id }}</p>
            <p><b>菜品名称:</b> {{ dishForm.name }}</p>
            <p><b>分类ID:</b> {{ dishForm.categoryId }}</p>
            <p><b>价格:</b> {{ dishForm.price }}</p>
            <p><b>描述:</b> {{ dishForm.description }}</p>
            <p><b>状态:</b> {{ dishForm.status === 1 ? '起售' : '停售' }}</p>
            <p><b>销量:</b> {{ dishForm.sale }}</p>
            <el-image v-if="dishForm.image" :src="dishForm.image" style="width: 80px; height: 80px;" fit="cover"/>
          </div>
          <template #footer>
            <el-button v-if="dishDialogMode === 'edit' || dishDialogMode === 'add'" type="primary" @click="submitDishForm">确认</el-button>
            <el-button @click="dishDialogVisible = false">取消</el-button>
          </template>
        </el-dialog>
      </div>
      <!-- 优惠券管理 -->
      <div v-else-if="activeMenu === 'coupons'">
        <el-card>
          <span>优惠券列表</span>
          <el-table :data="couponList" style="margin-top: 16px;">
            <el-table-column prop="id" label="ID" width="60"/>
            <el-table-column prop="name" label="名称"/>
            <el-table-column prop="discount" label="折扣"/>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button size="small" type="danger" @click="deleteCouponFn(scope.row.id)">删除</el-button>

              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
      <!-- 促销活动管理 -->
      <div v-else-if="activeMenu === 'promotions'">
        <el-card>
          <span>促销活动列表</span>
          <el-table :data="promotionList" style="margin-top: 16px;">
            <el-table-column prop="id" label="ID" width="60"/>
            <el-table-column prop="name" label="活动名"/>
            <el-table-column prop="description" label="描述"/>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button size="small" type="danger" @click="deletePromotion(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
      <!-- 销售统计 -->
      <div v-else-if="activeMenu === 'stat'">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card>
              <div>销售总额</div>
              <div style="font-size: 2em; color: #409EFF;">￥{{ salesTotal }}</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <div>总流量</div>
              <div style="font-size: 2em; color: #67C23A;">{{ traffic }}</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <div>热销商品</div>
              <el-table :data="salesList" size="small" style="margin-top: 8px;">
                <el-table-column prop="name" label="商品"/>
                <el-table-column prop="sales" label="销量"/>
              </el-table>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-main>
  </el-container>

  <el-dialog v-model="orderDialogVisible" title="订单详情">
    <el-descriptions :model="selectedOrder" 
    direction="vertical"
    :column="2"
    :size="'default'"
    border>
      <el-descriptions-item label="订单ID">{{ selectedOrder.id }}</el-descriptions-item>
      <el-descriptions-item label="状态">{{ selectedOrder.status }}</el-descriptions-item>
      <el-descriptions-item label="总价">￥{{ selectedOrder.total }}</el-descriptions-item>
      <el-descriptions-item label="下单时间">{{ selectedOrder.orderTime }}</el-descriptions-item>
      <el-descriptions-item label="订单详情">
        <el-table :data="selectedOrder.list" border>
          <el-table-column prop="name" label="商品名称"/>
          <el-table-column prop="unit" label="单价"/>
          <el-table-column prop="quantity" label="数量"/>
          <el-table-column prop="total" label="小计"/>
        </el-table>
      </el-descriptions-item>
      <!-- 其他字段可自行补充 -->
    </el-descriptions>
  </el-dialog>

  <!-- 用户查看弹窗 -->
  <el-dialog v-model="userDialogVisible" title="用户详情" width="400px">
    <div v-if="selectedUser">
      <p><b>ID:</b> {{ selectedUser.id }}</p>
      <p><b>用户名:</b> {{ selectedUser.username }}</p>
      <p><b>电话:</b> {{ selectedUser.phone }}</p>
      <p><b>Email:</b> {{ selectedUser.email }}</p>
      <p><b>注册时间:</b> {{ selectedUser.createTime }}</p>
    </div>
    <template #footer>
      <el-button @click="userDialogVisible = false">关闭</el-button>
    </template>
  </el-dialog>

  <!-- 员工查看/编辑弹窗 -->
  <el-dialog v-model="employeeDialogVisible" :title="employeeDialogMode === 'view' ? '查看员工' : '更新员工'" width="400px">
    <el-form v-if="employeeDialogMode === 'edit'" :model="employeeForm" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="employeeForm.username" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="employeeForm.password" type="password" show-password />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="employeeForm.name" />
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="employeeForm.phone" />
      </el-form-item>
    </el-form>
    <div v-else>
      <p><b>ID:</b> {{ employeeForm.id }}</p>
      <p><b>用户名:</b> {{ employeeForm.username }}</p>
      <p><b>电话:</b> {{ employeeForm.phone }}</p>
      <p><b>创建时间:</b> {{ employeeForm.createTime }}</p>
      <p><b>更新时间:</b> {{ employeeForm.updateTime }}</p>
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
import type {
  Coupon,
  Customer,
  Dish,
  DishDTO,
  Employee,
  EmployeeSaveDTO,
  EmployeeUpdateDTO,
  Order,
  Product,
  Promotion,
  Store
} from '../api/types'
import { logoutApi } from '../api/auth'
import router from '../router'
import {
  getOrderById,
  updateOrderStatus,
  getEmployees as getMerchantEmployees,
  getStores,
  getAllCustomers,
  getCoupons,
  getPromotions,
  getSales,
  getSalesTotal,
  getTraffic,
  deleteEmployee,
  deleteCoupon,
  deletePromotion,
  getOrderPage,
  rejectOrder,
  updateEmployee,
  saveEmployee,
  getEmployeePage,
  getEmployeeById,
  getCustomersPage
} from '../api/merchant'
import { getDishCategories, getDishPage, getSearchForDish, saveDish } from '../api/dish'

const activeMenu = ref('orders')
const orderId = ref(0)
const order = ref<Order>({})
const orderStatus = ref(0)
const orderPayStatus = ref('')
const orderStatusMap = {
  0: 'pending',
  1: 'unconfirmed',
  2: 'confirmed',
  3: 'delivering',
  4: 'completed',
  5: 'cancelled'
}
const orderPayStatusMap = {
  0: 'unpaid',
  1: 'paid',
  2: 'refunded'
}

const orderList = ref<Order[]>([])
const orderPage = ref(1)
const orderPageSize = ref(10)
const orderTotal = ref(0)
const orderDialogVisible = ref(false)
const selectedOrder = ref<Order>({})
const rejectReason = ref('')
const confirmAction = ref<0|1|2|3|4|5|null>(null)
const staffList = ref<Employee[]>([])
const storeList = ref<Store[]>([])
const userList = ref<Customer[]>([])
const couponList = ref<Coupon[]>([])
const promotionList = ref<Promotion[]>([])
const salesList = ref<Product[]>([])
const salesTotal = ref(0)
const traffic = ref(0)
const currentEmployee = ref<Partial<Employee>>({})
const isOrderSearchMode = ref(false)

// 员工管理相关
const employeeDialogVisible = ref(false)
const employeeDialogMode = ref<'view' | 'edit'>('view')
const employeeForm = ref<Partial<Employee>>({})
const employeePage = ref(1)
const employeePageSize = ref(10)
const employeeTotal = ref(0)

// 用户管理相关
const userDialogVisible = ref(false)
const selectedUser = ref<Customer>({})
const userPage = ref(1)
const userPageSize = ref(10)
const userTotal = ref(0)

// 菜品管理相关
const dishList = ref<Dish[]>([])
const dishPage = ref(1)
const dishPageSize = ref(10)
const dishTotal = ref(0)
const dishSearchName = ref('')
const dishDialogVisible = ref(false)
const dishDialogMode = ref<'view'|'edit'|'add'>('view')
const dishDialogTitle = ref('菜品详情')
const dishForm = ref<Partial<Dish>>({})
const dishCategories = ref<Dish[]>([])

const fetchOrderById = async () => {
  if (!orderId.value) return;

  try {
    const res = await getOrderById(Number(orderId.value));
    if(res.data.data) {
      orderList.value = [res.data.data];
      isOrderSearchMode.value = true
    } else {
      ElMessage.error('未找到该订单')
      orderList.value = []
    }
  } catch (error) {
    ElMessage.error('获取订单失败')
    orderList.value = []
  }
};

const fetchOrder = async () => {
  await fetchOrderPage(orderPage.value, orderPageSize.value)
}

const fetchOrderPage = async (page: number, size: number) => {
  const res = await getOrderPage({page, size})
  orderList.value = res.data.records
  orderTotal.value = res.data.total
  orderPageSize.value = res.data.size
}

const updateOrderStatusFn = async (id: number) => {
	await updateOrderStatus(id, Number(orderStatus.value))
	ElMessage.success('订单状态已更新')
	fetchOrderById()
}

const onOrderPageChange = async (page: number) => {
  orderPage.value = page
  await fetchOrderPage(page, orderPageSize.value)
}

const onOrderSizeChange = async (size: number) => {
  orderPageSize.value = size
  await fetchOrderPage(orderPage.value, size)
}

const exitOrderSearch = () => {
  isOrderSearchMode.value = false;
  orderId.value = 0;
  orderPage.value = 1;
  orderPageSize.value = 10;
  fetchOrderPage(orderPage.value, orderPageSize.value); // 重新加载默认分页
}

const handleOrderAction = async (row:Order) => {
  const status = row.status
  if(status === 'pending') {
    await showConfirmDialog(row, '是否确认接单？', 'accept')
  } else if(status === 'confirmed') {
    await confirmStartDelivery(row)
  } else if(status === 'delivering') {
    await confirmCompleteOrder(row)
  }
}

const acceptOrder = async (orderId: number) => {
  await updateOrderStatus(orderId, 2)
}

const showConfirmDialog = async (order: Order, message: string, actionType: 'accept' | 'reject') => {
  try {
    await ElMessageBox.confirm(message, '提示', { type: 'warning' });
    if (actionType === 'accept') {
      await acceptOrder(order.id!);
    } else {
      const reason = prompt('请输入拒单原因：');
      if (!reason) {
        ElMessage.warning('拒单原因不能为空');
        return;
      }
      await rejectOrder({
        orderId: order.id,
        reason
      });
    }
  } catch (error) {
    // 用户取消或发生错误
  }
};

async function startDelivery(number: number) {
  await updateOrderStatus(number, 3)
}

const confirmStartDelivery = async (order: Order) => {
  try {
    await ElMessageBox.confirm('是否开始派送？', '提示', { type: 'info' });
    await startDelivery(order.id!);
    ElMessage.success('已开始派送');
    await fetchOrderPage(orderPage.value, orderPageSize.value);
  } catch (error) {
    // 用户取消或发生错误
  }
};

async function completeOrder(number: number) {
  await updateOrderStatus(number, 4)
}

const confirmCompleteOrder = async (order: Order) => {
  try {
    await ElMessageBox.confirm('是否完成订单？', '提示', { type: 'success' });
    await completeOrder(order.id!);
    ElMessage.success('订单已完成');
    fetchOrderPage(orderPage.value, orderPageSize.value);
  } catch (error) {
    // 用户取消或发生错误
  }
};

const handleViewOrder = (order: Order) => {
  selectedOrder.value = order;
  orderDialogVisible.value = true;
};

const fetchStaff = async (page = employeePage.value, size = employeePageSize.value) => {
  const res = await getEmployeePage(page, size)
  staffList.value = res.data.records
  employeeTotal.value = res.data.total
  employeePageSize.value = res.data.size
}
const deleteEmployeeFn = async (id: number) => {
	await deleteEmployee(id)
	ElMessage.success('删除成功')
	fetchStaff()
}

const onEmployeePageChange = (page: number) => {
  employeePage.value = page
  fetchStaff(page, employeePageSize.value)
}
const onEmployeeSizeChange = (size: number) => {
  employeePageSize.value = size
  employeePage.value = 1
  fetchStaff(1, size)
}

// 查看员工信息
const handleViewEmployee = async (row: Employee) => {
  const res = await getEmployeeById(row.id!)
  employeeForm.value = { ...res.data.data }
  employeeDialogMode.value = 'view'
  employeeDialogVisible.value = true
}

// 编辑员工信息
const handleEditEmployee = (row: Employee) => {
  employeeForm.value = { ...row }
  employeeDialogMode.value = 'edit'
  employeeDialogVisible.value = true
}

// 添加员工
const handleAddEmployee = () => {
  employeeForm.value = {}
  employeeDialogMode.value = 'edit'
  employeeDialogVisible.value = true
}

// 提交员工修改
const submitEmployeeUpdate = async () => {
  try {
    if (employeeForm.value.id) {
      await updateEmployee(employeeForm.value.id, employeeForm.value as EmployeeUpdateDTO)
    } else {
      await saveEmployee(employeeForm.value as EmployeeSaveDTO)
    }
    ElMessage.success(employeeForm.value.id ? '更新成功' : '添加成功')
    employeeDialogVisible.value = false
    fetchStaff()
  } catch (e) {
    ElMessage.error(employeeForm.value.id ? '更新失败' : '添加失败')
  }
}

// 查看用户信息
const handleViewUser = (row: Customer) => {
  selectedUser.value = row
  userDialogVisible.value = true
}

const fetchStores = async () => {
  	const res = await getStores()
  	storeList.value = res.data.data
}

const fetchUsers = async (page = userPage.value, size = userPageSize.value) => {
  const res = await getCustomersPage(page, size)
  userList.value = res.data.records
  userTotal.value = res.data.total
  userPageSize.value = res.data.size
}

const onUserPageChange = (page: number) => {
  userPage.value = page
  fetchUsers(page, userPageSize.value)
}
const onUserSizeChange = (size: number) => {
  userPageSize.value = size
  userPage.value = 1
  fetchUsers(1, size)
}

const fetchDishes = async (page = dishPage.value, size = dishPageSize.value) => {
  if (dishSearchName.value) {

    let name = dishSearchName.value.trim()
    if (!name) {
      ElMessage.warning('请输入菜品名称')
      return
    }
    const res = await getSearchForDish(name)
    dishList.value = res.data.data || []
    dishTotal.value = dishList.value.length
    dishPageSize.value = size
    dishPage.value = 1
  } else {
    const res = await getDishPage(page, size)
    dishList.value =  res.data.records || []
    dishTotal.value = res.data.total
    dishPageSize.value = res.data.size || size
    dishPage.value = page
  }
}

const onDishPageChange = (page: number) => {
  dishPage.value = page
  fetchDishes(page, dishPageSize.value)
}
const onDishSizeChange = (size: number) => {
  dishPageSize.value = size
  dishPage.value = 1
  fetchDishes(1, size)
}
const resetDishSearch = () => {
  dishSearchName.value = ''
  fetchDishes(1, dishPageSize.value)
}

const fetchDishCategories = async () => {
  const res = await getDishCategories()
  dishCategories.value = res.data.data || []
}

const showDishDialog = (mode: 'view'|'edit'|'add', row?: any) => {
  dishDialogMode.value = mode
  dishDialogTitle.value = mode === 'add' ? '添加菜品' : mode === 'edit' ? '编辑菜品' : '菜品详情'
  if (mode === 'add') {
    dishForm.value = { status: 1 }
  } else if (row) {
    dishForm.value = { ...row }
  }
  dishDialogVisible.value = true
}

const submitDishForm = async () => {
  try {
    if (dishDialogMode.value === 'add') {
      await saveDish(dishForm.value as DishDTO)
      ElMessage.success('添加成功')
    } else if (dishDialogMode.value === 'edit') {
      // 这里可扩展编辑接口
      ElMessage.success('编辑功能待实现')
    }
    dishDialogVisible.value = false
    fetchDishes(dishPage.value, dishPageSize.value)
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const fetchCoupons = async () => {
	const res = await getCoupons()
	couponList.value = res.data.data
}

const deleteCouponFn = async (id: number) => {
	await deleteCoupon(id)
	ElMessage.success('删除成功')
	fetchCoupons()
}

const fetchPromotions = async () => {
	const res = await getPromotions()
	promotionList.value = res.data.data
}
const deletePromotionFn = async (id: number) => {
	await deletePromotion(id)
	ElMessage.success('删除成功')
	fetchPromotions()
}

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

const handleUpdateOrderStatus = async (id: number) => {
	await updateOrderStatus(id, Number(orderStatus.value))
	ElMessage.success('订单状态已更新')
	fetchOrderById()
}

const handleMenuSelect = (key: string) => {
  activeMenu.value = key
  if (key === 'orders') {
    isOrderSearchMode.value = false
    fetchOrderPage(orderPage.value, orderPageSize.value)
  }
  if (key === 'staff') fetchStaff()
  if (key === 'stores') fetchStores()
  if (key === 'users') fetchUsers()
  if (key === 'coupons') fetchCoupons()
  if (key === 'promotions') fetchPromotions()
  if (key === 'stat') fetchStats()
  if (key === 'chat') {
    router.push('/chat')
  }
}

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

const testFetchOrder = () => {
  orderList.value.push({
    id: 1,
    status: 'pending',
    payStatus: 'unpaid',
    orderTime: new Date('2023-10-01 12:00:00'),
    total: 100.00,
    list: [
      {
        id: 1,
        name: '测试商品',
        quantity: 2,
        unit: 50.00,
        total: 100.00
      },
      {
        id: 2,
        name: '测试商品2',
        quantity: 1,
        unit: 50.00,
        total: 50.00
      }
    ]
  })
}

const testFetchEmployee = () => {
  staffList.value.push({
    id: 1,
    username: 'testuser',
    name: '测试员工',
    phone: '1234567890',
    status: 'active',
    createTime: new Date('2023-10-01 12:00:00'),
    updateTime: new Date('2023-10-01 12:00:00')
  })
}

onMounted(() => {
  const storedUsername = localStorage.getItem('username');
  if (storedUsername) {
    currentEmployee.value.username = storedUsername;
  }
  // 默认加载员工列表
  fetchStaff()
  //testFetchEmployee() // 测试数据
  fetchUsers()
  fetchStores()
  fetchCoupons()
  fetchPromotions()
  fetchStats()
  fetchOrderPage(orderPage.value, orderPageSize.value)
  //testFetchOrder() // 测试数据
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
.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}
.sub-header {
  background-color: #f5f7fa;
  padding: 10px 20px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  border-bottom: 1px solid #ebeef5;
}
</style>