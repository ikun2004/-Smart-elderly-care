<template>
  <div>
    <h2 style="font-weight: bold; font-size: 32px; margin-bottom: 20px;">用户管理</h2>
    <el-card>
      <el-row style="margin-bottom: 20px;" align="middle">
        <el-col :span="8">
          <el-input v-model="searchKey" placeholder="搜索用户..." clearable @keyup.enter.native="fetchData" @clear="onClear" />
        </el-col>
      </el-row>
      <el-table :data="userList" style="width: 100%;" stripe border>
        <el-table-column label="序号" type="index" width="50" align="center" :index="indexMethod" />
        <el-table-column label="图片" width="100" align="center">
          <template #default="scope">
            <el-avatar :src="scope.row.avatar" size="large" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" min-width="50" align="center" />
        <el-table-column prop="gender" label="性别" min-width="20" align="center">
          <template #default="scope">
            <span>{{ genderText(scope.row.gender) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="10" align="center" />
        <el-table-column label="绑定老人"  min-width="20" align="center">
          <template #default="scope">
            <template v-if="scope.row.elderList && scope.row.elderList.length">
              <el-tag
                  v-for="elder in scope.row.elderList"
                  :key="elder.id"
                  style="margin: 2px; display: inline-flex; align-items: center;"
                  type="info"
              >
                {{ elder.name }}
                <el-icon
                    style="margin-left: 4px; cursor: pointer;"
                    @click.stop="onUnbindElder(scope.row.id, elder.id)"
                    title="解绑"
                >
                  <Close />
                </el-icon>
              </el-tag>
            </template>
            <template v-else>
              <span style="color: #bbb;">无</span>
            </template>
          </template>
        </el-table-column>
        <el-table-column label="操作"  min-width="50" align="center" fixed="right">
          <template #default="scope">
            <div style="display: flex; flex-direction: column; align-items: center; gap: 8px;">
              <el-button size="small" type="primary" @click="onEdit(scope.row)" :icon="Edit" style="width: 80px;">编辑</el-button>
              <el-button size="small" type="success" @click="onBindFamily(scope.row)" :icon="User" style="width: 80px;">绑定家人</el-button>
              <el-button size="small" type="danger" @click="onDelete(scope.row)" :icon="Delete" style="width: 80px;">删除</el-button>
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
      <span>确定要删除该用户吗？</span>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑用户对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑用户信息" width="600px">
      <el-form :model="editForm" label-width="100px" :rules="editRules" ref="editFormRef">
        <el-form-item label="头像" prop="avatar">
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
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="editForm.gender" placeholder="请选择性别">
            <el-option label="女" :value="0" />
            <el-option label="男" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="openId" prop="openId">
          <el-input v-model="editForm.openId" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="editForm.remark" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 解绑老人确认对话框 -->
    <el-dialog v-model="unbindDialogVisible" title="提示" width="300px">
      <span>确定要解绑该老人吗？</span>
      <template #footer>
        <el-button @click="unbindDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmUnbind">确定</el-button>
      </template>
    </el-dialog>

    <!-- 绑定老人对话框 -->
    <el-dialog v-model="bindDialogVisible" title="绑定老人" width="400px">
      <el-form :model="bindForm" label-width="80px">
        <el-form-item label="用户">
          <el-input v-model="currentUserName" disabled />
        </el-form-item>
        <el-form-item label="老人">
          <el-select
              v-model="bindForm.elderId"
              filterable
              remote
              reserve-keyword
              placeholder="请输入姓名搜索老人"
              :remote-method="handleElderSearch"
              :loading="false"
              style="width: 100%;"
          >
            <el-option
                v-for="elder in elderOptions"
                :key="elder.id"
                :label="elder.name"
                :value="elder.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="bindForm.remark" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bindDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmBind">绑定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, onMounted, reactive } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Plus, Edit, Delete, User, Close } from '@element-plus/icons-vue'

