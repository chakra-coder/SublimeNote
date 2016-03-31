# heartbeat

##### Server 1 上增加如下主机路由：
```bash
# Server 1 上访问10.0.2.107，走eth1网卡，即使用心跳线路
/sbin/route add -host 10.0.2.107 dev eth1

# 加入开机自启动配置里。下次启动会自动加载这个路由的配置
echo '/sbin/route add -host 10.0.2.101 dev eth1'>>/etc/rc.local
```


##### yum安装rpm包本地不清除的方法

```bash
[root@c1 ~]# sed -i 's#keepcache=0#keepcache=1#g' /etc/yum.conf
[root@c1 ~]# grep keepcache /etc/yum.conf 
keepcache=1
```

###### yum 安装软件

```bash
yum install heartbeat -y     # 执行两遍

```

###### yum 只下载不安装
```bash
# 安装yum-downloadonly包
yum install yum-downloadonly

#使用
yum install httpd --downloadonly --downloaddir=/tmp/rpm
```

###### yum 安装到指定目录
```bash
yum install --installroot=/usr/src/ vim
```







