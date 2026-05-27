<template>
  <div class="container">
    <h2>仓库管理</h2>
    <el-button type="primary" @click="showDialog = true">新增仓库</el-button>
    <el-table :data="pagedData" style="margin-top: 20px">
      <el-table-column prop="warehouseCode" label="仓库编码" width="150" />
      <el-table-column prop="warehouseName" label="仓库名称" width="200" />
      <el-table-column prop="address" label="地址" />
      <el-table-column prop="manager" label="负责人" width="120" />
      <el-table-column prop="phone" label="电话" width="150" />
    </el-table>
    
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 30, 50]"
      :total="warehouses.length"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; justify-content: center"
    />
    
    <el-dialog v-model="showDialog" title="新增仓库" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="仓库编码">
          <el-input v-model="form.warehouseCode" />
        </el-form-item>
        <el-form-item label="仓库名称">
          <el-input v-model="form.warehouseName" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.manager" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getWarehouses, createWarehouse } from '../api/warehouse'
import { ElMessage } from 'element-plus'

const warehouses = ref([])
const showDialog = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const form = ref({
  warehouseCode: '',
  warehouseName: '',
  address: '',
  manager: '',
  phone: ''
})

const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return warehouses.value.slice(start, end)
})

const loadData = async () => {
  const res = await getWarehouses()
  if (res.code === 200) warehouses.value = res.data
}

const handleSubmit = async () => {
  const res = await createWarehouse(form.value)
  if (res.code === 200) {
    ElMessage.success('创建成功')
    showDialog.value = false
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
