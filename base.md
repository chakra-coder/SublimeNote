# base

##### JS 面向对象

1. **js**
```javascript
(function(){
	function T(){
		//this.a = 'a';
		_this = this;
	};
	T.prototype = {
	    init: function(){
	        $('*[data-save]').each(function(){
		        $(this).bind('click',{flag:$(this)},_this.save);
	        });
	    },
		save: function(event){
			var data = event.data.flag;
			console.log(data.html());
		},
		back: function(event){
          var data = event.data.flag;
          window.location.href = 'index.jsp';
        }
	};
	window["t"] = new T();
})();
//初始化
t.init();
```
+ **html**
```javascript
#导入文件
<script type="text/javascript" src="js/orderManager.js"></script>
```