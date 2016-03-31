# NFS配置

####一、 **系统环境**

    系统平台：CentOS 6.5
    
    NFS Server IP：192.168.137.101
    
    防火墙已关闭/iptables: Firewall is not running.
####二、 **安装NFS服务**

+ 查看系统是否已安装NFS(系统默认安装)
+ 
```bash
[root@c1 tmp]# rpm -qa|grep nfs
nfs-utils-lib-1.1.5-6.el6.i686
nfs4-acl-tools-0.3.3-6.el6.i686
nfs-utils-1.2.3-39.el6.i686
```
    
####三、 **NFS服务器的配置**

NFS服务器的配置相对比较简单，只需要在相应的配置文件中进行设置，然后启动NFS服务器即可。

NFS的常用目录
```bash
/etc/exports                     NFS服务的主要配置文件
/usr/sbin/exportfs               NFS服务的管理命令
/usr/sbin/showmount              客户端的查看命令
/var/lib/nfs/etab                记录NFS分享出来的目录的完整权限设定值
/var/lib/nfs/xtab                记录曾经登录过的客户端信息
```

NFS服务的配置文件为 **`/etc/exports`**，这个文件是NFS的主要配置文件，不过系统并没有默认值，所以这个文件不一定会存在，可能要使用vim手动建立，然后在文件里面写入配置内容。

**`/etc/exports`**文件内容格式：


    <输出目录> [客户端1 选项（访问权限,用户映射,其他）] [客户端2 选项（访问权限,用户映射,其他）]


a. 输出目录：

    输出目录是指NFS系统中需要共享给客户机使用的目录；

b. 客户端：

客户端是指网络中可以访问这个NFS输出目录的计算机

客户端常用的指定方式

    指定ip地址的主机：192.168.0.200
    指定子网中的所有主机：192.168.0.0/24 192.168.0.0/255.255.255.0
    指定域名的主机：david.bsmart.cn
    指定域中的所有主机：*.bsmart.cn
    所有主机：*

c. 选项：

选项用来设置输出目录的访问权限、用户映射等。

NFS主要有3类选项：

访问权限选项

    设置输出目录只读：ro
    设置输出目录读写：rw

用户映射选项

    all_squash：将远程访问的所有普通用户及所属组都映射为匿名用户或用户组（nfsnobody）；
    no_all_squash：与all_squash取反（默认设置）；
    root_squash：将root用户及所属组都映射为匿名用户或用户组（默认设置）；
    no_root_squash：与rootsquash取反；
    anonuid=xxx：将远程访问的所有用户都映射为匿名用户，并指定该用户为本地用户（UID=xxx）；
    anongid=xxx：将远程访问的所有用户组都映射为匿名用户组账户，并指定该匿名用户组账户为本地用户组账户（GID=xxx）；

其它选项

    secure：限制客户端只能从小于1024的tcp/ip端口连接nfs服务器（默认设置）；
    insecure：允许客户端从大于1024的tcp/ip端口连接服务器；
    sync：将数据同步写入内存缓冲区与磁盘中，效率低，但可以保证数据的一致性；
    async：将数据先保存在内存缓冲区中，必要时才写入磁盘；
    wdelay：检查是否有相关的写操作，如果有则将这些写操作一起执行，这样可以提高效率（默认设置）；
    no_wdelay：若有写操作则立即执行，应与sync配合使用；
    subtree：若输出目录是一个子目录，则nfs服务器将检查其父目录的权限(默认设置)；
    no_subtree：即使输出目录是一个子目录，nfs服务器也不检查其父目录的权限，这样可以提高效率；

####四、 NFS服务器的启动与停止
```bash
 #service rpcbind status
 #rpcbind (pid  3514) 正在运行...
 #PS:默认开机rpcbind开启
 
 #启动nfs
 service nfs start
 #查询nfs状态
 service nfs status
 #关闭nfs
 service nfs stop
 
 #查询nfs自启动状态
 chkconfig --list nfs
 nfs             0:关闭  1:关闭  2:关闭  3:关闭  4:关闭  5:关闭  6:关闭
 #设置nfs服务在系统运行级别3和5自动启动
 chkconfig --level 35 nfs on
```
####五、实例
1. 将NFS Server 的`/tmp` 共享给`192.168.1.0/24`网段，权限读写
    ```bash
    vi /etc/exports
    
    /tmp 192.168.1.0/24(rw)
    ```
2. 重启nfs服务
    ```bash
    service nfs restart
    
    exportfs
    ```
3. 服务器端使用`showmount`命令查询NFS的共享状态 
 
 ```bash
[root@c2 ~]# showmount -e      //默认查看自己共享的服务，前提是要DNS能解析自己，不然容易报错
Export list for c1:
/tmp *
 ```
4. 客户端使用`showmount`命令查询NFS的共享状态
```bash
[root@c2 ~]# showmount -e 192.168.137.101
Export list for 192.168.137.101:
/tmp *
```
5. 客户端挂载NFS服务器中的共享目录

    ```bash
    # mount NFS服务器IP:共享目录 本地挂载点目录
    [root@c2 ~]# mount 192.168.137.101:/tmp /nfs-mount
    [root@c2 ~]# mount |grep nfs
    
    sunrpc on /var/lib/nfs/rpc_pipefs type rpc_pipefs (rw)
    nfsd on /proc/fs/nfsd type nfsd (rw)
    192.168.137.101:/tmp on /nfs-mount type nfs (rw,vers=4,addr=192.168.137.101,clientaddr=192.168.137.102)
    
    挂载成功
```

6. NFS的共享权限和访问控制

    客户端出现`Permission denied`，是因为NFS 服务器端共享的目录本身的写权限没有开放给其他用户，在服务器端打开该权限。 
    ```bash
    # chmod 777 -R /tmp
    ```
####六、启动自动挂载nfs文件系统

```bash

1、<server>:</remote/export> </local/directory> nfs < options> 0 0
# 例子
192.168.137.101:/tmp    /nfs-mount              nfs     defaults        0 0

2、手动挂载命令加入到/etc/rc.local中
    mount 10.0.2.107:/tmp/rpm /tmp/rpm/
```
 
####七、问题
**解决NFS**：clnt_create: RPC: Port mapper failure - Unable to receive: errno 113 (No route to host)

```bash
# 关闭防火墙
service iptables stop
# 开启不启动防火墙
chkconfig iptables off
```
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 