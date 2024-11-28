<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
      <el-form-item>
        <el-button type="primary" @click="onOverdueRequest">一键支付</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="filterTableRef" class="table-list" row-key="book_id" :data="tableData.filter((data) => !search || data.title.toLowerCase().includes(search.toLowerCase()))" style="width: 100%">
      <el-table-column width="10"></el-table-column>
      <el-table-column prop="title" label="书名" truncated></el-table-column>
      <el-table-column prop="isbn" label="ISBN" truncated></el-table-column>
      <el-table-column
          prop="lend_time"
          label="借书日期"
          column-key="lend_time"
      ></el-table-column>
      <el-table-column
          prop="return_time"
          label="应还日期"
          column-key="return_time"
      ></el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="搜索" />
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="paymentDialogVisible" title="支付信息">
      <p>总罚款金额：{{ fineTotal }} 元</p>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="paymentDialogVisible = false">确定</el-button>
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
import { defineComponent, onMounted, reactive, ref, toRefs } from 'vue';
import { useRouter } from 'vue-router';
import permission from '@/directive/permission';
import { ElMessage } from 'element-plus';
import Service from '../api/index'; // 导入 Service 类

interface Record {
  book_id: string;
  title: string;
  isbn: string;
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
    const router = useRouter();
    const filterTableRef = ref();
    const state = reactive<{
      loading: boolean;
      tableData: Record[];
      currentPage: number;
      pageSize: number;
      search: string;
      formInline: {
        user: string;
        region: string;
      };
      total: number;
      paymentDialogVisible: boolean; // 新增
      fineTotal: number; // 新增
    }>({
      loading: false,
      tableData: [],
      currentPage: 1,
      pageSize: 5,
      search: '',
      formInline: {
        user: '',
        region: ''
      },
      total: 1,
      paymentDialogVisible: false, // 新增
      fineTotal: 0 // 新增
    });


    onMounted(() => {
      getBookBorrowed();
    });

    const getBookBorrowed = async () => {
      console.log("getBookBorrowed exc");
      const data = { 'accessToken': sessionStorage.getItem('accessToken') };

      try {
        const res = await Service.postGetBorrowedBookByUserId(data);
        if (res && res.data) {
          state.tableData = [];
          const data = res.data[0];
          for (let i = 0; i < data.length; i++) {
            const record: Record = {
              book_id: data[i].book_id,
              title: data[i].title,
              isbn: data[i].isbn,
              lend_time: data[i].lend_time,
              return_time: data[i].return_time,
              returned: data[i].returned
            };
            state.tableData.push(record);
          }
          state.total = state.tableData.length;
        } else {
          console.log('getBookBorrowed empty');
        }
      } catch (err) {
        if (err instanceof Error) {
          ElMessage({
            type: 'warning',
            message: err.message
          });
        } else {
          console.error("An unexpected error occurred:", err);
        }
      }
    };

    const viewDetail = (row: Record) => {
      router.push({ path: `/overdueDetail/${row.book_id}` });
    };

    const handleSizeChange = (val: any) => {
      state.pageSize = val;
    };

    const handleCurrentChange = (val: any) => {
      state.currentPage = val;
    };

    const onOverdueRequest = () => {
      state.fineTotal = state.tableData.reduce((total, record) => {
        const fine = calculateFine(record.return_time);
        return total + fine;
      }, 0);
      state.paymentDialogVisible = true;
    };



    const calculateFine = (returnDate: string): number => {
      const currentDate = new Date();
      const dueDate = new Date(returnDate);
      const oneDay = 24 * 60 * 60 * 1000; // 一天的毫秒数
      const diffDays = Math.round((currentDate.getTime() - dueDate.getTime()) / oneDay);
      const finePerDay = 5; // 每天的罚款金额
      return diffDays > 0 ? diffDays * finePerDay : 0;
    };


    return {
      ...toRefs(state),
      filterTableRef,
      handleSizeChange,
      handleCurrentChange,
      viewDetail,
      onOverdueRequest,
      calculateFine
    };
  }
});
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
