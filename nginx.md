# nginx

平台为：rhel 6.4/CentOS-6.5 32bit

### 一、安装Nginx：

#### 1、解决依赖关系
```bash
# 方法1
yum groupinstall "Development Tools" "Server Platform Deveopment"
yum install openssl-devel pcre-devel

# 方法2
#安装make
yum -y install gcc automake autoconf libtool make

#安装g++
yum -y install gcc gcc-c++
#安装pcre
yum install openssl-devel pcre-devel
```

#### 2、安装

+ 首先添加用户`nginx`，实现以之运行nginx服务进程：
```bash
# groupadd -r nginx
# useradd -r -g nginx nginx
```

+ 接着开始编译和安装：
```bash
# ./configure \
  --prefix=/usr \
  --sbin-path=/usr/sbin/nginx \
  --conf-path=/etc/nginx/nginx.conf \
  --error-log-path=/var/log/nginx/error.log \
  --http-log-path=/var/log/nginx/access.log \
  --pid-path=/var/run/nginx/nginx.pid  \
  --lock-path=/var/lock/nginx.lock \
  --user=nginx \
  --group=nginx \
  --with-http_ssl_module \
  --with-http_flv_module \
  --with-http_stub_status_module \
  --with-http_gzip_static_module \
  --http-client-body-temp-path=/var/tmp/nginx/client/ \
  --http-proxy-temp-path=/var/tmp/nginx/proxy/ \
  --http-fastcgi-temp-path=/var/tmp/nginx/fcgi/ \
  --http-uwsgi-temp-path=/var/tmp/nginx/uwsgi \
  --http-scgi-temp-path=/var/tmp/nginx/scgi \
  --with-pcre
# make && make install
```

+ __说明：__
1. Nginx可以使用Tmalloc(快速、多线程的malloc库及优秀性能分析工具)来加速内存分配，使用此功能需要事先安装gperftools，而后在编译nginx添加--with-google_perftools_module选项即可。
2. 如果想使用nginx的perl模块，可以通过为configure脚本添加--with-http_perl_module选项来实现，但目前此模块仍处于实验性使用阶段，可能会在运行中出现意外，因此，其实现方式这里不再介绍。如果想使用基于nginx的cgi功能，也可以基于FCGI来实现，具体实现方法请参照网上的文档。

3. 为nginx提供SysV init脚本:

+ 新建文件/etc/rc.d/init.d/nginx，内容如下：

```bash
#!/bin/sh
#
# nginx - this script starts and stops the nginx daemon
#
# chkconfig:   - 85 15 
# description:  Nginx is an HTTP(S) server, HTTP(S) reverse \
#               proxy and IMAP/POP3 proxy server
# processname: nginx
# config:      /etc/nginx/nginx.conf
# config:      /etc/sysconfig/nginx
# pidfile:     /var/run/nginx.pid
 
# Source function library.
. /etc/rc.d/init.d/functions
 
# Source networking configuration.
. /etc/sysconfig/network
 
# Check that networking is up.
[ "$NETWORKING" = "no" ] && exit 0
 
nginx="/usr/sbin/nginx"
prog=$(basename $nginx)
 
NGINX_CONF_FILE="/etc/nginx/nginx.conf"
 
[ -f /etc/sysconfig/nginx ] && . /etc/sysconfig/nginx
 
lockfile=/var/lock/subsys/nginx
 
make_dirs() {
   # make required directories
   user=`nginx -V 2>&1 | grep "configure arguments:" | sed 's/[^*]*--user=\([^ ]*\).*/\1/g' -`
   options=`$nginx -V 2>&1 | grep 'configure arguments:'`
   for opt in $options; do
       if [ `echo $opt | grep '.*-temp-path'` ]; then
           value=`echo $opt | cut -d "=" -f 2`
           if [ ! -d "$value" ]; then
               # echo "creating" $value
               mkdir -p $value && chown -R $user $value
           fi
       fi
   done
}
 
start() {
    [ -x $nginx ] || exit 5
    [ -f $NGINX_CONF_FILE ] || exit 6
    make_dirs
    echo -n $"Starting $prog: "
    daemon $nginx -c $NGINX_CONF_FILE
    retval=$?
    echo
    [ $retval -eq 0 ] && touch $lockfile
    return $retval
}
 
stop() {
    echo -n $"Stopping $prog: "
    killproc $prog -QUIT
    retval=$?
    echo
    [ $retval -eq 0 ] && rm -f $lockfile
    return $retval
}
 
restart() {
    configtest || return $?
    stop
    sleep 1
    start
}
 
reload() {
    configtest || return $?
    echo -n $"Reloading $prog: "
    killproc $nginx -HUP
    RETVAL=$?
    echo
}
 
force_reload() {
    restart
}
 
configtest() {
  $nginx -t -c $NGINX_CONF_FILE
}
 
rh_status() {
    status $prog
}
 
rh_status_q() {
    rh_status >/dev/null 2>&1
}
 
case "$1" in
    start)
        rh_status_q && exit 0
        $1
        ;;
    stop)
        rh_status_q || exit 0
        $1
        ;;
    restart|configtest)
        $1
        ;;
    reload)
        rh_status_q || exit 7
        $1
        ;;
    force-reload)
        force_reload
        ;;
    status)
        rh_status
        ;;
    condrestart|try-restart)
        rh_status_q || exit 0
            ;;
    *)
        echo $"Usage: $0 {start|stop|status|restart|condrestart|try-restart|reload|force-reload|configtest}"
        exit 2
esac
```

