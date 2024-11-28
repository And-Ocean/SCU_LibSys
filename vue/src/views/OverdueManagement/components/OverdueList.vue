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
      <el-table-column prop="fine_amount" label="罚款金额" width="180"></el-table-column>
      <el-table-column prop="returned" label="是否归还" width="100"></el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="small" placeholder="搜索" />
        </template>
        <template #default="scope">
          <el-button size="small" @click="modifyPop(scope.$index, scope.row)">修改</el-button>
          <el-button size="small" @click="detailPop(scope.$index, scope.row)">查看详情</el-button>
          <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- Dialogs and Pagination remain unchanged -->
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
  lend_time: string;
  return_time: string;
  returned: string;
  fine_amount?: number; // 罚款金额可选
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
        lend_time: '',
        return_time: '',
        returned: '',
        fine_amount: 0 // 初始化罚款金额
      }
    });
    const formInline = reactive({
      user: '',
      region: ''
    });
    const formLabelWidth = '120px';
    const total = 1;

    onMounted(() => {
      getOverdueListData();
    });

    const calculateFine = (returnDate: string): number => {
      const currentDate = new Date();
      const dueDate = new Date(returnDate);
      const oneDay = 24 * 60 * 60 * 1000; // 一天的毫秒数
      const diffDays = Math.round((currentDate.getTime() - dueDate.getTime()) / oneDay);
      const finePerDay = 1; // 每天的罚款金额，例如1元
      return diffDays > 0 ? diffDays * finePerDay : 0;
    };

    const getOverdueListData = async () => {
      try {
        const res = await Service.postQueryOverdueList(); // 调用API函数
        if (res && res.data) {
          state.tableData = [];
          const data = res.data;
          for (let i = 0; i < data.length; i++) {
            const record: Record = {
              book_id: data[i].book_id,
              lend_time: data[i].lend_time,
              return_time: data[i].return_time,
              returned: data[i].returned,
              fine_amount: calculateFine(data[i].return_time) // 动态计算罚款金额
            };
            state.tableData.push(record);
          }
        } else {
          console.log('getOverdueListData RES MISS');
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

    const modifyPop = (index: number, row: any) => {
      state.modifyFormVisible = true;
      state.form = row;
    };

    const detailPop = (index: number, row: any) => {
      state.detailFormVisible = true;
      state.form = row;
    };

    const handleEdit = async () => {
      state.modifyFormVisible = false;
      const record = { ...state.form }; // 复制表单数据
      state.form = { // 初始化表单对象，确保包含所有期望的属性
        book_id: '',
        lend_time: '',
        return_time: '',
        returned: '',
        fine_amount: 0 // 初始化罚款金额
      };
      try {
        const res = await Service.postUpdateOverdueRecord(record);
        if (res) {
          await getOverdueListData(); // 确保使用 await
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



    const handleDelete = async (index: any, row: any) => {
      const record = { book_id: row.book_id };
      try {
        const res = await Service.postDeleteOverdueRecord(record);
        if (res) {
          state.tableData.splice(index, 1);
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


    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('lend_time');
    };

    const clearFilter = () => {
      filterTableRef.value.clearFilter();
    };

    const handleSizeChange = (val: any) => {
      state.pageSize = val;
    };

    const handleCurrentChange = (val: any) => {
      state.currentPage = val;
    };

    const onOverdueRequest = () => {
      router.replace('/overdueManagement/overdueRequest');
    };

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
