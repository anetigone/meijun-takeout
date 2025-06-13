import request from '../utils/request';
import type { ApiResponse, PageResult } from './api-response';
import type { Employee, EmployeePageQueryDTO, EmployeeSaveDTO, EmployeeUpdateDTO, Order, OrderConfirmDTO, OrderCancelDTO, OrderRejectionDTO, Store, StoreDTO, Customer, AfterSale, ApproveDTO, Coupon, CouponDTO, DistributeCouponDTO, Promotion, PromotionDTO, Product, WorkspaceDataVO, OrderOverviewVO, PromotionVO } from './types';


// 获取商户订单列表
export const getOrderById = (orderId: number) =>
    request.get<ApiResponse<Order>>(`/merchants/${orderId}`);

// 修改订单状态
export const updateOrderStatus = (orderId: number, status: number) =>
    request.put<ApiResponse<Order>>(`/merchants/orders/${orderId}/status`, { status });

// 获取订单分页列表
export const getOrderPage = (pageNum: number, pageSize: number) =>
    request.get<PageResult<Order>>('/merchants/orders/page', { params: { pageNum, pageSize } });

// 接单
export const confirmOrder = (orderConfirmDTO: OrderConfirmDTO) =>
    request.put<ApiResponse<string>>('/merchants/confirm', orderConfirmDTO);

// 拒单
 export const rejectOrder = (ordersRejectionDTO: OrderRejectionDTO) => 
  request.put<ApiResponse<string>>('/merchants/rejection', ordersRejectionDTO);

// 取消订单
 export const cancelOrder = (ordersCancelDTO: OrderCancelDTO) => 
  request.put<ApiResponse<string>>('/merchants/cancel', ordersCancelDTO);

// 获取所有员工
 export const getEmployees = () => 
  request.get<ApiResponse<Employee[]>>('/merchants/staff');

 export const getEmployeeById = (id: number) =>
  request.get<ApiResponse<Employee>>(`/merchants/staff/${id}`);

// 获取员工分页
 export const getEmployeePage = (employeePageQueryDTO: EmployeePageQueryDTO) => 
  request.get<PageResult<Employee>>('/merchants/staff/page', { params: employeePageQueryDTO });

// 保存员工信息
 export const saveEmployee = (employeeDTO: EmployeeSaveDTO) => 
  request.post<ApiResponse<Employee>>('/merchants/staff', employeeDTO);

// 修改员工信息
 export const updateEmployee = (id: number, employeeDTO: EmployeeUpdateDTO) => 
  request.put<ApiResponse<string>>(`/merchants/staff/${id}`, employeeDTO);

// 删除员工
 export const deleteEmployee = (id: number) => 
  request.delete<ApiResponse<string>>(`/merchants/staff/${id}`);

// 获取门店列表
 export const getStores = () => 
  request.get<ApiResponse<Store[]>>('/merchants/stores');

// 修改门店信息
 export const updateStore = (id: number, storeDTO: StoreDTO) => 
  request.put<ApiResponse<string>>(`/merchants/stores/${id}`, storeDTO);

// 获取所有用户
export const getAllCustomers = () =>
    request.get<ApiResponse<Customer[]>>('/merchants/users');

// 获取用户分页
export const getCustomersPage = (page: number, pageSize: number) =>
  request.get<PageResult<Customer>>('/merchants/users/page', { params: { page, pageSize } });

// 搜索用户
 export const searchCustomers = (name: string) => 
  request.get<ApiResponse<Customer[]>>('/merchants/users/search', { params: { name } });

// 获取售后列表
 export const getAfterSales = () => 
  request.get<ApiResponse<AfterSale[]>>('/merchants/after-sale');

// 审批售后
 export const approveAfterSale = (approveDTO: ApproveDTO) => 
  request.post<ApiResponse<string>>('/merchants/after-sale/approve', approveDTO);

// 拒绝售后
 export const rejectAfterSale = (approveDTO: ApproveDTO) => 
  request.post<ApiResponse<string>>('/merchants/after-sale/reject', approveDTO);

// 获取优惠券列表
 export const getCoupons = () => 
  request.get<ApiResponse<Coupon[]>>('/merchants/coupons');

// 添加优惠券
 export const addCoupon = (dto: CouponDTO) => 
  request.post<ApiResponse<{ couponId: number; status: string }>>('/merchants/coupons', dto);

// 删除优惠券
 export const deleteCoupon = (couponId: number) => 
  request.delete<ApiResponse<string>>(`/merchants/coupons/${couponId}`);

// 修改优惠券
 export const updateCoupon = (couponId: number, dto: CouponDTO) => 
  request.put<ApiResponse<{ couponId: number; status: string }>>(`/merchants/coupons/${couponId}`, dto);

// 发放优惠券
 export const distributeCoupon = (dto: DistributeCouponDTO) => 
  request.post<ApiResponse<string>>('/merchants/coupons/distribute', dto);

// 获取优惠活动列表
 export const getPromotions = () => 
  request.get<ApiResponse<Promotion[]>>('/merchants/promotions');

// 添加优惠活动
 export const savePromotion = (dto: PromotionDTO) => 
  request.post<ApiResponse<{ promotionId: number; status: string }>>('/merchants/promotions', dto);

// 删除优惠活动
 export const deletePromotion = (promotionId: number) => 
  request.delete<ApiResponse<string>>(`/merchants/promotions/${promotionId}`);

// 修改优惠活动
 export const updatePromotion = (promotionId: number, dto: PromotionDTO) => 
  request.put<ApiResponse<string>>(`/merchants/promotions/${promotionId}`, dto);

// 获取销售数据
 export const getSales = () => 
  request.get<ApiResponse<Product[]>>('/merchants/sales');

// 获取流量数据
 export const getTraffic = () => 
  request.get<ApiResponse<number>>('/merchants/traffic');

// 获取销售总额
 export const getSalesTotal = () => 
  request.get<ApiResponse<number>>('/merchants/sales/total');

// 获取工作台数据
 export const getWorkspaceData = () => 
  request.get<ApiResponse<WorkspaceDataVO>>('/merchants/workspace/data');

// 获取订单概览
 export const getOrderOverview = () => 
  request.get<ApiResponse<OrderOverviewVO>>('/merchants/workspace/order/overview');