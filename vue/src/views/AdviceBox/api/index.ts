import request from '@/utils/request'

const todolistApi = {
  getTodolist: '/api/advice/getRec',
  localHost:'http://localhost:8080',
  modifyTodolist: '/api/advice/createRec',
  addTodolist: '/api/todolist/add',
  deleteTodo: '/api/advice/deleteTodo',
  modifyTodolistActually: '/api/advice/modifyRec',
}


class Service {
  /**
   * @description POST 用户登录接口
   */
  static postGetTodoList() {
    return request({
      url: todolistApi.localHost + todolistApi.getTodolist,
      method: 'POST',
      json: true,
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
    if (record.reply_ctnt != "无") {
      record.replied = "已回复"
    }
    record.replyer_id = -1
    console.log(record)
    return request({
      url: todolistApi.localHost + todolistApi.modifyTodolistActually,
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
