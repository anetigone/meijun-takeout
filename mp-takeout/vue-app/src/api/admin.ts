import request from '../utils/request'
import type { Admin, AdminPageQueryDTO, AdminSaveDTO, AdminUpdateDTO, OrderOverviewVO, WorkspaceDataVO, Employee, Product } from './types'
import type { ApiResponse, PageResult } from './api-response'

// 获取所有管理员
export const getAdmins = () =>
  request.get<ApiResponse<Admin[]>>('/admin/all')

// 删除管理员
export const removeAdmin = (id: number) =>
  request.delete<ApiResponse<any>>(`/admin/delete/${id}`)

// 新增管理员
export const addAdmin = (data: AdminSaveDTO) =>
  request.post<ApiResponse<any>>('/admin/add', data)

// 更新管理员
export const upgradeAdmin = (data: AdminUpdateDTO) =>
  request.put<ApiResponse<any>>('/admin/update', data)

// 分页查询管理员（如有分页接口）
export const getAdminsByPage = (params: AdminPageQueryDTO) =>
  request.get<PageResult<Admin>>('/admin/page', { params })

export const getWorkspaceData = () =>
  request.get<ApiResponse<WorkspaceDataVO>>('/admin/workspace/data')

export const getAdminById = (id: number) =>
  request.get<ApiResponse<Admin>>(`/admin/${id}`)

export const getOrderOverview = () =>
  request.get<ApiResponse<OrderOverviewVO>>('/admin/workspace/order/overview')

export const getEmployees = () =>
  request.get<ApiResponse<Employee[]>>('/admin/employee/all')
export const removeEmployee = (id: number) =>
  request.delete<ApiResponse<any>>(`/admin/employee/${id}/delete`)

export const addEmployee = (data: AdminSaveDTO) =>
  request.post<ApiResponse<any>>('/admin/employee/add', data)

export const getEmployeeById = (id: number) =>
  request.get<ApiResponse<Employee>>(`/admin/employee/${id}`)

export const upgradeEmployee = (data: AdminUpdateDTO) =>
  request.put<ApiResponse<any>>('/admin/employee/update', data)

export const getEmployeesByPage = (params: AdminPageQueryDTO) =>
  request.get<PageResult<Employee>>('/admin/employee/page', { params })

export const getSales = () =>
  request.get<ApiResponse<Product[]>>('/admin/sales')

export const getTraffic = () =>
  request.get<ApiResponse<number>>('/admin/traffic')

export const getSalesTotal = () =>
  request.get<ApiResponse<number>>('/admin/sales/total')
