<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listRoles, getRolePermissions, createRole, updateRole, deleteRole, assignRolePermissions } from '../api/role'
import { listPermissions } from '../api/permission'

const roles = ref([])
const permissions = ref([])
const loading = ref(false)

const dialogVisible = ref(false)
const dialogTitle = ref('新增角色')
const form = reactive({ id: null, code: '', name: '', description: '' })

const permDialogVisible = ref(false)
const permForm = reactive({ roleId: null, roleName: '', checked: [] })

async function loadRoles() {
  loading.value = true
  try {
    const res = await listRoles()
    if (res.code === 200) {
      roles.value = res.data
    } else {
      ElMessage.error(res.message || '加载失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

async function loadPermissions() {
  const res = await listPermissions()
  if (res.code === 200) {
    permissions.value = res.data
  }
}

function openCreate() {
  dialogTitle.value = '新增角色'
  Object.assign(form, { id: null, code: '', name: '', description: '' })
  dialogVisible.value = true
}

function openEdit(row) {
  dialogTitle.value = '编辑角色'
  Object.assign(form, { id: row.id, code: row.code, name: row.name, description: row.description })
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    const res = form.id ? await updateRole(form.id, form) : await createRole(form)
    if (res.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadRoles()
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '保存失败')
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除角色「${row.name}」吗？`, '提示', { type: 'warning' })
  } catch {
    return
  }
  try {
    const res = await deleteRole(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadRoles()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '删除失败')
  }
}

async function openAssign(row) {
  permForm.roleId = row.id
  permForm.roleName = row.name
  permForm.checked = []
  const res = await getRolePermissions(row.id)
  if (res.code === 200) {
    permForm.checked = res.data
  } else {
    ElMessage.error(res.message || '加载权限失败')
  }
  permDialogVisible.value = true
}

async function handleAssignSubmit() {
  try {
    const res = await assignRolePermissions(permForm.roleId, permForm.checked)
    if (res.code === 200) {
      ElMessage.success('权限已更新')
      permDialogVisible.value = false
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '保存失败')
  }
}

onMounted(() => {
  loadRoles()
  loadPermissions()
})
</script>

<template>
  <div>
    <el-card>
      <div class="toolbar">
        <span class="title">角色列表</span>
        <div class="spacer"></div>
        <el-button type="success" @click="openCreate">新增角色</el-button>
      </div>

      <el-table :data="roles" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="code" label="编码" width="140" />
        <el-table-column prop="name" label="名称" width="140" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openAssign(row)">分配权限</el-button>
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="420px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="编码" required>
          <el-input v-model="form.code" placeholder="如 MANAGER" />
        </el-form-item>
        <el-form-item label="名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="permDialogVisible" :title="`分配权限：${permForm.roleName}`" width="440px">
      <el-checkbox-group v-model="permForm.checked">
        <div v-for="p in permissions" :key="p.id" class="perm-item">
          <el-checkbox :value="p.id">{{ p.name }}（{{ p.code }}）</el-checkbox>
        </div>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="permDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssignSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}
.title {
  font-size: 16px;
  font-weight: 600;
}
.spacer {
  flex: 1;
}
.perm-item {
  padding: 6px 0;
}
</style>