+ 而后为此脚本赋予执行权限：

```bash
# chmod +x /etc/rc.d/init.d/nginx
```

+ 添加至服务管理列表，并让其开机自动启动：
```bash
# chkconfig --add nginx
# chkconfig nginx on
```

+ 而后就可以启动服务并测试了：
```bash
# service nginx start
```


### 二、配置Nginx

  Nginx的代码是由一个核心和一系列的模块组成, 核心主要用于提供Web Server的基本功能，以及Web和Mail反向代理的功能；还用于启用网络协议，创建必要的运行时环境以及确保不同的模块之间平滑地进行交互。不过，大多跟协议相关的功能和某应用特有的功能都是由nginx的模块实现的。这些功能模块大致可以分为事件模块、阶段性处理器、输出过滤器、变量处理器、协议、upstream和负载均衡几个类别，这些共同组成了nginx的http功能。事件模块主要用于提供OS独立的(不同操作系统的事件机制有所不同)事件通知机制如kqueue或epoll等。协议模块则负责实现nginx通过http、tls/ssl、smtp、pop3以及imap与对应的客户端建立会话。

  Nginx的核心模块为Main和Events，此外还包括标准HTTP模块、可选HTTP模块和邮件模块，其还可以支持诸多第三方模块。Main用于配置错误日志、进程及权限等相关的参数，Events用于配置IO模型，如epoll、kqueue、select或poll等，它们是必备模块。

  Nginx的主配置文件由几个段组成，这个段通常也被称为nginx的上下文，每个段的定义格式如下所示。需要注意的是，其每一个指令都必须使用分号(;)结束，否则为语法错误。

```
<section> {
    <directive> <parameters>;
}
```

#### 2.1 配置main模块

下面说明main模块中的几个关键参数。

##### 2.1.1 error_log
```
用于配置错误日志，可用于main、http、server及location上下文中；语法格式为：

error_log file | stderr [ debug | info | notice | warn | error | crit | alert | emerg ]

如果在编译nginx时使用了--with-debug选项，还可以使用如下格式打开调试功能。

error_log LOGFILE [debug_core | debug_alloc | debug_mutex | debug_event | debug_http | debug_imap];

要禁用错误日志，不能使用“error_log off;”，而要使用类似如下选项：

error_log /dev/null crit;
```
##### 2.1.2 timer_resolution
```
用于降低gettimeofday()系统调用的次数。默认情况下，每次从kevent()、epoll、/dev/poll、select()或poll()返回时都会执行此系统调用。语法格式为：

timer_resolution interval

例如：

timer_resolution  100ms;

2.1.3 worker_cpu_affinity

通过sched_setaffinity()将worker绑定至CPU上，只能用于main上下文。语法格式为：

worker_cpu_affinity cpumask ...

例如：
worker_processes     4;
worker_cpu_affinity 0001 0010 0100 1000;
```

##### 2.1.4 worker_priority
```
为worker进程设定优先级(指定nice值)，此参数只能用于main上下文中，默认为0；语法格式为：

worker_priority number
```

