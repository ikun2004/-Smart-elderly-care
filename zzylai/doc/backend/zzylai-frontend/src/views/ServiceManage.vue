<template>
  <div>
    <h2 style="font-weight: bold; font-size: 32px; margin-bottom: 20px;">护理服务管理</h2>
    <el-card>
      <el-row style="margin-bottom: 20px;" align="middle">
        <el-col :span="8">
          <el-input v-model="searchKey" placeholder="搜索服务..." clearable @keyup.enter.native="fetchData" @clear="onClear" />
        </el-col>
        <el-col :span="4" :offset="12" style="text-align: right;">
          <el-button type="primary" @click="onAdd">+ 增加服务</el-button>
        </el-col>
      </el-row>
      <el-table :data="serviceList" style="width: 100%;" stripe border>
        <el-table-column label="序号" type="index" width="70" align="center" :index="indexMethod" />
        <el-table-column label="图片" width="100" align="center">
          <template #default="scope">
            <el-avatar :src="scope.row.image" size="large" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" min-width="110" align="center" />
        <el-table-column prop="price" label="价格" min-width="90" align="center">
          <template #default="scope">
            <span>￥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" min-width="50" align="center" />
        <el-table-column prop="orderNo" label="排序" min-width="50" align="center" />
        <el-table-column prop="nursingRequirement" label="护理说明" min-width="70" align="center" />
        <el-table-column prop="status" label="状态" min-width="90" align="center" show-overflow-tooltip>
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="info">禁用</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="100" align="center" fixed="right" show-overflow-tooltip>
          <template #default="scope">
            <el-button size="small" type="primary" @click="onEdit(scope.row)" style="margin-right: 8px;">编辑</el-button>
            <el-button size="small" type="danger" @click="onDelete(scope.row)">删除</el-button>
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
    <el-dialog v-model="deleteDialogVisible" title="提示" width="300px">
      <span>确定要删除该服务吗？</span>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑/新增服务对话框 -->
    <el-dialog v-model="editDialogVisible" :title="editDialogTitle" width="600px">
      <el-form :model="editForm" label-width="100px" :rules="editRules" ref="editFormRef">
        <el-form-item label="图片" prop="image">
          <el-upload
            class="avatar-uploader"
            action="/admin/upload"
            :show-file-list="true"
            :file-list="fileList"
            :on-success="handleUploadSuccess"
            :limit="1"
            :before-upload="() => true"
            list-type="picture-card"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="editForm.price" :min="0" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="editForm.unit" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNo">
          <el-input-number v-model="editForm.orderNo" :min="0" />
        </el-form-item>
        <el-form-item label="护理说明" prop="nursingRequirement">
          <el-input v-model="editForm.nursingRequirement" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="editForm.remark" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="editForm.status" placeholder="请选择状态">
            <el-option label="禁用" :value="0" />
            <el-option label="启用" :value="1" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 查看服务对话框 -->
    <el-dialog v-model="viewDialogVisible" title="服务详情" width="500px" :show-close="true">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="图片">
          <img :src="viewData.image" style="width:80px;height:80px;border-radius:8px;" v-if="viewData.image" />
        </el-descriptions-item>
        <el-descriptions-item label="名称">{{ viewData.name }}</el-descriptions-item>
        <el-descriptions-item label="价格">￥{{ viewData.price }}</el-descriptions-item>
        <el-descriptions-item label="单位">{{ viewData.unit }}</el-descriptions-item>
        <el-descriptions-item label="排序">{{ viewData.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="护理说明">{{ viewData.nursingRequirement }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewData.remark }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="viewData.status === 0" type="info">禁用</el-tag>
          <el-tag v-else-if="viewData.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="info">未知</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, onMounted, reactive } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const serviceList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(5)
const searchKey = ref('')
const deleteDialogVisible = ref(false)
const deleteId = ref(null)
const editDialogVisible = ref(false)
const editDialogTitle = ref('编辑服务')
const editForm = reactive({})
const editFormRef = ref()
const editRules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
}
const viewDialogVisible = ref(false)
const viewData = reactive({})
const fileList = ref([])

function fetchData() {
  axios.post('/admin/project/page', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    searchKey: searchKey.value,
    status: undefined
  }).then(res => {
    if (res.data.code === 200) {
      serviceList.value = res.data.data.records
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
function onClear() {
  pageNum.value = 1
  fetchData()
}
function handleUploadSuccess(res, file) {
  // 兼容 data 和 'data ' 字段
  const imgUrl = res.data || res['data '] || (res.data && res.data[0])
  if (res.code === 200 && imgUrl) {
    editForm.image = imgUrl
    fileList.value = [{ url: imgUrl }]
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error('图片上传失败')
  }
}

function onAdd() {
  editDialogTitle.value = '增加服务'
  Object.keys(editForm).forEach(key => {
    delete editForm[key]
  })
  Object.assign(editForm, {
    id: undefined,
    name: '',
    price: 0,
    unit: '',
    orderNo: 0,
    image: '',
    nursingRequirement: '',
    remark: '',
    status: 1
  })
  fileList.value = []
  editDialogVisible.value = true
}
function onEdit(row) {
  editDialogTitle.value = '编辑服务'
  axios.get(`/admin/project/${row.id}`).then(res => {
    if (res.data.code === 200) {
      Object.keys(editForm).forEach(key => { delete editForm[key] })
      Object.assign(editForm, res.data.data)
      fileList.value = res.data.data.image ? [{ url: res.data.data.image }] : []
      editDialogVisible.value = true
    }
  })
}
function onDelete(row) {
  deleteId.value = row.id
  deleteDialogVisible.value = true
}
function confirmDelete() {
  axios.delete(`/admin/project/${deleteId.value}`).then(res => {
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      deleteDialogVisible.value = false
      fetchData()
    }
  })
}
function submitEdit() {
  editFormRef.value.validate(valid => {
    if (!valid) return
    if (editDialogTitle.value === '增加服务') {
      axios.post('/admin/project', editForm).then(res => {
        if (res.data.code === 200) {
          ElMessage.success('新增成功')
          editDialogVisible.value = false
          fetchData()
        }
      })
    } else {
      axios.put('/admin/project', editForm).then(res => {
        if (res.data.code === 200) {
          ElMessage.success('修改成功')
          editDialogVisible.value = false
          fetchData()
        }
      })
    }
  })
}
function onView(row) {
  Object.assign(viewData, row)
  viewDialogVisible.value = true
}
onMounted(fetchData)
</script>
<style scoped>
.el-avatar {
  border: 1px solid #eee;
}
.avatar-uploader .avatar {
  width: 40px;
  height: 40px;
  display: block;
  border-radius: 8px;
}
</style> 