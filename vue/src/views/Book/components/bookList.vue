<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
    </el-form>

    <el-button v-if="isAdmin" type="primary" @click="onAddBookIsbn">添加新图书</el-button>
    <el-button v-if="isAdmin" type="primary" @click="onAddBookEntity">添加已有图书</el-button>


    <el-table ref="filterTableRef" class="table-list" row-key="book_id"
              :data="paginatedData" stripe style="width: 100%">
    <el-table-column width="10"></el-table-column>
    <el-table-column prop="title" label="书名" truncated width="200"></el-table-column>
    <el-table-column prop="isbn" label="ISBN" truncated width="200"></el-table-column>
    <el-table-column prop="author" label="作者" width="100"></el-table-column>
      <el-table-column prop="category" label="分类" width="100"></el-table-column>
      <el-table-column prop="publisher" label="出版社" truncated width="200"></el-table-column>
    <el-table-column align="right">
      <template #header>
        <el-input v-model="search" size="mini" placeholder="输入书名关键字搜索" @input="watchSearch" />
      </template>
      <template #default="scope">
        <el-button size="mini" @click="detailPop(scope.row)">书籍详情</el-button>
        <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red"
                       title="确定借阅这本书吗？" @confirm="onBorrowBook(scope.$index, scope.row)">
          <template #reference>
            <el-button size="mini" type="primary">借阅</el-button>
          </template>
        </el-popconfirm>
        <el-button v-if="isAdmin" size="mini" type="warning" @click="modifyPop(scope.row)">修改</el-button>
        <el-popconfirm
            confirm-button-text="删除全部"
            cancel-button-text="删除部分"
            icon="el-icon-info"
            icon-color="red"
            title="删除全部还是删除部分？"
            @confirm="handleDelete(scope.$index, scope.row)"
            @cancel="handleEntityDelete(scope.$index, scope.row)"
        >
          <template #reference>
            <el-button v-if="isAdmin" size="mini" type="danger">删除</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
    </el-table>

    <el-dialog v-model="modifyFormVisible" title="修改书籍信息">
      <el-form :model="form">
        <el-form-item label="书名" :label-width="formLabelWidth">
          <el-input v-model="form.title" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="ISBN" :label-width="formLabelWidth">
          <el-input v-model="form.isbn" autosize type="textarea"/>
        </el-form-item>
        <el-form-item label="作者" :label-width="formLabelWidth">
          <el-input v-model="form.author" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="简介" :label-width="formLabelWidth">
          <el-input v-model="form.summary" autosize type="textarea"/>
        </el-form-item>
        <el-form-item label="出版社" :label-width="formLabelWidth">
          <el-input v-model="form.publisher" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格" :label-width="formLabelWidth">
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEdit()">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-model="detailFormVisible" title="书籍详情">
      <el-form :model="form">
        <el-form-item label="书名&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.title }}
        </el-form-item>
        <el-form-item label="封面&nbsp;&nbsp;">
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
        <el-form-item label="简介&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.summary }}
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-model="borrowFormVisible" title="可借的书籍位置" width="25%">
      <div v-for="item in borrowList" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
        <span>{{ item.place }}</span>
        <el-button @click="borrowABook(item)">借阅这本</el-button>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="borrowFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog v-model="deleteEntityFormVisible" title="书籍信息" width="25%">
      <div v-for="item in borrowList" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
        <span>{{ item.id }}</span>
        <span>{{ item.place }}</span>
        <el-button @click="onDeleteBookEntity(item)" type="danger">删除这本</el-button>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="deleteEntityFormVisible = false">取 消</el-button>
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
import {computed, defineComponent, onMounted, reactive, ref, toRaw, toRefs, watch} from 'vue'
import { useRouter } from 'vue-router'
import permission from '@/directive/permission'
import Service from '../api/index'
import { ElMessage } from "element-plus"
import {TRUE} from "sass";

