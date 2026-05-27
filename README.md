# 仓储管理系统

## 系统功能

### 1. 仓库管理
- 支持多个仓库的设立和管理
- 仓库信息包括：编码、名称、地址、负责人、电话

### 2. 物资分类管理
- 用户可自定义物资分类
- 支持分类的增删改查

### 3. 物资台账管理
- 物资信息包括：物资编码、物资名称、规格、材质、供应商、品牌、物资分类
- 限制：相同的物资名称、规格、材质不能设立相同的物资编码
- 数据库层面通过唯一约束保证数据完整性

### 4. 入库管理
- 创建入库单，系统自动生成单据编码（格式：RK+年月日+流水号）
- 入库单包含：仓库、操作员、入库明细
- 确认入库后自动更新库存

### 5. 出库管理
- 创建出库单，系统自动生成单据编码（格式：CK+年月日+流水号）
- 出库单包含：仓库、操作员、领用人、出库明细
- 确认出库前检查库存，确认后自动扣减库存

### 6. 库存查询
- 按物资编码查询库存信息
- 按物资分类汇总库存信息
- 查询入库单信息
- 查询出库单信息

## 技术栈

### 后端
- Spring Boot 4.0.0
- Spring Data JPA
- MySQL 数据库
- Lombok

### 前端
- Vue 3
- Element Plus
- Vue Router
- Axios

## 启动说明

### 后端启动
1. 确保 MySQL 数据库已安装并运行
2. 创建数据库：`CREATE DATABASE warehouse_db;`
3. 修改 `hou/cangchu/src/main/resources/application.properties` 中的数据库连接信息
4. 进入后端目录：`cd hou/cangchu`
5. 运行：`./mvnw spring-boot:run` (Linux/Mac) 或 `mvnw.cmd spring-boot:run` (Windows)

### 前端启动
1. 进入前端目录：`cd qian/Cangchu_qian`
2. 安装依赖：`npm install`
3. 运行：`npm run dev`
4. 访问：http://localhost:5173

## 数据库设计

### 核心表
- warehouse: 仓库表
- material_category: 物资分类表
- material: 物资台账表
- inventory: 库存表
- inbound_order: 入库单主表
- inbound_order_detail: 入库单明细表
- outbound_order: 出库单主表
- outbound_order_detail: 出库单明细表

## 业务规则

1. 物资编码唯一性：物资编码在系统中必须唯一
2. 物资组合唯一性：相同名称、规格、材质的物资不能重复
3. 单据编码自动生成：入库单号(RK)和出库单号(CK)由系统自动生成，格式为：前缀+年月日+4位流水号
4. 库存管理：确认入库增加库存，确认出库扣减库存
5. 出库校验：出库时检查库存是否充足
