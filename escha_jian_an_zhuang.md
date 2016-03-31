<!-- toc -->
## ES插件

### 插件列表
+ inquisitor   分词
+ paramedic
+ bigdesk
+ kopf
+ segmentspy
+ marvel

**ps: ES-Path /root/opt/elasticsearch-1.7.1**
### 安装`head`
+ 
```bash
bin/plugin install head
```

### 安装`Marvel`
+ 
```bash
bin/plugin install elasticsearch/marvel/latest
```

## ES错误

### 启动Marve错误
```
marvel.agent.exporter: error connecting to [[0:0:0:0:0:0:0:0]:9200] [No route to host]
```
+ 解决方法:
```bash
#vim elasticsearch.yml
#添加
network.host: 192.168.137.107
# http端口
http.port: 9200
http.host: "192.168.137.107"
```

### 找不到`mysql`驱动
+ 驱动包不兼容
    + 更换驱动包
+ 驱动包路径错误
    + 放置在`$JAVA_HOME/jre/lib/ext/`下 