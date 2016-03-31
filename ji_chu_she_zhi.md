<!-- toc -->
# Linux基础设置
##### Linux主机名设置

```bash
#主机名设置
[root@localhost ~]# vim /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=Web-01
```

##### 添加多张网卡

```bash
#eth1变成eth0
vim /etc/udev/rules.d/70-persistent-net.rules

#编辑eth0 IP地址
vim /etc/sysconfig/network-scripts/ifcfg-eth0
```

##### Virtual Box 网卡MAC地址

+ CentOS-1
  + MAC:
  ```
  eth0    08:00:27:3F:48:85       HostOnly    ipaddr    192.168.137.101
  eth1    08:00:27:e1:36:70       Nat         ipaddr    10.0.2.101
  ```
+ CentOS-2
  * MAC:
  ```
  eth0    08:00:27:A2:B9:68       HostOnly    ipaddr    192.168.137.102
  ```
+ CentOS-3
  * MAC:
  ```
  eth0    08:00:27:16:DB:38       HostOnly    ipaddr    192.168.137.103
  ```
+ CentOS-4
  * MAC:
  ```
  eth0    08:00:27:4C:AA:E5       HostOnly    ipaddr    192.168.137.104
  ```
+ CentOS-Web-01
  * MAC:
  ```
  eth0    08:00:27:60:BC:F8       HostOnly    ipaddr    192.168.137.107
  eth1    08:00:27:92:53:06       Nat         ipaddr    10.0.2.107
  ```
+ CentOS-Web-02
  * MAC:
  ```
  eth0    08:00:27:60:BC:F9       HostOnly    ipaddr    192.168.137.108
  eth1    08:00:27:09:2F:95       Nat         ipaddr    10.0.2.108
  ```



#### Linux网络

```
ifconfig [ethX] 
    -a: 显示所有接口的配置住处
    
ifconfig ethX IP/MASK [up|down] 
    配置的地址立即生效，但重启网络服务或主机，都会失效；
 ```   
网络服务：
RHEL5:  /etc/init.d/network {start|stop|restart|status}
RHEL6: /etc/init.d/NetworkManager {start|stop|restart|status}

###### 网关：
```
route 
    add: 添加
        -host: 主机路由
        -net：网络路由
            -net 0.0.0.0
    route add -net|-host DEST gw NEXTHOP
    route add default gw NEXTHOP

del：删除
    -host
    -net 
    
    route del -net 10.0.0.0/8 
    route del -net 0.0.0.0
    route del default

    所做出的改动重启网络服务或主机后失效；

查看：
    route -n: 以数字方式显示各主机或端口等相关信息
```
    

##### 网络配置文件：
`/etc/sysconfig/network`

###### 网络接口配置文件：
```
/etc/sysconfig/network-scripts/ifcfg-INTERFACE_NAME
DEVICE=: 关联的设备名称，要与文件名的后半部“INTERFACE_NAME”保持一致; 
BOOTPROTO={static|none|dhcp|bootp}: 引导协议；要使用静态地址，使用static或none；dhcp表示使用DHCP服务器获取地址；
IPADDR=: IP地址
NETMASK=：子网掩码
GATEWAY=：设定默认网关；
ONBOOT=：开机时是否自动激活此网络接口；
HWADDR=： 硬件地址，要与硬件中的地址保持一致；可省；
USERCTL={yes|no}: 是否允许普通用户控制此接口；
PEERDNS={yes|no}: 是否在BOOTPROTO为dhcp时接受由DHCP服务器指定的DNS地址；

不会立即生效，但重启网络服务或主机都会生效；
```

#### 路由：
```
/etc/sysconfig/network-scripts/route-ethX
添加格式一：
DEST    via     NEXTHOP

添加格式二：
ADDRESS0=
NETMASK0=
GATEWAY0=


DNS服务器指定方法只有一种：
/etc/resolv.conf
nameserver DNS_IP_1
nameserver DNS_IP_2

指定本地解析：
/etc/hosts
主机IP    主机名 主机别名
172.16.0.1      www.magedu.com      www

DNS-->/etc/hosts-->DNS


配置主机名：
hostname HOSTNAME

立即生效，但不是永久有效；

/etc/sysconfig/network
HOSTNAME=
RHEL5：
    setup: system-config-network-tui
    system-config-network-gui

ifconfig, 老旧

iproute2
    ip
        link: 网络接口属性
        addr: 协议地址
        route: 路由

    link
        show
            ip -s link show
        set
            ip link set DEV {up|down}
            
    addr
        add
            ip addr add ADDRESS dev DEV
        del
            ip addr del ADDRESS dev DEV
        show
            ip addr show dev DEV to PREFIX
        flush
            ip addr flush dev DEV to PREFIX
       
        
一块网卡可以使用多个地址：
网络设备可以别名：
eth0
    ethX:X, eth0:0, eth0:1, ...
    
配置方法：
    ifconfig ethX:X IP/NETMASK
    
    /etc/sysconfig/network-scripts/ifcfg-ethX:X
    DEVICE=ethX:X

    非主要地址不能使用DHCP动态获取; 
``` 
#####ip
```
eth1, 添加个地址192.168.100.1
    
ip addr add 192.168.100.1/24 dev eth1 label eth1:0
primary address
secondary adress

192.168.100.6

10.0.1.0/24, 192.168.100.6
```
##### 路由:
```
route add -net 10.0.1.0/24 gw 192.168.100.6

ip route add to 10.0.1.0/24 dev eth1 via 192.168.100.6
    add, change, show, flush, replace
    
ifconfig eth0, 172.16.200.33/16

ifconfig eth0:0 172.16.200.33/16

TCP:
    URG 
    SYN
    ACK
    PSH
    RST
    FIN
```


