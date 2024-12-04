<template>
  <meta name="referrer" content="no-referrer">
  <div class="page-container">
    <div class="info">
      <el-divider content-position="left">个人中心</el-divider>
      <span style="text-align: left">个人中心通常用来作为个人基本信息展示和个人事务通知中心</span>
    </div>
    <el-row :gutter="20">
      <el-col :span="20" :offset="1">
        <el-card class="box-card">
          <div class="account-avatar">
            <img :src="getAvatarUrl(avatar)" alt="个人头像" />
            <div class="account-name">{{nickname}}</div>
          </div>
          <div class="account-detail">
            <el-descriptions class="detail" :column="1" :size="size">
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-user"></i>
                  账号（学号）:
                </template>
                {{username}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-chat-dot-round"></i>
                  性别:
                </template>
                {{sex}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-user"></i>
                  角色:
                </template>
                {{ role }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-chat-dot-round"></i>
                  手机号:
                </template>
                {{phone}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-chat-dot-round"></i>
                  地址:
                </template>
                {{address}}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, ref, onMounted, computed } from 'vue'
import { useStore } from '@/store'
import Service from './api/index'
import fullcalendar from './components/fullCalendar/index.vue'
import defaultAvatar from '../../assets/avatar-default.jpg';
interface stateType {
  dynamicTags: string[]
  inputVisible: boolean
  inputValue: string
  contentTip: string
}
export default defineComponent({
  name: 'PersonalCenter',
  components: {
    fullcalendar
  },
  setup() {
    const formLabelWidth = ref(100)
    const size = ref('medium')
    const showDesc = ref(true)
    const store = useStore()
    const username = computed(() => store.state.permissionModule.username)
    const nickname = computed(() => store.state.permissionModule.nickname)
    const role = computed(() => store.state.permissionModule.role)
    const sex = computed(() => store.state.permissionModule.sex)
    const phone = computed(() => store.state.permissionModule.phone)
    const address = computed(() => store.state.permissionModule.address)
    const avatar = computed(() => store.state.permissionModule.avatar)
    // mothods
    /**
     * @description 获取头像
     */
    const getAvatarUrl = (avatar: string) => {
      if (avatar) {
        // 简单的 URL 验证
        new URL(avatar)
        return avatar
      }
      else{
        return defaultAvatar;
      }
    }


    onMounted(() => {
    })
    return {
      formLabelWidth,
      getAvatarUrl,
      size,
      username,
      nickname,
      role,
      sex,
      phone,
      address,
      avatar,
      showDesc
    }
  }
})
</script>
<style lang="stylus" scoped>
.page-container{
      .info{
        text-align: left;
    padding-left: 20px;
    margin-bottom: 20px;
    font-size: 12px;
    }
}
.box-card{
  p{
    text-align :right;
  }
    .is-selected {
    color: #1989FA;
  }
  ul{
    margin-top: 4%;
    margin-left: 15%;
    width: 80%;
    text-align :left;
    position: relative;
    display: inline-block;
    background-color: white;
    li{
      text-align :left;
      .list-tag{
            background-color: #ecf5ff;
            border-color: #d9ecff;
            height: 32px;
            padding: 0 10px;
            line-height: 30px;
            font-size: 12px;
            color: #409EFF;
            border-width: 1px;
            border-style: solid;
            border-radius: 4px;
            box-sizing: border-box;
            white-space: nowrap;
            .el-icon-close{
              border-radius: 50%;
                text-align: center;
                position: relative;
                cursor: pointer;
                font-size: 12px;
                height: 16px;
                width: 16px;
                line-height: 16px;
                vertical-align: middle;
                top: -1px;
                right: -5px;

                :before{
                  display :block;
                }
             }
        .el-tag__close{
                  color: #409eff;
            }
      }

    }
  }
  margin-top:14px;
  .account-avatar{
    text-align: center;
    margin-bottom :24px;
    img{
      width:105px;
      height:105px;
      margin-bottom :20px;
      border-radius:50%;
    }
    .account-name{
      margin-bottom: 4px;
      color: rgba(0,0,0,.85);
      font-weight: 500;
      font-size: 20px;
      line-height: 28px;
    }

  }
  .account-detail{
    .detail{
      display :flex;
      flex-direction:column;
      justify-content :flex-start;
      align-items :flex-start;
    }
  }
  .divider{
    display: flex;
    clear: both;
    width: 100%;
    min-width: 100%;
    margin: 24px 0;
  }
  .divider-dashed{
    background: 0 0;
    border: dashed rgba(0,0,0,.06);
    border-width: 1px 0 0;
  }
  .tag-title{
    text-align:left;
  }
  .tag-dynamic{
        display: flex;
    flex-direction: row;
    justify-content: flex-start;
    flex-wrap: wrap;
    width: 100%;
    margin: 10px 0px;

  }
   .el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
}
</style>
