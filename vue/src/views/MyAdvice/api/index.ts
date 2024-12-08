import request from '@/utils/request'

const todolistApi = {
  getTodolist: '/api/advice/stuGetRec',
  localHost:'http://localhost:8080',
  modifyTodolist: '/api/advice/createRec',
  addTodolist: '/api/todolist/add',
  deleteTodo: '/api/advice/deleteTodo'
}


class Service {
  /**
   * @description POST 用户登录接口
   */
  static postGetTodoList() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: todolistApi.localHost + todolistApi.getTodolist,
      method: 'POST',
      json: true,
      data:data,
    }).then((res) => {
      if (res.status === 0) {
        console.log("postGetTodoList success")
        return res
      }
      return null
    })
  }

  static postModifyTodo(record:any) {
    record.accessToken = sessionStorage.getItem('accessToken')
    console.log(record)
    return request({
      url: todolistApi.localHost + todolistApi.modifyTodolist,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static deleteTodo(record:any) {
    return request({
      url: todolistApi.localHost + todolistApi.deleteTodo,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static addTodo(record:any) {
    record.todo_fin = '未完成'
    return request({
      url: todolistApi.localHost + todolistApi.addTodolist,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }
}
export default Service
