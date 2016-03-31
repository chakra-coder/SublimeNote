## Mysql错误

### `MySQL Daemon failed to start.`
+ ```bash
#查看mysql日志
less /var/log/mysqld.log 
```
+ `unknown variable 'default-character-set=utf8'`
```
用character_set_server=utf8来取代 default-character-set=utf8
```

### ` Cannot load from mysql.proc. The table is probably corrupted`
```bash
#
use mysql;
#
ALTER TABLE `proc` MODIFY COLUMN `comment`  text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL AFTER `sql_mode`;
```


