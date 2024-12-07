<template>
  <div v-loading="loading" class="new">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="100px" title="新增员工">
      <el-form-item label="用户名" prop="userName">
        <el-input v-model="form.userName">{{row.userName}}</el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="form.nickName">{{row.nickName}}</el-input>
      </el-form-item>
      <el-form-item label="性别" prop="userSex">
        <el-select v-model="form.userSex" placeholder="请选择性别">
          <el-option v-for="sex in sexs" :key="sex.value" :label="sex.label" :value="sex.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="userPhone">
        <el-input v-model="form.userPhone">{{row.userPhone}}</el-input>
      </el-form-item>
      <el-form-item label="地址" prop="userAddress">
        <el-input v-model="form.userAddress">{{row.userAddress}}</el-input>
      </el-form-item>
      <el-form-item label="角色" prop="userRole">
        <el-select v-model="form.userRole" placeholder="请选择角色">
          <el-option v-for="role in roles" :key="role.value" :label="role.label" :value="role.value"></el-option>
        </el-select>
      </el-form-item>
      <el-row class="btn-container">
        <el-button size="mini" type="primary" @click="saveData()"> <i class="fa fa-plus"> </i> 修改 </el-button>
      </el-row>
    </el-form>
  </div>
</template>
<script lang="ts">
import { computed, defineComponent, watchEffect, reactive, toRef, toRefs } from 'vue'
import { useStore } from '@/store'
import Service from './api/index'

interface stateTypes {
  url: String
  purl: String
  loading: Boolean
  form: {
    userSex: string
    userName: string
    nickName: string
    userPhone: string
    userAddress: string
    userRole: number
  }
}
export default defineComponent({
  name: 'RolesEdit',
  props: {
    currentRow: {
      type: Object,
      default: () => ({ userName:'',nickName:'',userSex:'',userPhone: '',userAddress:''})
    }
  },
  emits: ['success'],

  setup(props, { emit }) {
    // 析构获取 props 属性 basePath
    const currentRow = toRef(props, 'currentRow')
    const store = useStore()
    const lang = computed(() => store.getters['settingsModule/getLangState'])

    const state = reactive<stateTypes>({
      url: `/role/allow`,
      purl: `/role/permissions`,
      loading: false,
      form: {
        userSex: '',
        userName: '',
        nickName: '',
        userPhone: '',
        userAddress: '',
        userRole: 0
      }
    })
    const rules = {
      userName: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
      ],
      nickName: [
        { required: true, message: '请输入昵称', trigger: 'blur' },
      ],
      userSex: [
        { required: true, message: '请选择性别', trigger: 'change' },
      ],
      userPhone: [
        { required: true, message: '请输入电话号码', trigger: 'change' },
        { pattern: /^[0-9]{11}$/, message: '请输入11位数字的电话号码', trigger: 'blur' },
      ],
      userAddress: [
        { required: true, message: '请输入地址', trigger: 'change'}
      ],
      userRole: [
        { required: true, message: '请选择角色', trigger: 'change' }
      ]
    }
    const sexs = [
      { value: '男', label: '男' },
      { value: '女', label: '女' }
    ]
    const roles = [
      { value: 0, label: '学生' },
      { value: 1, label: '管理员' },
      { value: 2, label: '限制' }
    ]
    const row = computed(() => currentRow.value)
    // 可访问
    const routes = computed(() => store.state.permissionModule.routes)
    /**
     * 监听 currentRow 的变化并更新 form
     */
    watchEffect(() => {
      if (row.value) {
        state.form.userSex = row.value.userSex || '男'
        state.form.userName = row.value.userName || ''
        state.form.nickName = row.value.nickName || ''
        state.form.userPhone = row.value.userPhone || ''
        state.form.userAddress = row.value.userAddress || ''
        state.form.userRole = row.value.userRole || 0
      }
    })
    /**
     * @description 保存当前角色授权菜单
     */
    const saveData = () => {
      const data ={
        userName: state.form.userName,
        nickName: state.form.nickName,
        userSex: state.form.userSex,
        userPhone: state.form.userPhone,
        userAddress: state.form.userAddress,
        userRole: state.form.userRole,
        accessToken: sessionStorage.getItem('accessToken')
      }
      Service.postAdminUpdateUserInfo(data).then(res => {
        emit('success')
      })
    }

    return {
      ...toRefs(state),
      rules,
      sexs,
      roles,
      lang,
      row,
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
