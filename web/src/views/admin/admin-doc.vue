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
              <a-button type="primary" @click="handleQuery({page: 1, size: 100})">
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
            :data-source="level1"
            :loading="loading"
            :pagination="false"
        >
          <template #cover="{ text: cover }">
            <img v-if="cover" :src="cover" alt="avatar" />
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
    <a-modal v-model:open="open" title="文档表单" :confirm-loading="ModelLoading" @ok="handleModalOk">
      <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="wrapperCol">
        <a-form-item label="名称">
          <a-input v-model:value="doc.name" />
        </a-form-item>
        <a-form-item label="父文档">
          <a-tree-select
              v-model:value="doc.parent"
              show-search
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              allow-clear
              tree-default-expand-all
              :tree-data="treeSelectData"
              placeholder="请选择父文档"
              tree-node-filter-prop="label"
              :replaceFields="{label: 'name',key: 'id',value: 'id'}"
          >
          </a-tree-select>
        </a-form-item>
<!--        <a-form-item label="父文档">-->
<!--          <a-input v-model:value="doc.parent"/>-->
<!--          <a-select-->
<!--              ref="select"-->
<!--              v-model:value="doc.parent"-->
<!--          >-->
<!--            <a-select-option value="0">无</a-select-option>&lt;!&ndash;doc.id === c.id 如果当前表单的id跟选项的id是一样 就不能选&ndash;&gt;-->
<!--            <a-select-option v-for=" c in level1" :key="c.id" :value="c.id" :disabled="doc.id === c.id">-->
<!--              {{c.name}}-->
<!--            </a-select-option>-->
<!--          </a-select>-->
<!--        </a-form-item>-->
        <a-form-item label="排序">
          <a-input v-model:value="doc.sort"/>
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
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    //通过这个可以获取路由信息
    const route = useRoute()
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父文档',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];
    /*
      一级文档树，children属性就是二级文档
      [{
        id: "",
        name: "",
        children: [{
        id: "",
        name: "",
        }]
      }]
     */
    //一级文档树，children属性就是二级文档
    const level1 = ref();

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/list").then((response) => {
        loading.value = false;
        const data = response.data;
        //如果返回成功就进行查询 加了参数验证 如果page size异常就会报错
        if (data.success){
          docs.value = data.content;
          //初始化为空数组
          level1.value = [];
          //自己写的工具类
          level1.value=Tool.array2Tree(docs.value,0);

        }else {
          //使用ant design vue的message
          message.error(data.message)
        }
      });
    };

    //-------表单-------
    //因为树选择组件的属性状态，会随当前编辑的节点而变化
    const treeSelectData = ref();
    treeSelectData.value = [];

    const doc = ref({});
    const open = ref<boolean>(false);
    const ModelLoading = ref<boolean>(false);
    //------------编辑
    const edit = (record: any) => {
      open.value = true;
      doc.value = Tool.copy(record)

      //不能选择当前节点机器所有子孙节点，所有父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value,record.id);

      //为选择树添加一个 “无”
      treeSelectData.value.unshift({id: 0, name: '无'})

    };

    //------------新增
    const add = () => {
      open.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      }


      treeSelectData.value = Tool.copy(level1.value);

      //为选择树添加一个“无”
      treeSelectData.value.unshift({id: 0,name: '无'})
    };

    /*
    将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any,id: any) => {
      //遍历数组，即遍历某一层节点
      for(let i=0;i<treeSelectData.length;i++){
        const node = treeSelectData[i];
        if(node.id === id){
          //如果当前节点就是目标节点
          console.log("disabled",node);
          //将目标节点设置位disabled
          node.disabled = true;

          //遍历所有字节点，将所有字节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)){
            for(let j=0;j< children.length;j++){
              setDisable(children,children[j].id)
            }
          }else {
            // 如果当前节点不是目标节点，则到其子节点再找找看。
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              setDisable(children, id);
            }
          }
        }
      }
    }



    //------------删除
    const handleDelete = (id: number) => {
      console.log("id======>"+id)
      axios.delete("/doc/delete/"+ id ).then((response) => {
        const data = response.data;
        if (data.success){  //data = commonResp
          //重新加载列表
          handleQuery();
        }
      });
    };

    const handleModalOk = () => {
      ModelLoading.value = true;
    // -------保存--------
      axios.post("/doc/save", doc.value).then((response) => {
        //只要有返回就将loading效果去掉
        ModelLoading.value = false;
        const data = response.data;
        console.log()
        if (data.success){  //data = commonResp
          open.value = false;
          //重新加载列表
          handleQuery();
        }else {
          message.error(data.message)
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    return {

      docs,
      columns,
      loading,
      //树形结构
      level1,

      //查询
      param,
      handleQuery,

    //按钮
      edit,
      add,
      handleDelete,

      //表单内容
      doc,
      ModelLoading,
      handleModalOk,
      open,

      //文档分类无限级树
      treeSelectData
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

