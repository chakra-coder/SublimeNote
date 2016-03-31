<!-- toc -->
# JSUtils

### JS绑定模板

```javascript
/*----------  -----start----------------*/
    $(document).ready(function(){
        $("#f_btn_id").each(function(){
            $(this).bind("click",{flag:$(this)},jumpToUrl);
        });
        function jumpToUrl(event){
            var data = event.data.flag;
            console.log(data.attr("a_user_id"));
            window.location.href = "fabu.jsp?cust_id="+data.attr("a_cust_id");
        };
     });
/*----------  -----end----------------*/
        //页面全部加载后执行
    $(document).ready(function(){
        $(".wrap_con_r").find("*[a_flag]").each(function(){
            alert('a');
            $(this).bind("click",{flag:$(this)},toSupply);
        });
    });
```

### 在div中查找具有attr属性的所有元素

```javascript
    function genProAttr2(obj,fifter,attr_id,attr_code,attr_name){
        var tmp_str = "";
        //在div中查找具有attr属性的所有元素
        obj.find("*["+fifter+"]").each(function(){
            var tmp1 = "";
            if($(this).val()!="" && $(this).val()!="-1"){
                tmp1 = {attr_id:$(this).attr(attr_id),attr_code:$(this).attr(attr_code),attr_name:$(this).attr(attr_name),attr_value:$(this).val()};
                tmp_str =$.toJSON(tmp1)+"|"+ tmp_str;
            };
        });
        //替换,号
        return tmp_str.replace(/\"/g, "@");
    }
```

### 字符串去重复

```javascript
function ArrayRemoveByValue(str_value,arr_remove){
    var num_to_del =new  RegExp(str_value);
    var db_d =new  RegExp('\,{2}');
    var se_d =new  RegExp('(^\,)|(\,$)');
    arr_ret = arr_remove.toString().replace(num_to_del,'').replace(db_d,',').replace(se_d,'').split(',');
    return arr_ret;
}
```

