import request from '../utils/request'
import type { PageResult, ApiResponse } from './api-response'
import type { Dish } from './types'

export const getDishCategories = () => 
  request.get<ApiResponse<Dish[]>>('/dishes/categories')

export const getDishPage = (page: number, size: number) =>
  request.get<PageResult<Dish>>(`/dishes/page`, { params: { page, size } })

export const getSearchForDish = (name: string) => 
  request.get<ApiResponse<Dish[]>>(`/dishes/search`, { params: { name } })

export const getDishById = (id: number) =>
  request.get<ApiResponse<Dish>>(`/dishes/${id}`)

export const saveDish = (dish: Dish) =>
  request.post<ApiResponse<Dish>>(`/dishes/save`, dish)

export const updateDish = (dish: Dish) =>
  request.post<ApiResponse<Dish>>(`/dishes/update`, dish)