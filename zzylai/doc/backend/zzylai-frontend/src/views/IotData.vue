<template>
  <div>
    <h2 style="font-weight: bold; font-size: 32px; margin-bottom: 20px;">设备数据管理</h2>
    <el-card>
      <el-row style="margin-bottom: 20px;" align="middle">
        <el-col :span="8">
          <el-input v-model="searchKey" placeholder="搜索数据..." clearable @keyup.enter.native="fetchData" @clear="onClear" />
        </el-col>
     
      </el-row>
      <el-table :data="deviceList" style="width: 100%;" stripe border>
        <el-table-column label="序号" type="index" width="70" align="center" :index="indexMethod" />
        <el-table-column prop="deviceName" label="设备名字" min-width="120" align="center" />
        <el-table-column prop="nickname" label="昵称" min-width="120" align="center" />
        <el-table-column prop="iotId" label="iotID" min-width="200" align="center" />
        <el-table-column prop="productName" label="产品名字" min-width="120" align="center" />
        <el-table-column prop="functionId" label="功能" min-width="100" align="center" />
        <el-table-column prop="locationType" label="位置" min-width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.locationType === 0" type="success">随身设备</el-tag>
            <el-tag v-else-if="scope.row.locationType === 1" type="primary">固定设备</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataValue" label="值" min-width="80" align="center">
          <template #default="scope">
            <el-tag type="primary">{{ scope.row.dataValue }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alarmTime" label="数据上报时间" min-width="180" align="center">
          <template #default="scope">
            <span>{{ formatTime(scope.row.alarmTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="100" align="center" fixed="right">
          <template #default="scope">
            <div style="display: flex; flex-direction: column; align-items: center; gap: 8px;">
        
              <el-button 
                :icon="Delete"
                size="small" 
                type="danger" 
                @click="onDelete(scope.row)"
                title="删除"
              >删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
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
    <el-dialog v-model="deleteDialogVisible" title="提示" width="400px">
      <span>确定要删除该iot_id设备的所有数据吗？</span>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑设备对话框 -->
    <el-dialog v-model="editDialogVisible" :title="editDialogTitle" width="600px">
      <el-form :model="editForm" label-width="100px" :rules="editRules" ref="editFormRef">
        <el-form-item label="设备名字" prop="deviceName">
          <el-input v-model="editForm.deviceName" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="editForm.nickname" />
        </el-form-item>
        <el-form-item label="iotID" prop="iotId">
          <el-input v-model="editForm.iotId" />
        </el-form-item>
        <el-form-item label="产品名字" prop="productName">
          <el-input v-model="editForm.productName" />
        </el-form-item>
        <el-form-item label="功能" prop="functionId">
          <el-input v-model="editForm.functionId" />
        </el-form-item>
        <el-form-item label="位置类型" prop="locationType">
          <el-select v-model="editForm.locationType" placeholder="请选择位置类型">
            <el-option label="随身设备" :value="0" />
            <el-option label="固定设备" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据值" prop="dataValue">
          <el-input v-model="editForm.dataValue" />
        </el-form-item>
        <el-form-item label="数据上报时间" prop="alarmTime">
          <el-date-picker 
            v-model="editForm.alarmTime" 
            type="datetime" 
            value-format="YYYY-MM-DDTHH:mm:ss.SSS[Z]" 
            placeholder="选择日期时间" 
          />
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
import { ElMessage, ElMessageBox } from 'element-plus'

const deviceList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(5)
const searchKey = ref('')

const deleteDialogVisible = ref(false)
const deleteIotId = ref(null)

const editDialogVisible = ref(false)
const editForm = reactive({})
const editFormRef = ref()
const editRules = {
  deviceName: [{ required: true, message: '请输入设备名字', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  iotId: [{ required: true, message: '请输入iotID', trigger: 'blur' }],
  productName: [{ required: true, message: '请输入产品名字', trigger: 'blur' }],
  functionId: [{ required: true, message: '请输入功能', trigger: 'blur' }],
  dataValue: [{ required: true, message: '请输入数据值', trigger: 'blur' }]
}
const editDialogTitle = ref('编辑设备信息')

function fetchData() {
  axios.post('/admin/devicedata/list', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    searchKey: searchKey.value,
    status: 0
  }).then(res => {
    if (res.data.code === 200) {
      deviceList.value = res.data.data.records
      total.value = res.data.data.total
    }
  })
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

function formatTime(timeStr) {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

function onAdd() {
  editDialogTitle.value = '增加设备'
  Object.assign(editForm, {
    id: undefined,
    deviceName: '',
    nickname: '',
    iotId: '',
    productKey: '',
    productName: '',
    functionId: '',
    locationType: 0,
    dataValue: '',
    alarmTime: '',
    deviceDescription: '',
    remark: ''
  })
  editDialogVisible.value = true
}

function onEdit(row) {
  editDialogTitle.value = '编辑设备信息'
  Object.assign(editForm, row)
  editDialogVisible.value = true
}

function onDelete(row) {
  deleteIotId.value = row.iotId
  deleteDialogVisible.value = true
}

function confirmDelete() {
  axios.delete(`/admin/devicedata/${deleteIotId.value}`).then(res => {
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      deleteDialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.data.msg || '删除失败')
    }
  }).catch(error => {
    ElMessage.error('删除失败')
  })
}

function onClear() {
  pageNum.value = 1
  fetchData()
}

function submitEdit() {
  editFormRef.value.validate(valid => {
    if (!valid) return
    if (editDialogTitle.value === '增加设备') {
      axios.post('/admin/devicedata', editForm).then(res => {
        if (res.data.code === 200) {
          ElMessage.success('新增成功')
          editDialogVisible.value = false
          fetchData()
        } else {
          ElMessage.error(res.data.msg || '新增失败')
        }
      }).catch(error => {
        ElMessage.error('新增失败')
      })
    } else {
      axios.put('/admin/devicedata', editForm).then(res => {
        if (res.data.code === 200) {
          ElMessage.success('修改成功')
          editDialogVisible.value = false
          fetchData()
        } else {
          ElMessage.error(res.data.msg || '修改失败')
        }
      }).catch(error => {
        ElMessage.error('修改失败')
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
.avatar-uploader .avatar {
  width: 80px;
  height: 80px;
  display: block;
  border-radius: 50%;
}
</style> 