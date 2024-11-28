import request from '@/utils/request'

const borrowBookApi = {
  getBooksByUserId: '/api/bookBorrow/get_books_by_user_id',
  returnBooksByLendId: '/api/bookBorrow/return_by_lend_id',
  localHost:'http://localhost:8080',
}


class Service {

  static postGetBorrowedBookByUserId(data: any) {
    return request({
      url: borrowBookApi.localHost + borrowBookApi.getBooksByUserId,
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      // console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

  static returnBook(lend_id:any) {
    return request({
      url: borrowBookApi.localHost + borrowBookApi.returnBooksByLendId,
      method: 'POST',
      json: true,
      data: {lend_id: lend_id},
    }).then((res) => {
      // console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

}
export default Service