##### 2.1.5 worker_processes
```
worker进程是单线程进程。如果Nginx用于CPU密集型的场景中，如SSL或gzip，且主机上的CPU个数至少有2个，那么应该将此参数值设定为与CPU核心数相同；如果Nginx用于大量静态文件访问的场景中，且所有文件的总大小大于可用内存时，应该将此参数的值设定得足够大以充分利用磁盘带宽。

此参数与Events上下文中的work_connections变量一起决定了maxclient的值：
maxclients = work_processes * work_connections
```

##### 2.1.6 worker_rlimit_nofile
```
设定worker进程所能够打开的文件描述符个数的最大值。语法格式：

worker_rlimit_nofile number
```

### 2.2 配置Events模块

##### 2.2.1 worker_connections
```
设定每个worker所处理的最大连接数，它与来自main上下文的worker_processes一起决定了maxclients的值。

max clients = worker_processes * worker_connections

而在反向代理场景中，其计算方法与上述公式不同，因为默认情况下浏览器将打开2个连接，而nginx会为每一个连接打开2个文件描述符，因此，其maxclients的计算方法为：

max clients = worker_processes * worker_connections/4
```
##### 2.2.2 use
```
在有着多于一个的事件模型IO的应用场景中，可以使用此指令设定nginx所使用的IO机制，默认为./configure脚本选定的各机制中最适用当前OS的版本。语法格式：

use [ kqueue | rtsig | epoll | /dev/poll | select | poll | eventport ]

```
##### 2.3 一个配置示例
```bash
user nginx;
# the load is CPU-bound and we have 16 cores
worker_processes 16;
error_log /var/log/nginx/error.log;
pid /var/run/nginx.pid;

events {
    use epoll;
    worker_connections 2048;
}
```
###### 2.4 HTTP服务的相关配置
```
http上下文专用于配置用于http的各模块，此类指令非常的多，每个模块都有其专用指定，具体请参数nginx官方wiki关于模块部分的说明。大体上来讲，这些模块所提供的配置指令还可以分为如下几个类别。

客户端类指令：如client_body_buffer_size、client_header_buffer_size、client_header_timeout和keepalive_timeout等；
文件IO类指令：如aio、directio、open_file_cache、open_file_cache_min_uses、open_file_cache_valid和sendfile等；
hash类指令：用于定义Nginx为某特定的变量分配多大的内存空间，如types_hash_bucket_size、server_names_hash_bucket_size和variables_hash_bucket_size等；
套接字类指令：用于定义Nginx如何处理tcp套接字相关的功能，如tcp_nodelay(用于keepalive功能启用时)和tcp_nopush(用于sendfile启用时)等；
```
###### 2.5 虚拟服务器相关配置
```
server {
    <directive> <parameters>;
}
用于定义虚拟服务器相关的属性，常见的指令有backlog、rcvbuf、bind及sndbuf等。
```

###### 2.6 location相关的配置
```
location [modifier] uri {...} 或 location @name {…}

通常用于server上下文中，用于设定某URI的访问属性。location可以嵌套。

The prefix "@" specifies a named location. Such locations are not used during normal processing of requests, they are intended only to process internally redirected requests (see error_page, try_files). 如下面关于memcached的相关配置。

server {
  location / {
    set $memcached_key $uri;
    memcached_pass     name:11211;
    default_type       text/html;
    error_page         404 @fallback;
  }
 
  location @fallback {
    proxy_pass http://backend;
  }
}
```
### 三、Nginx反向代理

    Nginx通过proxy模块实现反向代理功能。在作为web反向代理服务器时，nginx负责接收客户请求，并能够根据URI、客户端参数或其它的处理逻辑将用户请求调度至上游服务器上(upstream server)。nginx在实现反向代理功能时的最重要指令为proxy_pass，它能够将location定义的某URI代理至指定的上游服务器(组)上。如下面的示例中，location的/uri将被替换为上游服务器上的/newuri。

    location /uri {
        proxy_pass http://www.magedu.com:8080/newuri;
    }
    

不过，这种处理机制中有两个例外。一个是如果location的URI是通过模式匹配定义的，其URI将直接被传递至上游服务器，而不能为其指定转换的另一个URI。例如下面示例中的/forum将被代理为http://www.magedu.com/forum。

    location ~ ^/bbs {
        proxy_pass http://www.magedu.com;
    }

第二个例外是，如果在loation中使用的URL重定向，那么nginx将使用重定向后的URI处理请求，而不再考虑上游服务器上定义的URI。如下面所示的例子中，传送给上游服务器的URI为/index.php?page=<match>，而不是/index。

    location / {
        rewrite /(.*)$ /index.php?page=$1 break;
        proxy_pass http://localhost:8080/index;
    }

