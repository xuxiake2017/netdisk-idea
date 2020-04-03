# netdisk-idea
nedisk网盘，适用于idea的java后台

## 使用需知
- 整体架构为ssm架构，前后端分离
- 使用shiro做登录认证
- 前端需配合netdisk-vue使用
- 使用tomcat，需要nginx代理
- 需要redis
- 储蓄文件使用了分布式文件服务器FastDFS
- 数据库mysql

## sql
netdisk.sql，储存过程为核心，用数据库实现了文件树

## 扩展
支持tomcat集群<br>
感谢`alexxiyang`提供的shiro-redis session共享方案<br>
github：[alexxiyang/shiro-redis](https://github.com/alexxiyang/shiro-redis)

## 后续
持续更新中
2018.12.17
