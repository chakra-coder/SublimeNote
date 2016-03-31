<!-- toc -->

# svn

### `svn`

1. **`svn`安装**
```bash
yum -y install subversion
yum -y install mod_dav_svn
```
+ **创建`SVN`根目录**
```bash
mkdir -p $HOME/svn/
```
+ **创建repo1库**
```bash
svnadmin create $HOME/svn/repo1
```

### `SVN`配置

1. **用户密码`passwd`配置**
```
vim /root/svn/repo1/conf/passwd
#
xyc = 123
lyf = 123
```

+ **权限控制`authz`配置**
```bash
[groups]            #组
admin = xyc,lyf   #创建一个admin组，将用户加入到组
[/]                 #根目录权限设置（就是“svn”这个文件夹）
#aaa = rw            #aaa对svn下的所有版本库有读写权限
[repo1:/]            #repo:/,表示对repo版本库下的所有资源设置权限
@admin = rw         #admin组的用户对repo1版本库有读写权限
```

+ **服务`svnserve.conf`配置**
```bash
[general]
#匿名访问的权限，可以是read,write,none,默认为read
anon-access = none
#使授权用户有写权限
auth-access = write
#密码数据库的路径
password-db = passwd
#访问控制文件
authz-db = authz
#认证命名空间，subversion会在认证提示里显示，并且作为凭证缓存的关键字
realm = /opt/svn/repo
```
+ **启动`SVN`**
```bash
svnserve -d -r /usr/local/svn       #指定SVN根目录
```
+ **查看`SVN`进程**
```bash
ps -ef|grep svnserve
```
+ **检测`SVN` 端口**
```bash
netstat -ln |grep 3690
```
+ **停止重启`SVN`**
```bash
 killall svnserve
```

### 启服务器及测试
1. **启`SVN`服务，并指定`SVN`的根目录**
```bash
svnserve -d -r /usr/local/svn
```

+ **使用`checkout`导出文件**
```bash
svn checkout svn://127.0.0.1/repo1/xyc /usr/local/web/xyc      #本机测试，必需写127.0.0.1
```

### 项目自动发布

1. **配置`svn post-commit`钩子**
```bash
#!/bin/bash
export LANG="zh_CN.UTF-8"
export LANG="zh_CN.UTF-8"
export LC_CTYPE="zh_CN.UTF-8"
svn update /usr/local/web/xyc/ --username=svnuser --password=svnpass --non-interactive
```
+ **授予可执行权限**
```bash
chmod a+x /usr/local/svn/repo1/hooks/post-commit
```


<!--
+ ****
```bash

```
-->