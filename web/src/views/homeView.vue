<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          v-model:selectedKeys="selectedKeys2"
          v-model:openKeys="openKeys"
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
      >
        <a-menu-item key="welcome">
          <MailOutlined />
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id" >
          <template v-slot:title>
            <span><user-outlined />{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
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
<!--       :pagination="pagination" 分页 -->
        <a-list item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3}"  :data-source="ebooks">
<!--          循环ebooks，读取数据-->
          <template #renderItem="{ item }">
            <a-list-item key="item.name">
              <template #actions>
<!--                三个小图标-->
                <span v-for="{ icon, text } in actions" :key="icon">
                  <component :is="icon" style="margin-right: 6px" />
                  {{ text }}
                </span>
              </template>
              <a-list-item-meta :description="item.description">
                <template #title>
                  <a :href="item.href">{{ item.name }}</a>
                </template>
                <template #avatar><a-avatar :src="item.cover" /></template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
//content
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
const listData: Record<string, string>[] = [];


/*
查询所有分类
 */
const level1 = ref();
let categorys: any;    //从显示分类一分类二 id 改为 分类一分类二 名称  改为全局变量
const handleQueryCategory = () => {
  axios.get("/category/list").then((response) => {
    const data = response.data;
    //如果返回成功就进行查询 加了参数验证 如果page size异常就会报错
    if (data.success){
      categorys = data.content;
      console.log("原始数组",categorys);

      level1.value = []
      level1.value = Tool.array2Tree(categorys,0);
      console.log("树形结构",level1.value)
    }else {
      //使用ant design vue的message
      message.error(data.message)
    }
  });

  const handleClick =() => {
    console.log("menu click")
  };
}



for (let i = 0; i < 23; i++) {

  listData.push({
    href: 'https://www.antdv.com/',
    title: `ant design vue part ${i}`,
    avatar: 'https://joeschmoe.io/api/v1/random',
    description:
        'Ant Design, a design language for background applications, is refined by Ant UED Team.',
    content:
        'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
  });
}

const pagination = {
  onChange: (page: number) => {
    console.log(page);
  },
  pageSize: 3,
};
const actions: Record<string, any>[] = [
  { icon: StarOutlined, text: '156' },
  { icon: LikeOutlined, text: '156' },
  { icon: MessageOutlined, text: '2' },
];

import { defineComponent,onMounted,ref} from 'vue';
import axios from "axios";
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";
export default defineComponent({
  name: 'HomeView',
  setup(){
    const ebooks = ref();

    //初始化
    onMounted(() =>{
      handleQueryCategory();
      axios.get("/list").then((response) => {
       const data = response.data;
       ebooks.value = data.content;
      });
    })

    return{
      ebooks,
      listData,
      //分页查询
      // pagination: {
      //   onChange: (page: number) => {
      //     console.log(page);
      //   },
      //   pageSize: 3,
      // },
      actions: [
        { icon: StarOutlined, text: '156' },
        { icon: LikeOutlined, text: '156' },
        { icon: MessageOutlined, text: '2' },
      ],

      //树级菜单
      level1,
    }
  }
});


</script>

<style scoped>
  .ant-avatar{
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
  }
</style>