##### Linux: openSSH
```	
	C/S
		服务器端：sshd, 配置文件/etc/ssh/sshd_config
		客户端：ssh, 配置文件/etc/ssh/ssh_config
			ssh-keygen: 密钥生成器
			ssh-copy-id: 将公钥传输至远程服务器
			scp：跨主机安全复制工具
				
			
		ssh: 
			ssh USERNAME@HOST
			ssh -l USERNAME HOST
			ssh USERNAME@HOST 'COMMAND'
			
		scp: 
			scp SRC DEST
				-r
				-a
			scp USERNAME@HOST:/path/to/somefile  /path/to/local
			scp /path/to/local  USERNAME@HOST:/path/to/somewhere
			
		ssh-keygen
			-t rsa	
				~/.ssh/id_rsa
				~/.ssh/id_rsa.pub
			-f /path/to/KEY_FILE
			-P '': 指定加密私钥的密码
		
				
		公钥追加保存到远程主机某用户的家目录下的.ssh/authorized_keys文件或.ssh/authorized_keys2文件中

		ssh-copy-id
			-i ~/.ssh/id_rsa.pub
			ssh-copy-id -i ~/.ssh/id_rsa.pub USERNAME@HOST
```	

### `CentOS 6.7`升级内核(源码编译)
1. 下载源码包
网址：`http://www.kernel.org`
在首页可以看到有 `stable`，`longterm` 等版本，一般选择下载 `longterm` 版本，因为此版本为提供长期支持的稳定版，因此我选择 `3.10.96`.
```bash
# wget https://cdn.kernel.org/pub/linux/kernel/v3.x/linux-3.10.96.tar.xz
```
2. 解压并进入目录
```bash
# tar -xf linux-3.12.16.tar.xz
# cd linux-3.12.16
```
3. 更新当前系统
```bash
# yum update
# yum upgrade
```
4. 安装编译内核所需要的软件包
```bash
# yum groupinstall "Development Tools"
# yum install ncurses-devel
# yum install qt-devel
# yum install hmaccalc zlib-devel binutils-devel elfutils-libelf-devel
```
5. 查看当前系统内核
```bash
# uname -r
2.6.32-358.11.1.el6.x86_64
```
6. 将当前系统内核的配置文件拷贝到当前目录
```bash
# cp /boot/config-2.6.32-358.11.1.el6.x86_64 .config
```
7. 使用当前系统内核配置，并自动接受每个新增选项的默认设置
```bash
# sh -c 'yes "" | make oldconfig'
```
8. 编译
```bash
# make bzImage
# make modules
# make modules_install
```
9. 安装
```bash
# make install
如果出现少量 ERROR 可以忽略
```
10. 修改 Grub 引导顺序
```bash
# vim /etc/grub.conf
一般新内核的位置都在第一个，所以设置 default=0.
```
11. 重启后查看内核版本号
```bash
# uname -r
3.12.16
```

12. 如果失败，转至第 5 步，在重新开始之前，需要清理上次编译的现场
```bash
# make mrproper
```

### `CentOS 6.7`升级内核(rpm安装)
>在yum的ELRepo源中，有mainline（3.13.1）、long-term（3.10.28）这2个内核版本，考虑到long-term更稳定，会长期更新，所以选择这个版本。

1. 安装`ELRepo`到`CentOS-6.7`中
```bash
rpm -ivh http://www.elrepo.org/elrepo-release-6-5.el6.elrepo.noarch.rpm
```
+ 安装**kernel-lt（lt=long-term）**
```bash
yum --enablerepo=elrepo-kernel install kernel-lt -y
```
+ 或者安装**kernel-ml（ml=mainline）**
```bash
yum --enablerepo=elrepo-kernel install kernel-ml -y
```
+ 编辑`grub.conf`文件，修改`Grub`引导顺序
```
vim /etc/grub.conf
修改default=0
```