<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getEmployeePage, createEmployee, updateEmployee, deleteEmployee, getEmployeeRoles, assignEmployeeRoles, resetEmployeePassword, exportEmployees, importEmployees } from '../api/employee'
import { getDepartments } from '../api/department'
import { listRoles } from '../api/role'
import { hasAuthority, isAdmin } from '../utils/auth'

const keyword = ref('')
const filterDepartmentId = ref(null)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const list = ref([])
const loading = ref(false)
const canWrite = hasAuthority('employee:write')
const canDelete = hasAuthority('employee:delete')
const admin = isAdmin()

const roles = ref([])
const pwdDialogVisible = ref(false)
const pwdForm = reactive({ id: null, name: '', newPassword: '' })
const fileInput = ref(null)

const departments = ref([])
const deptMap = computed(() => {
  const map = {}
  departments.value.forEach((d) => {
    map[d.id] = d.name
  })
  return map
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const form = reactive({
  id: null,
  employeeNo: '',
  name: '',
  gender: 1,
  phone: '',
  email: '',
  departmentId: null,
  status: 1,
  roleIds: []
})

const genderMap = { 0: '未知', 1: '男', 2: '女' }
const statusMap = { 0: '离职', 1: '在职' }

async function loadData() {
  loading.value = true
  try {
    const res = await getEmployeePage({ page: page.value, size: size.value, keyword: keyword.value, departmentId: filterDepartmentId.value })
    if (res.code === 200) {
      list.value = res.data.list
      total.value = res.data.total
    } else {
      ElMessage.error(res.message || '加载失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

async function loadDepartments() {
  const res = await getDepartments()
  if (res.code === 200) {
    departments.value = res.data
  }
}

async function loadRoles() {
  const res = await listRoles()
  if (res.code === 200) {
    roles.value = res.data
  }
}

function handleSearch() {
  page.value = 1
  loadData()
}

function handleReset() {
  keyword.value = ''
  filterDepartmentId.value = null
  page.value = 1
  loadData()
}

function openCreate() {
  dialogTitle.value = '新增员工'
  Object.assign(form, {
    id: null,
    employeeNo: '',
    name: '',
    gender: 1,
    phone: '',
    email: '',
    departmentId: null,
    status: 1,
    roleIds: []
  })
  dialogVisible.value = true
}

async function openEdit(row) {
  dialogTitle.value = '编辑员工'
  Object.assign(form, {
    id: row.id,
    employeeNo: row.employeeNo,
    name: row.name,
    gender: row.gender,
    phone: row.phone,
    email: row.email,
    departmentId: row.departmentId,
    status: row.status,
    roleIds: []
  })
  if (admin) {
    const r = await getEmployeeRoles(row.id)
    if (r.code === 200) {
      form.roleIds = r.data
    }
  }
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    const res = form.id ? await updateEmployee(form.id, form) : await createEmployee(form)
    if (res.code === 200) {
      const empId = form.id || res.data.id
      if (admin) {
        await assignEmployeeRoles(empId, form.roleIds || [])
      }
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadData()
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '保存失败')
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除员工「${row.name}」吗？`, '提示', { type: 'warning' })
  } catch {
    return
  }
  try {
    const res = await deleteEmployee(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '删除失败')
  }
}

function openResetPassword(row) {
  pwdForm.id = row.id
  pwdForm.name = row.name
  pwdForm.newPassword = ''
  pwdDialogVisible.value = true
}

async function handleResetPassword() {
  if (!pwdForm.newPassword || pwdForm.newPassword.length < 6) {
    ElMessage.warning('新密码至少 6 位')
    return
  }
  try {
    const res = await resetEmployeePassword(pwdForm.id, { newPassword: pwdForm.newPassword })
    if (res.code === 200) {
      ElMessage.success('密码已重置')
      pwdDialogVisible.value = false
    } else {
      ElMessage.error(res.message || '重置失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '重置失败')
  }
}

async function handleExport() {
  try {
    const blob = await exportEmployees()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = '员工列表.xlsx'
    a.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (e) {
    ElMessage.error(e.message || '导出失败')
  }
}

function triggerImport() {
  fileInput.value.click()
}

async function handleImportFile(e) {
  const file = e.target.files[0]
  if (!file) {
    return
  }
  try {
    const res = await importEmployees(file)
    if (res.code === 200) {
      ElMessage.success(`导入完成：成功 ${res.data.success} 条，失败 ${res.data.fail} 条`)
      loadData()
    } else {
      ElMessage.error(res.message || '导入失败')
    }
  } catch (err) {
    ElMessage.error(err.message || '导入失败')
  } finally {
    e.target.value = ''
  }
}

onMounted(() => {
  loadData()
  loadDepartments()
  if (admin) {
    loadRoles()
  }
})
</script>

<template>
  <div class="employee-list">
    <el-card>
      <div class="toolbar">
        <el-input
          v-model="keyword"
          placeholder="姓名 / 工号 / 手机号 / 邮箱"
          clearable
          style="width: 260px"
          @keyup.enter="handleSearch"
        />
        <el-select
          v-model="filterDepartmentId"
          placeholder="按部门筛选"
          clearable
          style="width: 160px"
          @change="handleSearch"
        >
          <el-option v-for="d in departments" :key="d.id" :label="d.name" :value="d.id" />
        </el-select>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
        <div class="spacer"></div>
        <el-button v-if="canWrite" type="success" @click="openCreate">新增员工</el-button>
        <el-button @click="handleExport">导出</el-button>
        <el-button v-if="canWrite" @click="triggerImport">导入</el-button>
        <input ref="fileInput" type="file" accept=".xlsx,.xls" style="display: none" @change="handleImportFile" />
      </div>

      <el-table :data="list" v-loading="loading" border stripe>
        <el-table-column prop="employeeNo" label="工号" width="100" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column label="性别" width="80">
          <template #default="{ row }">{{ genderMap[row.gender] }}</template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column label="部门" width="120">
          <template #default="{ row }">{{ deptMap[row.departmentId] || '—' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ statusMap[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="canWrite || canDelete" label="操作" width="230" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canWrite" link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button v-if="canDelete" link type="danger" @click="handleDelete(row)">删除</el-button>
            <el-button v-if="admin" link type="warning" @click="openResetPassword(row)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pagination"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        :page-sizes="[5, 10, 20, 50]"
        v-model:current-page="page"
        v-model:page-size="size"
        @current-change="loadData"
        @size-change="handleSearch"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="工号" required>
          <el-input v-model="form.employeeNo" placeholder="如 E100" />
        </el-form-item>
        <el-form-item label="姓名" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" style="width: 100%">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
            <el-option label="未知" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="form.departmentId" clearable style="width: 100%">
            <el-option v-for="d in departments" :key="d.id" :label="d.name" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="admin" label="角色">
          <el-select v-model="form.roleIds" multiple clearable style="width: 100%">
            <el-option v-for="r in roles" :key="r.id" :label="r.name" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">在职</el-radio>
            <el-radio :value="0">离职</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="pwdDialogVisible" title="重置密码" width="400px">
      <p style="margin-bottom: 12px">为员工「{{ pwdForm.name }}」设置新密码：</p>
      <el-form label-width="80px">
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="至少 6 位" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleResetPassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 16px;
}
.spacer {
  flex: 1;
}
.pagination {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>
