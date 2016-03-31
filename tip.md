<!-- toc -->
# tip

### 启动虚拟机
```bash
# mysql(196.168.1.216)
vmrun -T ws start "/root/vmware/CentOS-mysql/CentOS-mysql.vmx" nogui

启动mysql
[root@Mysql ~]# service mysqld start
启动httpd/bugzilla
[root@Mysql ~]# service httpd start

# svn(196.168.1.224)
vmrun -T ws start "/root/vmware/CentOS-svn-bak/CentOS-mysql.vmx" nogui

启动svn
svnserve -d -r /home/svn/
```

### 终端shell连接数据库乱码

```sql
mysql -uroot -h192.168.1.123 -p --default-character-set=utf8
```

```sql
mysql> show variables like 'char%';
+--------------------------+----------------------------+
| Variable_name            | Value                      |
+--------------------------+----------------------------+
| character_set_client     | latin1                     |
| character_set_connection | latin1                     |
| character_set_database   | latin1                     |
| character_set_filesystem | binary                     |
| character_set_results    | latin1                     |
| character_set_server     | latin1                     |
| character_set_system     | utf8                       |
| character_sets_dir       | /usr/share/mysql/charsets/ |
+--------------------------+----------------------------+
```

### `mysql`备份数据库
+ 
```sql
 mysqldump mailiqing -uroot -h192.168.1.123 -p --default-character-set=utf8 --ignore-table=mailiqing.ti_normal_asphaltpara
```


### 注释
```java

/*----------  -----start----------------*/

/*----------  -----end----------------*/



```





### 测试/正式 打包/解包
```bash
[root@jinzhaoAgent webapps]# tar -zcPvf ../bak/b2b2015-09-28.tar.gz --exclude=b2b/WEB-INF --exclude=b2b/special --exclude=b2b/data --exclude=b2b/images b2b/*

[root@jinzhaoAgent apache-tomcat-7.0.62]# tar -zxvf bak/b2b2015-09-28-119.tar.gz -C webapps


```


#### 本地VirtualBox 服务器目录配置
```
HOSTNAME            IPADDR                TYPE
--------            ------                ----
CentOS-Lvs-Master   192.168.137.108       Lvs-Master
CentOS-Data-01      192.168.137.107       Data-01
CentOS-Web-02       192.168.137.101       Web-02
```
> hosts映射

```
192.168.137.108   CentOS-Lvs-Master
10.0.2.107        CentOS-Data-01
10.0.2.101        CentOS-Web-01
10.0.2.102        CentOS-Web-02
```

> 目录设置

```
CentOS-Data-01
    nfs挂载目录:/usr/data/rpm
```


scp /etc/rc.d/init.d/nginx CentOS-Web-02:/etc/rc.d/init.d/


### `服务器目录配置`

> 软件存放位置

+ `/usr/local/src/rpm`

```
192.168.1.111
192.168.1.112

192.168.1.121       Mlq-Web-Ceshi
192.168.1.123       Mlq-Sql-master  数据库
192.168.1.124       Mlq-Web-R1
    nfs:
        挂载:
        mount -t nfs -o rsize=8192,wsize=8192 192.168.1.126:/usr/mailiqing/data /usr/tomcat/apache-tomcat-7.0.62/webapps/b2b/data
192.168.1.125       Mlq-Web-R2


192.168.1.126       Mlq-Data
    nfs:
        /usr/local/src/rpm 192.168.1.0/24(rw)   软件
        /usr/mailiqing/data 192.168.1.0/24(rw)  data目录
192.168.1.127       Mlq-Sql-slave  数据库
192.168.1.128       Mlq-Web-Zhengshi
```


>host:

```
192.168.1.124   Mlq-Web-R1
192.168.1.125   Mlq-Web-R2
192.168.1.126   Mlq-Data
```

>查看日志

```
tail -f /usr/tomcat/apache-tomcat-7.0.62/logs/catalina.out
```

---


### 正式服务器发布 脚本

+ **deploy.sh**
```bash
#!/bin/bash
IP_ARRAY=("Mlq-Web-R1" "Mlq-Web-R2")
USER="root"
REMOTE_CMD="/home/dishui/scripts/unpack.sh"
DATE=$(date +%Y-%m-%d)
FILE="/tmp/b2b$DATE.tar.gz"
tar -zcPvf $FILE --exclude=/usr/tomcat/apache-tomcat-7.0.62/webapps/b2b/data --exclude=/usr/tomcat/apache-tomcat-7.0.62/webapps/b2b/special /usr/tomcat/apache-tomcat-7.0.62/webapps/b2b/*
echo "测试打包完毕----"
for IP in ${IP_ARRAY[*]}
do
  scp $FILE $IP:/tmp
  echo "$ip--拷贝成功"
  sleep 5
  ssh -t -p 22 $USER@$IP $REMOTE_CMD
done
```
+ **unpack.sh**
```bash
#!/bin/bash
DATE=$(date +%Y-%m-%d)
FILE="/tmp/b2b${DATE}.tar.gz"
tar -zxvf $FILE /
```

### 批量重启`Tomcat`

