<template>
  <div>
    <h2 style="font-weight: bold; font-size: 32px; margin-bottom: 20px;">穿戴设备管理</h2>
    <el-card>
      <el-row style="margin-bottom: 20px;" align="middle">
        <el-col :span="8">
          <el-input v-model="searchKey" placeholder="搜索设备..." clearable @keyup.enter.native="fetchData" @clear="onClear" />
        </el-col>
        <el-col :span="8" :offset="8" style="text-align: right;">
          <el-button type="success" @click="onSync">同步数据</el-button>
          <el-button type="primary" @click="onAdd">+ 新增设备</el-button>
        </el-col>
      </el-row>
      <el-table :data="deviceList" style="width: 100%;" stripe border>
        <el-table-column label="序号" type="index" width="70" align="center" :index="indexMethod" />
        <el-table-column prop="deviceName" label="设备名字" min-width="120" align="center" />
        <el-table-column prop="nickname" label="昵称" min-width="120" align="center" />
        <el-table-column prop="iotId" label="iotID" min-width="200" align="center" />
        <el-table-column prop="productName" label="产品名字" min-width="120" align="center" />
        <el-table-column prop="functionId" label="功能" min-width="100" align="center">
          <template #default="scope">
            <span>{{ scope.row.functionId || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="locationType" label="位置" min-width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.locationType === 0" type="success">随身设备</el-tag>
            <el-tag v-else-if="scope.row.locationType === 1" type="primary">固定设备</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="nodeId" label="节点" min-width="80" align="center" />
        <el-table-column prop="deviceDescription" label="设备描述" min-width="120" align="center">
          <template #default="scope">
            <span>{{ scope.row.deviceDescription || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="180" align="center">
          <template #default="scope">
            <span>{{ formatTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="150" align="center" fixed="right">
          <template #default="scope">
            <div style="display: flex; flex-direction: column; align-items: center; gap: 8px;">
              <el-button 
                :icon="Edit"
                size="small" 
                type="primary" 
                @click="onEdit(scope.row)"
                title="编辑"
              >编辑</el-button>
              <el-button
                :icon="View"
                size="small"
                type="info"
                @click="onView(scope.row)"
                title="查看"
              >查看</el-button>
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
      <span>确定要删除该设备吗？</span>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 同步数据确认对话框 -->
    <el-dialog v-model="syncDialogVisible" title="提示" width="400px">
      <span>将同步huaweiIOT平台的产品和设备数据，确定开始同步吗？</span>
      <template #footer>
        <el-button @click="syncDialogVisible = false">取消</el-button>
        <el-button type="success" @click="confirmSync">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑设备对话框 -->
    <el-dialog v-model="editDialogVisible" :title="editDialogTitle" width="600px">
      <el-form :model="editForm" label-width="120px" :rules="editRules" ref="editFormRef">
        <el-form-item label="IOTID" prop="iotId">
          <el-input v-model="editForm.iotId" disabled />
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="editForm.deviceName" />
        </el-form-item>
        <el-form-item label="产品" prop="productKey">
          <el-select v-model="editForm.productKey" placeholder="请选择产品" @change="onProductChange">
            <el-option 
              v-for="product in productList" 
              :key="product.productId" 
              :label="product.name" 
              :value="product.productId" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="设备类型" prop="locationType">
          <el-select v-model="editForm.locationType" placeholder="请选择设备类型" @change="onLocationTypeChange">
            <el-option label="随身设备" :value="0" />
            <el-option label="固定设备" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="接入位置" prop="bindingLocation">
          <el-select 
            v-if="editForm.locationType === 0" 
            v-model="editForm.bindingLocation" 
            placeholder="请选择老人"
            filterable
            remote
            :remote-method="searchElders"
            :loading="elderLoading"
          >
            <el-option 
              v-for="elder in elderList" 
              :key="elder.id" 
              :label="elder.name" 
              :value="elder.id.toString()" 
            />
          </el-select>
          <el-input 
            v-else-if="editForm.locationType === 1" 
            v-model="editForm.bindingLocation" 
            placeholder="请输入接入位置"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="editForm.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 查看设备对话框 -->
    <el-dialog v-model="viewDialogVisible" title="设备详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="IOTID">{{ viewForm.iotId }}</el-descriptions-item>
        <el-descriptions-item label="设备名称">{{ viewForm.deviceName }}</el-descriptions-item>
        <el-descriptions-item label="产品">{{ viewForm.productName }}</el-descriptions-item>
        <el-descriptions-item label="设备类型">
          <el-tag v-if="viewForm.locationType === 0" type="success">随身设备</el-tag>
          <el-tag v-else-if="viewForm.locationType === 1" type="primary">固定设备</el-tag>
          <el-tag v-else type="info">未知</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="接入位置">{{ viewForm.bindingLocation }}</el-descriptions-item>
        <el-descriptions-item label="节点">{{ viewForm.nodeId }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewForm.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatTime(viewForm.createTime) }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import axios from 'axios'
import { Edit, Delete, View } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const deviceList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(5)
const searchKey = ref('')

const deleteDialogVisible = ref(false)
const deleteIotId = ref(null)

const syncDialogVisible = ref(false)

const editDialogVisible = ref(false)
const editForm = reactive({})
const editFormRef = ref()
const editRules = {
  deviceName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
  productKey: [{ required: true, message: '请选择产品', trigger: 'change' }],
  locationType: [{ required: true, message: '请选择设备类型', trigger: 'change' }],
  bindingLocation: [{ required: true, message: '请输入接入位置', trigger: 'blur' }]
}
const editDialogTitle = ref('编辑设备信息')

const viewDialogVisible = ref(false)
const viewForm = reactive({})

const productList = ref([])
const elderList = ref([])
const elderLoading = ref(false)

function fetchData() {
  axios.post('/admin/device/list', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    searchKey: searchKey.value
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

function fetchProductList() {
  axios.get('/admin/device/allProduct').then(res => {
    if (res.data.code === 200) {
      productList.value = res.data.data
    }
  })
}

function searchElders(query) {
  if (query !== '') {
    elderLoading.value = true
    axios.post('/admin/elder/page', {
      pageNum: 1,
      pageSize: 10,
      searchKey: query,
      status: 1
    }).then(res => {
      if (res.data.code === 200) {
        elderList.value = res.data.data.records
      }
      elderLoading.value = false
    })
  } else {
    elderList.value = []
  }
}

function onProductChange(value) {
  const product = productList.value.find(p => p.productId === value)
  if (product) {
    editForm.productName = product.name
  }
}

function onLocationTypeChange(value) {
  editForm.bindingLocation = ''
}

function onSync() {
  syncDialogVisible.value = true
}

function confirmSync() {
  axios.post('/admin/device/syncProductList').then(res => {
    if (res.data.code === 200) {
      ElMessage.success('同步成功')
      syncDialogVisible.value = false
      fetchData()
      fetchProductList()
    } else {
      ElMessage.error(res.data.msg || '同步失败')
    }
  }).catch(error => {
    ElMessage.error('同步失败')
  })
}

function onAdd() {
  editDialogTitle.value = '新增设备'
  Object.assign(editForm, {
    id: undefined,
    iotId: '',
    deviceName: '',
    productKey: '',
    productName: '',
    locationType: 0,
    bindingLocation: '',
    remark: '',
    nodeId: null
  })
  editDialogVisible.value = true
}

function onEdit(row) {
  editDialogTitle.value = '编辑设备信息'
  axios.get(`/admin/device/${row.iotId}`).then(res => {
    if (res.data.code === 200) {
      Object.assign(editForm, res.data.data)
      editDialogVisible.value = true
    }
  })
}

function onView(row) {
  axios.get(`/admin/device/${row.iotId}`).then(res => {
    if (res.data.code === 200) {
      Object.assign(viewForm, res.data.data)
      viewDialogVisible.value = true
    }
  })
}

function onDelete(row) {
  deleteIotId.value = row.iotId
  deleteDialogVisible.value = true
}

function confirmDelete() {
  axios.delete(`/admin/device/${deleteIotId.value}`).then(res => {
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
    if (editDialogTitle.value === '新增设备') {
      axios.post('/admin/device/register', editForm).then(res => {
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
      axios.post('/admin/device/edit', editForm).then(res => {
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

onMounted(() => {
  fetchData()
  fetchProductList()
})
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