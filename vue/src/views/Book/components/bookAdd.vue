<template>
  <div class="FormInfo">
    <el-row>
      <el-col :offset="1" :span="22">
        <div class="grid-content bg-purple-dark">
          <el-card class="box-card">
            <div style="text-align: left">
              <span>新图书添加</span>
              <el-divider></el-divider>
            </div>
            <el-form ref="isbnForm" style="text-align: left" :model="sizeForm" label-width="80px" size="mini">
              <el-form-item label="ISBN">
                <el-input v-model="sizeForm.isbn"></el-input>
              </el-form-item>
              <el-form-item label="标题" :label-width="formLabelWidth">
                <el-input v-model="sizeForm.title" autosize type="textarea"/>
              </el-form-item>
              <el-form-item label="价格" :label-width="formLabelWidth">
                <el-input v-model="sizeForm.price" autosize type="textarea"/>
              </el-form-item>
              <el-form-item label="作者" :label-width="formLabelWidth">
                <el-input v-model="sizeForm.author" autosize type="textarea"/>
              </el-form-item>
              <el-form-item label="类别" :label-width="formLabelWidth">
                <el-select v-model="sizeForm.category" placeholder="Select" style="width: auto">
                  <el-option
                      v-for="item in state.bookCategory"
                      :key="item.category"
                      :label="item.category"
                      :value="item.category"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="出版社" :label-width="formLabelWidth">
                <el-input v-model="sizeForm.publisher" autosize type="textarea"/>
              </el-form-item>
              <el-form-item label="简介" :label-width="formLabelWidth">
                <el-input v-model="sizeForm.summary" autosize type="textarea"/>
              </el-form-item>
              <el-form-item label="数量" :label-width="formLabelWidth">
                <el-input v-model="sizeForm.number" autosize type="textarea"/>
              </el-form-item>


              <el-form-item size="large">
                <el-button type="primary" @click="submitForm">确认添加</el-button>
                <el-button>取消</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-col
      >
    </el-row>
  </div>
</template>
<script lang="ts">
import {computed, defineComponent, onMounted, reactive, ref} from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Edit, DeleteFilled, Check, ArrowLeft } from '@element-plus/icons-vue'
import Service from "@/views/Book/api";

export default defineComponent({
  name: 'AdvanceForm',
  components: {
    Edit,
    DeleteFilled,
    Check,
    ArrowLeft
  },
  setup() {
    const router = useRouter()

    const state = reactive({
      bookCategory: []
    })

    const sizeForm = reactive({
      isbn: '',
      title: '',
      price: '',
      author: '',
      publisher: [],
      number: '',
      category: '',
      cover_url: 'https://www.helloimg.com/i/2024/12/03/674f0923be1c1.jpg',
      summary: ''
    })


    const isbnForm = ref()

    onMounted(() => {
      // eslint-disable-next-line no-console
      getBookCategory()
    })
    // methods
    const submitForm = () => {
      isbnForm.value.validate((valid: any): boolean => {
        if (valid) {
          let record = {
            isbn: sizeForm.isbn,
            title: sizeForm.title,
            price: sizeForm.price,
            author: sizeForm.author,
            publisher: sizeForm.publisher,
            borrownum : sizeForm.number,
            category: sizeForm.category,
            cover_url: sizeForm.cover_url,
            summary: sizeForm.summary,
          }
          try {
            Service.addBookIsbn(record).then((res) => {

            });
            ElMessage({
              type: 'success',
              message: '添加成功'
            })
          } catch (err) {
            ElMessage({
              type: 'warning',
              message: err.message
            })
            console.log('submit error')
            return false
          }
          sizeForm.isbn = ''
          sizeForm.title = ''
          sizeForm.price = ''
          sizeForm.author = ''
          sizeForm.publisher = []
          sizeForm.number = ''
          sizeForm.category = ''
          sizeForm.cover_url = ''
          sizeForm.summary = ''
          return true
        }
        console.log('submit error')
        return false
      })
    }
    const getBookCategory = () => {
      try {
        Service.postGetBookCategory().then((res) => {
          if (res) {
            state.bookCategory = res.data;
            console.log("category: "+JSON.stringify(state.bookCategory));
          } else {
            console.log('没有找到数据');
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        });
      }
    }
    const resetForm = () => {
      isbnForm.value.resetFields()
    }
    const handleBack = () => {
      router.go(-1)
    }
    const handleEdit = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      tableData[index].edit = true
    }
    /**
     * @description  useXXX写法,代替mixin有待改进的地方
     * */
    const checkEmpty = (row: any) => {
      const result = Object.keys(row).some((key) => row[key] === '')
      return result
    }
    const handleSave = (index: any, row: any): Boolean => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      if (checkEmpty(row)) {
        ElMessage.warning('保存前请完善信息！')
        return false
      }
      // save current row data and update table data;
      tableData[index].edit = false
      tableData[index] = row
      ElMessage({
        type: 'success',
        message: '保存成功'
      })
      return true
    }
    const handleDelete = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      tableData.splice(index, 1)
    }
    // 新增一条记录
    const handleAddRecord = () => {
      tableData.push({
        province: '',
        city: '',
        name: '',
        address: '',
        edit: true
      })
    }
    return {
      handleAddRecord,
      handleEdit,
      handleSave,
      handleDelete,
      handleBack,
      sizeForm,
      state,
      isbnForm,
      submitForm,
      resetForm,
    }
  }
})
</script>

<style lang="stylus" scoped>
.FormInfo{
    margin-top:20px;
    .demo-ruleForm{
        text-align :left;
    }
    .info{
        text-align: left;
    padding-left: 20px;
    margin-bottom: 20px;
    font-size: 12px;
    }
     .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
  }

  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .box-card {
    width:100%;
    min-height: 400px; /* 添加最小高度 */
  }

   .el-row {
       margin-bottom: 20px;
     }
}
</style>
