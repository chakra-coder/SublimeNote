# ES json demo

### 1、JDBC River Mapping

>  从数据库中导出数据到ES

+ 
```json
PUT /_river/b2b/_meta 
{
    "type" : "jdbc",
    "jdbc" : {
        "url" : "jdbc:mysql://localhost:3306/b2b",
        "user" : "root",
        "password" : "111111",
        "sql" : "select * from mlq_attribute",
        "index": "b2b",
        "type": "mlq_attribute"
    }
}
```


### 2、查询映射
```json
GET /b2b/_mapping
// Result
{
   "b2b": {
      "mappings": {
         "mlq_attribute": {
            "properties": {
               "attr_name": {
                  "type": "string",
                  "store": true,
                  "analyzer": "ik"
               },
               "attr_name_gm": {
                  "type": "string"
               },
               "attr_values": {
                  "type": "string"
               },
               "group_id": {
                  "type": "long"
               },
               "id": {
                  "type": "long"
               },
               "input_type": {
                  "type": "string"
               },
               "is_buyer_choose": {
                  "type": "long"
               },
               "is_guide": {
                  "type": "long"
               },
               "is_need": {
                  "type": "long"
               },
               "is_screening_condition": {
                  "type": "string"
               },
               "is_show": {
                  "type": "long"
               },
               "sort": {
                  "type": "long"
               }
            }
         }
      }
   }
}
```

## 2、elasticsearch river

###  parent
+ 
```json
//maping
PUT /b2b/mlq_goods/_mapping
{
  "mlq_goods":{
    "properties": {
      "title": {
         "type": "string","store":"yes","index":"analyzed","analyzer":"ik_analyzer"
      }
    }
 }
}
//river
PUT /_river/mlq_goods/_meta 
{
 "type" : "jdbc",
 "jdbc" : {
     "url" : "jdbc:mysql://localhost:3306/b2b",
     "user" : "root",
     "password" : "111111",
     "sql" : "select mg.id as _id, mg.* from mlq_goods mg",
     "index": "b2b",
     "type": "mlq_goods"
 }
}
```

###  child

+ 
```json
//maping
PUT /b2b/mlq_product/_mapping
{
  "mlq_product":{
    "_parent": {"type": "mlq_goods"},
    "properties": {
      "product_name": {
         "type": "string","store":"yes","index":"analyzed","analyzer":"ik_analyzer"
        }
   }
  }
}
//river
PUT /_river/mlq_product/_meta 
{
 "type" : "jdbc",
 "jdbc" : {
     "url" : "jdbc:mysql://localhost:3306/b2b",
     "user" : "root",
     "password" : "111111",
     "sql" : "select  mp.id as _id, mp.goods_id as _parent,mp.* from mlq_product mp",
     "index": "b2b",
     "type": "mlq_product"
 }
}
```

### 增加映射(没有的可以增加,已有的只能删除后增加)
+ 
```json
PUT /b2b/mlq_goods/_mapping
{
   "mlq_goods": {
      "properties": {
         "new_seller_nick_noa": {
            "type": "string",
            "index": "not_analyzed",
            "store": true
         }
      }
   }
}
```