import request from '@/utils/request'

const bookApi = {
  localHost:'http://localhost:8080',
  getBookIsbn: '/api/book/getBookIsbn',
  modifyBookIsbn: '/api/book/modifyBookISBN',
  addBookIsbn: '/api/book/addBookISBN',
  deleteBookIsbn: '/api/book/deleteBookISBN',
  getBookEntity: '/api/book/getBookEntity',
  modifyBookEntity: '/api/book/modifyBookEntity',
  addBookEntity: '/api/book/addBookEntity',
  deleteBookEntity: '/api/book/deleteBookEntity',
  borrowBook: '/api/bookBorrow/borrowBook',
  borrowABookById: '/api/bookBorrow/borrowABookById',
}


class Service {
  /**
   * @description POST 用户登录接口
   */
  static postGetBookIsbn() {
    return request({
      url: bookApi.localHost + bookApi.getBookIsbn,
      method: 'POST',
      json: true,
    }).then((res) => {
      if (res.status === 0) {
        console.log("postGetBookIsbn success")
        return res
      }
      return null
    })
  }

  static postModifyBookIsbn(record:any) {
    let bookIsbn = {
      isbn: record.isbn ,
      title: record.title ,
      price: record.price ,
      author: record.author ,
      publisher: record.pulisher ,
      borrownum: record.borrownum
    }
    return request({
      url: bookApi.localHost + bookApi.modifyBookIsbn,
      method: 'POST',
      json: true,
      data: bookIsbn,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static deleteBookIsbn(record:any) {
    return request({
      url: bookApi.localHost + bookApi.deleteBookIsbn,
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

  static addBookIsbn(record:any) {
    return request({
      url: bookApi.localHost + bookApi.addBookIsbn,
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

  static postGetBookEntity() {
    return request({
      url: bookApi.localHost + bookApi.getBookEntity,
      method: 'POST',
      json: true,
    }).then((res) => {
      if (res.status === 0) {
        console.log("postGetBookEntity success")
        return res
      }
      return null
    })
  }

  static postModifyBookEntity(record:any) {
    let bookEntity = {
      id: record.id,
      isbn: record.isbn,
      status: record.status,
      place: record.place
      }
    return request({
      url: bookApi.localHost + bookApi.modifyBookEntity,
      method: 'POST',
      json: true,
      data: bookEntity,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static deleteBookEntity(record:any) {
    return request({
      url: bookApi.localHost + bookApi.deleteBookEntity,
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

  static addBookEntity(record:any) {
    return request({
      url: bookApi.localHost + bookApi.addBookEntity,
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

  static getBorrowableBooks(record:any) {
    let data = {isbn:record.isbn, accessToken:sessionStorage.getItem('accessToken')}
    // console.log(data)
    return request({
      url: bookApi.localHost + bookApi.borrowBook,
      method: 'POST',
      json: true,
      data: data,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static borrowABookHandle(item: any) {
    item.accessToken = sessionStorage.getItem('accessToken')
    console.log(item)
    return request({
      url: bookApi.localHost + bookApi.borrowABookById,
      method: 'POST',
      json: true,
      data: item,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

}
export default Service
