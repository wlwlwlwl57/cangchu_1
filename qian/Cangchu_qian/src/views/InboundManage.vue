<template>
  <div class="container">
    <h2>入库管理</h2>
    <el-button type="primary" @click="showDialog = true">新增入库单</el-button>
    <el-table :data="pagedData" style="margin-top: 20px">
      <el-table-column prop="orderNo" label="入库单号" width="180" />
      <el-table-column prop="warehouseName" label="仓库" width="150" />
      <el-table-column prop="operator" label="操作员" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'CONFIRMED' ? 'success' : 'warning'">
            {{ row.status === 'CONFIRMED' ? '已确认' : '待确认' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button v-if="row.status === 'PENDING'" @click="confirm(row.id)" size="small" type="primary">确认入库</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 30, 50]"
      :total="orders.length"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; justify-content: center"
    />
    
    <el-dialog v-model="showDialog" title="新增入库单" width="800px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="仓库">
          <el-select v-model="form.warehouseId">
            <el-option v-for="w in warehouses" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作员">
          <el-input v-model="form.operator" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" />
        </el-form-item>
      </el-form>
      <h4>入库明细</h4>
      <el-button @click="addDetail" size="small">添加明细</el-button>
      <el-table :data="form.details" style="margin-top: 10px">
        <el-table-column label="物资">
          <template #default="{ row }">
            <el-select v-model="row.materialId">
              <el-option v-for="m in materials" :key="m.id" :label="m.materialName" :value="m.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="数量">
          <template #default="{ row }">
            <el-input-number v-model="row.quantity" :min="0" />
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getInboundOrders, createInboundOrder, confirmInboundOrder } from '../api/inbound'
import { getWarehouses } from '../api/warehouse'
import { getMaterials } from '../api/material'
import { ElMessage } from 'element-plus'

const orders = ref([])
const warehouses = ref([])
const materials = ref([])
const showDialog = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const form = ref({ warehouseId: null, operator: '', remark: '', details: [] })

const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return orders.value.slice(start, end)
})

const loadData = async () => {
  const res = await getInboundOrders()
  if (res.code === 200) orders.value = res.data
  const wRes = await getWarehouses()
  if (wRes.code === 200) warehouses.value = wRes.data
  const mRes = await getMaterials()
  if (mRes.code === 200) materials.value = mRes.data
}

const addDetail = () => {
  form.value.details.push({ materialId: null, quantity: 0 })
}

const handleSubmit = async () => {
  form.value.inboundDate = new Date().toISOString()
  const res = await createInboundOrder(form.value)
  if (res.code === 200) {
    ElMessage.success('创建成功')
    showDialog.value = false
    loadData()
  } else {
    ElMessage.error(res.message)
  }
}

const confirm = async (id) => {
  const res = await confirmInboundOrder(id)
  if (res.code === 200) {
    ElMessage.success('确认成功')
    loadData()
  } else {
    ElMessage.error(res.message)
  }
}

onMounted(loadData)
</script>

<style scoped>
.container { padding: 20px; }
</style>
