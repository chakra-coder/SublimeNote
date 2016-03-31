<!-- toc -->
# yum

### `yum`源
1.  
[阿里云`yum`源](http://mirrors.aliyun.com/repo/)

+ http://mirrors.aliyun.com/repo/
+ 备份你的原镜像文件，以免出错后可以恢复
```
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
```
+ 下载新的`CentOS-Base.repo` 到`/etc/yum.repos.d/`
```
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-6.repo
```
+ 运行`yum makecache`生成缓存
```
yum clean all
yum makecache
```


### yum安装rpm包本地不清除的方法

```bash
[root@c1 ~]# sed -i 's#keepcache=0#keepcache=1#g' /etc/yum.conf
[root@c1 ~]# grep keepcache /etc/yum.conf 
keepcache=1
```

### yum 安装软件

```bash
yum install heartbeat -y     # 执行两遍

```

### yum 只下载不安装
```bash
# 安装yum-downloadonly包
yum install yum-downloadonly

#使用
yum install httpd --downloadonly --downloaddir=/tmp/rpm
```

### yum 安装到指定目录
```bash
yum install --installroot=/usr/src/ vim
```

### yum 安装lrzsz 文件上传
```bash
 yum -y install lrzsz
```

### yum服务器配置
```bash
# 1. 安装工具createrepo
yum install createrepo

# 2. 建立repo发布目录 (如果使用存在的目录,飘过)
mkdir /tmp/rpm

# 3. 用createrepo生成meta信息
createrepo -o /tmp/rpm /tmp/rpm
```

### yum客户端配置
```bash
[root@CentOS-Web-02 ~]# cat /etc/yum.repos.d/Local-rpm.repo

[Local-rpm]
name=Local-rpm
baseurl=file:///tmp/rpm
enabled=1
gpgcheck=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-6

# 客户端更新缓存
yum clean all     可以不用
yum makecache
```
