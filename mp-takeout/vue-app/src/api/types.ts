export interface AuthLoginDTO {
  username: string;
  password: string;
  code: string;
  identity: string;
  kaptchaUuid: string;
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
