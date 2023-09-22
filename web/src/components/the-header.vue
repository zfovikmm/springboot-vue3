<template>
  <a-layout-header class="header">
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <div>
        <a-menu-item key="/home">
          <router-link to="/">首页</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/user" :style="user.id? {} : {display:'none'}">
          <router-link to="/admin/user">用户管理</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/ebook" :style="user.id? {} : {display:'none'}">
          <router-link to="/admin/ebook">电子书管理</router-link>
        </a-menu-item>
        <a-menu-item key="/admin/Category" :style="user.id? {} : {display:'none'}">
          <router-link to="/admin/category">分类管理</router-link>
        </a-menu-item>
        <a-menu-item key="/about">
          <router-link to="/about">关于我们</router-link>
        </a-menu-item>
        <div class="login-menu">
          <a-popconfirm
              title="确认退出登录?"
              ok-text="是"
              cancel-text="否"
              @confirm="logout()"
          >
            <item class="login-menu" v-show="user.id">
              <span>退出登录</span>
            </item>
          </a-popconfirm>
          <item class="login-menu" v-show="user.id">
            <span>您好：{{ user.name }}</span>
          </item>
          <item class="login-menu" v-show="!user.id" @click="showLoginModal">
            <span>登录</span>
          </item>
        </div>
      </div>
    </a-menu>
    <a-modal
        title="登录"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName"/>
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>


<script lang="ts">
import {defineComponent, ref, computed, onMounted} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import store from "@/store";
import {Tool} from "@/util/tool";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  setup() {
    // 登录后保存
    const user = computed(() => store.state.user);

    // 用来登录
    const loginUser = ref({
      loginName: "test",
      password: "123"
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    // 登录
    const login = () => {
      console.log("开始登录");
      //弹窗
      loginModalLoading.value = true;

      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");
          store.commit("setUser", data.content);
          window.location.reload(); // 刷新页面
        } else {
          message.error(data.message);
        }
      });
    };

    // 退出登录
    const logout = () => {
      console.log("退出登录开始");
      axios.get('/user/logout/' + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("退出登录成功！");
          store.commit("setUser", {});
          window.location.reload(); // 刷新页面
        } else {
          message.error(data.message);
        }
      });
    };

    // WebSocket
    let websocket: any;
    let token: any;
    const onOpen = () => {
      console.log('WebSocket连接成功，状态码：', websocket.readyState)
    };
    const onMessage = (event: any) => {
      console.log('WebSocket收到消息：', event.data);
      // notification['info']({
      //   message: '收到消息',
      //   description: event.data,
      // });
    };
    const onError = () => {
      console.log('WebSocket连接错误，状态码：', websocket.readyState)
    };
    const onClose = () => {
      console.log('WebSocket连接关闭，状态码：', websocket.readyState)
    };
    const initWebSocket = () => {
      // 连接成功
      websocket.onopen = onOpen;
      // 收到消息的回调
      websocket.onmessage = onMessage;
      // 连接错误
      websocket.onerror = onError;
      // 连接关闭的回调
      websocket.onclose = onClose;
    };

    onMounted(() => {
      // WebSocket
      if ('WebSocket' in window) {
        token = Tool.uuid(10);
        // 连接地址：ws://127.0.0.1:8880/ws/xxx
        websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
        initWebSocket()

        // 关闭
        // websocket.close();
      } else {
        alert('当前浏览器 不支持')
      }
    });


    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      user,
      logout
    }
  }
});

</script>

<style>
.logo {
  width: 120px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 16px 28px 16px 0;*/
  float: left;
  color: white;
  font-size: 18px;
}
.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}
</style>
