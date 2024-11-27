<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
      <el-form-item>
        <el-button type="primary" @click="onOverdueRequest">一键支付</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="filterTableRef" v-loading="loading" class="table-list" row-key="date" :data="tableData" style="width: 100%">
      <el-table-column prop="due_date" label="应还日期" sortable width="180" column-key="due_date"></el-table-column>
      <el-table-column prop="book_id" label="书籍ID" width="180" truncated> </el-table-column>
      <el-table-column prop="return_date" label="实际还书日期" truncated> </el-table-column>
      <el-table-column prop="reader_id" label="罚款金额" truncated> </el-table-column>
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
      <el-table-column
        prop="overdue_status"
        label="处理状态"
        width="100"
        :filters="[
          { text: '已处理', value: '已处理' },
          { text: '未处理', value: '未处理' }
        ]"
        :filter-method="filterStatus"
        filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag :type="scope.row.status === '已处理' ? 'primary' : 'success'" disable-transitions>{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="modifyFormVisible" title="修改逾期记录">
      <el-form :model="form">
        <el-form-item label="书籍ID" :label-width="formLabelWidth">
          <el-input v-model="form.book_id" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="应还日期" :label-width="formLabelWidth">
          <el-input v-model="form.due_date" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="罚款金额" :label-width="formLabelWidth">
          <el-input v-model="form.reader_id" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="实际还书日期" :label-width="formLabelWidth">
          <el-input v-model="form.return_date" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="处理状态" :label-width="formLabelWidth">
          <el-input v-model="form.status" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEdit">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-model="detailFormVisible" title="逾期记录详情">
      <el-form :model="form">
        <el-form-item label="书籍ID" :label-width="formLabelWidth">
          {{ form.book_id }}
        </el-form-item>
        <el-form-item label="罚款金额" :label-width="formLabelWidth">
          {{ form.reader_id }}
        </el-form-item>
        <el-form-item label="应还日期" :label-width="formLabelWidth">
          {{ form.due_date }}
        </el-form-item>
        <el-form-item label="实际还书日期" :label-width="formLabelWidth">
          {{ form.return_date }}
        </el-form-item>
        <el-form-item label="处理状态" :label-width="formLabelWidth">
          {{ form.status }}
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
import { computed, defineComponent, onMounted, reactive, ref, toRefs } from 'vue'
import { useRouter } from 'vue-router'
import permission from '@/directive/permission'
import { ElMessage } from 'element-plus'
import Service from '../api/index'

export default defineComponent({
  name: 'OverdueList',
  directives: {
    permission
  },
  setup() {
    const router = useRouter()
    const filterTableRef = ref()
    const state = reactive({
      loading: false,
      tableData: [],
      currentPage: 1,
      pageSize: 5,
      search: '',
      modifyFormVisible: false,
      detailFormVisible: false,
      form: {}
    })
    const formInline = reactive({
      user: '',
      region: ''
    })
    const total = 1

    onMounted(() => {
      getOverdueList()
    })

    // methods
    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('date')
    }

    const getOverdueList = () => {
      try {
        Service.postQueryOverdueList().then((res) => {
          if (res) {
            state.tableData = []
            const { data } = res
            for (let i = 0; i < data.length; i++) {
              const record = {
                book_id: data[i].bookId,
                reader_id: data[i].readerId,
                due_date: data[i].dueDate,
                return_date: data[i].returnDate,
                status: data[i].status
              }
              state.tableData.push(record)
            }
          } else {
            console.log('postQueryOverdueList RES MISS')
          }
        })
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
    }

    const clearFilter = () => {
      filterTableRef.value.clearFilter()
    }

    const filterStatus = (value: any, row: { status: any }) => row.status === value

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
      const record = state.form
      state.form = {}
      try {
        Service.postUpdateOverdueRecord(record).then((res) => {
          if (res) {
            getOverdueList()
          }
        })
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
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
        ElMessage({
          type: 'warning',
          message: err.message
        })
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
      filterStatus,
      modifyPop,
      detailPop
    }
  }
})
</script>

<style lang="stylus" scoped>
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
