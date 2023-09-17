<template>
  <a-layout>
    <a-layout style="padding: 0 24px 24px">

      <a-breadcrumb style="margin: 16px 0">
      </a-breadcrumb>
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <p>
          <a-form layout="inline" :model="param">
            <a-form-item>
              <a-input v-model:value="param.name" placeholder="名称">
              </a-input>
            </a-form-item>

            <a-form-item>
              <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
                查询
              </a-button>
            </a-form-item>

            <a-form-item>
              <a-button type="primary" @click="add()" >
                新增
              </a-button>
            </a-form-item>
          </a-form>
        </p>
        <a-table
            :columns="columns"
            :row-key="record => record.id"
            :data-source="ebooks"
            :pagination="pagination"
            :loading="loading"
            @change="handleTableChange"
        >
          <template #cover="{ text: cover }">
            <img v-if="cover" :src="cover" alt="avatar" />
          </template>

<!--          从显示分类一分类二 id 改为 分类一分类二 名称-->
          <template v-slot:category="{ text,record }">
            <span>{{ getCategoryName(record.category1Id)}} / {{ getCategoryName(record.category2Id)}}</span>
          </template>
          <template v-slot:action="{ text, record }">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
<!--                    气泡确认框-->
              <a-popconfirm
                  title="删除后不可恢复，确认删除？"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="danger" >
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-layout-content>
    </a-layout>
<!--    编辑弹出框-->
    <a-modal v-model:open="open" title="电子书表单" :confirm-loading="ModelLoading" @ok="handleModalOk">
      <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="wrapperCol">
        <a-form-item label="封面">
          <a-input v-model:value="ebook.cover" />
        </a-form-item>
        <a-form-item label="名称">
          <a-input v-model:value="ebook.name" />
        </a-form-item>
        <a-form-item label="分类">
<!--          级联组件-->
          <a-cascader
              v-model:value="categoryIds"
              :field-names="{ label: 'name', value: 'id',children: 'children'}"
              :options="level1"
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-input v-model:value="ebook.description" type="text"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout>
</template>
<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const param = ref();
    param.value = {};
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
        slots: {customRender: 'category' }
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/query", { params:
            {
              //page和size是从params中取值
              page: params.page,
              size: params.size,
              //模糊查询 param从响应式变量取值
              name: param.value.name
            }
      } ).then((response) => {
        loading.value = false;
        const data = response.data;
        //如果返回成功就进行查询 加了参数验证 如果page size异常就会报错
        if (data.success){
          ebooks.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        }else {
          //使用ant design vue的message
          message.error(data.message)
        }
      });
    };

    /*
    查询所有分类 级联组件使用需要
     */
    const level1 = ref();
    let categorys: any;    //从显示分类一分类二 id 改为 分类一分类二 名称  改为全局变量
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/list").then((response) => {
        loading.value = false;
        const data = response.data;
        //如果返回成功就进行查询 加了参数验证 如果page size异常就会报错
        if (data.success){
          categorys = data.content;
          console.log("原始数组",categorys);

          level1.value = []
          level1.value = Tool.array2Tree(categorys,0);
          console.log("树形结构",level1.value)

          //加载完分类后，再加载电子书，否则如果分类加载很慢，则电子书渲染会报错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize
          });

        }else {
          //使用ant design vue的message
          message.error(data.message)
        }
      });
    }

    //从显示分类一分类二 id 改为 分类一分类二 名称  获取名称
    const getCategoryName = (cid: number) => {
      console.log(cid)
      let result = "";
      categorys.forEach((item: any) => {
        console.log(item.id)
        if (item.id == cid){
          result = item.name;
        }
      });
      return result;
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };
    //-------表单-------
    const ebook = ref();
    const open = ref<boolean>(false);
    const ModelLoading = ref<boolean>(false);
    //级联组件
    const categoryIds = ref();

    //------------编辑
    const edit = (record: any) => {
      open.value = true;
      ebook.value = Tool.copy(record)
      //级联组件
      categoryIds.value = [ebook.value.category1Id,ebook.value.category2Id]
    };

    //------------新增
    const add = () => {
      open.value = true;
      ebook.value = {}
    };

    //------------删除
    const handleDelete = (id: number) => {
      console.log("id======>"+id)
      axios.delete("/delete/"+ id ).then((response) => {
        const data = response.data;
        if (data.success){  //data = commonResp
          //重新加载列表
          handleQuery({
            //重新查询当前页面的数据
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      });
    };

    const handleModalOk = () => {
      ModelLoading.value = true;
    //  级联组件
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
    // -------保存--------
      axios.post("/save", ebook.value).then((response) => {
        //只要有返回就将loading效果去掉
        ModelLoading.value = false;
        const data = response.data;
        console.log()
        if (data.success){  //data = commonResp
          open.value = false;

          //重新加载列表
          handleQuery({
            //重新查询当前页面的数据
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }else {
          message.error(data.message)
        }
      });
    };

    //初始
    onMounted(() => {
      handleQueryCategory();

    });

    return {

      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,

      //查询
      param,
      handleQuery,

      //按钮
      edit,
      add,
      handleDelete,

      //表单内容
      ebook,
      ModelLoading,
      handleModalOk,
      open,

      //级联变量返回
      categoryIds,
      level1,
      getCategoryName
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>

