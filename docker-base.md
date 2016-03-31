<!-- toc -->
# docker-base

### **set-up**

1. `CentOS 6.7 linux` 必须是64位
2. **关闭`SELinux`**
```bash
$sudo vi /etc/sysconfig/selinux
  - SELINUX=enforcing
  + SELINUX=disabled
```
+ 安装`docker`
```bash
$ sudo yum install http://mirrors.yun-idc.com/epel/6/i386/epel-release-6-8.noarch.rpm
$ sudo yum install docker-io
```
+ 获取镜像
```bash
docker pull dl.dockerpool.com:5000/ubuntu:14.04
```

### 使用
1. 列出本地镜像
```bash
docker images
```
+ 启动`docker`
```bash
docker run -t -i ubuntu:14.04 /bin/bash
```

### 操作`Docker`
1. `build`
```bash
docker build -t csphere/centos:7.1 .
```
2. `run`
```bash
#
-it 前台,交互模式
```
+ `log`
```bash
docker logs -f <ID>
```
+ 删除镜像
```bash
docker rmi [-f] <ID>
```
+ 删除容器
```bash
#删除所有容器
docker rm $(docker ps -a -q)
```
+ 关闭所有容器
```bash
docker stop $(docker ps -a -q)
```
+ 进入一个容器的交互模式
```bash
docker exec -it <CONTAINER Name> /bin/bash
```
+ 挂载目录
```bash
docker run -it -v /src/webapp:/opt/webapp ubuntu:14.04 /bin/bash
```
+ 载入镜像
```bash
docker load < ubuntu_14.04.tar
```
+ 查看`docker`网络
```
docker network ls
```
+ 容器生成镜像
```bash
docker commit 614122c0aabb aoct/apache2
```

### 修改`Docker`镜像的存储位置 

1. 备份fstab文件
```bash
cp /etc/fstab /etc/fstab.$(date +%Y-%m-%d)
```
+ 停止`docker`， 用`rsync`同步`/var/lib/docker`到新位置
    1. 如果`rsync`没有安装
```bash
yum -y install rsync
```
    + 停止`docker`
```bash
service docker stop
```
    + 同步数据
```bash
#在数据分区中建立要挂载的目录
mkdir /data/docker
#使用rsync工具同步
rsync -aXS /var/lib/docker/.  /data/docker/
```

+ 修改`fstab`
```bash
#vi /etc/fstab
/data/docker /var/lib/docker  none bind 0 0
```












