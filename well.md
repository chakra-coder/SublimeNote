### 一点 一点
#### js
+ **jquery-aop**
[jquery-aop][jquery-aop]
[jquery-aop]: https://github.com/gonzalocasas/jquery-aop

+ **demo**
```javascript
// 导入 aop.js jquery9.min.js
//
(function(){
    function T(){
        this.init();
        _this = this;
    };
    T.prototype = {
        init: function(){
            $('*[data-save]').each(function(){
                $(this).bind('click',{flag:$(this)},_this.save);
            });
            $('*[data-delete]').each(function(){
                $(this).bind('click',{flag:$(this)},_this.delete);
            });
        },
        save: function(event){
            var data = event.data.flag;
            console.log(data.html());
        },
        delete: function(event){
            var data = event.data.flag;
            console.log(data.html());
        }
    };
    //前置
    jQuery.aop.before( {target: T, method: 'save'},
        function() {
            console.log('before sava!');
        }
    );
    //后置
    jQuery.aop.after( {target: T, method: 'delete'},
        function() {
            console.log('after delete!');
        }
    );
    window["t"] = new T();
})();
```


