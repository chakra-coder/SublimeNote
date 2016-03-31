<!-- toc -->
# mysql

### linux安装mysql设置
+ `/usr/bin/mysqladmin -u root password '123'`
    + 设置mysql用户名/密码 
+ `mysql`状态/开启/停止
    + `service mysqld status`
    + `service mysqld start`
    + `service mysqld stop`
+ 任意主机以用户root和密码mypwd连接到mysql服务器
```
mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123' WITH GRANT OPTION;
mysql> flush privileges;
```
+ IP为`192.168.1.102`的主机以用户myuser和密码mypwd连接到mysql服务器
```
mysql> GRANT ALL PRIVILEGES ON *.* TO 'myuser'@'192.168.1.102' IDENTIFIED BY 'mypwd' WITH GRANT OPTION;
mysql> flush privileges;
```

### Mysql 备份/还原

1、 导出结构不导出数据

`mysqldump -d 数据库名 -uroot -p > xxx.sql`

2、导出数据不导出结构

`mysqldump -t 数据库名 -uroot -p > xxx.sql`

3、 导出数据和表结构

`mysqldump 数据库名 -uroot -p > xxx.sql`

4、导出特定表的结构

`mysqldump -uroot -p -B数据库名 --table 表名 > xxx.sql`

5、 source 还原

```sql
mysql>source d:\wcnc_db.sql
```
6、windows下导入数据库
```sql
mysql -uroot -p111111 -Dmailiqing<c:\Users\dishui\Desktop\tmp\mailiqing2016-01-20.sql
```
### mysql数据库操作
1. **创建数据库(编码utf8)**
```bash
CREATE DATABASE `mailiqing` CHARACTER SET utf8; 
```

### 终端shell连接数据库乱码

```sql
mysql -uroot -h192.168.1.123 -p --default-character-set=utf8
```

### 数据库编码
1. 查看mysql编码
```bash
show variables like 'character%';
+--------------------------+----------------------------+
| Variable_name            | Value                      |
+--------------------------+----------------------------+
| character_set_client     | utf8                       |
| character_set_connection | utf8                       |
| character_set_database   | utf8                       |
| character_set_filesystem | binary                     |
| character_set_results    | utf8                       |
| character_set_server     | utf8                       |
| character_set_system     | utf8                       |
| character_sets_dir       | /usr/share/mysql/charsets/ |
+--------------------------+----------------------------+
--查看数据库编码
SHOW VARIABLES LIKE 'character_set_database'; 
```

### `mysql`修改密码
1. **关闭`mysql`服务**
```bash
service mysqld stop
```
+ **安全方式连接`mysql`**
```bash
mysqld_safe --skip-grant-tables
```
+ **进入数据库**
```bash
mysql
#
use mysql;
#
select * from user;                     
    Empty set (0.00 sec)
#
INSERT INTO mysql.user (Host,User,Password) VALUES('%','root', PASSWORD('luhua123'));
#
update user set password=password('luhua123') where user='root'; 
#
flush privileges;
#
exit;
```
+ **杀死`mysql`进程**
```bash
service mysqld stop
```

### mysql 新增、删除用户和权限分配

1. 新增用户
```sql
--
insert into mysql.user(Host,User,Password) values("localhost","lionbule",password("hello1234"));
--
flush privileges;
```
2. 修改用户密码
```sql
--
update mysql.user set password=password('new password') where User="lionbule" and Host="localhost";
--
flush privileges;
```
3. 删除用户
```sql
--
DELETE FROM user WHERE User="lionbule" and Host="localhost";
--
flush privileges;
```
4. 权限分配
    5. grant用法
```
权限：
    常用总结, ALL/ALTER/CREATE/DROP/SELECT/UPDATE/DELETE
数据库：
     *.*                    表示所有库的所有表
     test.*                表示test库的所有表
     test.test_table  表示test库的test_table表     
用户名：
     mysql账户名
登陆主机：
     允许登陆mysql server的客户端ip
     '%'表示所有ip
     'localhost' 表示本机
     '192.168.10.2' 特定IP
密码：
      账户对应的登陆密码
```
    + 例子
```sql
grant all  on test.* to lionbule@'%' identified by 'hello1234';
flush privileges;
```

### 存储过程
1. 查看存储过程
```sql
SHOW CREATE PROCEDURE `mailiqing`.`product_call`; 
```

### 查看
1. 数据库编码
```sql
SHOW VARIABLES LIKE 'character_set_database';
```
2. 改变数据库编码
```sql
ALTER DATABASE `mailiqing` CHARACTER SET utf8; 
```

### `mysql`备份数据库
+ 
```sql
 mysqldump mailiqing -uroot -h192.168.1.123 -p --default-character-set=utf8 --ignore-table=mailiqing.ti_normal_asphaltpara > data.sql
```
