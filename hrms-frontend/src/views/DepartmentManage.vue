<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDepartments, createDepartment, updateDepartment, deleteDepartment } from '../api/department'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增部门')
const form = reactive({ id: null, name: '', description: '' })

async function loadData() {
  loading.value = true
  try {
    const res = await getDepartments()
    if (res.code === 200) {
      list.value = res.data
    } else {
      ElMessage.error(res.message || '加载失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  dialogTitle.value = '新增部门'
  Object.assign(form, { id: null, name: '', description: '' })
  dialogVisible.value = true
}

function openEdit(row) {
  dialogTitle.value = '编辑部门'
  Object.assign(form, { id: row.id, name: row.name, description: row.description })
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    const res = form.id ? await updateDepartment(form.id, form) : await createDepartment(form)
    if (res.code === 200) {
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
    await ElMessageBox.confirm(`确定删除部门「${row.name}」吗？`, '提示', { type: 'warning' })
  } catch {
    return
  }
  try {
    const res = await deleteDepartment(row.id)
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

onMounted(loadData)
</script>

<template>
  <div>
    <el-card>
      <div class="toolbar">
        <span class="title">部门列表</span>
        <div class="spacer"></div>
        <el-button type="success" @click="openCreate">新增部门</el-button>
      </div>

      <el-table :data="list" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="部门名称" width="180" />
        <el-table-column prop="description" label="描述" min-width="240" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="420px">
      <el-form :model="form" label-width="80px">
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
</style>
