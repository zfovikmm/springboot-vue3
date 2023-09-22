<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          v-model:selectedKeys="selectedKeys2"
          v-model:openKeys="openKeys"
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MailOutlined/>
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <span><user-outlined/>{{ item.name }}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined/>
            <span>{{ child.name }}</span>
          </a-menu-item>
        </a-sub-menu>
        <!--        <a-menu-item key="tip" :disabled="true">-->
        <!--          <span>以上菜单在分类管理配置</span>-->
        <!--        </a-menu-item>-->
      </a-menu>
    </a-layout-sider>
    <a-layout style="padding: 0 24px 24px">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item>Home</a-breadcrumb-item>
        <a-breadcrumb-item>List</a-breadcrumb-item>
        <a-breadcrumb-item>App</a-breadcrumb-item>
      </a-breadcrumb>
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <!--        欢迎页面-->
        <div class="welcome" v-show="isShowWelcome">
          <the-welcome>欢迎来到电子书库</the-welcome>
        </div>
        <!--       :pagination="pagination" 分页 -->
        <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3}"
                :data-source="booklist">
          <!--          循环ebooks，读取数据-->
          <template #renderItem="{ item }">
            <a-list-item key="item.name">
              <template #actions>
                <span>
                <component v-bind:is="'FileOutlined'" style="margin-right: 8px"/>
                {{ item.docCount }}
              </span>
                <span>
                <component v-bind:is="'UserOutlined'" style="margin-right: 8px"/>
                {{ item.viewCount }}
              </span>
                <span>
                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
                {{ item.voteCount }}
              </span>
              </template>
              <a-list-item-meta :description="item.description">
                <template #title>
                  <router-link :to="'/doc?ebookId=' + item.id">
                    {{ item.name }}
                  </router-link>
                </template>
                <template #avatar><a-avatar :src="item.cover"/></template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>

      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'HomeView',
  setup() {
    const ebooks = ref();
    const openKeys =  ref();
    const booklist = ref();

    /*
查询所有分类
 */
    const level1 = ref();
    let categorys: any;    //从显示分类一分类二 id 改为 分类一分类二 名称  改为全局变量
    const handleQueryCategory = () => {
      axios.get("/category/list").then((response) => {
        const data = response.data;
        //如果返回成功就进行查询 加了参数验证 如果page size异常就会报错
        if (data.success) {

          categorys = data.content;
          console.log("原始数组", categorys);

          // 加载完分类后，将侧边栏全部展开
          openKeys.value = [];
          for (let i = 0; i < categorys.length; i++) {
            openKeys.value.push(categorys[i].id)
          }

          level1.value = []
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构", level1.value)
        } else {
          //使用ant design vue的message
          console.log("失败")
          message.error(data.message)
        }
      });
    };

    //响应式变量 确认是否显示欢迎页面
    const isShowWelcome = ref(true);
    let categoryId2 = 0;

    /*
    查询数据
    */
    const handleQueryEbook = () => {
      const ebooks = ref();
      axios.get("/query", {
        params: {
          page: 1,
          size: 100,
          categoryId2: categoryId2
        }
      }).then((response) => {
        const data = response.data;
        booklist.value = data.content.list;
      });
    }

    const handleClick = (value: any) => {
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
    }

    //初始化
    onMounted(() => {
      handleQueryCategory();
      //handleQueryEbook();
    });

    return {
      booklist,
      ebooks,
      // actions: [
      //   {icon: StarOutlined, text: '156'},
      //   {icon: LikeOutlined, text: '156'},
      //   {icon: MessageOutlined, text: '2'},
      // ],
      pagination: {
        onChange: (page: any) => {
          console.log(page);
        },
        pageSize: 3,
      },

      //树级菜单
      handleClick,
      level1,

      // 欢迎界面
      isShowWelcome,
      openKeys,


    }
  }
});


</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
