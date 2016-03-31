<!-- toc -->
# 软件安装

### `RPM`安装软件
1. 指定安装目录
```bash
rpm -ivh --prefix=/opt/temp  xxx.rpm
```

### 安装`Java`
1. 查看`Linux`自带的`JDK`是否已安装
```bash
java -version
```
> ps:如果出现`openjdk`，最好还是先卸载掉`openjdk`,在安装`sun`公司的`jdk`.
+ 查看`jdk`安装信息
```bash
rpm -qa|grep java 
rpm -qa|grep jdk
```
+ 卸载`OpenJDK`，执行以下操作
```bash
rpm -e --nodeps tzdata-java-2012c-1.el6.noarch  
rpm -e --nodeps java-1.7.0-openjdk-1.7.0.45-1.45.1.11.1.el6.x86_64  
```
+ 新建`java`安装目录
```bash
mkdir /usr/java  
```
+ 将之前下载的`jdk`解压缩并安装
```bash
#源码
tar -zxvf  jdk-7u71-linux-i586.tar.gz  
#rpm
rpm -ivh jdk-7u79-linux-i586.rpm --prefix=/usr/java/
```
+ 在`profile`文件中加入`java`环境变量
```bash
vi /etc/profile  
#  
export JAVA_HOME=/usr/java/jdk1.7.0_71  
export CLASSPATH=.:%JAVA_HOME%/lib/dt.jar:%JAVA_HOME%/lib/tools.jar  
export PATH=$PATH:$JAVA_HOME/bin   
```
+ 使文件立即生效
```bash
source /etc/profile
```