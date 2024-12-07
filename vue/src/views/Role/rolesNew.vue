<template>
  <div v-loading="loading" class="new">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="100px" title="新增员工">
      <el-form-item label="用户名" prop="userName">
        <el-input v-model="form.userName" placeholder="请输入学号"></el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="form.nickName" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="userSex">
        <el-select v-model="form.userSex" placeholder="请选择性别">
          <el-option v-for="sex in sexs" :key="sex.value" :label="sex.label" :value="sex.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="userPhone">
        <el-input v-model="form.userPhone" placeholder="请输入用户手机号码"></el-input>
      </el-form-item>
      <el-form-item label="详细地址" prop="userAddress">
        <el-input v-model="form.userAddress" placeholder="请输入用户地址"></el-input>
      </el-form-item>
      <el-row class="btn-container">
        <el-button size="mini" type="primary" @click="submitForm()"> <i class="fa fa-plus"> </i> 新增 </el-button>
      </el-row>
    </el-form>
  </div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, ref } from 'vue'
import Service from "@/views/Role/api";

export default defineComponent({
  name: 'RoleNew',
  emits: ['success'],
  setup(props, { emit }) {
    const rules = {
      userName: [
        { required: true, message: '请输入员工ID', trigger: 'blur' },
      ],
      userPhone:[
        {min:11,max:11,message:'请输入11位手机号'}
      ]
    }
    const sexs = [
      { value: '男', label: '男' },
      { value: '女', label: '女' }
    ]

    const url = `/role/add`
    const formRef = ref()
    // 只将响应式数据进行响应式处理
    const state = reactive({
      form: {
        userName: '',
        nickName: '',
        userSex: '',
        userPhone: '',
        userAddress: '',
      },
      loading: false
    })
    /**
     * @description 提交新建角色处理函数
     */
    const submitForm = () => {
      formRef.value.validate((valid: any): boolean => {
        if (valid) {
          const data ={
            userName: state.form.userName,
            nickName: state.form.nickName,
            userSex: state.form.userSex,
            userPhone: state.form.userPhone,
            userAddress: state.form.userAddress,
            accessToken: sessionStorage.getItem('accessToken')
          }
          Service.postAdminAddUser(data).then(res => {
            if(res.status === 0) {
              emit('success')
            }
          })
          return true
        }
        return false
      })
    }
    return {
      submitForm,
      rules,
      sexs,
      formRef,
      url,
      ...toRefs(state)
    }
  }
})
</script>
<style lang="stylus" scoped></style>
