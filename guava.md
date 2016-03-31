<!-- toc -->
# guava

### 操作
1. **遍历`list`**
```java
import com.google.common.base.MoreObjects;
import static com.google.common.base.Preconditions.*;
for(T t : MoreObjects.firstNonNull(t_l, Collections.<T> emptyList())){
	 logger.info(t);
 }
try {
	checkElementIndex(0,mg.getMpp_l().size());
} catch (Exception e) {
	jw.value(serverUrl+mg.getImg_large());
}
```
+ **判断是否为空值**
```java
if(Optional.fromNullable(obj).isPresent()){
    //do something			
}
```
+ 过滤
```java
Predicate<Ti_invoice> self_p = new Predicate<Ti_invoice>() {
	public boolean apply(Ti_invoice ti) {
		Ti_invoice_prop tmp_tip = (Ti_invoice_prop)ti;
		Ti_goodsorder_prop tmp_tgp = (Ti_goodsorder_prop)tmp_tip.getTg();
		Mlq_goods tmp_mgp = tmp_tgp.getMg();
		if("201506265aD3LcU".equals(tmp_mgp.getSeller_id())){
			return true;
		}
		return false;
	}
};
```