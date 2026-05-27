<template>
  <div class="container">
    <h2>物资分类管理</h2>
    <el-button type="primary" @click="showDialog = true">新增分类</el-button>
    <el-table :data="pagedData" style="margin-top: 20px">
      <el-table-column prop="categoryCode" label="分类编码" width="150" />
      <el-table-column prop="categoryName" label="分类名称" width="200" />
      <el-table-column prop="description" label="描述" />
    </el-table>
    
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 30, 50]"
      :total="categories.length"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; justify-content: center"
    />
    
    <el-dialog v-model="showDialog" title="新增分类" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="分类编码">
          <el-input v-model="form.categoryCode" />
        </el-form-item>
        <el-form-item label="分类名称">
          <el-input v-model="form.categoryName" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
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
import { getCategories, createCategory } from '../api/category'
import { ElMessage } from 'element-plus'

const categories = ref([])
const showDialog = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const form = ref({ categoryCode: '', categoryName: '', description: '' })

const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return categories.value.slice(start, end)
})

const loadData = async () => {
  const res = await getCategories()
  if (res.code === 200) categories.value = res.data
}

const handleSubmit = async () => {
  const res = await createCategory(form.value)
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
