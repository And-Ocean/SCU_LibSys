import request from '@/utils/request';

const overdueManagementApi = {
  getOverdueList: '/api/overdueManagement/getOverdueList',
  localHost: 'http://localhost:8080',
  modifyOverdueRecord: '/api/overdueManagement/modifyOverdueRecord',
  addOverdueRecord: '/api/overdueManagement/addOverdueRecord',
  deleteOverdueRecord: '/api/overdueManagement/deleteOverdueRecord'
}

class Service {
  static postQueryOverdueList() {
    return request({
      url: overdueManagementApi.localHost + overdueManagementApi.getOverdueList,
      method: 'POST',
      json: true
    }).then((res) => {
      if (res.status === 0) {
        console.log('postQueryOverdueList success');
        return res;
      }
      return null;
    });
  }

  static postAddOverdueRecord(record: any) {
    return request({
      url: overdueManagementApi.localHost + overdueManagementApi.addOverdueRecord,
      method: 'POST',
      json: true,
      data: record
    }).then((res) => {
      if (res.status === 0) {
        return res;
      }
      return null;
    });
  }

  static postUpdateOverdueRecord(record: any) {
    return request({
      url: overdueManagementApi.localHost + overdueManagementApi.modifyOverdueRecord,
      method: 'POST',
      json: true,
      data: record
    }).then((res) => {
      if (res.status === 0) {
        return res;
      }
      return null;
    });
  }

  static postDeleteOverdueRecord(record: any) {
    return request({
      url: overdueManagementApi.localHost + overdueManagementApi.deleteOverdueRecord,
      method: 'POST',
      json: true,
      data: record
    }).then((res) => {
      if (res.status === 0) {
        return res;
      }
      return null;
    });
  }
}

// 导出 Service 类
export default Service;
