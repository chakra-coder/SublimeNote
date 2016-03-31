# tmp-sql

#### Sql

1. **mlq_goods**
```sql
SELECT 
    mlq_goods_new.id,
    mlq_goods_new.is_self,
    mlq_goods_new.new_seller_nick 
  FROM
    (SELECT 
      *,
      (SELECT 
        cust_name 
      FROM
        ti_member 
      WHERE cust_id = seller_id) new_seller_nick 
    FROM
      mlq_goods) mlq_goods_new 
  WHERE mlq_goods_new.new_seller_nick LIKE '%自营%' 
```