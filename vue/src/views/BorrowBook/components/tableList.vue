<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
    </el-form>
    <el-table ref="filterTableRef" class="table-list" row-key="book_id" :data="tableData.filter((data) => !search || data.title.toLowerCase().includes(search.toLowerCase()))" style="width: 100%">
      <el-table-column  width="10" ></el-table-column>
      <el-table-column prop="title" label="书名" truncated> </el-table-column>
      <el-table-column prop="isbn" label="ISBN" truncated> </el-table-column>
      <el-table-column
          prop="return_time"
          label="还书日期"
          sortable
          column-key="return_time"
      >
      </el-table-column>
      <el-table-column prop="author" label="作者" width="100"> </el-table-column>
      <el-table-column prop="publisher" label="出版社"> </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="输入书名关键字搜索" />
        </template>
        <template #default="scope">
          <el-button size="mini" @click="detailPop(scope.row)">书籍详情</el-button>
        </template>
      </el-table-column>
      <el-table-column width="120">
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
                :src="form.url"
                :zoom-rate="1.2"
                :max-scale="7"
                :min-scale="0.2"
                :preview-src-list="[form.url]"
                fit="cover"
            />
          </div>
        </el-form-item>
        <el-form-item label="简介&nbsp;&nbsp;" :label-width="formLabelWidth">
          还没有这个字段
        </el-form-item>
        <el-form-item label="作者&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.author }}
        </el-form-item>
        <el-form-item label="ISBN&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.isbn }}
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
      getBookBorrowed()
    })

    // methods
    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('date')
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
            var data = res.data[0]
            for (let i = 0; i < data.length; i++) {
              var record = {
                title: data[i].title,
                isbn: data[i].isbn,
                status: data[i].status,
                returned: data[i].returned,
                lend_time: data[i].lend_time,
                return_time: data[i].return_time,
                publisher: data[i].publisher,
                price: data[i].price,
                author: data[i].author,
                url: 'https://www.helloimg.com/i/2024/11/26/6745da68c904e.jpg',
                lend_id: data[i].lend_id,
              }
              state.tableData.push(record)
            }
            // console.log(state.tableData)
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
      console.log(`每页 ${val} 条`)
      state.pageSize = val
      // request api to change tableData
    }

    const handleReturnBook = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      // console.log(index, row)
      try {
        Service.returnBook(row).then((res) => {
          if (res) {
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
    }

     // 分页数据处理
     // @param data [Array] 需要分页的数据
     //  @param num [Number] 当前第几页
     //  @param size [Number] 每页显示多少条
    // const getList = (data, num, size) => {
    //   let list, start, end
    //   start = (num - 1) * size
    //   end = start + size
    //   list = data.filter((item, index) => {
    //     return index >= start && index < end
    //   })
    //   list.forEach((item, index) => {
    //     item.seq = index + start
    //   })
    //   return list
    // }

    const handleCurrentChange = (val: any) => {
      // eslint-disable-next-line no-console
      console.log(`当前页: ${val}`)
      state.currentPage = val
      // request api to change tableData
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
