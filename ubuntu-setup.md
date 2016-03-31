# Ubuntu配置

### ip地址配置



### 添加sudoers
+ **sudo**
```bash
sudo vi /etc/sudoers
#添加
#dishui  ALL=(ALL)  ALL
```

### DNS重启后不失效
1. **通过`/etc/network/interfaces`，在它的最后增加一句：**
```
dns-nameservers 114.114.114.114
```
2. **`/etc/resolvconf/resolv.conf.d/base`（这个文件默认是空的）**
```
nameserver 8.8.8.8
nameserver 8.8.4.4
保存后,执行
resolvconf -u
```