export interface AuthLoginDTO {
  username: string;
  password: string;
  code: string;
  identity: string;
  uuid: string;
}

export interface AuthLoginVo {
  id: number;
  username: string;
  name: string;
  uuid: string;
  token: string;
}

export interface KaptchaVO {
  uuid: string;
  code: string;
}

export interface Admin {
  id?: number;
  uuid?: string;
  username?: string;
  password?: string;
  email?: string;
  role?: string;
  createTime?: Date;
  updateTime?: Date;
}

export interface AdminPageQueryDTO {
  pageNum: number;
  pageSize: number;
}

export interface AdminUpdateDTO {
  id?: number;
  username?: string;
  password?: string;
  email?: string;
}

export interface AdminSaveDTO {
  username: string;
  password: string;
  email: string;
}

export interface Employee {
  id?: number;
  uuid?: string;
  username?: string;
  password?: string;
  name?: string;
  phone?: string;
  status?: string;
  createTime?: Date;
  updateTime?: Date;
}

export interface EmployeePageQueryDTO {
  pageNum: number;
  pageSize: number;
}

export interface EmployeeSaveDTO {
  username: string;
  password: string;
  name: string;
  phone: string;
}

export interface EmployeeUpdateDTO {
  id: number;
  username?: string;
  password?: string;
  name?: string;
  phone?: string;
  status?: string;
}

export interface Product {
  id?: number;
  name?: string;
  type?: string;
  sales?: number;
}

export interface WorkspaceDataVO {
  turnover?: number;
  validOrderCount?: number;
  orderCompletionRate?: number;
  unitPrice?: number;
  newUsers?: number;
}

export interface OrderOverviewVO {
  waitingOrders?: number;
  deliveredOrders?: number;
  completedOrders?: number;
  cancelledOrders?: number;
  allOrders?: number;
}

export interface OrderDetail {
  id?: number
  name?: string
  orderId?: number
  itemId?: number
  itemType?: string
  unit?: number
  quantity?: number
  total?: number
}

export interface Order {
  id?: number;
  status?: string;
  payStatus?: string;
  total?: number;
  orderTime?: Date;
  orderNumber?: string;
  remark?: string;
  list?: OrderDetail[]
}

export interface Store {
  id?: number;
  name?: string;
  address?: string;
}

export interface Customer {
  id?: number;
  uuid?: string;
  username?: string;
  password?: string;
  name?: string;
  phone?: string;
  email?: string;
  avatarUrl?: string;
  balance?: number;
  createTime?: Date;
  updateTime?: Date;
}

export interface Coupon {
  id?: number;
  userId?: number;
  name?: string;
  description?: string;
  type?: string;
  value?: number;
  minAmount?: number;
  maxAmount?: number;
  startTime?: Date;
  endTime?: Date;
  createTime?: Date;
  updateTime?: Date;
}

export interface Promotion {
  id?: number;
  name?: string;
  description?: string;
  imgUrl?: string;
  startTime?: Date;
  endTime?: Date;
  createTime?: Date;
  updateTime?: Date;
}

export interface AfterSale {
  id?: number;
  orderId?: number;
  userId?: number;
  type?: string;
  reason?: string;
  content?: string;
  status?: string;
  createTime?: string;
  updateTime?: string;
}

export interface ApproveDTO {
  userId?: number;
  RequestId?: number;
}

export interface CouponDTO {
  userId?: number;
  name?: string;
  couponType?: string;
  description?: string;
  value?: number;
  startTime?: Date;
  endTime?: Date;
}

export interface DistributeCouponDTO {
  userId?: number;
  couponId?: number;
  customerIds?: number[];
}

export interface PromotionDTO {
  userId?: number;
  name?: string;
  description?: string;
  imgUrl?: string;
  startTime?: Date;
  endTime?: Date;
}

export interface StoreDTO {
  merchantId?: number;
  name?: string;
  address?: string;
}

export interface OrderCancelDTO {
  id?: number;
  userId?: number;
  orderId?: number;
  reason?: string;
}

export interface OrderConfirmDTO {
  id?: number;
  status?: string;
  storeId?: number;
  address?: string;
  contact?: string;
  contactPhone?: string;
  remark?: string;
}

export interface OrderRejectionDTO {
  id?: number;
  userId?: number;
  orderId?: number;
  reason?: string;
}

export interface OrderRefundDTO {
  userId?: number;
  orderId?: number;
  reason?: string;
}

export interface PromotionVO {
  promotionId?: number
  status?: string
}