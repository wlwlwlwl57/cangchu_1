<template>
  <div id="app">
    <el-container>
      <el-aside width="200px">
        <div class="logo">仓储管理系统</div>
        <el-menu 
          :default-active="$route.path" 
          router 
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b">
          <el-menu-item index="/warehouse">
            <span>仓库管理</span>
          </el-menu-item>
          <el-menu-item index="/category">
            <span>物资分类</span>
          </el-menu-item>
          <el-menu-item index="/material">
            <span>物资台账</span>
          </el-menu-item>
          <el-menu-item index="/inbound">
            <span>入库管理</span>
          </el-menu-item>
          <el-menu-item index="/outbound">
            <span>出库管理</span>
          </el-menu-item>
          <el-menu-item index="/inventory">
            <span>库存查询</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  height: 100vh;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
}

.el-container {
  height: 100%;
}

.el-aside {
  background-color: #545c64;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  background-color: #409eff;
}

.el-menu {
  border: none;
}

.el-main {
  background-color: #f0f2f5;
}
</style>
