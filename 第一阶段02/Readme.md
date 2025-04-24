这是一个基于 Node.js 和前端技术的社交媒体应用（第一阶段实现），旨在提供用户动态发布、点赞、评论、关注等功能。本项目使用 Express 作为后端框架，MySQL 作为数据库，前端通过 HTML/CSS/JavaScript 实现交互。

当前日期：2025年4月11日

作者：
pq

版本：1.0.0

项目功能
用户认证：支持用户注册、登录和退出登录。
动态管理：用户可以发布、编辑、删除动态（支持文本和图片）。
社交互动：
点赞与取消点赞，支持多用户计数。
评论动态并查看评论列表。
关注与取消关注其他用户。
个人主页：
显示用户发布的动态、点赞过的动态和评论过的动态。
编辑个人资料（昵称、简介、头像）。
实时更新：动态、点赞数和评论数实时刷新。
项目结构
项目根目录：E:\homework\x-beijixingdeyan\第一阶段02

项目结构：
x-beijixingdeyan\第一阶段02\
│
├── server\              # 后端服务目录
│   ├── config\          # 配置文件
│   │   └── db.js        # 数据库连接配置（MySQL）
│   ├── middleware\      # 中间件
│   │   └── auth.js      # 认证中间件，验证用户 token
│   ├── models\          # 数据模型
│   │   └── Post.js      # 动态相关数据库操作
│   ├── routes\          # API 路由
│   │   └── posts.js     # 动态相关路由（增删改查、点赞、评论）
│   ├── uploads\         # 上传文件存储目录
│   │   └── posts\       # 动态图片存储
│   └── server.js        # 服务入口，Express 应用
│
├── scripts\             # 前端脚本目录
│   ├── api.js           # API 封装，处理前后端交互
│   ├── main.js          # 主页逻辑（动态列表、交互）
│   └── profile.js       # 个人主页逻辑（资料、动态、选项卡）
│
├── styles\              # 样式目录
│   └── style.css        # 全局 CSS 样式
│
├── assets\              # 静态资源
│   └── images\          # 图片资源
│       └── default-avatar.jpg  # 默认用户头像
│
├── index.html           # 主页 HTML
├── login.html           # 登录页面 HTML
├── profile.html         # 个人主页 HTML
│
├── node_modules\        # Node.js 依赖
├── package.json         # 项目依赖和脚本配置
└── README.md            # 项目说明文件（本文件）

目录说明
server/：
config/db.js：配置 MySQL 数据库连接参数。
middleware/auth.js：JWT 认证中间件，确保 API 请求需要登录。
models/Post.js：动态相关的 CRUD 操作，包括点赞和评论功能。
routes/posts.js：定义动态相关的 RESTful API 路由。
uploads/posts/：存储用户上传的动态图片。
server.js：Express 服务启动文件，监听端口并加载路由。

scripts/：
api.js：封装 fetch 请求，提供 GET、POST、PUT、DELETE 方法，并处理 token 认证。
main.js：主页的 JavaScript 逻辑，渲染动态列表并处理点赞、评论等交互。
profile.js：个人主页逻辑，管理用户资料编辑、动态选项卡切换和退出登录。

styles/：
style.css：定义页面样式，包括动态、评论、按钮等的布局和效果。

assets/images/：
default-avatar.jpg：默认头像，用于用户未上传头像时显示。

HTML 文件：
index.html：主页，展示所有用户的动态。
login.html：登录页面，用于用户认证。
profile.html：个人主页，展示用户信息和动态。

技术栈
前端：
HTML5 / CSS3 / JavaScript (ES6+)
后端：
Node.js
Express.js
数据库：
MySQL
依赖：
mysql2：数据库驱动
multer：文件上传处理
jsonwebtoken：用户认证
（其他依赖见 package.json）

安装与运行
前提条件
Node.js（建议 v16.x 或以上）
MySQL（建议 v8.x）
项目路径：E:\homework\x-beijixingdeyan\第一阶段02
安装步骤
克隆或进入项目目录：
bash
cd E:\homework\x-beijixingdeyan\第一阶段02
安装依赖：
bash
npm install
配置数据库：
创建 MySQL 数据库（例如 social_app）。
在 server/config/db.js 中配置数据库连接：
javascript
const pool = require('mysql2/promise').createPool({
    host: 'localhost',
    user: 'root',
    password: 'your_password',
    database: 'social_app'
});
初始化表结构（参考 SQL文件 或手动创建 users, posts, post_likes, post_comments 等表）。
启动后端服务：
bash
cd server
node server.js
默认端口：5000（若改为3000，需同步更新 scripts/api.js）。
访问页面：
打开浏览器，访问 http://localhost:5000/index.html。
登录后可访问 profile.html 和其他功能。
使用说明
登录：
访问 login.html，输入用户名和密码登录。
登录成功后自动跳转到 index.html。
发布动态：
在主页输入内容并上传图片，点击发布。
互动：
点击“喜欢”按钮点赞或取消点赞。
点击“评论”按钮查看并添加评论。
个人主页：
访问 profile.html，查看动态、点赞和评论记录。
点击“编辑资料”修改信息，点击“退出登录”返回登录页。
已知问题与待办
已解决：
点击评论显示所有评论信息。
点赞支持取消并正确计数。
退出登录按钮无效。
待办（可选）：
添加动态删除确认提示。
优化图片上传大小限制。
支持动态分页加载。
贡献
如需贡献代码或报告问题，请联系 
2037545140@qq.com

致谢
感谢 xAI 的 Grok 3 提供的代码调试与优化支持！