+ **tomcat.sh**
```bash
#!/bin/bash
IP_ARRAY=("Mlq-Web-R1" "Mlq-Web-R2")
USER="root"
REMOTE_CMD_START="nohup sh /home/dishui/scripts/start-tom.sh>/dev/null 2>&1"
REMOTE_CMD_STOP="nohup sh /home/dishui/scripts/stop-tom.sh>/dev/null 2>&1"
case $1 in
    start | begin)
      for IP in ${IP_ARRAY[*]}
      do
        ssh -t -p 22 $USER@$IP $REMOTE_CMD_START
                echo "$IP--启动成功!"
      done
    ;;
    stop | end)
      for IP in ${IP_ARRAY[*]}
      do
        ssh -t -p 22 $USER@$IP $REMOTE_CMD_STOP
                echo "$IP--关闭成功!"
      done
    ;;
    restart)
      for IP in ${IP_ARRAY[*]}
      do
        ssh -t -p 22 $USER@$IP $REMOTE_CMD_START
                echo "$IP--重启成功!"
      done
    ;;
    *)
        echo "你是不是傻!"
        echo "$0 stop 批量关闭tomcat"
        echo "$0 start 批量关闭tomcat"
        echo "$0 restart 批量重启tomcat"
    ;;
esac
```

### 启动`Tomcat`

+ **start-tom.sh**
```bash
#!/bin/bash
TOM_COUNT=`ps -ef|grep -c tomcat`
ONE=1
TWO=2
if [ $TOM_COUNT -ge $TWO ]; then
    /usr/tomcat/apache-tomcat-7.0.62/bin/shutdown.sh
    sleep 10
    pid=`pgrep java`
    if [ ! $pid ]; then
      echo "$HOSTNAME 正在启动! "
      /usr/tomcat/apache-tomcat-7.0.62/bin/startup.sh
    else
      echo "$HOSTNAME 没有完全杀死!"
      echo "请稍等!!!"
      sleep 3
      kill -9 $pid
      echo "$HOSTNAME 正在启动! "
      /usr/tomcat/apache-tomcat-7.0.62/bin/startup.sh
    fi
else
    echo "$HOSTNAME 正在启动! "
    /usr/tomcat/apache-tomcat-7.0.62/bin/startup.sh
fi
```

### 关闭Tomcat

+ **stop-tom.sh**
```bash
#!/bin/bash
TOM_COUNT=`ps -ef|grep -c tomcat`
ONE=1
TWO=2
if [ $TOM_COUNT -ge $TWO ]; then
    /usr/tomcat/apache-tomcat-7.0.62/bin/shutdown.sh
    sleep 10
    pid=`pgrep java`
    if [ ! $pid ]; then
      echo "$HOSTNAME 已关闭! "
    else
      echo "$HOSTNAME 没有完全杀死!"
      echo "请稍等!!!"
      sleep 3
      kill -9 $pid
      echo "$HOSTNAME 已被完全干掉!"
    fi
else
    echo "$HOSTNAME 已关闭! "
fi
``` 

### 部分文件部署到正式服务器

+ **copy.sh**
```bash
#!/bin/bash
while read LINE
do
  if [ -f $LINE ]; then
    echo "$LINE is 文件"
    scp ${myline} Mlq-Web-R1:${myline%/*} &&\
    scp ${myline} Mlq-Web-R2:${myline%/*}
  elif [ -d $LINE ]; then
    echo "$LINE is 文件夹"
    scp -r ${LINE} Mlq-Web-R1:${LINE} &&\
    scp -r ${LINE} Mlq-Web-R2:${LINE}
  else
    echo "$LINE 不存在"
  fi
done < $1
```

### 备份

+ **backup.sh**
```bash
#!/bin/bash
ip_array=("Mlq-Web-R1" "Mlq-Web-R2")
user="root"
remote_cmd="/home/dishui/scripts/pack.sh"
DATE=$(date +%Y-%m-%d)
for ip in ${ip_array[*]}
do
    ssh -t -p 22 $user@$ip $remote_cmd
done
```

### **脚本使用**

+ 
```bash
#Mlq-Web-R1,Mlq-Web-R2备份到每个机器的当前目录下
sh /home/dishui/scripts/backup.sh
#测试服务器部署到正式服务器
sh /home/dishui/scripts/deploy.sh
#批量操作Tomcat
sh /home/dishui/scripts/tomcat.sh start|stop|restart
#部分文件部署到正式服务器
cd /home/dishui/scripts
vim filelist
#将需要部署的文件地址添加到filelist文件
#例:(每个地址一行)
#/usr/tomcat/apache-tomcat-7.0.62/webapps/b2b/program/company/goods/goodsList.jsp
#/usr/tomcat/apache-tomcat-7.0.62/webapps/b2b/special/111
# i 进入编辑模式, esc 退出编辑模式, :wq 保存退出
sh copy.sh filelist
```

### 本地`mysql`、`svn` 

+ 服务器

| 主机  | 196.168.1.220 |
| -- | -- |
| mysql | 196.168.1.216 |
| svn | 196.168.1.224 |

+  **操作**
    1. 查看虚拟机是否启动
```bash
# vmrun list
Total running VMs: 2
/root/vmware/CentOS-mysql/CentOS-mysql.vmx
/root/vmware/CentOS-svn-bak/CentOS-mysql.vmx
#有以上信息说明,已经启动
```
    + 启动虚拟机
```bash
#ssh 连接 196.168.1.220
#启动216
vmrun -T ws start "/root/vmware/CentOS-mysql/CentOS-mysql.vmx" nogui
#启动224
vmrun -T ws start "/root/vmware/CentOS-svn-bak/CentOS-mysql.vmx" nogui
#重启
vmrun reset "/root/vmware/CentOS-svn-bak/CentOS-mysql.vmx" hard
```
    + 启动`mysql`
```bash
#ssh 登录216
#启动mysql
service mysqld start
```
    + 启动`svn`
```bash
#ssh 登录224
#启动svn
svnserve -d -r /home/svn/
```


