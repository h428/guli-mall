<template>
  <div>
    <el-tree :props="defaultProps" :data="menus" node-key="catId"
             :expand-on-click-node="false" show-checkbox
             :default-expanded-keys="defaultExpandedKeys"
             @node-click="handleNodeClick"
    >
    <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="data.catLevel < 3"
            type="text"
            size="mini"
            @click="() => append(data)">
            Append
          </el-button>
          <el-button
            v-if="data.children.length === 0"
            type="text"
            size="mini"
            @click="() => remove(node, data)">
            Delete
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog title="收货地址" :visible.sync="dialogVisible">
      <el-form :model="category">
        <el-form-item label="分类名称" :label-width="formLabelWidth">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addCategory">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
export default {
  name: 'category',
  data () {
    return {
      formLabelWidth: '120px',
      menus: [],
      defaultExpandedKeys: [],
      category: { name: '', parentCid: null, catLevel: 0, showStatus: 1, sort: 0, productCount: 0 },
      dialogVisible: false,
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  methods: {
    async addCategory () {
      console.log('category ', this.category)
      await this.$http({
        url: this.$http.adornUrl('/product/category/save'),
        method: 'post',
        data: this.$http.adornData(this.category, false)
      })
      this.defaultExpandedKeys = [this.category.parentCid]
      this.getMenus()
      this.dialogVisible = false
    },
    append (data) {
      this.category.parentCid = data.catId
      this.category.name = ''
      this.dialogVisible = true
      // const newChild = {id: Math.random(), name: 'testtest', children: []}
      // if (!data.children) {
      //   this.$set(data, 'children', [])
      // }
      // data.children.push(newChild)
    },
    async remove (node, data) {
      const thenCall = async () => {
        const ids = [data.catId]
        await this.$http({
          url: this.$http.adornUrl('/product/category/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        })
        this.$message({
          message: '菜单删除成功',
          type: 'success'
        })
        // 前端移除当前 node
        const parent = node.parent
        const children = parent.data.children || parent.data
        console.log('children ', children)
        console.log('data ', data)
        const index = children.findIndex(d => d.catId === data.catId)
        console.log('index ', index)
        children.splice(index, 1)
        console.log('children ', children)
      }
      this.$confirm(`是否删除【${data.name}】菜单`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(thenCall)
    },
    handleNodeClick (data) {
      console.log('click ', data)
    },
    getMenus () {
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then(({data}) => {
        this.menus = data.data
      })
    }
  },
  // 生命周期
  created () {
    this.getMenus()
  }
}
</script>

<style scoped>

</style>
