# 🏥 中州养老 AI + IoT 智慧养老管理平台

> 基于 Spring Boot 3 + Vue 3 + AI 大模型 + 华为云 IoT 的智慧养老院管理系统

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.5-4FC08D.svg)](https://vuejs.org/)
[![Java](https://img.shields.io/badge/Java-19-orange.svg)](https://www.oracle.com/java/)

---

## 📖 项目简介

**中州养老**是一个面向养老机构的智慧化管理平台，通过 **AI 大模型 + 物联网（IoT）+ 微信小程序** 三大技术支柱，将管理者、老人家属和智能设备三方连接在一起，实现养老服务的数字化、智能化升级。

### 🎯 核心角色

| 角色 | 终端 | 主要功能 |
|------|------|---------|
| 🧑‍💼 **养老机构管理员** | Web 管理后台 | 老人管理、房间管理、护理服务管理、IoT 设备管理、健康评估、数据统计 |
| 👨‍👩‍👧 **老人家属** | 微信小程序 | 查看老人信息、预约探视、订购护理服务、AI 智能助手 |
| ⌚ **IoT 智能设备** | 华为云 IoTDA | 智能手表、门锁等设备数据实时上报（体温、心率、血氧等） |

---

## ✨ 核心功能

### 🧑‍💼 管理后台（Vue 3 + Element Plus）

- **老人信息管理** — 老人档案 CRUD，支持按姓名/身份证/手机号搜索
- **家属管理** — 家属账号管理，家属与老人绑定/解绑
- **房间管理** — 房型 CRUD（名称、床位、价格、照片），状态启用/禁用
- **护理项目管理** — 护理服务 CRUD（名称、价格、单位、图片、护理要求）
- **预约管理** — 探视预约审核、状态管理
- **订单管理** — 护理服务订单查看、退款处理
- **IoT 设备管理** — 设备注册/同步/删除（对接华为云 IoTDA 平台）
- **设备数据查看** — 查询设备上报的历史数据
- **📊 数据统计** — 首页仪表盘（用户数、老人数、今日预约、今日订单）
- **🏥 AI 健康评估** — 上传体检报告 PDF → AI 自动分析 → 输出健康评分、风险评估、护理等级建议、八系统健康分数、异常指标解读、入院建议

### 👨‍👩‍👧 家属端（微信小程序）

- **微信登录** — 微信授权一键登录
- **我的老人** — 绑定/查看家中老人信息
- **预约探视** — 在线预约探视时间，查看剩余名额
- **护理服务** — 浏览护理项目、下单购买、申请退款
- **🤖 AI 智能助手** — 流式对话机器人，支持：
  - 查询已绑定的老人列表
  - 语音/文字预约探视
  - 查询预约记录
  - 取消预约
  - 查询取消次数
  - 基于知识库（RAG）的智能问答

### ⌚ IoT 物联网

- **AMQP 消息消费** — 启动时自动连接华为云 IoTDA，订阅设备数据队列
- **设备数据入库** — 实时接收设备遥测数据，解析并存入数据库
- **设备生命周期** — 注册、同步、删除设备（通过华为云 SDK）
- **设备模拟器** — 内置智能手表模拟器（体温、心率、血氧、电量）

### 🧠 AI 能力

- **大模型**：阿里云百炼（DashScope）qwen-max
- **RAG 检索增强**：基于知识库文档的向量检索问答
- **Function Calling**：AI 可调用业务函数（预约、查询等）
- **流式对话**：SSE（Server-Sent Events）流式输出
- **健康报告分析**：PDF 文本提取 → AI 结构化分析 → JSON 输出

---

## 🛠️ 技术栈

### 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.5.0 | 主框架 |
| MyBatis Plus | 3.5.5 | ORM / 分页 |
| MySQL | 8.0+ | 关系型数据库 |
| Redis | 6.0+ | 缓存 & 临时存储 |
| Spring AI | 1.0.0-M2 | AI 集成框架 |
| Spring Cache | - | 缓存抽象 |
| JWT (jjwt) | 0.11.5 | 身份认证（30天有效期） |
| Springdoc OpenAPI | 2.5.0 | API 文档（Swagger UI） |
| Apache PDFBox | 2.0.24 | PDF 文本提取 |
| 华为云 IoTDA SDK | 3.1.76 | 物联网设备管理 |
| Apache Qpid JMS | 0.61.0 | AMQP 1.0 消息消费 |
| 阿里云 OSS SDK | 3.15.1 | 文件/图片上传 |
| Hutool | 5.8.13 | 通用工具库 |
| Lombok | 1.18.28 | 代码简化 |

### 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5.17 | 前端框架 |
| Vite | 7.x | 构建工具 |
| Element Plus | 2.10.2 | UI 组件库 |
| ECharts | 5.6.0 | 数据可视化 |
| Axios | 1.10.0 | HTTP 客户端 |
| Vue Router | 4.5.1 | 路由管理 |
| Vuex | 4.0.2 | 状态管理 |

### 小程序

| 技术 | 说明 |
|------|------|
| uni-app | 跨平台开发框架 |
| 微信小程序 | 家属端运行平台 |

### 云服务 & 外部集成

| 服务 | 用途 |
|------|------|
| 🟠 **阿里云 OSS** | 图片/文件存储 |
| 🟣 **阿里云百炼（DashScope）** | AI 大模型 qwen-max |
| 🔵 **华为云 IoTDA** | 物联网设备平台 |
| 🟢 **微信开放平台** | 小程序登录 & 手机号授权 |

---

## 📁 项目结构

```
springboot-zzyl/
├── zzylai/                              # 主体项目
│   ├── src/main/java/cn/yangeit/
│   │   ├── ZZYLAIApplication.java       # Spring Boot 启动类
│   │   ├── common/                      # 工具类
│   │   │   ├── AliOSSUtils.java         # 阿里云 OSS 上传工具
│   │   │   ├── JwtUtils.java            # JWT 令牌工具
│   │   │   ├── PDFUtil.java             # PDF 文本提取
│   │   │   └── PropertySample.java      # IoT 设备模拟器
│   │   ├── config/                      # 配置类
│   │   │   ├── AIModelInvoker.java      # AI 模型调用器
│   │   │   ├── IotClientConfig.java     # 华为云 IoT 客户端配置
│   │   │   ├── LoginCheckInterceptor.java # JWT 登录拦截器
│   │   │   ├── ReservTools.java         # AI Function Calling 工具
│   │   │   ├── SwaggerConfig.java       # API 文档配置
│   │   │   ├── WebConfig.java           # Web 配置 / 拦截器注册
│   │   │   └── GlobalExceptionHandler.java # 全局异常处理
│   │   ├── controller/
│   │   │   ├── admin/                   # 管理后台 API
│   │   │   │   ├── ElderAdminController.java
│   │   │   │   ├── FamilyMemberAdminController.java
│   │   │   │   ├── RoomTypeAdminController.java
│   │   │   │   ├── NursingProjectAdminController.java
│   │   │   │   ├── ReservationAdminController.java
│   │   │   │   ├── OrderAdminController.java
│   │   │   │   ├── DeviceController.java
│   │   │   │   ├── DeviceDataController.java
│   │   │   │   ├── HealthAssessmentController.java
│   │   │   │   ├── UploadController.java
│   │   │   │   └── AdminDataController.java
│   │   │   └── customer/                # 小程序端 API
│   │   │       ├── FamilyMemberController.java
│   │   │       ├── MemberRoomTypeController.java
│   │   │       ├── MemberNursingProjectController.java
│   │   │       ├── MemberReservationController.java
│   │   │       └── OpenAiController.java
│   │   ├── job/                         # 定时任务 & IoT 消息消费
│   │   │   ├── AmqpClient.java          # AMQP 消息消费者
│   │   │   └── HuaWeiIotConfigProperties.java
│   │   ├── pojo/                        # 实体类
│   │   ├── service/                     # 业务逻辑层
│   │   ├── mapper/                      # MyBatis 数据访问层
│   │   └── vo/                          # 视图对象
│   ├── src/main/resources/
│   │   └── application.yml              # 主配置文件
│   ├── doc/
│   │   ├── zzylai.sql                   # 数据库建表 & 种子数据
│   │   ├── device.sql                   # IoT 设备相关表结构
│   │   ├── backend/zzylai-frontend/     # Vue 3 管理后台前端
│   │   └── mp-weixin/                   # uni-app 微信小程序
│   └── pom.xml                          # Maven 项目配置
├── .gitignore
├── LICENSE
└── README.md
```

---

## 🚀 快速开始

### 环境要求

- **JDK** 19+
- **Maven** 3.6+
- **MySQL** 8.0+
- **Redis** 6.0+
- **Node.js** 18+（前端）

### 1. 克隆项目

```bash
git clone https://github.com/ikun2004/-Smart-elderly-care.git
cd Smart-elderly-care
```

### 2. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS zzylai DEFAULT CHARACTER SET utf8mb4;

-- 导入表结构和种子数据
mysql -u root -p zzylai < zzylai/doc/zzylai.sql
mysql -u root -p zzylai < zzylai/doc/device.sql
```

### 3. 配置 application.yml

编辑 `zzylai/src/main/resources/application.yml`，填入你的实际配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/zzylai?...  # 数据库连接
    username: your-db-username                    # 数据库用户名
    password: your-db-password                    # 数据库密码
  redis:
    host: localhost                               # Redis 地址
    password: your-redis-password                 # Redis 密码（如果有）
  ai:
    dashscope:
      api-key: your-dashscope-api-key             # 阿里云百炼 API Key

aliyun:
  accessKeyId: your-aliyun-access-key-id          # 阿里云 AccessKey
  accessKeySecret: your-access-key-secret         # 阿里云 AccessKey Secret
  bucketName: your-bucket-name                    # OSS Bucket 名称

huaweicloud:
  ak: your-huaweicloud-ak                         # 华为云 AK
  sk: your-huaweicloud-sk                         # 华为云 SK
  projectId: your-project-id                      # 华为云项目 ID
  # ...其他华为云 IoT 配置
```

> ⚠️ **重要**：所有敏感字段已清空为占位符，请替换为你自己的真实配置后再运行。

### 4. 启动后端

```bash
cd zzylai
mvn spring-boot:run
```

服务启动后访问：
- API 服务：`http://localhost:9995`
- Swagger 文档：`http://localhost:9995/swagger-ui/index.html`

### 5. 启动管理后台前端

```bash
cd zzylai/doc/backend/zzylai-frontend
npm install
npm run dev
```

浏览器访问 Vite 开发服务器地址（默认 `http://localhost:5173`）。

### 6. 运行微信小程序

1. 下载 [微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)
2. 导入 `zzylai/doc/mp-weixin/` 目录
3. 在 `WechatService.java` 中配置你的小程序 AppID 和 AppSecret

---

## 📊 业务流程图

```
┌──────────────────────────────────────────────────────────────┐
│                      🔐 微信小程序端                          │
│                                                              │
│  家属登录 ──→ 绑定老人 ──→ 预约探视 / 购买服务 / AI 咨询      │
│                        ↑              ↓                      │
│                   微信授权登录    华为云 IoT 数据              │
└──────────────────────────────────────────────────────────────┘
                               │
                               ↓
┌──────────────────────────────────────────────────────────────┐
│                   🖥️ Spring Boot 后端 (9995)                 │
│                                                              │
│  JWT 鉴权 ──→ REST API ──→ MyBatis Plus ──→ MySQL           │
│       │              │              │                        │
│       │         Spring AI ────→ 阿里云百炼 (qwen-max)        │
│       │              │              │                        │
│       │         AMQP Client ──→ 华为云 IoTDA                 │
│       │              │              │                        │
│       │         阿里云 OSS ────→ 文件/图片存储                │
│       │                                                      │
│       └──── Redis（缓存）                                    │
└──────────────────────────────────────────────────────────────┘
                               │
                               ↓
┌──────────────────────────────────────────────────────────────┐
│                   🖥️ Vue 3 管理后台                           │
│                                                              │
│  数据统计 ── 老人管理 ── 房间管理 ── 护理项目 ── 健康评估      │
│                     │                           │            │
│               IoT 设备管理                   AI 分析报告       │
└──────────────────────────────────────────────────────────────┘
```

---

## 🔌 API 文档

启动后端后访问 Swagger UI：

```
http://localhost:9995/swagger-ui/index.html
```

### 主要 API 分组

| 分组 | 前缀 | 说明 |
|------|------|------|
| 老人管理 | `/admin/elder` | 老人信息 CRUD |
| 家属管理 | `/admin/user` | 家属信息管理 |
| 房型管理 | `/admin/roomTypes` | 房间类型管理 |
| 护理项目管理 | `/admin/project` | 护理服务管理 |
| 预约管理 | `/admin/reservation` | 探视预约管理 |
| 订单管理 | `/admin/orders` | 服务订单管理 |
| 设备管理 | `/admin/device` | IoT 设备管理 |
| 设备数据 | `/admin/devicedata` | IoT 数据查询 |
| 健康评估 | `/admin/healthAssessment` | AI 健康评估 |
| 文件上传 | `/admin/upload` | OSS 文件上传 |
| 数据统计 | `/admin/home/data` | 仪表盘统计 |
| 客户-用户 | `/customer/user` | 小程序登录 & 绑定 |
| 客户-房型 | `/customer/roomTypes` | 房型浏览 |
| 客户-服务 | `/customer/orders` | 服务订购 |
| 客户-预约 | `/customer/reservation` | 探视预约 |
| AI 助手 | `/customer/ai` | 流式 AI 对话 |

---

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

---

## ⚠️ 安全提醒

- 本仓库所有敏感信息（数据库密码、API Key、云服务密钥）均已清空为占位符
- **切勿将真实凭证提交到公开仓库**
- 建议将 `application.yml` 中的敏感配置移至环境变量或外部配置中心
- 如果历史上传过包含真实凭证的 commit，请及时在云平台控制台重置相关密钥

---

## 📄 License

本项目基于 [MIT License](LICENSE) 开源。

---

## 📮 联系方式

如有问题或建议，欢迎通过 GitHub Issues 交流。

---

<p align="center">
  <sub>Made with ❤️ for better elderly care</sub>
</p>
