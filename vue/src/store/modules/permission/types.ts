import { RouteRecordRaw } from 'vue-router'

export default interface permissionStateTypes {
  email:String
  username: String
  nickname: String
  role: String
  address: String
  sex: String
  phone: String
  avatar: String
  //permissions: String[]
  accessRoutes: Array<RouteRecordRaw>
  routes: Array<RouteRecordRaw>
  authedRoutes: String[]
  // eslint-disable-next-line semi
}
