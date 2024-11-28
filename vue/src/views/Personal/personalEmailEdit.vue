<template>
    <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="100px" title="修改绑定邮箱">
      <el-form-item label="原绑定邮箱" prop="oldEmail">
        <el-input v-model="form.oldemail" type="email" placeholder="旧的邮箱">
        </el-input>
      </el-form-item>
      <el-form-item label="新绑定邮箱" prop="newEmail">
        <el-input v-model="form.newemail" type="email" placeholder="新的邮箱">
        </el-input>
      </el-form-item>
      <el-form-item label="验证码" prop="userRole">
        <el-input v-model="form.captcha" type="number" placeholder="旧邮箱收到的验证码">
        </el-input>
      </el-form-item>
      <el-row class="btn-container">
        <el-button size="mini" type="primary" @click="saveData()"> <i class="fa fa-plus"> </i> 修改 </el-button>
      </el-row>
    </el-form>
</template>
<script lang="ts">
import { computed, defineComponent, watchEffect, reactive, toRef, toRefs } from 'vue'

export default defineComponent({
  name: 'PersonalEmailEdit',
  setup(props, { emit }) {
    const rules = {
      oldemail: [{ required: true, type:'email', message: '请输入邮箱', trigger: 'change' }],
      newemail: [{ required: true, type:'email', message: '请输入邮箱', trigger: 'change' }],
      captcha: [{ required: true, type:'number', message: '请输入验证码', trigger: 'blur' }],
    }
    /**
     * @description 保存当前角色授权菜单
     */

    return {
      rules,
      saveData
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
