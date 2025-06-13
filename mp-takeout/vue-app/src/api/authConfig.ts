export interface AuthConfig {
    token: string;
    uuid: string;
    userType?: string; // 0: 管理员, 1: 商家, 2: 员工
    username: string;
    expireTime: number;
}