export default defineComponent({
  name: 'bookList',
  computed: {
    TRUE() {
      return TRUE
    }
  },
  directives: {
    permission
  },
  setup() {
    const router = useRouter()
    const filterTableRef = ref()
    const state = reactive({
      allBooks: [],
      paginatedData: [],  // 分页后的图书数据
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      search: '',
      modifyFormVisible: false,
      detailFormVisible: false,
      deleteEntityFormVisible: false,
      form: {},
      isAdmin: false,  // 默认不是管理员
      total: 0, // 总记录数,
      bookCategory: [],
      borrowFormVisible: false,
      borrowList: {},
      isPopconfirmVisible: false,
    })
    const formInline = reactive({
      user: '',
      region: ''
    })
    const total = 1

    // 获取用户角色
    const getUserRole = () => {
      const role = localStorage.getItem('role');
      if (role === 'admin') {
        state.isAdmin = true;  // 当前用户是管理员
      } else {
        state.isAdmin = false;  // 当前用户不是管理员
      }
    }

    onMounted(() => {
      getBookList()
      getUserRole()  // 获取当前用户角色
    })

    const getBookList = () => {
      console.log("getPersonalBookList exc")
      try {
        Service.postGetBookIsbn().then((res) => {
          if (res && res.data) {
            state.allBooks = res.data;  // 获取所有书籍
            state.total = state.allBooks.length;  // 更新总记录数
            updatePaginatedData();  // 更新分页数据
          } else {
            console.log('没有找到数据')
          }
        })
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
    }

    // 更新分页数据
    const updatePaginatedData = () => {
      // 过滤包含搜索关键字的书籍
      let filteredBooks = state.allBooks;

      // 如果有搜索关键词，按书名进行过滤
      if (state.search) {
        filteredBooks = filteredBooks.filter((book) =>
            book.title.toLowerCase().includes(state.search.toLowerCase()) // 忽略大小写
        );
      }

      // 更新分页数据
      const start = (state.currentPage - 1) * state.pageSize;
      const end = state.currentPage * state.pageSize;
      state.paginatedData = filteredBooks.slice(start, end);  // 根据当前页和页大小提取分页数据
      state.total = filteredBooks.length;  // 更新总记录数
    };

// 监听 `search` 字段的变化，实时更新分页数据
    const watchSearch = () => {
      updatePaginatedData();  // 过滤数据并更新分页
    };

// 监听 `search` 输入框
    watch(() => state.search, watchSearch);

    // 当前页变更时更新分页数据
    const handleCurrentChange = (val: number) => {
      state.currentPage = val;
      updatePaginatedData();  // 更新分页数据
    }

    // 每页记录数变更时更新分页数据
    const handleSizeChange = (val: number) => {
      state.pageSize = val;
      updatePaginatedData();  // 更新分页数据
    }

    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('date')
    }

    const clearFilter = () => {
      filterTableRef.value.clearFilter()
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
      const record = state.form
      state.form = {}
      try {
        Service.postModifyBookIsbn(record).then((res) => {
          if (res) {
            // console.log(res)
          } else {
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
      // eslint-disable-next-line no-console
      if (!state.isAdmin) {
        ElMessage({
          type: 'warning',
          message: '您没有权限删除此书籍'
        })
        return
      }
      console.log(index, row)
      const record = {
        isbn: row.isbn
      }
      try {
        Service.deleteBookIsbn(record).then((res) => {
          if (res) {
            // console.log(res)
          } else {
          }
        })
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
      state.tableData.splice(index, 1)
    }

    const handleEntityDelete = (index: any, row: any) => {
      try {
        Service.getBorrowableBooks(row).then((res) => {
          if (res) {
            state.borrowList = res.data[0];
            state.deleteEntityFormVisible = true
          } else {
          }
        })
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
      state.tableData.splice(index, 1)
    }

    const onDeleteBookEntity = (item:any) => {
      // console.log("book item: ", item)
      const rawItem = toRaw(item)
      try {
        Service.deleteBookEntity(rawItem).then((res) => {
          if (res) {
            ElMessage({
              type: 'success',
              message: "删除成功！"
            })
            state.borrowFormVisible = false
          } else {
            ElMessage({
              type: 'warning',
              message: "请刷新页面"
            })
          }
        })
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
    }

    const onSubmit = () => {
      // eslint-disable-next-line no-console
      console.log('submit!')
    }

    const onAddBookIsbn = () => {
      // eslint-disable-next-line no-console
      router.replace('/Book/bookAdd')
    }

    const onAddBookEntity = () => {
      // eslint-disable-next-line no-console
      router.replace('/Book/bookEntityAdd')
    }

    const onBorrowBook = (index: any, row: any) => {
      try {
        Service.getBorrowableBooks(row).then((res) => {
          if (res) {
            // console.log("可借列表已得到")
            state.borrowList = res.data[0];
            state.borrowFormVisible = true
          } else {
          }
        })
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
      state.tableData.splice(index, 1)
    }

    const borrowABook = (item:any) => {
      try {
        Service.borrowABookHandle(item).then((res) => {
          if (res) {
            ElMessage({
              type: 'success',
              message: "借书成功！"
            })
            state.borrowFormVisible = false
          } else {
            ElMessage({
              type: 'warning',
              message: "请刷新页面"
            })
          }
        })
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
    }


    return {
      formInline,
      ...toRefs(state),
      handleCurrentChange,
      handleSizeChange,
      onSubmit,
      onAddBookIsbn,
      onAddBookEntity,
      onBorrowBook,
      onDeleteBookEntity,
      handleEdit,
      handleDelete,
      handleEntityDelete,
      filterTableRef,
      resetDateFilter,
      clearFilter,
      modifyPop,
      detailPop,
      watchSearch,
      borrowABook
    }
  }
})
</script>

