##1、nohup命令及其输出文件 

>　　nohup命令：如果你正在运行一个进程，而且你觉得在退出帐户时该进程还不会结束，那么可以使用nohup命令。该命令可以在你退出帐户/关闭终端之后继续运行相应的进程。nohup就是不挂起的意思( n ohang up)。 

---
>该命令的一般形式为：nohup command & 

+ 
```bash
ls xxx 1>out.txt 2>&1 
nohup /mnt/Nand3/H2000G >/dev/null 2>&1 & 
```
>对于& 1 更准确的说应该是文件描述符 1,而1 一般代表的就是`STDOUT_FILENO`,实际上这个操作就是一个`dup2(2)`调用.他标准输出到`all_result`,然后复制标准输出到文件描述符`2(STDERR_FILENO)`,其后果就是文件描述符1和2指向同一个文件表项,也可以说错误的输出被合并了.其中0 表示键盘输入 1表示屏幕输出2表示错误输出.把标准出错重定向到标准输出,然后扔到`/DEV/NULL`下面去。通俗的说，就是把所有标准输出和标准出错都扔到垃圾桶里面。 

> `command >out.file 2>&1 &`

+ 
`command >out.file`是将`command`的输出重定向到`out.file`文件，即输出内容不打印到屏幕上，而是输出到`out.file`文件中。 `2>&1` 是将标准出错重定向到标准输出，这里的标准输出已经重定向到了`out.file`文件，即将标准出错也输出到`out.file`文件中。最后一个`&` ， 是让该命令在后台执行。 


+ 试想`2>1`代表什么，`2`与`>`结合代表错误重定向，而`1`则代表错误重定向到一个文件`1`，而不代表标准输出； 
换成`2>&1`，`&`与`1`结合就代表标准输出了，就变成错误重定向到标准输出. 

+ 你可以用 
    + `ls 2>1`测试一下，不会报没有`2`文件的错误，但会输出一个空的文件`1`； 
    + `ls xxx 2>1`测试，没有`xxx`这个文件的错误输出到了`1`中； 
    + `ls xxx 2>&1`测试，不会生成`1`这个文件了，不过错误跑到标准输出了； 
    + `ls xxx >out.txt 2>&1`, 实际上可换成 `ls xxx 1>out.txt 2>&1`；重定向符号>默认是1,错误和输出都传到`out.txt`了。 
       为何`2>&1`要写在后面？ 


>`command > file 2>&1`

+ 
首先是`command > file`将标准输出重定向到`file`中， `2>&1` 是标准错误拷贝了标准输出的行为，也就是同样被重定向到`file`中，最终结果就是标准输出和错误都被重定向到`file`中。

> `command 2>&1 >file` 

+ 
`2>&1` 标准错误拷贝了标准输出的行为，但此时标准输出还是在终端。>`file` 后输出才被重定向到`file`，但标准错误仍然保持在终端。 

**用`strace`可以看到：**
1. `command > file 2>&1` 
这个命令中实现重定向的关键系统调用序列是： 
`open(file) == 3 `
`dup2(3,1) `
`dup2(1,2) `

2. `command 2>&1 >file `
这个命令中实现重定向的关键系统调用序列是： 
`dup2(1,2) `
`open(file) == 3 `
`dup2(3,1) `

>为什么要用 `/dev/null 2>&1`

1. 
这样的写法.这条命令的意思是将标准输出和错误输出全部重定向到`/dev/null`中,也就是将产生的所有信息丢弃.

2. 
下面我就为大家来说一下, `command > file 2>file`   与`command > file 2>&1` 有什么不同的地方. 

    1. 首先`~command > file 2>file` 的意思是将命令所产生的标准输出信息,和错误的输出信息送到file 中.
    2. `command   > file 2>file` 这样的写法,stdout和stderr都直接送到file中, `file`会被打开两次,这样`stdout`和`stderr`会互相覆盖,这样写相当使用了`FD1`和`FD2`两个同时去抢占`file` 的管道. 
    3. 而`command >file 2>&1` 这条命令就将stdout直接送向`file`, `stderr` 继承了`FD1`管道后,再被送往`file`,此时,`file` 只被打开了一次,也只使用了一个管道`FD1`,它包括了`stdout`和st`derr`的内容. 
    4. 从`IO`效率上,前一条命令的效率要比后面一条的命令效率要低,所以在编写`shell`脚本的时候,较多的时候我们会用`command > file 2>&1` 这样的写法. 