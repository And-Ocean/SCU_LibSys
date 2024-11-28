<template>
  <el-form ref="settingPasswordFormRef" :model="resetPasswordForm" :rules="rules" label-position="right" label-width="100px" title="修改绑定邮箱">
    <el-form-item label="原密码" prop="oldPassword">
      <el-input v-model="resetPasswordForm.oldPassword" type="password" placeholder="旧的密码">
      </el-input>
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input v-model="resetPasswordForm.newPassword" type="password" placeholder="新的密码">
      </el-input>
    </el-form-item>
    <el-form-item label="新密码验证" prop="newPassword">
      <el-input v-model="resetPasswordForm.newPassword" type="password" placeholder="再次输入新的密码">
      </el-input>
    </el-form-item>
    <el-row class="btn-container">
      <el-button size="mini" type="primary" @click="submitResetPasswordForm()"> <i class="fa fa-plus"> </i> 修改 </el-button>
    </el-row>
  </el-form>
</template>
<script lang="ts">
import { computed, defineComponent, reactive, ref } from 'vue'
import Service from "@/views/Personal/api";
import {ElMessage} from "element-plus/es";
import {useRouter} from "vue-router";
import { encrypt } from '@/utils/aes' // aes 密码加密
export default defineComponent({
  name: 'PersonalPasswordEdit',
  emits: ['success'],
  setup(props,{ emit }) {
    const router = useRouter()
    const settingPasswordFormRef = ref()
    const rules = {
      oldPassword: [{ required: true, type:'password', message: '请输入密码', trigger: 'change' }],
      newPassword: [{ required: true, type:'password', message: '请输入密码', trigger: 'change' }],
    }
    const resetPasswordForm = reactive({
      oldPassword: '',
      newPassword: '',
    })
    const submitResetPasswordForm = async() => {
      const { oldPassword, newPassword } = resetPasswordForm
      const data = {
        oldPassword: encrypt(oldPassword),
        newPassword: encrypt(newPassword),
        accessToken: sessionStorage.getItem('accessToken')
      }
        const res = await Service.postSetPassword(data)
        console.log(res)
        if(res.status === 0) {
          ElMessage({
            type: 'success',
            message: res.data.message
          })
          emit('success')
        }
        else{
          ElMessage({
            type: 'error',
            message: res.data.message
          })
        }
    }
    return {
      rules,
      resetPasswordForm,
      submitResetPasswordForm
    }
  }
})
</script>
<style lang="stylus" scoped>
.btns {
  text-align: right;
}

.el-transfer {
  display: inline-block;
  text-align: left;
}

.transfer {
  text-align: center;
}

.card-header {
  text-align: left;
}
</style>
