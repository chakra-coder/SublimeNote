<!-- toc -->
# fedora-setup

### 安装 Fedora 后要做的事
1. **关闭`SELinux`**
```bash
$sudo vi /etc/sysconfig/selinux

 - SELINUX=enforcing
  + SELINUX=disabled
```

+ **修改 host name:**
```bash
$ hostnamectl set-hostname --static "myhostname"
```

+ **启动`ssh`以便能远程访问**
```bash
systemctl start sshd
systemctl enable sshd
```