3.1 proxy模块的指令

proxy模块的可用配置指令非常多，它们分别用于定义proxy模块工作时的诸多属性，如连接超时时长、代理时使用http协议版本等。下面对常用的指令做一个简单说明。

proxy_connect_timeout：nginx将一个请求发送至upstream server之前等待的最大时长；
proxy_cookie_domain：将upstream server通过Set-Cookie首部设定的domain属性修改为指定的值，其值可以为一个字符串、正则表达式的模式或一个引用的变量；
proxy_cookie_path: 将upstream server通过Set-Cookie首部设定的path属性修改为指定的值，其值可以为一个字符串、正则表达式的模式或一个引用的变量；
proxy_hide_header：设定发送给客户端的报文中需要隐藏的首部；
proxy_pass：指定将请求代理至upstream server的URL路径；
proxy_set_header：将发送至upsream server的报文的某首部进行重写；
proxy_redirect：重写location并刷新从upstream server收到的报文的首部；
proxy_send_timeout：在连接断开之前两次发送至upstream server的写操作的最大间隔时长；
proxy_read_timeout：在连接断开之前两次从接收upstream server接收读操作的最大间隔时长；

如下面的一个示例：
    proxy_redirect off;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    client_max_body_size 10m;
    client_body_buffer_size 128k;
    proxy_connect_timeout 30;
    proxy_send_timeout 15;
    proxy_read_timeout 15;

3.2 upstream模块

与proxy模块结合使用的模块中，最常用的当属upstream模块。upstream模块可定义一个新的上下文，它包含了一组宝岛upstream服务器，这些服务器可能被赋予了不同的权重、不同的类型甚至可以基于维护等原因被标记为down。

upstream模块常用的指令有：
ip_hash：基于客户端IP地址完成请求的分发，它可以保证来自于同一个客户端的请求始终被转发至同一个upstream服务器；
keepalive：每个worker进程为发送到upstream服务器的连接所缓存的个数；
least_conn：最少连接调度算法；
server：定义一个upstream服务器的地址，还可包括一系列可选参数，如：
    weight：权重；
    max_fails：最大失败连接次数，失败连接的超时时长由fail_timeout指定；
    fail_timeout：等待请求的目标服务器发送响应的时长；
    backup：用于fallback的目的，所有服务均故障时才启动此服务器；
    down：手动标记其不再处理任何请求；

例如：
    upstream backend {
      server www.magedu.com weight=5;
      server www2.magedu.com:8080       max_fails=3  fail_timeout=30s;
    }

upstream模块的负载均衡算法主要有三种，轮调(round-robin)、ip哈希(ip_hash)和最少连接(least_conn)三种。

此外，upstream模块也能为非http类的应用实现负载均衡，如下面的示例定义了nginx为memcached服务实现负载均衡之目的。

    upstream memcachesrvs {
        server 172.16.100.6:11211;
        server 172.16.100.7:11211;
    }
    
    server {
        location / {
        set $memcached_key "$uri?$args";
        memcached_pass memcachesrvs;
        error_page 404 = @fallback;
        }
    
        location @fallback {
             proxy_pass http://127.0.0.1:8080;
        }
    }

3.3 if判断语句

在location中使用if语句可以实现条件判断，其通常有一个return语句，且一般与有着last或break标记的rewrite规则一同使用。但其也可以按需要使用在多种场景下，需要注意的是，不当的使用可能会导致不可预料的后果。

location / {
    if ($request_method == “PUT”) {
        proxy_pass http://upload.magedu.com:8080;
    } 

    if ($request_uri ~ "\.(jpg|gif|jpeg|png)$") {
        proxy_pass http://imageservers;
        break;
    }
}

upstream imageservers {
    server 172.16.100.8:80 weight 2;
    server 172.16.100.9:80 weight 3;
}

3.3.1 if语句中的判断条件

正则表达式匹配：
    ~：与指定正则表达式模式匹配时返回“真”，判断匹配与否时区分字符大小写；
    ~*：与指定正则表达式模式匹配时返回“真”，判断匹配与否时不区分字符大小写；
    !~：与指定正则表达式模式不匹配时返回“真”，判断匹配与否时区分字符大小写；
    !~*：与指定正则表达式模式不匹配时返回“真”，判断匹配与否时不区分字符大小写；

