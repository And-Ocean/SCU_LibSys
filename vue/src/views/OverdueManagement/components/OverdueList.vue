<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
      <el-form-item>
        <el-button type="primary" @click="onOverdueRequest">一键支付</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="filterTableRef" v-loading="loading" class="table-list" row-key="lend_time" :data="tableData" style="width: 100%">
      <el-table-column prop="lend_time" label="借书时间" sortable width="180" column-key="lend_time"></el-table-column>
      <el-table-column prop="book_id" label="书籍ID" width="180"></el-table-column>
      <el-table-column prop="return_time" label="应还时间" width="180"></el-table-column>
      <el-table-column prop="user_id" label="用户ID" width="180"></el-table-column>
      <el-table-column prop="returned" label="是否归还" width="100"></el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="small" placeholder="搜索" />
        </template>
        <template #default="scope">
          <el-button size="small" @click="modifyPop(scope.row)">修改</el-button>
          <el-button size="small" @click="detailPop(scope.row)">查看详情</el-button>
          <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="modifyFormVisible" title="修改借书记录">
      <el-form :model="form">
        <el-form-item label="书籍ID" :label-width="formLabelWidth">
          <el-input v-model="form.book_id" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="应还时间" :label-width="formLabelWidth">
          <el-input v-model="form.return_time" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户ID" :label-width="formLabelWidth">
          <el-input v-model="form.user_id" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="是否归还" :label-width="formLabelWidth">
          <el-input v-model="form.returned" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEdit">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-model="detailFormVisible" title="借书记录详情">
      <el-form :model="form">
        <el-form-item label="书籍ID" :label-width="formLabelWidth">
          {{ form.book_id }}
        </el-form-item>
        <el-form-item label="应还时间" :label-width="formLabelWidth">
          {{ form.return_time }}
        </el-form-item>
        <el-form-item label="用户ID" :label-width="formLabelWidth">
          {{ form.user_id }}
        </el-form-item>
        <el-form-item label="是否归还" :label-width="formLabelWidth">
          {{ form.returned }}
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailFormVisible = false">确定</el-button>
      </div>
    </el-dialog>

    <el-pagination
        :hide-on-single-page="true"
        :current-page="currentPage"
        :page-sizes="[5, 10, 15, 20, 25]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    >
    </el-pagination>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref, toRefs } from 'vue'
import { useRouter } from 'vue-router'
import permission from '@/directive/permission'
import { ElMessage } from 'element-plus'
import Service from '../api/index'

interface Record {
  book_id: string;
  user_id: string;
  lend_time: string;
  return_time: string;
  returned: string;
}

export default defineComponent({
  name: 'OverdueList',
  directives: {
    permission
  },
  setup() {
    const router = useRouter()
    const filterTableRef = ref()
    const state = reactive<{
      loading: boolean;
      tableData: Record[];
      currentPage: number;
      pageSize: number;
      search: string;
      modifyFormVisible: boolean;
      detailFormVisible: boolean;
      form: Record;
    }>({
      loading: false,
      tableData: [],
      currentPage: 1,
      pageSize: 5,
      search: '',
      modifyFormVisible: false,
      detailFormVisible: false,
      form: {
        book_id: '',
        user_id: '',
        lend_time: '',
        return_time: '',
        returned: ''
      }
    });
    const formInline = reactive({
      user: '',
      region: ''
    })
    const formLabelWidth = '120px'
    const total = 1

    onMounted(() => {
      getOverdueList()
    })

    // methods
    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('lend_time')
    }

    const getOverdueList = () => {
      try {
        Service.postQueryOverdueList().then((res) => {
          if (res) {
            state.tableData = []
            const { data } = res
            for (let i = 0; i < data.length; i++) {
              const record = {
                book_id: data[i].book_id,
                user_id: data[i].user_id,
                lend_time: data[i].lend_time,
                return_time: data[i].return_time,
                returned: data[i].returned
              }
              state.tableData.push(record)
            }
          } else {
            console.log('postQueryOverdueList RES MISS')
          }
        })
      } catch (err) {
        if (err instanceof Error) {
          ElMessage({
            type: 'warning',
            message: err.message
          })
        } else {
          console.error("An unexpected error occurred:", err);
        }
      }
    }


    const clearFilter = () => {
      filterTableRef.value.clearFilter()
    }

    const modifyPop = (row: any) => {
      state.modifyFormVisible = true
      state.form = row
    }

    const detailPop = (row: any) => {
      state.detailFormVisible = true
      state.form = row
    }

    const handleEdit = () => {
      state.modifyFormVisible = false
      const record = { ...state.form } // 复制表单数据
      state.form = { // 初始化表单对象，确保包含所有期望的属性
        book_id: '',
        user_id: '',
        lend_time: '',
        return_time: '',
        returned: ''
      }
      try {
        Service.postUpdateOverdueRecord(record).then((res) => {
          if (res) {
            getOverdueList()
          }
        })
      } catch (err) {
        if (err instanceof Error) {
          ElMessage({
            type: 'warning',
            message: err.message
          })
        } else {
          console.error("An unexpected error occurred:", err);
        }
      }
    }



    const handleDelete = (index: any, row: any) => {
      const record = { book_id: row.book_id }
      try {
        Service.postDeleteOverdueRecord(record).then((res) => {
          if (res) {
            state.tableData.splice(index, 1)
          }
        })
      } catch (err) {
        if (err instanceof Error) {
          ElMessage({
            type: 'warning',
            message: err.message
          })
        } else {
          console.error("An unexpected error occurred:", err);
        }
      }
    }


    const handleSizeChange = (val: any) => {
      state.pageSize = val
    }

    const handleCurrentChange = (val: any) => {
      state.currentPage = val
    }

    const onOverdueRequest = () => {
      router.replace('/overdueManagement/overdueRequest')
    }

    return {
      formInline,
      formLabelWidth,
      total,
      ...toRefs(state),
      handleCurrentChange,
      handleSizeChange,
      onOverdueRequest,
      handleEdit,
      handleDelete,
      filterTableRef,
      resetDateFilter,
      clearFilter,
      modifyPop,
      detailPop
    }
  }
})
</script>

<style scoped>
.table-container {
  .form-inline {
    margin: 15px;
    text-align: left;
  }
  .table-list {
    margin: 15px;
  }
}
</style>
