<template>
  <div>
    <h2 style="font-weight: bold; font-size: 32px; margin-bottom: 20px;">老人管理</h2>
    <el-card>
      <el-row style="margin-bottom: 20px;" align="middle">
        <el-col :span="8">
          <el-input v-model="searchKey" placeholder="搜索老人..." clearable @keyup.enter.native="fetchData" @clear="onClear" />
        </el-col>
        <el-col :span="4" :offset="12" style="text-align: right;">
          <el-button type="primary" @click="onAdd">+ 增加老人</el-button>
        </el-col>
      </el-row>
      <el-table :data="elderList" style="width: 100%;" stripe border>
        <el-table-column label="序号" type="index" width="70" align="center" :index="indexMethod" />
        <el-table-column label="照片" width="100" align="center">
          <template #default="scope">
            <el-avatar :src="scope.row.image" size="large" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名字" min-width="70" align="center" />
        <el-table-column prop="sex" label="性别" min-width="50" align="center">
          <template #default="scope">
            <span>{{ sexText(scope.row.sex) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" min-width="120" align="center" />
        <el-table-column prop="birthday" label="生日" min-width="120" align="center" />
        <el-table-column prop="status" label="状态" min-width="70三" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="info">禁用</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">启用</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="warning">请假</el-tag>
            <el-tag v-else-if="scope.row.status === 3" type="info">退住中</el-tag>
            <el-tag v-else-if="scope.row.status === 4" type="primary">入住中</el-tag>
            <el-tag v-else-if="scope.row.status === 5" type="default">已退住</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="100" align="center" fixed="right">
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
                :icon="Monitor"
                size="small"
                type="success"
                @click="onHealthData(scope.row)"
                title="健康"
              >健康</el-button>
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
    <el-dialog v-model="deleteDialogVisible" title="提示" width="300px">
      <span>确定要删除该老人吗？</span>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑老人对话框 -->
    <el-dialog v-model="editDialogVisible" :title="editDialogTitle" width="600px">
      <el-form :model="editForm" label-width="100px" :rules="editRules" ref="editFormRef">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="头像" prop="image">
          <el-upload
            class="avatar-uploader"
            action="/admin/upload"
            :show-file-list="true"
            :file-list="avatarFileList"
            :on-success="handleAvatarSuccess"
            :limit="1"
            :before-upload="() => true"
            list-type="picture-card"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCardNo">
          <el-input v-model="editForm.idCardNo" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="editForm.sex" placeholder="请选择性别">
            <el-option label="女" :value="0" />
            <el-option label="男" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="editForm.status" placeholder="请选择状态">
            <el-option label="禁用" :value="0" />
            <el-option label="启用" :value="1" />
            <el-option label="请假" :value="2" />
            <el-option label="退住中" :value="3" />
            <el-option label="入住中" :value="4" />
            <el-option label="已退住" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="生日" prop="birthday">
          <el-date-picker v-model="editForm.birthday" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="editForm.address" />
        </el-form-item>
        <el-form-item label="身份证正面">
          <el-upload
            class="avatar-uploader"
            action="/admin/upload"
            :show-file-list="true"
            :file-list="idCardPortraitFileList"
            :on-success="handleIdCardPortraitSuccess"
            :limit="1"
            :before-upload="() => true"
            list-type="picture-card"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="身份证反面">
          <el-upload
            class="avatar-uploader"
            action="/admin/upload"
            :show-file-list="true"
            :file-list="idCardNationalEmblemFileList"
            :on-success="handleIdCardNationalEmblemSuccess"
            :limit="1"
            :before-upload="() => true"
            list-type="picture-card"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
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
import { Edit, Delete, Plus, Monitor } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const elderList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(5)
const searchKey = ref('')

const deleteDialogVisible = ref(false)
const deleteId = ref(null)

const editDialogVisible = ref(false)
const editForm = reactive({})
const editFormRef = ref()
const editRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  // 其他校验规则可补充
}
const editDialogTitle = ref('编辑老人信息')

// 上传图片 fileList
const avatarFileList = ref([])
const idCardPortraitFileList = ref([])
const idCardNationalEmblemFileList = ref([])

function handleAvatarSuccess(res, file) {
  const imgUrl = res.data || res['data '] || (res.data && res.data[0])
  if (res.code === 200 && imgUrl) {
    editForm.image = imgUrl
    avatarFileList.value = [{ url: imgUrl }]
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error('头像上传失败')
  }
}
function handleIdCardPortraitSuccess(res, file) {
  const imgUrl = res.data || res['data '] || (res.data && res.data[0])
  if (res.code === 200 && imgUrl) {
    editForm.idCardPortraitImg = imgUrl
    idCardPortraitFileList.value = [{ url: imgUrl }]
    ElMessage.success('身份证正面上传成功')
  } else {
    ElMessage.error('身份证正面上传失败')
  }
}
function handleIdCardNationalEmblemSuccess(res, file) {
  const imgUrl = res.data || res['data '] || (res.data && res.data[0])
  if (res.code === 200 && imgUrl) {
    editForm.idCardNationalEmblemImg = imgUrl
    idCardNationalEmblemFileList.value = [{ url: imgUrl }]
    ElMessage.success('身份证反面上传成功')
  } else {
    ElMessage.error('身份证反面上传失败')
  }
}

function fetchData() {
  axios.post('/admin/elder/page', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    searchKey: searchKey.value,
    status: 1
  }).then(res => {
    if (res.data.code === 200) {
      elderList.value = res.data.data.records
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
function sexText(sex) {
  if (sex === 1 || sex === '1') return '男'
  if (sex === 0 || sex === '0') return '女'
  return ''
}
function onAdd() {
  editDialogTitle.value = '增加老人'
  Object.assign(editForm, {
    id: undefined,
    name: '',
    image: '',
    idCardNo: '',
    sex: 0,
    status: 1,
    phone: '',
    birthday: '',
    address: '',
    idCardPortraitImg: '',
    idCardNationalEmblemImg: ''
  })
  avatarFileList.value = []
  idCardPortraitFileList.value = []
  idCardNationalEmblemFileList.value = []
  editDialogVisible.value = true
}
function onEdit(row) {
  editDialogTitle.value = '编辑老人信息'
  axios.get(`/admin/elder/${row.id}`).then(res => {
    if (res.data.code === 200) {
      Object.assign(editForm, res.data.data)
      avatarFileList.value = res.data.data.image ? [{ url: res.data.data.image }] : []
      idCardPortraitFileList.value = res.data.data.idCardPortraitImg ? [{ url: res.data.data.idCardPortraitImg }] : []
      idCardNationalEmblemFileList.value = res.data.data.idCardNationalEmblemImg ? [{ url: res.data.data.idCardNationalEmblemImg }] : []
      editDialogVisible.value = true
    }
  })
}
function onDelete(row) {
  deleteId.value = row.id
  deleteDialogVisible.value = true
}
function confirmDelete() {
  axios.delete(`/admin/elder/${deleteId.value}`).then(res => {
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
    if (editDialogTitle.value === '增加老人') {
      axios.post('/admin/elder', editForm).then(res => {
        if (res.data.code === 200) {
          ElMessage.success('新增成功')
          editDialogVisible.value = false
          fetchData()
        }
      })
    } else {
      axios.put('/admin/elder', editForm).then(res => {
        if (res.data.code === 200) {
          ElMessage.success('修改成功')
          editDialogVisible.value = false
          fetchData()
        }
      })
    }
  })
}
function onHealthData(row) {
  ElMessage.info(`查看 ${row.name} 的健康数据`)
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