<!-- toc -->
# 分区
1. 首先查看机器上有多少块硬盘：
```
$ fdisk -l
#
Disk /dev/sdd: 1000.2 GB, 1000204886016 bytes
255 heads, 63 sectors/track, 121601 cylinders
Units = cylinders of 16065 * 512 = 8225280 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk identifier: 0x00000000
#
#
Disk /dev/sdd doesn't contain a valid partition table
#
Disk /dev/sdh: 1000.2 GB, 1000204886016 bytes
255 heads, 63 sectors/track, 121601 cylinders
Units = cylinders of 16065 * 512 = 8225280 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk identifier: 0xd54c485e
#
#
   Device Boot      Start         End      Blocks   Id  System
/dev/sdh1               1      121601   976760001   83  Linux
#
我的服务器上有很多块，有上述信息可知，目前sdd硬盘还为挂载
```

2. 硬盘分区
```
$ fdisk /dev/sdd
-
n->p->1->回车->回车
-
意思就是新建一个主分区，大小是整个sdd磁盘
-
最后执行w，写入磁盘
-
此时磁盘已经分区，但是还没有文件系统，磁盘依然不能用
```
3. 格式化磁盘并写入文件系统
```
$ mkfs.ext4 /dev/sdd1 
-
等待命令执行完成
```
4. 挂载新硬盘到操作系统的某个节点上
```
$ mkdir /mnt/sdd
-
$ mount /dev/sdd1 /mnt/sdd
```

5. 执行df命令查看磁盘信息，确认挂载新硬盘成功
```
$ df
-
Filesystem           1K-blocks      Used Available Use% Mounted on
/dev/sda3            928204272   4191296 876862896   1% /
tmpfs                 32981876        12  32981864   1% /dev/shm
/dev/sda1               198337     31960    156137  17% /boot
/dev/sdh1            961432072    204436 912389636   1% /mnt/sdh
/dev/sdd1            961432072    204436 912389636   1% /mnt/sdd
```
**以上加粗字体就是新增磁盘。**
+ 开机挂载硬盘
```
手动挂载：
          mount /dev/sdb5 /mnt/ljp1
          mount /dev/sdb6 /mnt/ljp2
自动挂载：
          vi /etc/fstab
          /dev/sda3 /mnt/sda2  ext4     defaults     0 0
```
