<template>
  <div class="PersonalSetting">
    <el-row>
      <el-col :offset="1" :span="22">
        <div class="grid-content bg-purple-dark">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <el-button class="button" type="text" @click="handleBack"><i class="el-icon-arrow-left" />返回</el-button>

                <span>个人设置</span>
                <div></div>
              </div>
            </template>
            <el-tabs :tab-position="tabPosition">
              <el-tab-pane label="基本设置">
                <div class="set-title">
                  <span>基本设置</span>
                </div>
                <div class="set-info">
                  <div class="form-info">
                    <el-form ref="settingFormRef" :model="settingForm" :rules="rules" label-width="100px" class="demo-ruleForm">
                      <el-form-item label="用户名称" prop="nickname">
                        <el-input v-model="settingForm.nickname" placeholder="请输入用户名" maxlength="10"></el-input>
                      </el-form-item>
                      <el-form-item label="联系电话" prop="mobile">
                        <el-input v-model="settingForm.phone" placeholder="请输入11位大陆手机号码"></el-input>
                      </el-form-item>
                      <el-form-item label="个人地址" prop="desc">
                        <el-input v-model="settingForm.address" type="textarea" placeholder="个人详细住址" maxlength="120"></el-input>
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary" :loading="updateLoading" @click="submitForm()">更新基本信息</el-button>
                        <el-button @click="resetForm()">重置</el-button>
                      </el-form-item>
                    </el-form>
                  </div>
                  <div class="avatar">
                    <div class="preview">
                      <span>头像</span>
                      <img src="../../assets/avatar-default.jpg" />
                    </div>
                    <el-upload
                      class="avatar-uploader"
                      action="https://jsonplaceholder.typicode.com/posts/"
                      :show-file-list="false"
                      :on-success="handleAvatarSuccess"
                      :before-upload="beforeAvatarUpload"
                    >
                      <el-button style="margin-left: 10px" size="small" type="success"><i class="el-icon-upload"></i>更换头像</el-button>
                    </el-upload>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="安全设置">
                <div class="set-title">
                  <span>安全设置</span>
                </div>
                <div class="secure-item">
                  <div class="secure-info">
                    <span class="secure-key">账户密码</span>
                  </div>
                  <div class="opera-btn" @click="showPasswordDialog = true"><span>修改</span></div>
                </div>
                <div class="secure-item">
                  <div class="secure-info">
                    <span class="secure-key">绑定邮箱</span>
                    <span class="secure-value">已绑定邮箱：{{email}}</span>
                  </div>
                  <div class="opera-btn" @click="showEmailDialog = true"><span>修改</span></div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="新消息通知">
                <div class="set-title">
                  <span>新消息通知</span>
                </div>
                <div class="secure-item">
                  <div class="secure-info">
                    <span class="secure-key">系统消息</span>
                    <span class="secure-value">系统消息将以系统内部渠道通知</span>
                  </div>
                  <el-tooltip :content="'是否开启系统消息: '" placement="top">
                    <el-switch v-model="sysSwitch" active-color="#13ce66" inactive-color="#ff4949" :active-value="true" :inactive-value="false"> </el-switch>
                  </el-tooltip>
                </div>
                <div class="secure-item">
                  <div class="secure-info">
                    <span class="secure-key">代办任务</span>
                    <span class="secure-value">代办任务将以系统内部渠道通知</span>
                  </div>
                  <el-tooltip :content="'是否开启代办任务消息: '" placement="top">
                    <el-switch v-model="taskSwitch" active-color="#13ce66" inactive-color="#ff4949" :active-value="true" :inactive-value="false"> </el-switch>
                  </el-tooltip>
                </div>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </div>
      </el-col>
    </el-row>
    <el-dialog title="修改绑定邮箱" :visible.sync="showEmailDialog" width="30%" :before-close="handleClose">
      <PersonalEmailEdit @save="handleSaveEmail" />
    </el-dialog>
    <el-dialog title="修改密码" :visible.sync="showPasswordDialog" width="30%" :before-close="handleClose">
      <PersonalPasswordEdit @save="handleSavePassword" />
    </el-dialog>
  </div>
