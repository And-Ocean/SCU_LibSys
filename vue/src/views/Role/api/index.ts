import request from '@/utils/request'

const roleApi = {
  queryAuthedPermission: '/api/auth/permission/routes'
}

class Service {
  /**
   * @description POST 管理员查询用户信息列表
   */
  static postAdminQueryUserList(data: any) {
    return request({
      url: 'http://localhost:8080/api/admin/user/list',
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  /**
   * @description POST 修改当前用户信息
   */
  static postAdminUpdateUserInfo(data: any) {
    return request({
      url: 'http://localhost:8080/api/admin/user/update',
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  /**
   * @description POST 新增用户
   */
  static postAdminAddUser(data: any) {
    return request({
      url: 'http://localhost:8080/api/admin/user/add',
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  /**
   * @description POST 删除用户
   */
  static postAdminDeleteUser(data: any) {
    return request({
      url: 'http://localhost:8080/api/admin/user/delete',
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
}
export default Service
