<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
      <el-form-item>
        <el-button type="primary" @click="modifyPop">提交意见</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="filterTableRef" class="table-list" :data="paginatedData" style="width: 100%"
              @filter-change="handleFilterChange">
      <el-table-column prop="ad_title" label="意见标题" truncated> </el-table-column>
      <el-table-column prop="ad_ctnt" label="意见内容" truncated> </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="按标题快速搜索" @input="watchSearch" />
        </template>
        <template #default="scope">
          <el-button size="mini" @click="detailPop(scope.row)">查看详情</el-button>
          <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
      <el-table-column
          prop="replied"
          column-key="replied"
          label="状态"
          width="100"
          :filters="fin_options"
          :filtered-value="filters.replied"
          :filter-multiple="false"
          filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag :type="scope.row.replied === '已回复' ? 'primary' : 'success'" disable-transitions>{{ scope.row.replied}}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="modifyFormVisible" title="提交意见">
      <el-form :model="form">
        <el-form-item label="标题" :label-width="formLabelWidth">
          <el-input v-model="form.ad_title" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="内容" :label-width="formLabelWidth">
          <el-input v-model="form.ad_ctnt" autosize type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEdit()">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog v-model="detailFormVisible" title="意见详情">
      <el-form :model="form">
        <el-form-item label="ID&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.ad_id }}
        </el-form-item>
        <el-form-item label="标题&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.ad_title }}
        </el-form-item>
        <el-form-item label="内容&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.ad_ctnt }}
        </el-form-item>
        <el-form-item label="创建日期&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.crt_time }}
        </el-form-item>
        <el-form-item label="回复情况&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.replied }}
        </el-form-item>
        <el-form-item label="回复&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.reply_ctnt }}
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
  name: 'myAdviceBoxList',
  directives: {
    permission
  },
  setup() {
    // 思考 ref 响应式和 reactive 响应式的区别； 修改对象属性值，是否会刷新数据

    const router = useRouter()
    const filterTableRef = ref(null)
    const state = reactive({
      tableData: [
        {
          todo_ddl: '后台沒打开',
          todo_title: '后台沒打开',
          replied: '后台沒打开',
          todo_ctnt: 'NO'
        },
      ],
      currentPage: 1,
      pageSize: 5,
      search: '',
      modifyFormVisible: false,
      detailFormVisible: false,
      form: {},

      paginatedData: [],
      record_cnt: 0,

      fin_options: [
        {
          value:'未回复',
          text:'未回复',
        },
        {
          value:'已回复',
          text:'已回复',
        },
      ],

      filters: { // 筛选条件
        replied: [] // 筛选的状态，可以是 '已完成' 或 '未完成'
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
      page_size_get_set()
      // filterPresetTest()
      getPersonalTodoList()
    })
    // methods
    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('date')
    }

    const page_size_get_set = () => {
      const pageSizeTmp = sessionStorage.getItem("TODOLIST_PAGE_SIZE");
      if (!pageSizeTmp) {
        sessionStorage.setItem("TODOLIST_PAGE_SIZE", 5);
        state.pageSize = 5;
      } else {
        // ####parseInt#### is IMPORTANT!!!!
        state.pageSize = parseInt(pageSizeTmp, 10);
      }
    }

    const filterPresetTest = () => {
      if (sessionStorage.getItem("showUncompleted") === '1') {
        state.filters.replied[0] = "未完成"
        sessionStorage.setItem("showUncompleted", 0)
      }
    }


    const getPersonalTodoList = () => {
      // console.log("getPersonalTodoList exc")
      try {
        Service.postGetTodoList().then((res) => {
          if (res) {
            state.tableData = []
            // console.log('getPersonalTodoList get')
            // console.log(res)
            const data = res.data[0]
            console.log(data)
            state.record_cnt = data.length
            state.tableData = data
            updatePaginatedData()  // 更新分页数据
          } else {
            console.log('getPersonalTodoList RES MISS')
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
    // const filterStatus = (value: any, row: { replied: any }) => row.replied === value
    const filterHandler = (value: any, row: { [x: string]: any }, column: { property: any }) => {
      const { property } = column
      return row[property] === value
    }

    // 更新分页数据
    const updatePaginatedData = () => {
      let recordsToFilter = state.tableData;
      if (state.search) {
        recordsToFilter = recordsToFilter.filter((record) =>
            record.ad_ctnt.toLowerCase().includes(state.search.toLowerCase()) // 忽略大小写
        );
      }

      if (state.filters.replied[0] && state.filters.replied[0] !== '') {
        recordsToFilter = recordsToFilter.filter((record) =>
            record.replied == state.filters.replied[0]
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


    const modifyPop = () => {
      state.modifyFormVisible = true
    }

    const detailPop = (row) => {
      state.detailFormVisible = true
      state.form = row
    }

    const handleEdit = () => {
      // eslint-disable-next-line no-console
      state.modifyFormVisible = false
      let record = state.form
      try {
        Service.postModifyTodo(record).then((res) => {
          if (res) {
            getPersonalTodoList()
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
    }

    const handleDelete = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      // console.log(index, row)
      let record = {
        todo_id: row.ad_id
      }
      try {
        Service.deleteTodo(record).then((res) => {
          if (res) {
            // console.log(res)
            getPersonalTodoList()
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
    }
    const handleSizeChange = (val: any) => {
      state.pageSize = val;
      sessionStorage.setItem("TODOLIST_PAGE_SIZE", val)
      updatePaginatedData();  // 更新分页数据
    }

    const handleCurrentChange = (val: number) => {
      state.currentPage = val;
      updatePaginatedData();  // 更新分页数据
    }

    const onSubmit = () => {
      // eslint-disable-next-line no-console
      console.log('submit!')
    }

    const onAddTodo = () => {
      // eslint-disable-next-line no-console
      router.replace('/todoList/todoAdd')
    }

    const handleFilterChange = (filters: any) => {
      state.filters.replied = filters.replied;   // 只有一个条件在 `filters` 中
      // console.log(state.filters.replied);
      // console.log(filters.replied);
      updatePaginatedData(); // 更新分页数据
    }

    return {
      formInline,
      total,
      ...toRefs(state),
      handleCurrentChange,
      handleSizeChange,
      onSubmit,
      onAddTodo,
      handleEdit,
      handleDelete,
      filterTableRef,
      resetDateFilter,
      clearFilter,
      formatter,
      filterTag,
      // filterStatus,
      handleFilterChange,
      filterHandler,
      modifyPop,
      detailPop,
      watchSearch,
      page_size_get_set,
      filterPresetTest,
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