文件及目录匹配判断：
    -f, !-f：判断指定的路径是否为存在且为文件；
    -d, !-d：判断指定的路径是否为存在且为目录；
    -e, !-e：判断指定的路径是否存在，文件或目录均可；
    -x, !-x：判断指定路径的文件是否存在且可执行；

3.3.2 nginx常用的全局变量

下面是nginx常用的全局变量中的一部分，它们经常用于if语句中实现条件判断。

$args 
$content_length
$content_type 
$document_root 
$document_uri 
$host 
$http_user_agent 
$http_cookie 
$limit_rate 
$request_body_file 
$request_method 
$remote_addr 
$remote_port 
$remote_user 
$request_filename 
$request_uri 
$query_string 
$scheme
$server_protocol 
$server_addr 
$server_name 
$server_port 
$uri

四、反向代理性能优化

在反向代理场景中，nginx有一系列指令可用于定义其工作特性，如缓冲区大小等，给这些指令设定一个合理的值，可以有效提升其性能。

4.1 缓冲区设定

nginx在默认情况下在将其响应给客户端之前会尽可能地接收来upstream服务器的响应报文，它会将这些响应报文存暂存于本地并尽量一次性地响应给客户端。然而，在来自于客户端的请求或来自upsteam服务器的响应过多时，nginx会试图将之存储于本地磁盘中，这将大大降低nginx的性能。因此，在有着更多可用内存的场景中，应该将用于暂存这些报文的缓冲区调大至一个合理的值。

proxy_buffer_size size：设定用于暂存来自于upsteam服务器的第一个响应报文的缓冲区大小；
proxy_buffering on|off：启用缓冲upstream服务器的响应报文，否则，如果proxy_max_temp_file_size指令的值为0，来自upstream服务器的响应报文在接收到的那一刻将同步发送至客户端；一般情况下，启用proxy_buffering并将proxy_max_temp_file_size设定为0能够启用缓存响应报文的功能，并能够避免将其缓存至磁盘中；
proxy_buffers 8 4k|8k：用于缓冲来自upstream服务器的响应报文的缓冲区大小；



4.2 缓存

nginx做为反向代理时，能够将来自upstream的响应缓存至本地，并在后续的客户端请求同样内容时直接从本地构造响应报文。

proxy_cache zone|off：定义一个用于缓存的共享内存区域，其可被多个地方调用；缓存将遵从upstream服务器的响应报文首部中关于缓存的设定，如 "Expires"、"Cache-Control: no-cache"、 "Cache-Control: max-age=XXX"、"private"和"no-store" 等，但nginx在缓存时不会考虑响应报文的"Vary"首部。为了确保私有信息不被缓存，所有关于用户的私有信息可以upstream上通过"no-cache" or "max-age=0"来实现，也可在nginx设定proxy_cache_key必须包含用户特有数据如$cookie_xxx的方式实现，但最后这种方式在公共缓存上使用可能会有风险。因此，在响应报文中含有以下首部或指定标志的报文将不会被缓存。
    Set-Cookie
    Cache-Control containing "no-cache", "no-store", "private", or a "max-age" with a non-numeric or 0 value
    Expires with a time in the past
    X-Accel-Expires: 0
proxy_cache_key：设定在存储及检索缓存时用于“键”的字符串，可以使用变量为其值，但使用不当时有可能会为同一个内容缓存多次；另外，将用户私有信息用于键可以避免将用户的私有信息返回给其它用户；
proxy_cache_lock：启用此项，可在缓存未命令中阻止多个相同的请求同时发往upstream，其生效范围为worker级别；
proxy_cache_lock_timeout：proxy_cache_lock功能的锁定时长；
proxy_cache_min_uses：某响应报文被缓存之前至少应该被请求的次数；
proxy_cache_path：定义一个用记保存缓存响应报文的目录，及一个保存缓存对象的键及响应元数据的共享内存区域(keys_zone=name:size)，其可选参数有：
    levels：每级子目录名称的长度，有效值为1或2，每级之间使用冒号分隔，最多为3级；
    inactive：非活动缓存项从缓存中剔除之前的最大缓存时长；
    max_size：缓存空间大小的上限，当需要缓存的对象超出此空间限定时，缓存管理器将基于LRU算法对其进行清理；
    loader_files：缓存加载器(cache_loader)的每次工作过程最多为多少个文件加载元数据；
    loader_sleep：缓存加载器的每次迭代工作之后的睡眠时长；
    loader_threashold：缓存加载器的最大睡眠时长；
    例如：  proxy_cache_path  /data/nginx/cache/one    levels=1      keys_zone=one:10m;
            proxy_cache_path  /data/nginx/cache/two    levels=2:2    keys_zone=two:100m;
            proxy_cache_path  /data/nginx/cache/three  levels=1:1:2  keys_zone=three:1000m;
