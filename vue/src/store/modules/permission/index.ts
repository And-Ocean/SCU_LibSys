/* eslint-disable no-restricted-syntax */
import { Module } from 'vuex'
import { RouteRecordRaw } from 'vue-router'
import router, { constantRoutes, asyncRoutes } from '@/router'
import permissionStateTypes from './types'
import RootStateTypes from '../../types'
import Service from './api'

const email = localStorage.getItem('email') || ''
const username = localStorage.getItem('username') || ''
const nickname = localStorage.getItem('nickname') || ''
const role = localStorage.getItem('role') || ''
const address = localStorage.getItem('department') || ''
const sex = localStorage.getItem('sex') || ''
const phone = localStorage.getItem('phone') || ''
const avatar = localStorage.getItem('avatar') || ''
// create a new Store Modules.
const permissionModule: Module<permissionStateTypes, RootStateTypes> = {
  namespaced: true,
  state: {
    email,//邮箱
    username,//账号
    nickname,//用户名
    role, // 用户包含的角色,
    address,//用户地址
    sex,//用户性别
    phone,// 用户手机号
    avatar,//用户头像
    //暂时不用permissions: [],
    accessRoutes: constantRoutes, // 可访问路由集合
    routes: constantRoutes, // 所有路由集合
    authedRoutes: []
  },
  mutations: {
    setEmail: (state: permissionStateTypes, { userEmail }) => {
      state.email = userEmail
      console.log(state.email)
    },
    setUsername: (state: permissionStateTypes, { userName }) => {
      state.username = userName
      console.log(state.username)
    },
    setNickname: (state: permissionStateTypes, { nickName }) => {
      state.nickname = nickName
      console.log(state.nickname)
    },
    setRoles: (state: permissionStateTypes, { roleName }) => {
      state.role = roleName
      console.log(state.role)
    },
    setAddress: (state: permissionStateTypes, { userAddress }) => {
      state.address = userAddress
      console.log(state.address)
    },
    setSex: (state: permissionStateTypes, { userSex }) => {
      state.sex = userSex
      console.log(state.sex)
    },
    setPhone: (state: permissionStateTypes, { userPhone }) => {
      state.phone = userPhone
      console.log(state.phone)
    },
    setAvatar: (state: permissionStateTypes, { userAvatar }) => {
      state.avatar = userAvatar
      console.log(state.avatar)
    },
    setAccessRoutes: (state: permissionStateTypes, routes) => {
      state.accessRoutes = constantRoutes.concat(routes)
    },
    setRoutes: (state: permissionStateTypes, routes) => {
      state.routes = constantRoutes.concat(routes)
    },
    setAuthedRoutes: (state: permissionStateTypes, authedRoutes: string[]) => {
      state.authedRoutes = authedRoutes
      localStorage.setItem('authedRoutes', JSON.stringify(authedRoutes))
    },
    /**
    setPermissions: (state: permissionStateTypes, permissions: string[]) => {
      state.permissions = permissions
    }
     */
  },
  actions: {
    // 异步接口请求，动态添加路由
    getPermissonRoutes({ commit }, payload: any) {
      // api request
      const data = {
        roleName: payload.roleName,
      }
      // 后端根据角色名称，查询授权菜单
      Service.postAuthPermission(data).then((res) => {
        const { authedRoutes } = res.data
        commit('setAuthedRoutes', authedRoutes)
        // 过滤只显示授权菜单
        const accessedRoutes: RouteRecordRaw[] = []

        for (const path of authedRoutes) {
          for (const item of asyncRoutes) {
            if (item.path === path) {
              accessedRoutes.push(item)
            }
          }
        }
        // 动态添加路由  vue-router4.x 暂时没有addRoutes
        router.isReady().then(() => {
          accessedRoutes.forEach((route: RouteRecordRaw) => {
            const routeName: any = route.name
            if (!router.hasRoute(routeName)) {
              router.addRoute(route)
            }
          })
          router.options.routes = constantRoutes.concat(asyncRoutes)
          console.log(router)
          commit('setAccessRoutes', accessedRoutes)
        })
      })
    },
    /**
    getPermissions({ commit }) {
      // 后端根据角色名称，查询授权菜单
      Service.postPermissions({}).then((res) => {
        const { permissions } = res.data
        commit('setPermissions', permissions)
      })
    },
    */
    getRoutes({ commit }) {
      // api request
      // 动态添加路由  vue-router4.x 暂时没有addRoutes
      if (localStorage.getItem('authedRoutes')) {
        const authedRoutes = JSON.parse(localStorage.getItem('authedRoutes') as string)
        const accessedRoutes: RouteRecordRaw[] = []
        for (const path of authedRoutes) {
          for (const item of asyncRoutes) {
            if (item.path === path) {
              accessedRoutes.push(item)
            }
          }
        }
        accessedRoutes.forEach((route: RouteRecordRaw) => {
          const routeName: any = route.name
          if (!router.hasRoute(routeName)) {
            router.addRoute(route)
          }
        })
      }

      commit('setRoutes', asyncRoutes)
    },
    // 授权角色
    getPermissonRoles({ commit }, payload: any) {
      // api request
      localStorage.setItem('role', payload.roleName)
      commit('setRoles', payload)

    },
    getUserInfos({ commit }, payload: any) {
      if('userEmail' in payload){
        localStorage.setItem('email', payload.userEmail);
        commit('setEmail', payload);
      }
      if('userName' in payload){
        localStorage.setItem('username', payload.userName);
        commit('setUsername', payload);
      }
      if ('nickName' in payload) {
        localStorage.setItem('nickname', payload.nickName);
        commit('setNickname', payload);
      }
      if ('userSex' in payload) {
        localStorage.setItem('sex', payload.userSex);
        commit('setSex', payload);
      }
      if ('userPhone' in payload) {
        localStorage.setItem('phone', payload.userPhone);
        commit('setPhone', payload);
      }
      if ('userAddress' in payload) {
        localStorage.setItem('address', payload.userAddress);
        commit('setAddress', payload);
      }
      if ('userAvatar' in payload) {
        localStorage.setItem('avatar', payload.userAvatar);
        commit('setAvatar', payload);
      }
    }

  },
  getters: {
    getAccessRoutes(state: permissionStateTypes) {
      return state.routes
    },
    authedRoutes(state: permissionStateTypes) {
      return state.authedRoutes
    },
    getEmail(state: permissionStateTypes) {
      return state.email
    },
    getUsername(state: permissionStateTypes) {
      return state.username
    },
    getNickname(state: permissionStateTypes) {
      return state.nickname
    },
    getRoles(state: permissionStateTypes) {
      return state.role
    },
    getSex(state: permissionStateTypes) {
      return state.sex
    },
    getAddress(state: permissionStateTypes) {
      return state.address
    },
    getPhone(state: permissionStateTypes) {
      return state.phone
    },
    getAvatar(state: permissionStateTypes) {
      return state.avatar
    },
    /**
    getPermission(state: permissionStateTypes) {
      return state.permissions
    }
     */
  }
}
export default permissionModule
