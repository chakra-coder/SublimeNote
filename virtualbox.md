<!-- toc -->
# VirtualBox Tip

### VBox中出现UUID have already exists解决方法

+ 进入到`VBoxManage.exe`所在目录
    ```bash
    # 参数:虚拟vdi硬盘路径
    VBoxManage.exe internalcommands sethduuid G:\VirtualMachine\CentOS-WEB\CentOS-WEB-01.vdi
    ```

### 通信问题
+ `host-only`模式下
    - 主机`ping`通虚拟机  √
    - 虚拟机`ping`主机 ✘
```
关闭win10防火墙
```

### 修改硬盘大小

```bash
#7d91f211-d3e4-4185-bd9a-314c9b39856d
#修改成40G
VBoxManage modifyhd uuid –-resize 40960
```