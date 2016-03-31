# maven

#### maven设置
1. **仓库设置 `‪C:\Users\dishui\.m2\settings.xml`**
```xml
    <!--mirrors中添加 -->
	<mirror>
		<id>osc</id>
		<mirrorOf>central</mirrorOf>
		<url>http://maven.oschina.net/content/groups/public/</url>
	</mirror>
	<mirror>
		<id>osc_thirdparty</id>
		<mirrorOf>thirdparty</mirrorOf>
		<url>http://maven.oschina.net/content/repositories/thirdparty/</url>
	</mirror>
```

+ **导出`jar包`到默认目录 `targed/dependency` **

    在eclipse中，选择项目的`pom.xml`文件，
    点击右键菜单中的`RunAs`,
    在弹出的`Configuration`窗口中，
    在`Goals`输入 `dependency:copy-dependencies`后，点击运行
