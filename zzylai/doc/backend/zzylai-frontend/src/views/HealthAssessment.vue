<template>
  <div>
    <h2 style="font-weight: bold; font-size: 32px; margin-bottom: 20px;">体检报告管理</h2>
    <el-card>
      <el-row style="margin-bottom: 20px;" align="middle">
        <el-col :span="8">
          <el-input v-model="searchKey" placeholder="搜索体检报告..." clearable @keyup.enter.native="fetchData" @clear="onClear" />
        </el-col>
        <el-col :span="4" :offset="12" style="text-align: right;">
          <el-button type="primary" @click="onAdd">+ 添加健康记录</el-button>
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table :data="healthList" style="width: 100%;" stripe border>
        <el-table-column label="序号" type="index" width="70" align="center" :index="indexMethod" />
        <el-table-column prop="elderName" label="老人" min-width="100" align="center" />
        <el-table-column prop="idCard" label="身份证" min-width="180" align="center" />
        <el-table-column prop="age" label="年龄" min-width="80" align="center" />
        <el-table-column prop="healthScore" label="健康评分" min-width="80" align="center">
          <template #default="scope">
            <span>{{ scope.row.healthScore }} 分</span>
          </template>
        </el-table-column>
        <el-table-column prop="physicalExamInstitution" label="体检机构" min-width="120" align="center" />
        <el-table-column prop="admissionStatus" label="入住状态" min-width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.admissionStatus === 0" type="success">已入住</el-tag>
            <el-tag v-else-if="scope.row.admissionStatus === 1" type="info">未入住</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="suggestionForAdmission" label="建议入住" min-width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.suggestionForAdmission === 0" type="success">建议入住</el-tag>
            <el-tag v-else-if="scope.row.suggestionForAdmission === 1" type="danger">不建议入住</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" align="center">
          <template #default="scope">
            <span>{{ formatTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="140" align="center" fixed="right">
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
              <el-button 
                size="small" 
                type="info" 
                @click="onView(scope.row)"
                style="width: 80px;"
                title="体检报告"
              >AI体检报告</el-button>
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
      <span>确定要删除该健康记录吗？</span>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑体检报告对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑体检报告" width="600px">
      <el-form :model="editForm" label-width="120px" :rules="editRules" ref="editFormRef">
        <el-form-item label="老人姓名" prop="elderName">
          <el-input v-model="editForm.elderName" />
        </el-form-item>
        <el-form-item label="身份证号码" prop="idCard">
          <el-input 
            v-model="editForm.idCard" 
            placeholder="请输入18位或15位身份证号码"
            maxlength="18"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="健康评分" prop="healthScore">
          <el-input v-model="editForm.healthScore" />
        </el-form-item>
        <el-form-item label="体检机构" prop="physicalExamInstitution">
          <el-input v-model="editForm.physicalExamInstitution" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 新增体检报告对话框 -->
    <el-dialog v-model="addDialogVisible" title="添加健康记录" width="600px">
      <el-form :model="addForm" label-width="120px" :rules="addRules" ref="addFormRef">
        <el-form-item label="老人姓名" prop="elderName">
          <el-input v-model="addForm.elderName" />
        </el-form-item>
        <el-form-item label="身份证号码" prop="idCard">
          <el-input 
            v-model="addForm.idCard" 
            placeholder="请输入18位或15位身份证号码"
            maxlength="18"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="体检单位" prop="physicalExamInstitution">
          <el-input v-model="addForm.physicalExamInstitution" />
        </el-form-item>
        <el-form-item label="体检报告" prop="physicalReportUrl">
          <el-upload
            class="upload-demo"
            action="/admin/healthAssessment/upload"
            :data="{ idCard: addForm.idCard }"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :file-list="fileList"
            accept=".pdf"
            :limit="1"
            :disabled="!canUploadReport"
          >
            <el-button type="primary" :disabled="!canUploadReport">
              {{ canUploadReport ? '点击上传PDF报告' : '请先输入身份证号码' }}
            </el-button>
            <template #tip>
              <div class="el-upload__tip">
                <span v-if="!canUploadReport" style="color: #f56c6c;">请先输入身份证号码</span>
                <span v-else>只能上传PDF文件，且不超过60MB</span>
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false" :disabled="aiProcessing">取消</el-button>
        <el-button type="primary" @click="submitAdd" :loading="aiProcessing" :disabled="aiProcessing">
          {{ aiProcessing ? 'AI诊断中...' : '确定' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- AI诊断进度条 -->
    <el-dialog v-model="aiProgressVisible" title="AI诊断进度" width="400px" :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <div style="text-align: center; padding: 20px;">
        <el-icon style="font-size: 48px; color: #409EFF; margin-bottom: 20px;">
          <Loading />
        </el-icon>
        <div style="font-size: 18px; margin-bottom: 20px; color: #409EFF;">正在AI诊断中...</div>
        <div style="font-size: 14px; color: #666; margin-bottom: 20px;">请耐心等待，AI正在分析体检报告</div>
        <el-progress 
          :percentage="aiProgressPercentage" 
          :stroke-width="8"
          :show-text="false"
          :indeterminate="true"
        />
        <div style="margin-top: 10px; font-size: 12px; color: #999;">
          预计需要1-3分钟
        </div>
      </div>
    </el-dialog>

    <!-- 体检报告详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="体检报告详情" width="1000px" @opened="onRadarDialogOpened">
      <div v-if="viewData">
        <!-- 基本信息 -->
        <div style="border:1px solid #ccc;padding:16px;margin-bottom:16px;">
          <div style="font-weight:bold;margin-bottom:8px;">基本信息</div>
          <el-row :gutter="20">
            <el-col :span="6">老人姓名：{{ viewData.elderName }}</el-col>
            <el-col :span="6">老人身份证号：{{ viewData.idCard }}</el-col>
            <el-col :span="6">年龄：{{ viewData.age }}</el-col>
            <el-col :span="6">性别：{{ viewData.gender === 1 ? '男' : '女' }}</el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top:8px;">
            <el-col :span="6">出生日期：{{ viewData.birthDate }}</el-col>
            <el-col :span="6">体检机构：{{ viewData.physicalExamInstitution }}</el-col>
            <el-col :span="6">体检报告：<a :href="viewData.physicalReportUrl" target="_blank">查看</a></el-col>
            <el-col :span="6">体检时间：{{ formatTime(viewData.assessmentTime) }}</el-col>
          </el-row>
        </div>
        <!-- 体检总结 -->
        <div style="border:1px solid #ccc;padding:16px;margin-bottom:16px;">
          <div style="font-weight:bold;margin-bottom:8px;">体检总结</div>
          <el-row :gutter="20">
            <el-col :span="6">健康评分：{{ viewData.healthScore }}</el-col>
            <el-col :span="6">风险等级：{{ viewData.riskLevel }}</el-col>
            <el-col :span="6">护理等级：{{ viewData.nursingLevelName }}</el-col>
            <el-col :span="6">入住状态：<el-tag v-if="viewData.admissionStatus === 0" type="success">已入住</el-tag><el-tag v-else-if="viewData.admissionStatus === 1" type="info">未入住</el-tag><el-tag v-else type="info">未知</el-tag></el-col>
            <el-col :span="6">建议入住：<el-tag v-if="viewData.suggestionForAdmission === 0" type="success">建议入住</el-tag><el-tag v-else-if="viewData.suggestionForAdmission === 1" type="danger">不建议入住</el-tag><el-tag v-else type="info">未知</el-tag></el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top:8px;">
            <el-col :span="12">评估时间：{{ formatTime(viewData.assessmentTime) }}</el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top:8px;">
            <el-col :span="24">报告摘要：<span style="color:#d44">{{ viewData.reportSummary }}</span></el-col>
          </el-row>
        </div>
        <!-- 疾病风险（占位） -->
        <div style="border:1px solid #ccc;padding:16px;margin-bottom:16px;">
          <div style="font-weight:bold;margin-bottom:8px;">疾病风险和健康分数</div>
          <div style="display:flex;gap:16px;">
            <div style="flex:1;height:220px;background:#f5f5f5;display:flex;align-items:center;justify-content:center;">
              <div ref="diseaseBarRef" style="width:100%;height:220px;"></div>
            </div>
            <div style="flex:1;height:220px;background:#f5f5f5;display:flex;align-items:center;justify-content:center;">
              <div ref="radarChartRef" style="width:100%;height:220px;"></div>
            </div>
          </div>
        </div>
        <!-- 异常分析（表格） -->
        <div style="border:1px solid #ccc;padding:16px;margin-bottom:16px;">
          <div style="font-weight:bold;margin-bottom:8px;">异常分析</div>
          <el-table :data="abnormalTableData" border size="small">
            <el-table-column prop="conclusion" label="结论" width="120" />
            <el-table-column prop="examinationItem" label="项目名称" width="120" />
            <el-table-column prop="result" label="检查结果" width="120" />
            <el-table-column prop="referenceValue" label="参考值" width="120" />
            <el-table-column prop="unit" label="单位" width="80" />
            <el-table-column prop="interpret" label="解读" />
            <el-table-column prop="advice" label="建议" />
          </el-table>
        </div>
        <div style="text-align:center;margin:24px 0;">
          <el-button type="primary" @click="viewDialogVisible=false">返回</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import axios from 'axios'
import { Edit, Delete, Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { nextTick } from 'vue'

const healthList = ref([])
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
  elderName: [{ required: true, message: '请输入老人姓名', trigger: 'blur' }],
  idCard: [{ validator: validateIdCard, trigger: 'blur' }],
  healthScore: [{ required: true, message: '请输入健康评分', trigger: 'blur' }],
  physicalExamInstitution: [{ required: true, message: '请输入体检机构', trigger: 'blur' }]
}

const addDialogVisible = ref(false)
const addForm = reactive({
  elderName: '',
  idCard: '',
  physicalExamInstitution: '',
  physicalReportUrl: ''
})
const addFormRef = ref()
const addRules = {
  elderName: [{ required: true, message: '请输入老人姓名', trigger: 'blur' }],
  idCard: [{ validator: validateIdCard, trigger: 'blur' }],
  physicalExamInstitution: [{ required: true, message: '请输入体检单位', trigger: 'blur' }],
  physicalReportUrl: [{ required: true, message: '请上传体检报告', trigger: 'change' }]
}
const fileList = ref([])

const aiProcessing = ref(false)
const aiProgressVisible = ref(false)
const aiProgressPercentage = ref(0)

const viewDialogVisible = ref(false)
const viewData = ref(null)
const abnormalTableData = ref([])

// 详情对话框相关响应式数据
const radarChartRef = ref(null)
let radarChartInstance = null

const diseaseRadarRef = ref(null)
let diseaseRadarInstance = null

const diseaseBarRef = ref(null)
let diseaseBarInstance = null

// 计算属性：是否可以上传报告
const canUploadReport = computed(() => {
  return addForm.idCard && addForm.idCard.trim().length > 0
})

// 身份证号码校验函数
function validateIdCard(rule, value, callback) {
  if (!value) {
    callback(new Error('请输入身份证号码'))
    return
  }
  
  // 身份证号码正则表达式
  const idCardReg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  
  if (!idCardReg.test(value)) {
    callback(new Error('身份证号码格式不正确'))
    return
  }
  
  // 15位身份证校验
  if (value.length === 15) {
    const year = parseInt(value.substring(6, 8))
    const month = parseInt(value.substring(8, 10))
    const day = parseInt(value.substring(10, 12))
    
    if (year < 0 || year > 99) {
      callback(new Error('身份证号码年份不正确'))
      return
    }
    if (month < 1 || month > 12) {
      callback(new Error('身份证号码月份不正确'))
      return
    }
    if (day < 1 || day > 31) {
      callback(new Error('身份证号码日期不正确'))
      return
    }
  }
  
  // 18位身份证校验
  if (value.length === 18) {
    const year = parseInt(value.substring(6, 10))
    const month = parseInt(value.substring(10, 12))
    const day = parseInt(value.substring(12, 14))
    
    if (year < 1900 || year > new Date().getFullYear()) {
      callback(new Error('身份证号码年份不正确'))
      return
    }
    if (month < 1 || month > 12) {
      callback(new Error('身份证号码月份不正确'))
      return
    }
    if (day < 1 || day > 31) {
      callback(new Error('身份证号码日期不正确'))
      return
    }
  }
  
  callback()
}

function fetchData() {
  axios.post('/admin/healthAssessment/list', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    searchKey: searchKey.value
  }).then(res => {
    if (res.data.code === 200) {
      healthList.value = res.data.data.records
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
  addForm.elderName = ''
  addForm.idCard = ''
  addForm.physicalExamInstitution = ''
  addForm.physicalReportUrl = ''
  fileList.value = []
  addDialogVisible.value = true
}

function onEdit(row) {
  axios.get(`/admin/healthAssessment/${row.id}`).then(res => {
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
  axios.delete(`/admin/healthAssessment/${deleteId.value}`).then(res => {
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
    axios.put('/admin/healthAssessment', editForm).then(res => {
      if (res.data.code === 200) {
        ElMessage.success('修改成功')
        editDialogVisible.value = false
        fetchData()
      }
    })
  })
}

function beforeUpload(file) {
  // 检查身份证号码是否已输入
  if (!addForm.idCard || addForm.idCard.trim().length === 0) {
    ElMessage.error('请先输入身份证号码')
    return false
  }
  
  const isPDF = file.type === 'application/pdf'
  const isLt60M = file.size / 1024 / 1024 < 60

  if (!isPDF) {
    ElMessage.error('只能上传PDF文件!')
    return false
  }
  if (!isLt60M) {
    ElMessage.error('文件大小不能超过60MB!')
    return false
  }
  return true
}

function handleUploadSuccess(res, file) {
  if (res.code === 200) {
    addForm.physicalReportUrl = res.data
    ElMessage.success('体检报告上传成功')
  } else {
    ElMessage.error('体检报告上传失败')
  }
}

function handleUploadError() {
  ElMessage.error('体检报告上传失败')
}

function submitAdd() {
  addFormRef.value.validate(valid => {
    if (!valid) return
    
    // 开始AI诊断流程
    aiProcessing.value = true
    aiProgressVisible.value = true
    aiProgressPercentage.value = 0
    
    // 模拟进度条增长
    const progressInterval = setInterval(() => {
      if (aiProgressPercentage.value < 90) {
        aiProgressPercentage.value += Math.random() * 10
      }
    }, 1000)
    
    // 调用保存接口
    axios.post('/admin/healthAssessment', addForm).then(res => {
      if (res.data.code === 200) {
        // 完成进度条
        aiProgressPercentage.value = 100
        setTimeout(() => {
          ElMessage.success('添加成功，AI诊断完成')
          addDialogVisible.value = false
          fetchData()
          // 清理状态
          aiProcessing.value = false
          aiProgressVisible.value = false
          aiProgressPercentage.value = 0
          clearInterval(progressInterval)
        }, 500)
      } else {
        ElMessage.error(res.data.msg || '添加失败')
        aiProcessing.value = false
        aiProgressVisible.value = false
        aiProgressPercentage.value = 0
        clearInterval(progressInterval)
      }
    }).catch(error => {
      ElMessage.error('添加失败')
      console.error(error)
      aiProcessing.value = false
      aiProgressVisible.value = false
      aiProgressPercentage.value = 0
      clearInterval(progressInterval)
    })
  })
}

// 解析systemScore并渲染雷达图
function renderRadarChart(systemScoreStr) {
  if (!systemScoreStr) return
  let systemScore = {}
  try {
    systemScore = typeof systemScoreStr === 'string' ? JSON.parse(systemScoreStr) : systemScoreStr
  } catch (e) {
    return
  }
  const indicators = [
    { name: '呼吸系统', max: 100 },
    { name: '消化系统', max: 100 },
    { name: '内分泌系统', max: 100 },
    { name: '免疫系统', max: 100 },
    { name: '循环系统', max: 100 },
    { name: '泌尿系统', max: 100 },
    { name: '运动系统', max: 100 },
    { name: '感觉系统', max: 100 }
  ]
  const values = [
    systemScore.breathingSystem || 0,
    systemScore.digestiveSystem || 0,
    systemScore.endocrineSystem || 0,
    systemScore.immuneSystem || 0,
    systemScore.circulatorySystem || 0,
    systemScore.urinarySystem || 0,
    systemScore.motionSystem || 0,
    systemScore.senseSystem || 0
  ]
  nextTick(() => {
    if (!radarChartInstance) {
      radarChartInstance = echarts.init(radarChartRef.value)
    }
    const option = {
      tooltip: {},
      radar: {
        indicator: indicators,
        radius: 80
      },
      series: [{
        type: 'radar',
        data: [
          {
            value: values,
            name: '系统分值',
            areaStyle: { color: 'rgba(64,158,255,0.2)' },
            lineStyle: { color: '#409EFF' },
            symbol: 'circle',
            itemStyle: { color: '#409EFF' }
          }
        ]
      }]
    }
    radarChartInstance.setOption(option)
  })
}

function renderDiseaseRadarChart(diseaseRiskStr) {
  if (!diseaseRiskStr) return
  let diseaseRisk = {}
  try {
    diseaseRisk = typeof diseaseRiskStr === 'string' ? JSON.parse(diseaseRiskStr) : diseaseRiskStr
  } catch (e) {
    return
  }
  const indicators = [
    { name: '健康', max: 100 },
    { name: '警惕', max: 100 },
    { name: '风险', max: 100 },
    { name: '危险', max: 100 },
    { name: '极高危', max: 100 }
  ]
  const values = [
    diseaseRisk.healthy || 0,
    diseaseRisk.caution || 0,
    diseaseRisk.risk || 0,
    diseaseRisk.danger || 0,
    diseaseRisk.severeDanger || 0
  ]
  nextTick(() => {
    if (!diseaseRadarInstance) {
      diseaseRadarInstance = echarts.init(diseaseRadarRef.value)
    }
    const option = {
      tooltip: {},
      radar: {
        indicator: indicators,
        radius: 80
      },
      series: [{
        type: 'radar',
        data: [
          {
            value: values,
            name: '疾病风险',
            areaStyle: { color: 'rgba(245,108,108,0.2)' },
            lineStyle: { color: '#f56c6c' },
            symbol: 'circle',
            itemStyle: { color: '#f56c6c' }
          }
        ]
      }]
    }
    diseaseRadarInstance.setOption(option)
  })
}

function renderDiseaseBarChart(diseaseRiskStr, age) {
  // 年龄段分组
  const ageGroups = ['20岁', '30岁', '40岁', '50岁', '60岁', '70岁', '80岁', '90岁+']
  // 模拟数据（百分比）
  const staticData = [
    { healthy: 90, caution: 7, risk: 2, danger: 1, severeDanger: 0 }, // 20岁
    { healthy: 88, caution: 8, risk: 2, danger: 2, severeDanger: 0 }, // 30岁
    { healthy: 85, caution: 10, risk: 3, danger: 2, severeDanger: 0 }, // 40岁
    { healthy: 80, caution: 12, risk: 5, danger: 3, severeDanger: 0 }, // 50岁
    { healthy: 70, caution: 15, risk: 8, danger: 7, severeDanger: 0 }, // 60岁
    { healthy: 55, caution: 18, risk: 12, danger: 12, severeDanger: 3 }, // 70岁
    { healthy: 40, caution: 20, risk: 15, danger: 20, severeDanger: 5 }, // 80岁
    { healthy: 25, caution: 25, risk: 20, danger: 20, severeDanger: 10 } // 90+
  ]
  // 当前用户年龄段索引
  let userAgeIdx = 0
  if (age) {
    const n = Number(age)
    if (n >= 90) userAgeIdx = 7
    else if (n >= 80) userAgeIdx = 6
    else if (n >= 70) userAgeIdx = 5
    else if (n >= 60) userAgeIdx = 4
    else if (n >= 50) userAgeIdx = 3
    else if (n >= 40) userAgeIdx = 2
    else if (n >= 30) userAgeIdx = 1
    else userAgeIdx = 0
  }
  // 用diseaseRisk替换当前用户年龄段的数据
  let diseaseRisk = {}
  try {
    diseaseRisk = typeof diseaseRiskStr === 'string' ? JSON.parse(diseaseRiskStr) : diseaseRiskStr
  } catch (e) {}
  if (diseaseRisk && Object.keys(diseaseRisk).length > 0) {
    staticData[userAgeIdx] = {
      healthy: diseaseRisk.healthy || 0,
      caution: diseaseRisk.caution || 0,
      risk: diseaseRisk.risk || 0,
      danger: diseaseRisk.danger || 0,
      severeDanger: diseaseRisk.severeDanger || 0
    }
  }
  // 拆分系列
  const healthy = staticData.map(d => d.healthy)
  const caution = staticData.map(d => d.caution)
  const risk = staticData.map(d => d.risk)
  const danger = staticData.map(d => d.danger)
  const severeDanger = staticData.map(d => d.severeDanger)
  nextTick(() => {
    if (!diseaseBarInstance) {
      diseaseBarInstance = echarts.init(diseaseBarRef.value)
    }
    const option = {
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      legend: { data: ['健康', '提示', '风险', '危险', '严重危险'] },
      grid: { left: 40, right: 40, bottom: 40, top: 40 },
      xAxis: { type: 'category', data: ageGroups },
      yAxis: { type: 'value', max: 100, axisLabel: { formatter: '{value}%' } },
      series: [
        { name: '健康', type: 'bar', stack: 'total', barWidth: 30, itemStyle: { color: '#67c23a' }, data: healthy },
        { name: '提示', type: 'bar', stack: 'total', itemStyle: { color: '#f6c02e' }, data: caution },
        { name: '风险', type: 'bar', stack: 'total', itemStyle: { color: '#faad14' }, data: risk },
        { name: '危险', type: 'bar', stack: 'total', itemStyle: { color: '#a94848' }, data: danger },
        { name: '严重危险', type: 'bar', stack: 'total', itemStyle: { color: '#bb09f1' }, data: severeDanger }
      ],
      graphic: [{
        type: 'group',
        left: userAgeIdx * (100 / 8) + 7 + '%',
        top: '20%',
        children: [
          {
            type: 'rect',
            shape: { width: 60, height: 30 },
            style: { fill: '#409eff', opacity: 0.9, rx: 6, ry: 6 },
            z: 100
          },
          {
            type: 'text',
            left: 10,
            top: 7,
            style: { text: '所在\n位置', fill: '#fff', font: 'bold 14px sans-serif' },
            z: 101
          },
          {
            type: 'circle',
            left: 25,
            top: -10,
            shape: { r: 7 },
            style: { fill: '#fff', stroke: '#409eff', lineWidth: 3 },
            z: 102
          }
        ]
      }]
    }
    diseaseBarInstance.setOption(option)
  })
}

function onView(row) {
  axios.get(`/admin/healthAssessment/${row.id}`).then(res => {
    if (res.data.code === 200) {
      viewData.value = res.data.data
      // 模拟获取异常分析数据
      abnormalTableData.value = [
        { conclusion: '正常', examinationItem: '血压', result: '120/80 mmHg', referenceValue: '120/80 mmHg', unit: 'mmHg', interpret: '血压正常', advice: '继续保持' },
        { conclusion: '异常', examinationItem: '血糖', result: '10.5 mmol/L', referenceValue: '3.9-6.1 mmol/L', unit: 'mmol/L', interpret: '血糖偏高', advice: '建议复查' },
        { conclusion: '正常', examinationItem: '心率', result: '70次/分', referenceValue: '60-100次/分', unit: '次/分', interpret: '心率正常', advice: '继续保持' },
        { conclusion: '异常', examinationItem: '血氧', result: '95%', referenceValue: '95-99%', unit: '%', interpret: '血氧偏低', advice: '建议吸氧' },
        { conclusion: '正常', examinationItem: '体温', result: '36.8℃', referenceValue: '36.0-37.0℃', unit: '℃', interpret: '体温正常', advice: '继续观察' }
      ]
      // 渲染雷达图
      renderRadarChart(res.data.data.systemScore)
      // 渲染疾病风险雷达图
      renderDiseaseRadarChart(res.data.data.diseaseRisk)
      // 渲染疾病风险堆叠柱状图
      renderDiseaseBarChart(res.data.data.diseaseRisk, res.data.data.age)
      viewDialogVisible.value = true
    }
  })
}

function onRadarDialogOpened() {
  if (radarChartInstance) {
    radarChartInstance.dispose()
    radarChartInstance = null
  }
  if (diseaseRadarInstance) {
    diseaseRadarInstance.dispose()
    diseaseRadarInstance = null
  }
  if (diseaseBarInstance) {
    diseaseBarInstance.dispose()
    diseaseBarInstance = null
  }
  if (radarChartRef.value && viewData.value && viewData.value.systemScore) {
    radarChartInstance = echarts.init(radarChartRef.value)
    renderRadarChart(viewData.value.systemScore)
  }
  if (diseaseRadarRef.value && viewData.value && viewData.value.diseaseRisk) {
    diseaseRadarInstance = echarts.init(diseaseRadarRef.value)
    renderDiseaseRadarChart(viewData.value.diseaseRisk)
  }
  if (diseaseBarRef.value && viewData.value && viewData.value.diseaseRisk) {
    diseaseBarInstance = echarts.init(diseaseBarRef.value)
    renderDiseaseBarChart(viewData.value.diseaseRisk, viewData.value.age)
  }
}

onMounted(fetchData)
</script>

<style scoped>
.el-avatar {
  border: 1px solid #eee;
}
</style> 