const userList = ref([])
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
}
const avatarFileList = ref([])

// 解绑相关
const unbindDialogVisible = ref(false)
const unbindMemberId = ref(null)
const unbindElderId = ref(null)

function onUnbindElder(memberId, elderId) {
  unbindMemberId.value = memberId
  unbindElderId.value = elderId
  unbindDialogVisible.value = true
}
function confirmUnbind() {
  axios.post('/admin/user/unbing', {
    memberId: unbindMemberId.value,
    elderId: unbindElderId.value
  }).then(res => {
    if (res.data.code === 200) {
      ElMessage.success('解绑成功')
      unbindDialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.data.msg || '解绑失败')
    }
  })
}

// 绑定老人弹窗相关
const bindDialogVisible = ref(false)
const bindForm = reactive({
  memberId: null,
  elderId: null,
  remark: ''
})
const elderOptions = ref([])
const elderSearchKey = ref('')
const elderTotal = ref(0)
const elderPageNum = ref(1)
const elderPageSize = ref(10)
const currentUserName = ref('')

function onBindFamily(row) {
  bindForm.memberId = row.id
  bindForm.elderId = null
  bindForm.remark = ''
  currentUserName.value = row.name
  elderSearchKey.value = ''
  elderPageNum.value = 1
  fetchElderOptions()
  bindDialogVisible.value = true
}
function fetchElderOptions() {
  axios.post('/admin/elder/page', {
    pageNum: elderPageNum.value,
    pageSize: elderPageSize.value,
    searchKey: elderSearchKey.value,
    status: 1
  }).then(res => {
    if (res.data.code === 200) {
      elderOptions.value = res.data.data.records
      elderTotal.value = res.data.data.total
    }
  })
}
function handleElderSearch(val) {
  elderSearchKey.value = val
  elderPageNum.value = 1
  fetchElderOptions()
}
function handleElderPageChange(val) {
  elderPageNum.value = val
  fetchElderOptions()
}
function confirmBind() {
  if (!bindForm.elderId) {
    ElMessage.warning('请选择老人')
    return
  }
  axios.post('/admin/user/add', bindForm).then(res => {
    if (res.data.code === 200) {
      ElMessage.success('绑定成功')
      bindDialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.data.msg || '绑定失败')
    }
  })
}

function handleAvatarSuccess(res, file) {
  const imgUrl = res.data || res['data '] || (res.data && res.data[0])
  if (res.code === 200 && imgUrl) {
    editForm.avatar = imgUrl
    avatarFileList.value = [{ url: imgUrl }]
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error('头像上传失败')
  }
}

function onAdd() {
  Object.assign(editForm, {
    id: undefined,
    avatar: '',
    name: '',
    gender: 0,
    openId: '',
    remark: ''
  })
  avatarFileList.value = []
  editDialogVisible.value = true
}
function onEdit(row) {
  axios.get(`/admin/user/${row.id}`).then(res => {
    if (res.data.code === 200) {
      Object.assign(editForm, res.data.data)
      avatarFileList.value = res.data.data.avatar ? [{ url: res.data.data.avatar }] : []
      editDialogVisible.value = true
    }
  })
}
function onDelete(row) {
  deleteId.value = row.id
  deleteDialogVisible.value = true
}
function confirmDelete() {
  axios.delete(`/admin/user/${deleteId.value}`).then(res => {
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
    axios.put('/admin/user', editForm).then(res => {
      if (res.data.code === 200) {
        ElMessage.success('修改成功')
        editDialogVisible.value = false
        fetchData()
      }
    })
  })
}
function fetchData() {
  axios.post('/admin/user/page', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    searchKey: searchKey.value
  }).then(res => {
    if (res.data.code === 200) {
      userList.value = res.data.data.records
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
function genderText(gender) {
  if (gender === 1 || gender === '1') return '男'
  if (gender === 0 || gender === '0') return '女'
  return ''
}
function onClear() {
  pageNum.value = 1
  fetchData()
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