### 拼接
```javascript
/*---------- 拼接 -----start----------------*/
    function contact(dom,attr){
        var val = "";
        $(''+dom+'['+attr+']').each(function(index,element){
            var this_val = $(this).val();
            if(this_val != ""){
                if(index == 0){
                    val = this_val;
                }else{
                    val = val +'-'+this_val;
                }
            }
        });
        return val;
        //console.log(val);
    };
/*---------- 拼接 -----end----------------*/
```
### jQuery.validate.form 插件 校验
> 网址
[jQuery Validate](http://www.runoob.com/jquery/jquery-plugin-validate.html)

-- 
>Jquery Validate 验证规则

+ `required:true`              必输字段
+ `remote:”check.php”`         使用ajax方法调用check.php验证输入值
+ `email:true`                 必须输入正确格式的电子邮件
+ `url:true`                   必须输入正确格式的网址
+ `date:true`                  必须输入正确格式的日期
+ `dateISO:true`               必须输入正确格式的日期(ISO)，例如：2009-06-23，1998/01/22 只验证格式，不验证有效性
+ `number:true`                必须输入合法的数字(负数，小数)
+ `digits:true`                必须输入整数
+ `creditcard:`                必须输入合法的信用卡号
+ `equalTo:”#field”          输入值必须和#field相同
+ `accept:`                   输入拥有合法后缀名的字符串（上传文件的后缀）
+ `maxlength:5`               输入长度最多是5的字符串(汉字算一个字符)
+ `minlength:10`              输入长度最小是10的字符串(汉字算一个字符)
+ `rangelength:[5,10]`        输入长度必须介于 5 和 10 之间的字符串”)(汉字算一个字符)
+ `range:[5,10]`              输入值必须介于 5 和 10 之间
+ `max:5`                     输入值不能大于5
+ `min:10`                    输入值不能小于10

>Demo

+ 
```javascript
/*----------------------校验----------------------*/
!function(){
    $jquery17("#skuForm").validate({
        //添加验证规则
        rules: {
            brand_price:{
                lt:true,
                min:0
            },
            product_price:{
                min:0
            }
        },
        //提示信息
        messages:{
            brand_price:{
                min:"输入值不能小于{0}"
            },
            product_price:{
                min:'输入值不能小于{0}'
            }
        }
    });
    // 验证值必须大于特定值(不能等于)
    $jquery17.validator.addMethod("lt", function(value, element, param) {
        var isTrue = false;
        $(element).parent().parent().find('input[name="product_price"]').each(function(){
            if(value*1 < $(this).val()*1){
                isTrue = true;
            }
        });
        return isTrue;
    }, $jquery17.validator.format('平台供货价不能大于价格!'));
}();
--
//校验
if($jquery17("#skuForm").valid()){
	ajaxSubmitForm($("#productAttrForm")[0]);
}
```


### Fancybox

+ **基本用法**
```javascript
$(function(){
    $jquery17("#iframe").fancybox({
        //在body上指定宽高
		'width':'70%',
		//'height':'75%',
		'autoScale':true,
		'afterClose':jumpurl
	});
    //关闭弹框
    parent.$jquery17.fancybox.close();
});
```
+ 样式
```html
在body上指定宽高
<body style="width: 310px;height: 176px;">
```
+ **iframe中,在最上层页面弹框**
```javascript
//在top层页面导入fancybox相关文件
/*
<link rel="stylesheet" type="text/css" href="/js/jquery.fancybox.css?v=2.1.5" media="screen" />
<jsp:include page="/program/company/goods/base_script.jsp"></jsp:include>
<script type="text/javascript" src="/js/jquery.fancybox.js?v=2.1.5"></script>
*/
$(document).ready(function(){
  //弹框
  top.$jquery17(top.frames['main'].$('#iframe-test')[0]).fancybox({
        'width':'80%',
        //'height':'75%',
        //'afterClose':a
    });
});
```

### **常用Js方法**

+ 
```javascript
function T(){
        this.t = 't';
};
T.prototype = {
   //判断对象不为空
	isEmpty: function(variable){
		//var data = event.data.flag;
		var tmp_var = variable || ''; 
		if(typeof tmp_var == "null" || tmp_var.replace(/(^s*)|(s*$)/g, "").length ==0){
			return true;
		}
		return false;
	},
	//人民币转大写
	numToCny: function(n){     
	    var fraction = ['角', '分'];  
	    var digit = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];  
	    var unit = [ ['元', '万', '亿'], ['', '拾', '佰', '仟']  ];  
	    var head = n < 0? '欠': '';  
	    n = Math.abs(n);  

	    var s = '';  

	    for (var i = 0; i < fraction.length; i++)   
	    {  
	        s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');  
	    }  
	    s = s || '整';  
	    n = Math.floor(n);  

	    for (var i = 0; i < unit[0].length && n > 0; i++)   
	    {  
	        var p = '';  
	        for (var j = 0; j < unit[1].length && n > 0; j++)   
	        {  
	            p = digit[n % 10] + unit[1][j] + p;  
	            n = Math.floor(n / 10);  
	        }  
	        s = p.replace(/(零.)*零$/, '').replace(/^$/, '零')  + unit[0][i] + s;  
	    }  
	    return head + s.replace(/(零.)*零元/, '元').replace(/(零.)+/g, '零').replace(/^整$/, '零元整');  
	},
	//只能输入数字
	onlyNum: function() {
        if(!(event.keyCode==46)&&!(event.keyCode==8)&&!(event.keyCode==37)&&!(event.keyCode==39))
        if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105)))
        event.returnValue=false;
    },
    // 只能输入数字
	keyUp: function(data) {
		var ob = data;
	    if (!ob.value.match(/^[\+\-]?\d*?\.?\d*?$/)){
	    	ob.value = ob.t_value;
	    }else{
	    	ob.t_value = ob.value; 
	    }	
	    if (ob.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/)){
	    	ob.o_value = ob.value;
	    }
    },
    //拼接字符串
    contact: function(dom,attr){
        var val = "";
        $(''+dom+'['+attr+']').each(function(index,element){
            var this_val = $(this).val();
            if(this_val != ""){
                if(index == 0){
                    val = this_val;
                }else{
                    val = val +'-'+this_val;
                }
            }
        });
        return val;
        //console.log(val);
    };
  };
```

### a标签绑定多个方法

+ **绑定两个keyup方法** 
```javascript
<a href="javascript:;" data-multi onkeyup="ba.t.keyUp(this)">xxxx</a>
//
<script type="text/javascript">
  $('*[data-multi]').each(function(){
      $(this).bind('keyup',{flag:$(this)},ba.t.multi);
  });
</script>
``` 

### 动态添加form
1. **form** 
```javascript
    // 创建Form  
    var form = $('<form></form>');  
    // 设置属性  
    form.attr('action', '/doTradeReg.do');  
    form.attr('method', 'post');  
    form.attr('name', 'addForm'); 
    
    var id_input = $('<input type="hidden" name="id" value="'+invoice_id+'"/>');  
    
    form.append(id_input);
    
    form.appendTo(document.body);
```
+ **dom**
```javascript
  dynamicAdd: function(tag){
	  var dom = $('<'+tag+'></'+tag+'>');  
	  
	  dom.attr('class', 'fancybox.iframe iframe');
	  dom.attr('href','javascript:;');
	  
	  dom.appendTo(document.body);
  },
```


