<!-- toc -->
# java-snippets

### path
+ 
```
/usr/tomcat/apache-tomcat-7.0.62/webapps/b2b
```

### Es.jsp
+ 
```jsp
<%@page import="com.bizoss.trade.mlq_goods.Mlq_goods"%>
<%@page import="com.bizoss.createIndex.impl.GetSearchAllTest"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	GetSearchAllTest gsat = new GetSearchAllTest();
	List<Mlq_goods> mg_l = gsat.getAll(Mlq_goods.class);
	
	request.setAttribute("mg_l", mg_l);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>领取</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
	<form name="Es" action="Es.jsp" method="post">
		<input type="submit" value="查询"></input>
	</form>
</body>
</html>
```

### 获取当前`class`文件路径

+  
```java
String img_original = mpp.getImg_original();
String path = this.getClass().getClassLoader().getResource("/").getPath();
String[] paths = path.split("/WEB-INF");
//web-path
String web_path = paths[0];
File img = new File(web_path+img_original);
if(!img.exists()){
	mgp.setImg_large("/data/goods_image1/10/1/a0e27f4ce4e84e1286d8832c0f7cdff0.png");
}else{
	mgp.setImg_large(img_original);
}
```