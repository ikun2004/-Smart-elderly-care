<template>
  <div>
    <h2 style="font-weight: bold; font-size: 32px; margin-bottom: 20px;">预约管理</h2>
    <el-card>
      <!-- Tab栏 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabClick" style="margin-bottom: 20px;">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="待报到" name="0" />
        <el-tab-pane label="已完成" name="1" />
        <el-tab-pane label="已取消" name="2" />
      </el-tabs>

      <!-- 搜索栏 -->
      <el-row style="margin-bottom: 20px;" align="middle">
        <el-col :span="8">
          <el-input v-model="searchKey" placeholder="搜索预约..." clearable @keyup.enter.native="fetchData" @clear="onClear" />
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table :data="reservationList" style="width: 100%;" stripe border>
        <el-table-column label="序号" type="index" width="70" align="center" :index="indexMethod" />
        <el-table-column prop="name" label="老人" min-width="100" align="center" />
        <el-table-column prop="visitor" label="探访人" min-width="100" align="center" />
        <el-table-column prop="mobile" label="手机" min-width="120" align="center" />
        <el-table-column prop="time" label="探访时间" min-width="160" align="center">
          <template #default="scope">
            <span>{{ formatTime(scope.row.time) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="探访类型" min-width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.type === 0" type="success">参观预约</el-tag>
            <el-tag v-else-if="scope.row.type === 1" type="primary">探访预约</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" min-width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待报到</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已完成</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="danger">已取消</el-tag>
            <el-tag v-else-if="scope.row.status === 3" type="info">过期</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120" align="center" />
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
      <span>确定要删除该预约吗？</span>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑预约对话框 -->
    <el-dialog v-model="editDialogVisible" :title="editDialogTitle" width="600px">
      <el-form :model="editForm" label-width="100px" :rules="editRules" ref="editFormRef">
        <el-form-item label="老人姓名" prop="name">
          <el-input v-model="editForm.name" disabled />
        </el-form-item>
        <el-form-item label="探访人" prop="visitor">
          <el-input v-model="editForm.visitor" disabled />
        </el-form-item>
        <el-form-item label="手机号码" prop="mobile">
          <el-input v-model="editForm.mobile" />
        </el-form-item>
        <el-form-item label="探访时间" prop="time">
          <el-date-picker 
            v-model="editForm.time" 
            type="datetime" 
            value-format="YYYY-MM-DDTHH:mm:ss" 
            placeholder="选择日期时间" 
          />
        </el-form-item>
        <el-form-item label="探访类型" prop="type">
          <el-select v-model="editForm.type" placeholder="请选择探访类型" disabled>
            <el-option label="参观预约" :value="0" />
            <el-option label="探访预约" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="editForm.status" placeholder="请选择状态">
            <el-option label="待报到" :value="0" />
            <el-option label="已完成" :value="1" />
            <el-option label="已取消" :value="2" />
            <el-option label="过期" :value="3" />
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

const reservationList = ref([])
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
  visitor: [{ required: true, message: '请输入探访人', trigger: 'blur' }],
  mobile: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  time: [{ required: true, message: '请选择探访时间', trigger: 'change' }],
  type: [{ required: true, message: '请选择探访类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}
const editDialogTitle = ref('编辑预约信息')

function fetchData() {
  const status = activeTab.value === 'all' ? undefined : parseInt(activeTab.value)
  axios.post('/admin/reservation/page', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    searchKey: searchKey.value,
    status: status
  }).then(res => {
    if (res.data.code === 200) {
      reservationList.value = res.data.data.records
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

function formatTime(time) {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

function onAdd() {
  editDialogTitle.value = '新增预约'
  Object.assign(editForm, {
    id: undefined,
    name: '',
    visitor: '',
    mobile: '',
    time: '',
    type: 0,
    status: 0,
    remark: ''
  })
  editDialogVisible.value = true
}

function onEdit(row) {
  editDialogTitle.value = '编辑预约信息'
  axios.get(`/admin/reservation/${row.id}`).then(res => {
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
  axios.delete(`/admin/reservation/${deleteId.value}`).then(res => {
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
    if (editDialogTitle.value === '新增预约') {
      // TODO: 实现新增预约接口
      ElMessage.info('新增预约功能待实现')
    } else {
      axios.put('/admin/reservation', editForm).then(res => {
        if (res.data.code === 200) {
          ElMessage.success('修改成功')
          editDialogVisible.value = false
          fetchData()
        }
      })
    }
  })
}

onMounted(fetchData)
</script>

<style scoped>
.el-avatar {
  border: 1px solid #eee;
}
</style> 