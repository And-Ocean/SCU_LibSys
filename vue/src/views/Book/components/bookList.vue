<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
    </el-form>
    <el-table ref="filterTableRef" class="table-list" row-key="book_id"
              :data="tableData.filter((data) => !search || data.title.toLowerCase().includes(search.toLowerCase()))"
              style="width: 100%">
      <el-table-column width="10"></el-table-column>
      <el-table-column prop="title" label="书名" truncated></el-table-column>
      <el-table-column prop="isbn" label="ISBN" truncated></el-table-column>

      <el-table-column prop="author" label="作者" width="100"></el-table-column>
      <el-table-column prop="publisher" label="出版社"></el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="输入书名关键字搜索"/>
        </template>
        <template #default="scope">
          <el-button size="mini" @click="detailPop(scope.row)">书籍详情</el-button>
        </template>
      </el-table-column>
      <el-table-column width="120">
        <template #default="scope">
          <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red"
                         title="确定借阅这本书吗？" @confirm="onBorrowBook(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger">借阅</el-button>
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
        <el-form-item label="封面&nbsp;&nbsp;">
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
          {{ form.publisher }}
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
  name: 'bookList',
  directives: {
    permission
  },
  setup() {
    const router = useRouter()
    const filterTableRef = ref()
    const state = reactive({
      tableData: [],
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
      getBookList()
    })
    // methods
    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('date')
    }

    const getBookList = () => {
      console.log("getPersonalBookList exc")
      try {
        Service.postGetBookIsbn().then((res) => {
          if (res) {
            state.tableData = []
            console.log('getBookList get')
            console.log(res)
            var data = res.data
            for (let i = 0; i < data.length; i++) {
              var record = {
                isbn: data[i].isbn,
                title: data[i].title,
                price: data[i].price,
                author: data[i].author,
                publisher: data[i].publisher,
                borrownum: data[i].borrownum,
                }
              state.tableData.push(record)
            }
          } else {
            console.log('getPersonalBookList RES MISS')
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

    const handleEdit = () => {
      // eslint-disable-next-line no-console
      state.modifyFormVisible = false
      let record = state.form
      state.form = {}
      try {
        Service.postModifyBookIsbn(record).then((res) => {
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
    }
    const handleDelete = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      let record = {
        isbn: row.isbn
      }
      try {
        Service.deleteBookIsbn(record).then((res) => {
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
    const handleSizeChange = (val: any) => {
      // eslint-disable-next-line no-console
      console.log(`每页 ${val} 条`)
      state.pageSize = val
      // request api to change tableData
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
    const onSubmit = () => {
      // eslint-disable-next-line no-console
      console.log('submit!')
    }

    const onAddBookIsbn = () => {
      // eslint-disable-next-line no-console
      router.replace('/Book/bookIsbnAdd')
    }

    const onAddBookEntity = () => {
      // eslint-disable-next-line no-console
      router.replace('/Book/bookEntityAdd')
    }

    const onBorrowBook = () => {
      router.replace('../../borrowBook')
    }

    return {
      formInline,
      total,
      ...toRefs(state),
      handleCurrentChange,
      handleSizeChange,
      onSubmit,
      onAddBookIsbn,
      onAddBookEntity,
      handleEdit,
      handleDelete,
      filterTableRef,
      resetDateFilter,
      clearFilter,
      formatter,
      filterTag,
      filterStatus,
      filterHandler,
      modifyPop,
      detailPop,
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

