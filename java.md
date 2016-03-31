<!-- toc -->
# java

###引入

1. **文件**
```java
//支持EL表达式
<%@ page isELIgnored="false"%> 
//支持C标签
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
//fn函数
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
/program/company/goods/base_script.jsp
```
+ 日志
```java
private static Logger logger = Logger.getLogger(Clazz clazz);
```

###jsp分页

+ **分页查询**
```java
    Mlq_goods_prop mg = new Mlq_goods_prop();
	Mlq_goodsInfo mgi = new Mlq_goodsInfo();
	//1:Url 
	String url = "goodsList.jsp";
	//2:name='''&isDisplay=1
	StringBuilder params = new StringBuilder();
	//商品列表
		String goods_status = request.getParameter("goods_status");
		if(goods_status!=null && !"".equals(goods_status.trim())){
			mg.setGoods_status(goods_status);
			params.append("&goods_status="+goods_status);
		}
	//每页显示条数
	Integer perPageCount = PageContant.PER_PAGE_COUNT;
	//mysql查询起始页
	String current_page = request.getParameter("pageNo");
	Integer startPage = 1;
	if(current_page!=null){
	    startPage = Pagination.cpn(Integer.parseInt(current_page));
	}
	mg.setStart((startPage-1)*perPageCount);
	mg.setLimit(perPageCount);
	//总条数
	Integer totalCount = totalCount = mgi.getCountByObjMg(mg);
	//商品列表
	List<Mlq_goods_prop> mg_l = mgi.getListByPage(mg);	
	Pagination pagination = new Pagination(startPage,perPageCount,totalCount);
	//页面展示功能
	pagination.pageView(url, params.toString());
	pagination.setList(mg_l);
	request.setAttribute("pagination", pagination);
```

###分页样式

+ 
```jsp
    <c:forEach items="${pagination.pageView }" var="page">
		${page}
	</c:forEach>
```

### 根据ID查询

+ 
```java
    String order_no="";
    if(request.getParameter("order_no")!=null){
        order_no = request.getParameter("order_no");
    }
    //Ti_goodsorderInfo ti_goodsorderInfo=new Ti_goodsorderInfo();
    //Ti_goodsorder tg=ti_goodsorderInfo.getTi_goodsorderByID(order_no);
    request.setAttribute("tg", tg);
```


### Java json 样品

+ 
```json
"{"+
      "'buyer_bank': '1',"+
      "'buyer_bank_no': '1',"+
      "'buyer_name': '1',"+
      "'consignee_contact': '1',"+
      "'goods_brand': '1',"+
      "'goods_name': '1',"+
      "'goods_price': 23.4,"+
      "'id': '1',"+
      "'invoice_amount': 23.4,"+
      "'invoice_goods_num': 23.4,"+
      "'invoice_num': 23.4,"+
      "'invoice_type': '1',"+
      "'kaiju_type': '1',"+
      "'order_no': '1',"+
      "'parent_no': '1',"+
      "'place_of_delivery': '1',"+
      "'seller_name': '1',"+
      "'ship_price': 23.4,"+
      "'taxpayer_number': '1',"+
      "'total_order_amount': 23.4,"+
      "'trade_no': '1',"+
      "'transport_type': '1'"+
"}"
```

### java ajax

+ 
```java
<%@page import="com.bizoss.trade.ti_workflow_item.Ti_workflow_item"%>
<%@page import="com.bizoss.trade.ti_invoice.Ti_invoice"%>
<%@page import="com.bizoss.trade.ti_workflow_item.Ti_workflow_itemInfo"%>
<%@page import="com.bizoss.trade.ti_invoice.Ti_invoiceInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.bizoss.frame.util.PackToList"%>
<%@page import="com.bizoss.trade.ti_order_operator.Ti_order_operatorInfo"%>
<%@page import="com.bizoss.trade.ti_order_operator.Ti_order_operator_prop"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.*"%>
<%	request.setCharacterEncoding("UTF-8") ;
		String order_no = request.getParameter("order_no");
		Ti_invoiceInfo tii = new Ti_invoiceInfo();
		Ti_workflow_itemInfo twii = new Ti_workflow_itemInfo();
		Ti_invoice new_tmp_ti = new Ti_invoice();
		Ti_workflow_item tmp_twi = null;
		//new_tmp_ti.setId("20151127n14Edwi");
		if(!StringUtils.isBlank(order_no)){
			new_tmp_ti.setId(order_no);
			//获取管理工作流的票据
			tmp_twi = tii.getWorkFlowRelTi_invoice(new_tmp_ti);
		}
		JSONObject jo = new JSONObject();
		if(null == tmp_twi){
			//是否已经关联
			jo.put("is_rel", true);
		}else{
			jo.put("is_rel", false);
		}
		//设置返回的头
		response.setContentType("application/Json;charset=utf-8");
		response.getWriter().write(jo.toString());
%>
```

### **JSTL Fn**

+ 
```java
    //退出循环
    <c:set var="flag1" value="true" />
    <c:forEach items="${tolp_l }" var="tolp">
		<c:if test="${tolp.version == status.index && flag1}">
		    //截取字符串
			${fn:substring(tolp.create_date,11,16) }
			<c:set var="flag1" value="false"/>
		</c:if>
	</c:forEach>
```


### 字符替换

+ 
```java
    theString = theString.replace(">", "&gt;");  
    theString = theString.replace("<", "&lt;");  
    theString = theString.replace(" ", "&nbsp;");  
    theString = theString.replace("\"", "&quot;");  
    theString = theString.replace("\'", "&#39;");  
    theString = theString.replace("\\", "\\\\");//对斜线的转义  
    theString = theString.replace("\n", "\\n");  
    theString = theString.replace("\r", "\\r");  
```

