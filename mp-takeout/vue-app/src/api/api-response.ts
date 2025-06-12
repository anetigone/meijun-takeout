export interface ApiResponse<T = any>  {
  code: number;
  data: T;
  msg: string;
}

export interface PageResult<T = any> {
  total: number;
  records: T[];
  current: number;
  size: number;
  code?: number;
  msg?: string;
}