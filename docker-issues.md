<!-- toc -->
# docker-issues

### 系统信息
 
> Linux CentOS-Docker 2.6.32-573.el6.x86_64 

### `pull`镜像
1. `error`
```
Error response from daemon: invalid registry endpoint https://dl.dockerpool.com:5000/v0/: unable to ping registry endpoint https://dl.dockerpool.com:5000/v0/
v2 ping attempt failed with error: Get https://dl.dockerpool.com:5000/v2/: tls: oversized record received with length 28012
 v1 ping attempt failed with error: Get https://dl.dockerpool.com:5000/v1/_ping: tls: oversized record received with length 28012. If this private registry supports only HTTP or HTTPS with an unknown CA certificate, please add `--insecure-registry dl.dockerpool.com:5000` to the daemon's arguments. In the case of HTTPS, if you have access to the registry's CA certificate, no need for the flag; simply place the CA certificate at /etc/docker/certs.d/dl.dockerpool.com:5000/ca.crt
```
    + 解决
```bash
vim /etc/sysconfig/docker
other_args="--insecure-registry dl.dockerpool.com:5000"
```

### Object "netns" is unknown, try "ip help".
1. 
在docker使用的过程中如果要自定义容器的网络设备(主要是指定IP地址)，就要用到网络namespace（可以理解为进程级别的网络设备），关于namespace介绍可以查阅其它相关质料。centos默认安装的iproute包是不支持网络namespace操作的，使用ip netns会出现类似这样的报错：
```
Object "netns" is unknown, try "ip help".
```
+ 
谷歌度娘了一番，解决的方法都是升级内核和升级指定的iproute包。其实内核是不用的升级的，centos6.6版本的内核支持网络namespace操作的。只需要升级iproute包即可：
```
#安装yum源
[root@xx-xxxx ~]# yum install -y http://rdo.fedorapeople.org/rdo-release.rpm
#修改yum的地址，网上提供的地址目前是无效的，貌似地址目前已经变成下面这个了
[root@xx-xxxx ~]# cat /etc/yum.repos.d/rdo-release.repo
[openstack-kilo]
name=OpenStack Kilo Repository
baseurl=https://repos.fedorapeople.org/repos/openstack/EOL/openstack-icehouse/epel-6/
skip_if_unavailable=0
enabled=1
gpgcheck=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-RDO-kilo
[root@xx-xxxx ~]#
#安装指定yum源的iproute包
[root@xx-xxxx ~]#yum install -y iproute
```
centos6.6只要升级指定的iproute包即可，不用升级内核。centos6其它版本的没有测试，未知。