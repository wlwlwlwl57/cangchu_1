<template>
  <div class="container">
    <h2>物资台账管理</h2>
    <el-button type="primary" @click="showDialog = true">新增物资</el-button>
    <el-table :data="pagedData" style="margin-top: 20px">
      <el-table-column prop="materialCode" label="物资编码" width="120" />
      <el-table-column prop="materialName" label="物资名称" width="150" />
      <el-table-column prop="specification" label="规格" width="150" />
      <el-table-column prop="material" label="材质" width="100" />
      <el-table-column prop="supplier" label="供应商" width="150" />
      <el-table-column prop="brand" label="品牌" width="120" />
    </el-table>
    
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 30, 50]"
      :total="materials.length"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; justify-content: center"
    />
    
    <el-dialog v-model="showDialog" title="新增物资" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="物资编码">
          <el-input v-model="form.materialCode" />
        </el-form-item>
        <el-form-item label="物资名称">
          <el-input v-model="form.materialName" />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.specification" />
        </el-form-item>
        <el-form-item label="材质">
          <el-input v-model="form.material" />
        </el-form-item>
        <el-form-item label="供应商">
          <el-input v-model="form.supplier" />
        </el-form-item>
        <el-form-item label="品牌">
          <el-input v-model="form.brand" />
        </el-form-item>
        <el-form-item label="物资分类">
          <el-select v-model="form.categoryId" placeholder="请选择">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.categoryName" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit" />
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
import { getMaterials, createMaterial } from '../api/material'
import { getCategories } from '../api/category'
import { ElMessage } from 'element-plus'

const materials = ref([])
const categories = ref([])
const showDialog = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const form = ref({
  materialCode: '', materialName: '', specification: '', material: '',
  supplier: '', brand: '', categoryId: null, unit: ''
})

const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return materials.value.slice(start, end)
})

const loadData = async () => {
  const res = await getMaterials()
  if (res.code === 200) materials.value = res.data
  const catRes = await getCategories()
  if (catRes.code === 200) categories.value = catRes.data
}

const handleSubmit = async () => {
  const res = await createMaterial(form.value)
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
