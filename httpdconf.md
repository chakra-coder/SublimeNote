<!-- toc -->
### httpd.conf

1. **打开`httpd`配置文件**
```bash
//首先备份一下
/etc/httpd/conf/httpd.conf
```

+ **`apache` 显示文件树** 
```vim
# httpd.conf
<Directory "/var/www/icons">
    Options Indexes MultiViews
    #这个地方是控制是否在浏览器上显示树状目录,把Indexes MultiViews改成MultiViews就不能树状显示目录了。
    AllowOverride None
    Order allow,deny
    Allow from all
</Directory>
```

+  配置虚拟主机
```xml
ServerName  210.14.137.119
<VirtualHost *:7080>
    ServerName 210.14.137.119
    DocumentRoot /var/www/html/
    <Directory /var/www/html/>
         AddHandler cgi-script .cgi
         Options +Indexes +ExecCGI
         DirectoryIndex index.cgi
         AllowOverride Limit FileInfo Indexes
         Order deny,allow
         Allow from all
    </Directory>
    Alias /rpm "/usr/local/src/rpm"
    <Directory /usr/local/src/rpm>
        Options Indexes FollowSymLinks MultiViews
        AllowOverride None
        Order allow,deny
        allow from all
    </Directory>
</VirtualHost>
```