proxy_cache_use_stale：在无法联系到upstream服务器时的哪种情形下(如error、timeout或http_500等)让nginx使用本地缓存的过期的缓存对象直接响应客户端请求；其格式为：
    proxy_cache_use_stale error | timeout | invalid_header | updating | http_500 | http_502 | http_503 | http_504 | http_404 | off 
proxy_cache_valid [ code ...] time：用于为不同的响应设定不同时长的有效缓存时长，例如：proxy_cache_valid  200 302  10m;
proxy_cache_methods [GET HEAD POST]：为哪些请求方法启用缓存功能；
proxy_cache_bypass string：设定在哪种情形下，nginx将不从缓存中取数据；例如：
     proxy_cache_bypass $cookie_nocache $arg_nocache $arg_comment;
    proxy_cache_bypass $http_pragma $http_authorization;

4.2.1 使用示例

http {
    proxy_cache_path  /data/nginx/cache  levels=1:2    keys_zone=STATIC:10m
                                         inactive=24h  max_size=1g;
    server {
        location / {
            proxy_pass             http://www.magedu.com;
            proxy_set_header       Host $host;
            proxy_cache            STATIC;
            proxy_cache_valid      200  1d;
            proxy_cache_valid      301 302 10m;
            proxy_cache_vaild      any 1m;
            proxy_cache_use_stale  error timeout invalid_header updating
                                   http_500 http_502 http_503 http_504;
        }
    }
}


4.3 压缩

nginx将响应报文发送至客户端之前可以启用压缩功能，这能够有效地节约带宽，并提高响应至客户端的速度。通常编译nginx默认会附带gzip压缩的功能，因此，可以直接启用之。

http {
    gzip on;
    gzip_http_version 1.0;
    gzip_comp_level 2;
    gzip_types text/plain text/css application/x-javascript text/xml application/xml application/xml+rss text/javascript application/javascript application/json;
    gzip_disable msie6;
}

gzip_proxied指令可以定义对客户端请求哪类对象启用压缩功能，如“expired”表示对由于使用了expire首部定义而无法缓存的对象启用压缩功能，其它可接受的值还有“no-cache”、“no-store”、“private”、“no_last_modified”、“no_etag”和“auth”等，而“off”则表示关闭压缩功能。





五、配置示例

5.1 反向代理

server {
        listen       80;
        server_name  www.magedu.com;
        add_header X-Via $server_addr;  

        location / {
            root   html;
            index  index.html index.htm;
            if ($request_method ~* "PUT") {
                proxy_pass http://172.16.100.12;
                break;
            }
        }

        location /bbs {
            proxy_pass http://172.16.100.11/;
        }
} 

此例中，对http://www.magedu.com/bbs/的请求将被转发至http://172.16.100.11/这个URL，切记最后的/不应该省去；而/匹配的URL中请求方法为“PUT”时，将被转发至http://172.16.100.12/这个URL。

另外，add_header用于让nginx在响应给用户的报文中构造自定义首部，其使用格式为“add_header NAME VALUE”。

可以使用curl命令对配置好的服务进行请求，以验正其效果。如：
# curl -I http://www.magedu.com/bbs/
HTTP/1.1 200 OK
Server: nginx/1.4.1
Date: Tue, 14 May 2013 10:19:10 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 15
Connection: keep-alive
Last-Modified: Tue, 30 Apr 2013 09:34:09 GMT
ETag: "186e9f-f-b4076640"
X-Via: 172.16.100.107
Accept-Ranges: bytes


在后端服务器172.16.100.12上装载dav模块，并开放其dav功能，而后验正文件上传效果。开放dav功能的方法如下：

首先启用如下两个模块：
LoadModule dav_module modules/mod_dav.so
LoadModule dav_fs_module modules/mod_dav_fs.so

而后配置相应主机的目录如下所示，关键是其中的dav一行。
<Directory "/var/www/html">
    dav on
    Options Indexes FollowSymLinks
    Order allow,deny
    Allow from all
</Directory>

