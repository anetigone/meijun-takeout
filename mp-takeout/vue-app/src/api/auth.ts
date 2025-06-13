import request from '../utils/request';
import type { AuthLoginDTO, AuthLoginVo, KaptchaVO } from './types';
import type {ApiResponse} from "./api-response.ts";

export const login = (data: AuthLoginDTO) =>
  request.post<ApiResponse<AuthLoginVo>>('/auth/login', data);

export const getCaptchaBase64 = () =>
  request.get<ApiResponse<KaptchaVO>>('/auth/base64-captcha');

export const logoutApi = () =>
    request.post('/auth/logout');
