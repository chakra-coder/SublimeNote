<!-- toc -->
# Linux basic

### 命令历史的使用技巧：
+ `!n`:执行命令历史中的第n条命令；
+ `!-n`:执行命令历史中的倒数第n条命令； 
+ `!!`: 执行上一条命令；
+ `!string`:执行命令历史中最近一个以指定字符串开头的命令
+ `!$`:引用前一个命令的最后一个参数; 
+ `Esc, `.
+ `Alt+.`	

### 文件
1. 查看文件夹大小
```bash
du -sh docker-training/ 
```

### 获取`ip`地址
```bash
ifconfig|grep "inet addr:"|grep -v "127.0.0.1"|cut -d: -f2|awk '{print $1}'
```

### 端口查看
1. `linux` 查询某个端口被什么进程占用的命令
```bash
lsof -i|grep 80 # 端口号即可获取进程号
```
2. `linux`查询进程占用哪些端口
```bash
netstat -nlap
```