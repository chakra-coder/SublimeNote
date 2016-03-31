<!-- toc -->
# mysql

### mysql
1. **安装`yum-downloadonly`**
```bash
 yum install yum-downloadonly
```

+ **安装`mysql5.6 yum源`**
```bash
wget http://dev.mysql.com/get/mysql57-community-release-el6-7.noarch.rpm
```
    1. **`yum`库导入到你的本地**
```bash
yum localinstall mysql57-community-release-el6-7.noarch.rpm 
```
    + **指定安装`mysql5.6`**
```bash
vim /etc/yum.repos.d/mysql-community.repo
##
# Enable to use MySQL 5.6
[mysql56-community]
name=MySQL 5.6 Community Server
baseurl=http://repo.mysql.com/yum/mysql-5.6-community/el/6/$basearch/
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql
##
# diable to use MySQL 5.7
[mysql57-community]
name=MySQL 5.7 Community Server
baseurl=http://repo.mysql.com/yum/mysql-5.7-community/el/6/$basearch/
enabled=0
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql
```
    + **查询`yum mysql`版本**
```bash
yum list | grep mysql
```
    + **`yum`只下载`MySQLServer`不安装**
```bash
yum install mysql-community-server --downloadonly --downloaddir=rpm
```
+ **制作本地`yum`源**
    1. 安装`createrepo` 
```bash
yum install createrepo
```
    + **建立`repo`发布目录 (如果使用存在的目录,飘过)**
```bash
mkdir /root/rpm
```
    + **把`mysql`安装文件放入`/root/rpm`目录**
    + **修改`yum`源为本地源**
```bash
cd /etc/yum.repos.d/
#把CentOS*.repo修改CentOS*.repo-bak
vim Local-rpm.repo
#添加内容
[Local-rpm]
name=Local-rpm
baseurl=file:///root/rpm
enabled=1
gpgcheck=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-6
```
    + **用`createrepo`生成`meta`信息**
```bash
createrepo -o /root/rpm /root/rpm
```
    + **查看本地`yum`源是否配置成功**
```bash
yum list | grep mysql
```
    + **安装`mysql5.6`**
```bash
yum install mysql-community-server
```


### `mysql`安装后基本配置

+ **设置`mysql`服务器端默认编码**
```bash
vim /etc/my.cnf
#添加
character_set_server=utf8
```