接着尝试访问代理服务器：
# curl -I -T /etc/inittab http://www.magedu.com/
HTTP/1.1 100 Continue

HTTP/1.1 201 Created
Server: nginx/1.4.1
Date: Tue, 14 May 2013 10:20:15 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 261
Location: http://172.16.100.107/inittab
Connection: keep-alive
X-Via: 172.16.100.107

<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html><head>
<title>201 Created</title>
</head><body>
<h1>Created</h1>
<p>Resource /inittab has been created.</p>
<hr />
<address>Apache/2.2.3 (Red Hat) Server at 172.16.100.12 Port 80</address>
</body></html>


5.2 启用缓存
http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;

    proxy_cache_path /nginx/cache/first  levels=1:2   keys_zone=first:10m max_size=512m;

    server {
        listen       80;
        server_name  www.magedu.com;

        location / {
            root   html;
            index  index.html index.htm;
            if ($request_method ~* "PUT") {
                proxy_pass http://172.16.100.12;
                break;
            }
        }

        location /bbs {
            proxy_pass http://172.16.100.11/;
            proxy_cache first;
            proxy_cache_valid 200 1d;
            proxy_cache_valid 301 302 10m;
            proxy_cache_valid any 1m;
        }
    } 
}


5.3 使用upstream

5.3.1 不启用缓存

http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;
 
    upstream websrv {
        server 172.16.100.11 weight=1;
        server 172.16.100.12 weight=1;
        server 127.0.0.1:8080 backup;
    }
    server {
        listen       80;
        server_name  www.magedu.com;

        add_header X-Via $server_addr;

        location / {
            proxy_pass http://websrv;
            index  index.html index.htm;

            if ($request_method ~* "PUT") {
                proxy_pass http://172.16.100.12;
                break;
            }
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }

    server {
        listen 8080;
        server_name localhost;
        root /nginx/htdocs;
        index index.html;
    }
}

测试效果：默认情况下，nginx对定义了权重的upstream服务器使用加权轮调的方法调度访问，因此，其多次访问应该由不同的服务器进行响应。如下所示。

# curl  http://172.16.100.107/
RS2.magedu.com

# curl  http://172.16.100.107/
RS1.magedu.com

根据上面的配置，如果172.16.100.11和172.16.100.12两个upstream服务器均宕机时，将由本地监听在8080端口的虚拟主机进行响应。
# curl  http://172.16.100.107/
Sorry...


5.3.2 为upstream启用缓存

http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;
 
    proxy_cache_path /nginx/cache/first  levels=1:2   keys_zone=first:10m max_size=512m;
 
    upstream websrv {
        server 172.16.100.11 weight=1;
        server 172.16.100.12 weight=1;
        server 127.0.0.1:8080 backup;
    }
    server {
        listen       80;
        server_name  www.magedu.com;

        add_header X-Via $server_addr;
        add_header X-Cache-Status $upstream_cache_status;

        location / {
            proxy_pass http://websrv;
            proxy_cache first;
            proxy_cache_valid 200 1d;
            proxy_cache_valid 301 302 10m;
            proxy_cache_valid any 1m;
            index  index.html index.htm;

            if ($request_method ~* "PUT") {
                proxy_pass http://172.16.100.12;
                break;
            }
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }

    server {
        listen 8080;
        server_name localhost;
        root /nginx/htdocs;
        index index.html;
    }
}


第一次访问某可缓存资源时，在本地缓存中尚未有其对应的缓存对象，因此，其一定为未命中状态。而第二次请求时，则可以直接从本地缓存构建响应报文。
# curl -I http://www.magedu.com/
HTTP/1.1 200 OK
Server: nginx/1.4.1
Date: Tue, 14 May 2013 10:53:07 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 15
Connection: keep-alive
Last-Modified: Tue, 30 Apr 2013 09:34:09 GMT
ETag: "186e9f-f-b4076640"
Accept-Ranges: bytes
X-Via: 172.16.100.107
X-Cache-Status: MISS

# curl -I http://www.magedu.com/
HTTP/1.1 200 OK
Server: nginx/1.4.1
Date: Tue, 14 May 2013 10:53:09 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 15
Connection: keep-alive
Last-Modified: Tue, 30 Apr 2013 09:34:09 GMT
ETag: "186e9f-f-b4076640"
X-Via: 172.16.100.107
X-Cache-Status: HIT
Accept-Ranges: bytes
