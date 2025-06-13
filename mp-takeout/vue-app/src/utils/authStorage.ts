import { type AuthConfig } from "../api/authConfig"

export const setAuth = (auth: AuthConfig) => {
    const expireAt = Date.now() + auth.expireTime;
    localStorage.setItem('token', auth.token);
    localStorage.setItem('uuid', auth.uuid);
    localStorage.setItem('userType', auth.userType?.toString() || '');
    localStorage.setItem('username', auth.username);
    localStorage.setItem('expireAt', expireAt.toString());
}

export const clearAuth = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('uuid');
    localStorage.removeItem('userType');
    localStorage.removeItem('username');
    localStorage.removeItem('expireAt');
}

export const checkAuthExpired = (): boolean => {
    const expireAt = localStorage.getItem('expireAt');
    const now = Date.now();
    if (!expireAt || now > Number(expireAt)) {
        clearAuth();
        return false;
    }
    return true;
}