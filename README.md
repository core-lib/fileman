# fileman [![](https://www.jitpack.io/v/core-lib/fileman.svg)](https://www.jitpack.io/#core-lib/fileman)
### 基于HTTP协议和Servlet容器构建的可内嵌磁盘文件管理工具
框架将磁盘文件的操作抽象为HTTP的RESTful行为，采用 Java SPI 的插件化机制构建，内置提供：
* 文件列举
* 文件下载 / 上传，支持多线程下载以及断点续传
* 文件在线预览
* 权限认证
* 通过 Java SPI 拓展自定义特性

等功能，实现可通过浏览器或任意HTTP客户端管理远程服务端的磁盘文件。

