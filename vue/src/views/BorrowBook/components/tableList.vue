<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
    </el-form>
    <el-table ref="filterTableRef" class="table-list" row-key="book_id" :data="paginatedData" style="width: 100%"
              @filter-change="handleFilterChange">
      <el-table-column  width="10" ></el-table-column>
      <el-table-column
          v-if="isAdmin"
          prop="overdue"
          column-key="overdue"
          label="状态"
          width="100"
          :filters="fin_options"
          :filtered-value="filters.overdue"
          :filter-multiple="false"
          filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag :type="scope.row.overdue === 1 ? 'danger' : 'success'" disable-transitions>{{ scope.row.overdue === 1 ? "逾期": "正常" }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="书名" truncated> </el-table-column>
      <el-table-column prop="isbn" label="ISBN" truncated> </el-table-column>
      <el-table-column
          prop="return_time"
          label="应还日期"
          sortable
          column-key="return_time"
      >
      </el-table-column>
      <el-table-column v-if="!isAdmin" prop="author" label="作者" width="100"> </el-table-column>
      <el-table-column v-if="!isAdmin" prop="publisher" label="出版社"> </el-table-column>
      <el-table-column v-if="isAdmin" prop="username" label="借书用户名" width="100"> </el-table-column>
      <el-table-column v-if="isAdmin" prop="email" label="Email"> </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="输入用户名搜索" @input="watchSearch"/>
        </template>
        <template #default="scope">
          <el-button size="mini" @click="detailPop(scope.row)">书籍详情</el-button>
        </template>
      </el-table-column>
      <el-table-column v-if="isAdmin" width="120">
        <template #default="scope">
          <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定已经还书吗？" @confirm="handleReturnBook(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger">已经还书</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="detailFormVisible" title="书籍详情">
      <el-form :model="form">
        <el-form-item label="书名&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.title }}
        </el-form-item>
        <el-form-item label="封面&nbsp;&nbsp;" >
          <div class="demo-image__preview">
            <el-image
                style="width: 100px; height: 100px"
                :src="form.cover_url"
                :zoom-rate="1.2"
                :max-scale="7"
                :min-scale="0.2"
                :preview-src-list="[form.cover_url]"
                fit="cover"
            />
          </div>
        </el-form-item>
        <el-form-item v-if="isAdmin" label="用户ID&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.user_id }}
        </el-form-item>
        <el-form-item v-if="isAdmin" label="借书用户名&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.username }}
        </el-form-item>
        <el-form-item v-if="isAdmin" label="性别&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.sex }}
        </el-form-item>
        <el-form-item v-if="isAdmin" label="电话&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.phone }}
        </el-form-item>
        <el-form-item v-if="isAdmin" label="Email&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.email }}
        </el-form-item>
        <el-form-item v-if="isAdmin" label="住址&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.address }}
        </el-form-item>

        <el-form-item label="简介&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.summary }}
        </el-form-item>
        <el-form-item label="作者&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.author }}
        </el-form-item>
        <el-form-item label="ISBN&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.isbn }}
        </el-form-item>
        <el-form-item label="类别&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.category }}
        </el-form-item>
        <el-form-item label="价格&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.price }}
        </el-form-item>
        <el-form-item label="出版社&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{form.publisher }}
        </el-form-item>
        <el-form-item label="应还日期&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.return_time }}
        </el-form-item>
        <el-form-item label="借书时间&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.lend_time }}
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <el-pagination
        :current-page="currentPage"
        :page-sizes="[5, 10, 15, 20, 25]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="record_cnt"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    >
    </el-pagination>

  </div>
</template>
<script lang="ts">
import { computed, defineComponent, onMounted, reactive, ref, toRefs, watch } from 'vue'
import { useRouter } from 'vue-router'
import permission from '@/directive/permission'
import Service from '../api/index'
import {ElMessage} from "element-plus";

export default defineComponent({
  name: 'BORROWED_BOOK',
  directives: {
    permission
  },
  setup() {
    // 思考 ref 响应式和 reactive 响应式的区别； 修改对象属性值，是否会刷新数据

    const router = useRouter()
    const filterTableRef = ref()
    const state = reactive({
      tableData: [
        {
          ddl: '后台沒打开',
          title: '后台沒打开',
          status: '后台沒打开',
          content: '后台沒打开'
        },
      ],
      currentPage: 1,
      pageSize: 5,
      search: '',
      modifyFormVisible: false,
      detailFormVisible: false,
      form: {},
      isAdmin: false,  // 默认不是管理员
      paginatedData: [],
      record_cnt: 0,

      fin_options: [
        {
          value:-1,
          text:'未逾期',
        },
        {
          value:1,
          text:'已逾期',
        },
      ],
      filters: { // 筛选条件
        overdue: [] // 筛选的状态，可以是 '已完成' 或 '未完成'
      },
    })
    const formInline = reactive({
      user: '',
      region: ''
    })
    // const total = computed(() => state.tableData.length)
    // A quick fix here
    const total = 1

    onMounted(() => {
      // eslint-disable-next-line no-console
      getUserRole()
      const pageSizeTmp = sessionStorage.getItem("BOOK_BORROW_PAGE_SIZE");
      if (!pageSizeTmp) {
        sessionStorage.setItem("BOOK_BORROW_PAGE_SIZE", 5);
        state.pageSize = 5;
      } else {
        // ####parseInt#### is IMPORTANT!!!!
        state.pageSize = parseInt(pageSizeTmp, 10);
      }
      getList()

    })

    // 更新分页数据
    const updatePaginatedData = () => {
      // 过滤包含搜索关键字的书籍
      let recordsToFilter = state.tableData;

      // 如果有搜索关键词，按书名进行过滤
      if (state.search) {
        recordsToFilter = recordsToFilter.filter((record) =>
            record.username.toLowerCase().includes(state.search.toLowerCase()) // 忽略大小写
        );
      }

      if (state.filters.overdue[0] && state.filters.overdue[0] !== '') {
        console.log(state.filters.overdue[0])
        recordsToFilter = recordsToFilter.filter((record) =>
            record.overdue == state.filters.overdue[0]
        );
      }

      // 更新分页数据
      // console.log(state.pageSize)
      const start = (state.currentPage - 1) * state.pageSize;
      const end = state.currentPage * state.pageSize;
      state.paginatedData = recordsToFilter.slice(start, end);  // 根据当前页和页大小提取分页数据
      state.record_cnt = recordsToFilter.length;  // 更新总记录数
    };

    const watchSearch = () => {
      updatePaginatedData();  // 过滤数据并更新分页
    };
    watch(() => state.search, watchSearch);

    const getList = () => {
      if (!state.isAdmin) {
        getBookBorrowed()
      } else {
        getAllBorrowRecords()
      }
    }

    // methods
    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('date')
    }

    const getAllBorrowRecords = async() => {

      try {
        await Service.postGetBorrowedBook().then((res) => {
          if (!res) {
            console.log("no getAllBorrowRecords records ")
          } else {
            state.tableData = []
            state.tableData = res.data[0]
            console.log(state.tableData)
            updatePaginatedData()
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
    }

    const handleFilterChange = (filters: any) => {
      state.filters.overdue = filters.overdue;   // 只有一个条件在 `filters` 中
      updatePaginatedData(); // 更新分页数据
    }

    const getUserRole = () => {
      state.isAdmin = localStorage.getItem('role') === 'admin';
    }


    const getBookBorrowed = async() => {
      console.log("getBookBorrowed exc")
      const data = {'accessToken':sessionStorage.getItem('accessToken')}

      try {
        await Service.postGetBorrowedBookByUserId(data).then((res) => {
          if (!res) {
            //还没有借书哦
            // console.log('getBookBorrowed empty')
          } else {
            state.tableData = []
            // console.log('getBookBorrowed get')
            // console.log(res)
            state.tableData = res.data[0]
            // console.log(state.tableData)
            updatePaginatedData()
          }
        });
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

    const formatter = (row: { address: any }) => row.address
    const filterTag = (value: any, row: { Tag: any }) => row.tag === value
    const filterStatus = (value: any, row: { status: any }) => row.status === value
    const filterHandler = (value: any, row: { [x: string]: any }, column: { property: any }) => {
      const { property } = column
      return row[property] === value
    }

    const modifyPop = (row) => {
      state.modifyFormVisible = true
      state.form = row
    }

    const detailPop = (row) => {
      state.detailFormVisible = true
      state.form = row
    }

    const handleSizeChange = (val: any) => {
      // eslint-disable-next-line no-console
      state.pageSize = val
      sessionStorage.setItem("BOOK_BORROW_PAGE_SIZE", val)
      updatePaginatedData();  // 更新分页数据
      // request api to change tableData
    }

    const handleReturnBook = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      try {
        Service.returnBook(row).then((res) => {
          if (res) {
            getAllBorrowRecords()
            // console.log(res)
          } else {
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
      state.tableData.splice(index, 1)
      updatePaginatedData()
    }

    const handleCurrentChange = (val: any) => {
      state.currentPage = val
      updatePaginatedData();  // 更新分页数据
    }

    return {
      formInline,
      total,
      ...toRefs(state),
      handleCurrentChange,
      handleSizeChange,
      filterTableRef,
      resetDateFilter,
      clearFilter,
      formatter,
      filterTag,
      filterStatus,
      filterHandler,
      modifyPop,
      detailPop,
      handleReturnBook,
      getUserRole,
      getList,
      getAllBorrowRecords,
      watchSearch,
      updatePaginatedData,
      handleFilterChange
    }
  }
})
</script>
<style lang="stylus" scoped>
.table-container{
    .form-inline{
        margin:15px;
        text-align:left;
    }
    .table-list{
        margin:15px;
    }

}
</style>

<style scoped>
.demo-image__error .image-slot {
  font-size: 30px;
}
.demo-image__error .image-slot .el-icon {
  font-size: 30px;
}
.demo-image__error .el-image {
  width: 100%;
  height: 200px;
}
</style>
