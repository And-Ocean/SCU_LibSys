import request from '@/utils/request'

const borrowBookApi = {
  getBooksByUserId: '/api/bookBorrow/get_books_by_user_id',
  returnBooksByLendId: '/api/bookBorrow/return_by_lend_id',
  getBooksAll: '/api/bookBorrow/get_all_records',
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

  static postGetBorrowedBook() {
    return request({
      url: borrowBookApi.localHost + borrowBookApi.getBooksAll,
      method: 'POST',
      json: true,
      data: {}
    }).then((res) => {
      console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

  static returnBook(row:any) {
     // console.log(row)
    const data = {data:[row.lend_id, row.isbn]}
    return request({
      url: borrowBookApi.localHost + borrowBookApi.returnBooksByLendId,
      method: 'POST',
      json: true,
      data: data,
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
