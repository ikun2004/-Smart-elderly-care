<template>
  <div>
    <h2 style="font-weight: bold; font-size: 32px; margin-bottom: 20px;">房型管理</h2>
    <el-card>
      <el-row style="margin-bottom: 20px;" align="middle">
        <el-col :span="8">
          <el-input v-model="searchKey" placeholder="搜索房型..." clearable @keyup.enter.native="fetchData" @clear="onClear" />
        </el-col>
        <el-col :span="4" :offset="12" style="text-align: right;">
          <el-button type="primary" @click="onAdd">+ 增加房型</el-button>
        </el-col>
      </el-row>
      <el-table :data="roomList" style="width: 100%;" stripe border>
        <el-table-column label="序号" type="index" width="70" align="center" :index="indexMethod" />
        <el-table-column label="图片" width="100" align="center">
          <template #default="scope">
            <el-avatar :src="scope.row.photo" size="large" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" min-width="110" align="center" />
        <el-table-column prop="bedCount" label="床位数量" min-width="90" align="center" />
        <el-table-column prop="price" label="价格" min-width="100" align="center">
          <template #default="scope">
            <span>￥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120" align="center">
          <template #default="scope">
            <span>{{ scope.row.remark || scope.row.introduction }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" min-width="90" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="info">禁用</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="140" align="center" fixed="right">
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
      <span>确定要删除该房型吗？</span>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑/新增房型对话框 -->
    <el-dialog v-model="editDialogVisible" :title="editDialogTitle" width="600px">
      <el-form :model="editForm" label-width="100px" :rules="editRules" ref="editFormRef">
        <el-form-item label="图片" prop="photo">
          <el-upload
            class="avatar-uploader"
            action="/admin/upload"
            :show-file-list="true"
            :file-list="photoFileList"
            :on-success="handlePhotoSuccess"
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
        <el-form-item label="床位数量" prop="bedCount">
          <el-input-number v-model="editForm.bedCount" :min="0" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="editForm.price" :min="0" />
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
  </div>
</template>
<script setup>
import { ref, onMounted, reactive } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const roomList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(5)
const searchKey = ref('')
const deleteDialogVisible = ref(false)
const deleteId = ref(null)
const editDialogVisible = ref(false)
const editDialogTitle = ref('编辑房型')
const editForm = reactive({})
const editFormRef = ref()
const editRules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
}
const photoFileList = ref([])

function handlePhotoSuccess(res, file) {
  const imgUrl = res.data || res['data '] || (res.data && res.data[0])
  if (res.code === 200 && imgUrl) {
    editForm.photo = imgUrl
    photoFileList.value = [{ url: imgUrl }]
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error('图片上传失败')
  }
}

function fetchData() {
  axios.post('/admin/roomTypes/page', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    searchKey: searchKey.value,
    status: undefined // 可根据需要传递
  }).then(res => {
    if (res.data.code === 200) {
      roomList.value = res.data.data.records
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
function onAdd() {
  editDialogTitle.value = '增加房型'
  Object.assign(editForm, {
    id: undefined,
    name: '',
    bedCount: 0,
    price: 0,
    introduction: '',
    photo: '',
    status: 1,
    remark: ''
  })
  photoFileList.value = []
  editDialogVisible.value = true
}
function onEdit(row) {
  editDialogTitle.value = '编辑房型'
  axios.get(`/admin/roomTypes/${row.id}`).then(res => {
    if (res.data.code === 200) {
      Object.assign(editForm, res.data.data)
      photoFileList.value = res.data.data.photo ? [{ url: res.data.data.photo }] : []
      editDialogVisible.value = true
    }
  })
}
function onDelete(row) {
  deleteId.value = row.id
  deleteDialogVisible.value = true
}
function confirmDelete() {
  axios.delete(`/admin/roomTypes/${deleteId.value}`).then(res => {
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
    if (editDialogTitle.value === '增加房型') {
      axios.post('/admin/roomTypes', editForm).then(res => {
        if (res.data.code === 200) {
          ElMessage.success('新增成功')
          editDialogVisible.value = false
          fetchData()
        }
      })
    } else {
      axios.put('/admin/roomTypes', editForm).then(res => {
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
.avatar-uploader .avatar {
  width: 80px;
  height: 80px;
  display: block;
  border-radius: 8px;
}
</style> 