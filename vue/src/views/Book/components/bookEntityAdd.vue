<template>
  <div class="FormInfo">
    <el-row>
      <el-col :offset="1" :span="22">
        <div class="grid-content bg-purple-dark">
          <el-card class="box-card">
            <div style="text-align: left">
              <span>已有图书添加</span>
              <el-divider></el-divider>
            </div>
            <el-form ref="isbnForm" style="text-align: left" :model="sizeForm" label-width="80px" size="mini">
              <el-form-item label="ISBN">
                <el-autocomplete
                    v-model="sizeForm.isbn"
                    :fetch-suggestions="querySearch"
                    clearable
                    placeholder="请输入 ISBN"
                    class="inline-input w-50"
                    @select="handleSelect"
                />
              </el-form-item>
              <el-form-item label="位置" :label-width="formLabelWidth">
                <el-input v-model="sizeForm.place" autosize type="textarea"/>
              </el-form-item>

              <el-form-item size="large">
                <el-button type="primary" @click="submitForm">确认添加</el-button>
                <el-button>取消</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import Service from "@/views/Book/api";

export default defineComponent({
  name: 'AdvanceForm',
  setup() {
    const sizeForm = reactive({
      isbn: '',
      status: 1,
      place: '',
    })

    const isbnForm = ref()

    const availableISBNs = ref<string[]>([])

    const fetchISBNs = async () => {
      try {
        const res = await Service.postGetBookIsbn()
        // console.log("isbn original: " + JSON.stringify(res))
        if (res && res.data) {
          availableISBNs.value = res.data.map((item: any) => item.isbn).filter(isbn => isbn !== '')
          // console.log("isbn:", JSON.stringify(availableISBNs.value))
        }
      } catch (error) {
        console.error('获取ISBN失败', error)
        ElMessage({
          type: 'error',
          message: '获取ISBN列表失败'
        })
      }
    }


    const querySearch = (queryString: string, cb: any) => {
      const results = queryString
          ? availableISBNs.value
              .filter(isbn => isbn.toLowerCase().includes(queryString.toLowerCase()))
              .map(isbn => ({ value: isbn }))  // 将结果格式化为对象数组
          : availableISBNs.value.map(isbn => ({ value: isbn }));
      console.log("查询结果:", results);
      cb(results);  // 返回给 `el-autocomplete`
    }



    const createFilter = (queryString: string) => {
      return (item: string) => {
        return item.toLowerCase().indexOf(queryString.toLowerCase()) === 0
      }
    }

    const handleSelect = (item: { value: string }) => {
      console.log('选择的 ISBN:', item.value);  // 提取并打印 item.value
      sizeForm.isbn = item.value;  // 将 item.value 设置为 sizeForm.isbn
    }



    const submitForm = () => {
      isbnForm.value.validate((valid: any): boolean => {
        if (valid) {
          let record = {
            isbn: sizeForm.isbn,
            status: sizeForm.status,
            place: sizeForm.place,
          }
          try {
            Service.addBookEntity(record).then((res) => {
              // 处理成功的响应
            })
            ElMessage({
              type: 'success',
              message: '添加成功'
            })
          } catch (err) {
            ElMessage({
              type: 'warning',
              message: err.message
            })
            console.log('提交出错')
            return false
          }
          sizeForm.isbn = ''
          sizeForm.status = 1
          sizeForm.place = ''
          return true
        }
        console.log('提交出错')
        return false
      })
    }

    onMounted(() => {
      fetchISBNs()
    })

    return {
      sizeForm,
      isbnForm,
      submitForm,
      querySearch,
      handleSelect,
    }
  }
})
</script>

<style lang="stylus" scoped>
.FormInfo {
  margin-top: 20px;
  .demo-ruleForm {
    text-align: left;
  }
  .info {
    text-align: left;
    padding-left: 20px;
    margin-bottom: 20px;
    font-size: 12px;
  }
  .box-card {
    width: 100%;
    min-height: 400px; /* 设置最小高度 */
  }
  .el-row {
    margin-bottom: 20px;
  }
}
</style>
