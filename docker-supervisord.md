# docker-supervisord

### 配置文件
+ 
[`supervisor`官方配置文件](http://supervisord.org/configuration.html)

### 启动`es`
```
[program:163gs]
numprocs = 2
numprocs_start = 8850
user = projects
process_name = 163gs-%(process_num)s
directory = /home/projects/163.gs/
#命令
command = /home/projects/163.gs/env/bin/python /home/projects/163.gs/main.py --port=%(process_num)s
#自动重启
autorestart = true
redirect_stderr = true
stdout_logfile = /var/log/supervisor/163gs.log
stderr_logfile = /var/log/supervisor/163gs-error.log
```