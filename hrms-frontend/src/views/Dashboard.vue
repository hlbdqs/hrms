<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { getOverview, getByDepartment, getByGender, getByStatus } from '../api/stats'

const overview = ref({ employees: 0, departments: 0, roles: 0, active: 0 })
const deptRef = ref(null)
const genderRef = ref(null)
const statusRef = ref(null)
let charts = []

const genderMap = { 0: '未知', 1: '男', 2: '女' }
const statusMap = { 0: '离职', 1: '在职' }

async function loadData() {
  try {
    const [o, d, g, s] = await Promise.all([getOverview(), getByDepartment(), getByGender(), getByStatus()])
    if (o.code === 200) {
      overview.value = o.data
    }
    renderBar(deptRef.value, '部门人数分布', (d.data || []).map((x) => x.name), (d.data || []).map((x) => x.cnt))
    renderPie(genderRef.value, '性别分布', (g.data || []).map((x) => ({ name: genderMap[x.gender] || '未知', value: x.cnt })))
    renderPie(statusRef.value, '在职状态', (s.data || []).map((x) => ({ name: statusMap[x.status] || '未知', value: x.cnt })))
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  }
}

function renderBar(el, title, names, values) {
  const chart = echarts.init(el)
  chart.setOption({
    title: { text: title, left: 'center', textStyle: { fontSize: 14 } },
    tooltip: {},
    grid: { left: 40, right: 20, top: 50, bottom: 30 },
    xAxis: { type: 'category', data: names },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: values, itemStyle: { color: '#409eff' }, barWidth: '50%' }]
  })
  charts.push(chart)
}

function renderPie(el, title, data) {
  const chart = echarts.init(el)
  chart.setOption({
    title: { text: title, left: 'center', textStyle: { fontSize: 14 } },
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{ type: 'pie', radius: ['40%', '65%'], data }]
  })
  charts.push(chart)
}

onMounted(loadData)
onBeforeUnmount(() => {
  charts.forEach((c) => c.dispose())
  charts = []
})
</script>

<template>
  <div>
    <el-row :gutter="16" class="cards">
      <el-col :span="6">
        <el-card><div class="stat"><div class="num">{{ overview.employees }}</div><div class="label">员工总数</div></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div class="stat"><div class="num">{{ overview.departments }}</div><div class="label">部门数</div></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div class="stat"><div class="num">{{ overview.roles }}</div><div class="label">角色数</div></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div class="stat"><div class="num">{{ overview.active }}</div><div class="label">在职人数</div></div></el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="charts">
      <el-col :span="12">
        <el-card><div ref="deptRef" class="chart"></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div ref="genderRef" class="chart"></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div ref="statusRef" class="chart"></div></el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.cards {
  margin-bottom: 16px;
}
.stat {
  text-align: center;
  padding: 8px 0;
}
.num {
  font-size: 28px;
  font-weight: 700;
  color: #409eff;
}
.label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
.charts {
  margin-top: 16px;
}
.chart {
  height: 320px;
}
</style>
