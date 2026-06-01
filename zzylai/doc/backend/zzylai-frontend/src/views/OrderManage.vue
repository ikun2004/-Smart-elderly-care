<template>
  <div>
    <h2 style="font-weight: bold; font-size: 32px; margin-bottom: 20px;">订单管理</h2>
    <el-card>
      <!-- Tab栏 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabClick" style="margin-bottom: 20px;">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="待支付" name="0" />
        <el-tab-pane label="待执行" name="1" />
        <el-tab-pane label="已执行" name="2" />
        <el-tab-pane label="已完成" name="3" />
        <el-tab-pane label="已关闭" name="4" />
        <el-tab-pane label="已退款" name="5" />
      </el-tabs>

      <!-- 搜索栏 -->
      <el-row style="margin-bottom: 20px;" align="middle">
        <el-col :span="8">
          <el-input v-model="searchKey" placeholder="搜索订单..." clearable @keyup.enter.native="fetchData" @clear="onClear" />
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table :data="orderList" style="width: 100%;" stripe border>
        <el-table-column label="序号" type="index" width="70" align="center" :index="indexMethod" />
        <el-table-column label="图片" width="100" align="center">
          <template #default="scope">
            <el-avatar :src="scope.row.image" size="large" />
          </template>
        </el-table-column>
        <el-table-column prop="serviceName" label="服务" min-width="100" align="center" />
        <el-table-column prop="userName" label="老人" min-width="100" align="center" />
        <el-table-column prop="amount" label="金额" min-width="100" align="center">
          <template #default="scope">
            <span style="color: #f56c6c; font-weight: bold;">¥{{ scope.row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120" align="center" />
        <el-table-column prop="status" label="状态" min-width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待支付</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="primary">待执行</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="info">已执行</el-tag>
            <el-tag v-else-if="scope.row.status === 3" type="success">已完成</el-tag>
            <el-tag v-else-if="scope.row.status === 4" type="danger">已关闭</el-tag>
            <el-tag v-else-if="scope.row.status === 5" type="info">已退款</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" align="center" />
        <el-table-column label="操作" min-width="100" align="center" fixed="right">
          <template #default="scope">
            <div style="display: flex; flex-direction: column; align-items: center; gap: 8px;">
              <el-button 
                :icon="Edit"
                size="small" 
                type="primary" 
                @click="onEdit(scope.row)"
                style="width: 80px;"
                title="编辑"
              >编辑</el-button>
              <el-button 
                :icon="Delete"
                size="small" 
                type="danger" 
                @click="onDelete(scope.row)"
                style="width: 80px;"
                title="删除"
              >删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        style="margin-top: 20px; text-align: right;"
        background
        layout="sizes, prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="pageNum"
        :page-sizes="[5, 10, 15, 20]"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </el-card>

    <!-- 删除确认对话框 -->
    <el-dialog v-model="deleteDialogVisible" title="提示" width="300px">
      <span>确定要删除该订单吗？</span>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑订单对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑订单信息" width="600px">
      <el-form :model="editForm" label-width="100px" :rules="editRules" ref="editFormRef">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="editForm.orderNo" disabled />
        </el-form-item>
        <el-form-item label="服务名称" prop="serviceName">
          <el-input v-model="editForm.serviceName" disabled />
        </el-form-item>
        <el-form-item label="老人姓名" prop="userName">
          <el-input v-model="editForm.userName" disabled />
        </el-form-item>
        <el-form-item label="订单金额" prop="amount">
          <el-input v-model="editForm.amount" disabled>
            <template #append>元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <el-input v-model="editForm.createTime" disabled />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="editForm.status" placeholder="请选择状态">
            <el-option label="待支付" :value="0" />
            <el-option label="待执行" :value="1" />
            <el-option label="已执行" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已关闭" :value="4" />
            <el-option label="已退款" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="editForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import axios from 'axios'
import { Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const orderList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(5)
const searchKey = ref('')
const activeTab = ref('all')

const deleteDialogVisible = ref(false)
const deleteId = ref(null)

const editDialogVisible = ref(false)
const editForm = reactive({})
const editFormRef = ref()
const editRules = {
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

function fetchData() {
  const status = activeTab.value === 'all' ? undefined : parseInt(activeTab.value)
  axios.post('/admin/orders/page', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    searchKey: searchKey.value,
    status: status
  }).then(res => {
    if (res.data.code === 200) {
      orderList.value = res.data.data.records
      total.value = res.data.data.total
    }
  })
}

function handleTabClick(tab) {
  activeTab.value = tab.props.name
  pageNum.value = 1
  fetchData()
}

function handlePageChange(val) {
  pageNum.value = val
  fetchData()
}

function handleSizeChange(val) {
  pageSize.value = val
  pageNum.value = 1
  fetchData()
}

function indexMethod(index) {
  return (pageNum.value - 1) * pageSize.value + index + 1
}

function onEdit(row) {
  axios.get(`/admin/orders/${row.id}`).then(res => {
    if (res.data.code === 200) {
      Object.assign(editForm, res.data.data)
      editDialogVisible.value = true
    }
  })
}

function onDelete(row) {
  deleteId.value = row.id
  deleteDialogVisible.value = true
}

function confirmDelete() {
  axios.delete(`/admin/orders/${deleteId.value}`).then(res => {
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      deleteDialogVisible.value = false
      fetchData()
    }
  })
}

function onClear() {
  pageNum.value = 1
  fetchData()
}

function submitEdit() {
  editFormRef.value.validate(valid => {
    if (!valid) return
    axios.put('/admin/orders', editForm).then(res => {
      if (res.data.code === 200) {
        ElMessage.success('修改成功')
        editDialogVisible.value = false
        fetchData()
      }
    })
  })
}

onMounted(fetchData)
</script>

<style scoped>
.el-avatar {
  border: 1px solid #eee;
}
</style> 