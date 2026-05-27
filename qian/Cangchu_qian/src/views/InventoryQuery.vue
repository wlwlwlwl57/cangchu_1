<template>
  <div class="container">
    <h2>库存查询</h2>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="模糊查询" name="search">
        <div class="search-box">
          <el-input 
            v-model="searchKeyword" 
            placeholder="请输入物资编码、名称或规格进行模糊查询" 
            style="width: 400px"
            clearable
            @keyup.enter="queryByKeyword"
          />
          <el-button type="primary" @click="queryByKeyword" style="margin-left: 10px">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </div>
        <el-table :data="pagedSearchData" style="margin-top: 20px" v-loading="loading">
          <el-table-column prop="materialCode" label="物资编码" width="120" />
          <el-table-column prop="materialName" label="物资名称" width="150" />
          <el-table-column prop="specification" label="规格" width="150" />
          <el-table-column prop="material" label="材质" width="100" />
          <el-table-column prop="categoryName" label="分类" width="120" />
          <el-table-column prop="warehouseName" label="仓库" width="150" />
          <el-table-column prop="quantity" label="库存数量" width="100" align="right" />
          <el-table-column prop="unit" label="单位" width="80" />
        </el-table>
        <el-pagination
          v-if="searchInventoryList.length > 0"
          v-model:current-page="searchPage"
          v-model:page-size="searchPageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="searchInventoryList.length"
          layout="total, sizes, prev, pager, next, jumper"
          style="margin-top: 20px; justify-content: center"
        />
      </el-tab-pane>
      
      <el-tab-pane label="按物资编码查询" name="material">
        <div class="search-box">
          <el-input v-model="materialCode" placeholder="请输入完整物资编码" style="width: 300px" clearable />
          <el-button type="primary" @click="queryByMaterial" style="margin-left: 10px">查询</el-button>
        </div>
        <el-table :data="materialInventory" style="margin-top: 20px">
          <el-table-column prop="materialCode" label="物资编码" width="120" />
          <el-table-column prop="materialName" label="物资名称" width="150" />
          <el-table-column prop="warehouseName" label="仓库" width="150" />
          <el-table-column prop="quantity" label="库存数量" width="100" align="right" />
          <el-table-column prop="unit" label="单位" width="80" />
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="按分类汇总" name="category">
        <el-button type="primary" @click="queryByCategory">查询</el-button>
        <el-table :data="categoryInventory" style="margin-top: 20px">
          <el-table-column prop="categoryCode" label="分类编码" width="150" />
          <el-table-column prop="categoryName" label="分类名称" width="200" />
          <el-table-column prop="totalQuantity" label="总库存" align="right" />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { getInventoryByMaterial, searchInventory as searchInventoryApi, getInventoryByCategory } from '../api/inventory'
import { ElMessage } from 'element-plus'

const activeTab = ref('search')
const materialCode = ref('')
const searchKeyword = ref('')
const materialInventory = ref([])
const searchInventoryList = ref([])
const categoryInventory = ref([])
const loading = ref(false)
const searchPage = ref(1)
const searchPageSize = ref(10)

const pagedSearchData = computed(() => {
  const start = (searchPage.value - 1) * searchPageSize.value
  const end = start + searchPageSize.value
  return searchInventoryList.value.slice(start, end)
})

const queryByKeyword = async () => {
  if (!searchKeyword.value || searchKeyword.value.trim() === '') {
    ElMessage.warning('请输入查询关键词')
    return
  }
  loading.value = true
  try {
    const res = await searchInventoryApi(searchKeyword.value.trim())
    if (res.code === 200) {
      searchInventoryList.value = res.data
      searchPage.value = 1
      if (res.data.length === 0) {
        ElMessage.info('未查询到相关库存信息')
      }
    } else {
      ElMessage.error(res.message)
    }
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  searchInventoryList.value = []
  searchPage.value = 1
}

const queryByMaterial = async () => {
  if (!materialCode.value) {
    ElMessage.warning('请输入物资编码')
    return
  }
  const res = await getInventoryByMaterial(materialCode.value)
  if (res.code === 200) {
    materialInventory.value = res.data
    if (res.data.length === 0) {
      ElMessage.info('未查询到该物资的库存信息')
    }
  } else {
    ElMessage.error(res.message)
  }
}

const queryByCategory = async () => {
  const res = await getInventoryByCategory()
  if (res.code === 200) {
    categoryInventory.value = res.data
  }
}
</script>

<style scoped>
.container { 
  padding: 20px; 
}
.search-box {
  display: flex;
  align-items: center;
}
</style>
