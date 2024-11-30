<template>
  <div>
    <el-card class="card-ctrl">
      <el-row>
        <el-col :span="8" style="text-align: left">
          <el-button type="primary" size="small" @click="onCreate">
            <el-icon><plus /></el-icon>
            新增</el-button
          >
        </el-col>
        <el-col :span="16" style="text-align: right">
          <el-input
              v-model="searchKeyword"
              placeholder="请输入关键词"            style="width: 200px; margin-right: 10px"
          ></el-input>
          <el-button type="primary" size="small" @click="onSearch">搜索</el-button>
        </el-col>
      </el-row>
      <br />
      <el-table v-loading="loading" :data="data" stripe class="table">
        <el-table-column prop="userName" label="学号" align="center"></el-table-column>
        <el-table-column prop="nickName" label="用户名" align="center"></el-table-column>
        <el-table-column prop="userSex" label="性别" align="center"></el-table-column>
        <el-table-column prop="userPhone" label="电话号码" align="center"></el-table-column>
        <el-table-column prop="userAddress" label="个人住址" align="center"></el-table-column>


        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-tooltip class="item" effect="dark" content="信息修改" placement="bottom">
              <el-button circle plain type="primary" size="small" @click="onEdit(scope.$index, scope.row)">
                <el-icon><edit /></el-icon>
              </el-button>
            </el-tooltip>
            <el-tooltip  class="item" effect="dark" content="删除" placement="bottom">
              <el-button circle plain type="danger" size="small" @click="onDelete(scope.$index, scope.row)">
                <el-icon><minus /></el-icon>
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          :current-page="param.page"
          :page-size="param.limit"
          layout="sizes,prev,pager,next,total"
          :page-sizes="[5, 10, 20]"
          :total="total"
          background
          @current-change="onCurrentChange"
          @size-change="onSizeChange"
        >
        </el-pagination>
      </div>
    </el-card>

    <el-dialog v-model="edit_visible" center :title="posted.userRow.nickName">
      <role-edit :current-row="posted.userRow" @success="onEditSuccess"></role-edit>
    </el-dialog>
    <el-dialog v-model="add_visible" title="新增员工">
      <role-new @success="onCreateSuccess"></role-new>
    </el-dialog>
  </div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Minus, Plus, Refresh } from '@element-plus/icons-vue'
import RoleEdit from './rolesEdit.vue'
import RoleNew from './rolesNew.vue'
import Service from './api/index'
const useConfirmDelete = async (row: any) => {
  return new Promise((resolve, reject) => {
    ElMessageBox.confirm('此操作将删除该用户所有数据, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
        .then(async () => {
          // 此处执行接口异步删除员工
          const data = {
            userName: row.userName,
            accessToken: sessionStorage.getItem('accessToken')
          };
          const res = await Service.postAdminDeleteUser(data);
          if (res.status === 0) {
            ElMessage({
              type: 'success',
              message: '删除成功'
            });
          } else {
            ElMessage({
              type: 'error',
              message: '删除失败'
            });
            reject();
          }
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '已取消删除'
          });
          reject();
        });
  });
};
export default defineComponent({
  name: 'RoleManage',
  components: {
    RoleEdit,
    RoleNew,
    Edit,
    Minus,
    Plus,
    Refresh
  },
  setup() {
    const state = reactive({
      url: {
        c: '/role/add',
        r: '/role/list',
        u: '/role/update',
        d: '/role/delete'
      },
      param: {
        limit: 10,
        page: 1,
      },
      data: [
        //{ userName: '超级管理员', userDepartment:'',userRole:'',userPhone:''},
      ],
      loading: false,
      is_search: false,
      add_visible: false,
      edit_visible: false,
      detail_visible: false,
      posted: {
        userRow: {
          userName:'',
          nickName:'',
          userSex: '',
          userPhone:'',
          userAddress:''
        }
      },
      searchKeyword: '' // 添加 searchKeyword 变量
    })
    // 动态计算total;
    const total = computed(() => state.data.length)
    /**
     * @description 请求接口获取当前设置角色，默认始终有超级管理员角色
     */
    const fetchData = async() => {
      state.is_search = false
      const data = {
        'accessToken':sessionStorage.getItem('accessToken'),
        'keyword': state.searchKeyword // 添加搜索关键字
      }
      const adminUserInfo = await Service.postAdminQueryUserList(data)
      if (adminUserInfo.status === 0) {
        state.data = adminUserInfo.data
      }
    }
    const onCurrentChange = (val:number) => {
      state.param.page = val
      fetchData()
    }
    const onSizeChange = (val: number) => {
      state.param.limit = val
      fetchData()
    }
    const onCreate = () => {
      state.add_visible = true
      fetchData()
    }
    const onCreateSuccess = (val: any) => {
      state.add_visible = false
      fetchData()
    }
    const onEditSuccess = () => {
      state.edit_visible = false
      fetchData()
    }
    /**
     * @description 选择点击编辑授权角色；roleName
     */
    const onEdit = (index: any, row: any) => {
      console.log('row', row)
      state.posted.userRow.userName = row.userName
      state.posted.userRow.nickName = row.nickName
      state.posted.userRow.userSex = row.userSex
      state.posted.userRow.userPhone = row.userPhone
      state.posted.userRow.userAddress = row.userAddress
      state.edit_visible = true
    }
    const onDelete = (index: any, row: any) => {
      console.log(index, row)
      useConfirmDelete(row).then(() => {
        fetchData();
      });
    }
    const onSearch = () => {
      state.param.page = 1; // 重置页码为第一页
      fetchData();
    };

    //初始调用
    fetchData()

    return {
      ...toRefs(state),
      total,
      onCurrentChange,
      onSizeChange,
      onCreate,
      onCreateSuccess,
      onEditSuccess,
      onEdit,
      onDelete,
      onSearch,
      fetchData
    }
  }
})
</script>
<style lang="stylus" scoped></style>