</template>
<script lang="ts">
import { ElMessage } from 'element-plus'
import { defineComponent, onMounted, reactive, ref, toRefs } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from '@/store'
import Service from './api/index'
import LoginService from '../Login/api/index'
import PersonalPasswordEdit from "@/views/Personal/personalPasswordEdit.vue";
// eslint-disable-next-line no-unused-vars
type VoidNoop = (arg0?: Error) => void
export default defineComponent({
  name: 'PersonalSetting',
  components: {PersonalPasswordEdit},
  setup() {
    const email = localStorage.getItem('email')
    const router = useRouter()
    const tabPosition = ref('left')
    const settingFormRef = ref()
    const store = useStore()
    const showEmailDialog = ref(false)
    const showPasswordDialog = ref(false)

    const noticeSwitch = reactive({
      userSwitch: false,
      sysSwitch: true,
      taskSwitch: true
    })
    const settingForm = reactive({
      nickname: '',
      address: '',
      phone: '',
      accessToken: sessionStorage.getItem('accessToken')
    })
    const resetEmailForm = reactive({
      oldemail: '',
      newemail: '',
      captcha: null,
      accessToken: sessionStorage.getItem('accessToken')
    })
    const resetPasswordForm = reactive({
      oldpassword: '',
      newpassword: '',
      accessToken: sessionStorage.getItem('accessToken')
    })

    const imageUrl = ref()
    const updateLoading = ref(false)

    // eslint-disable-next-line no-unused-vars
    const validateMobile = (rule: any, value: string, callback: VoidNoop) => {
      if (value === '') {
        callback(new Error('手机号码不可为空哦'))
      } else {
        const reg = /^1[3-9]\d{9}$/
        if (reg.test(value)) {
          callback()
        } else {
          callback(new Error('请输入正确的手机号码'))
        }
      }
    }

    //
    const rules = {
      nickname: {  message: '请输入昵称', trigger: ['blur', 'change'] },
      address: { message: '请输入个人地址', trigger: ['blur', 'change'] },
      phone: { validator: validateMobile, trigger: ['blur', 'change'] }
    }
    onMounted(() => {})
    // methods

    const handleBack = () => {
      router.go(-1)
    }
    const submitForm = () => {
      settingFormRef.value.validate(async (valid: any) => {
        if (valid) {
          try {
            updateLoading.value = true
            const data = {
              ...settingForm
            }
            const res = await Service.postSetBasicInfo(data)
            console.log(res)
            store.dispatch('permissionModule/getUserInfos', res.data)
            updateLoading.value = false
            ElMessage({
              type: 'success',
              message: res.data.message
            })
          } catch (err) {
            console.error(err)
          }
          // 执行通过校验以后的操作；
          return true
        }
        return false
      })
    }
    const resetForm = () => {
      settingFormRef.value.resetFields()
    }
    const handleAvatarSuccess = (res: any, file: { raw: any }) => {
      imageUrl.value = URL.createObjectURL(file.raw)
    }
    const beforeAvatarUpload = (file: { raw: any; type: string; size: number }) => {
      // const isJPG = file.type === 'image/jpeg'
      if (!/(gif|png|jpg|jpeg)$/i.test(file.type)) {
        ElMessage({
          message: '上传头像图片只能是gif|png|jpg|jpeg 格式!',
          type: 'warning'
        })
        return false
      }
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isLt2M) {
        ElMessage.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
    }
    const handleClose = (done: () => void) => {
      done()
    }

    const handleSaveEmail = (data: any) => {
      // 处理保存邮箱逻辑
      ElMessage({
        type: 'success',
        message: '邮箱修改成功'
      })
      showEmailDialog.value = false
    }
    const handleSavePassword = (data: any) => {
      // 处理保存邮箱逻辑
      ElMessage({
        type: 'success',
        message: '密码修改成功'
      })
      showPasswordDialog.value = false
    }

    return {
      handleBack,
      handleClose,
      tabPosition,
      settingFormRef,
      email,
      settingForm,
      resetEmailForm,
      resetPasswordForm,
      submitForm,
      resetForm,
      handleAvatarSuccess,
      beforeAvatarUpload,
      showEmailDialog,
      showPasswordDialog,
      handleSaveEmail,
      handleSavePassword,
      submitEmailForm,
      submitPasswordForm,
      rules,
      imageUrl,
      ...toRefs(noticeSwitch),
      updateLoading
    }
  }
})
</script>

<style lang="stylus" scoped>
.PersonalSetting{
    margin-top:20px;
    .demo-ruleForm{
        text-align :left;
    }
    .set-title{
      text-align :left;
    }
    .secure-item{
      width:100%;
      padding:20px;
      border-bottom:1px solid #f0f0f0;
      display :flex;
      flex-direction:row;
      justify-content :space-between;
      align-items :center;
      .secure-info{
         display :flex;
      flex-direction:column;
      justify-content :flex-start;
      align-items :flex-start;
        .secure-key{
          margin-bottom: 4px;
          color: rgba(0,0,0,.85);
          font-size: 14px;
          line-height: 1.6;
        }
        .secure-value{
          color: rgba(0,0,0,.45);
          font-size: 14px;
          line-height: 1.6;
        }
      }
      .opera-btn{
        color:#1890ff;
        cursor:pointer;

      }
    }
    .set-info{
      display :flex;
      flex-direction :row;
      justify-content :space-around;
      align-items :flex-start;
      .form-info{


      }
      .avatar{
        display :flex;
        flex-direction:row;
        justify-content:flex-start;
        align-items :flex-end;
        .preview{
           display :flex;
        flex-direction:column;
        justify-content:flex-start;
        align-items :flex-start;
        margin-right:20px;
         img{
            width:174px;
          height:174px;
          border-radius:50%;
         }
        }

          .avatar-uploader .el-upload:hover {
            border-color: #409EFF;
          }
          .avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            line-height: 178px;
            text-align: center;
          }
          .avatar {
            width: 178px;
            height: 178px;
            display: block;
          }
      }
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
  }
}
</style>
