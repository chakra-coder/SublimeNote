# curl

### mlq_goods
+ 
```json
curl -XPOST "http://192.168.1.126:9200/b2b/mlq_goods/_search" -d'
{
    "from": 0,
    "size": 1
}'
//数量
curl -XGET "http://192.168.1.126:9200/b2b/mlq_goods/_count"
```

### mlq_product
+ 
```json
curl -XGET "http://192.168.1.126:9200/b2b/mlq_goods/_mapping"
curl -XGET "http://192.168.1.126:9200/b2b/mlq_product/_mapping"
//删除
curl -XDELETE "http://192.168.1.126:9200/b2b"
curl -XPOST "http://192.168.1.126:9200/b2b/mlq_product/_search" -d'
{
    "from": 0,
    "size": 1
}'
```

### mappings
+ 
```json
//mapping
curl -XPUT "http://192.168.1.126:9200/b2b" -d'
{
   "settings": {
      "number_of_replicas": "1",
      "number_of_shards": "1"
   },
   "mappings": {
      "mlq_goods": {
         "properties": {
            "id": {
               "type": "string",
               "store": "yes",
               "index": "not_analyzed"
            },
            "category": {
               "type": "string",
               "store": "yes",
               "index": "analyzed",
               "analyzer": "ik"
            },
           "cat_code": {
              "type": "string",
              "store": "yes",
              "index": "analyzed",
              "analyzer": "mmseg"
           },
            "brand_name": {
               "type": "string",
               "store": "yes",
               "index": "analyzed",
               "analyzer": "ik"
            },
            "brand_name_noa": {
               "type": "string",
               "store": "yes",
               "index": "not_analyzed"
            },
            "new_seller_nick": {
               "type": "string",
               "store": "yes",
               "index": "analyzed",
               "analyzer": "ik"
            },
            "new_seller_nick_noa": {
               "type": "string",
               "store": "yes",
               "index": "not_analyzed"
            },
            "goods_status": {
               "type": "string",
                "store": "yes",
               "index": "not_analyzed"
            },
            "title": {
               "type": "string",
               "store": "yes",
               "index": "analyzed",
               "analyzer": "ik"
            }
         }
      },
      "mlq_product": {
         "_parent": {
            "type": "mlq_goods"
         },
         "properties": {
            "id": {
               "type": "string",
               "store": "yes",
               "index": "not_analyzed"
            },
            "sale_region": {
               "type": "string",
               "store": "yes",
               "index": "analyzed",
               "analyzer": "ik"
            },
            "repository_region": {
               "type": "string",
               "store": "yes",
               "index": "analyzed",
               "analyzer": "ik"
            },
            "repository_region_simple": {
               "type": "string",
               "store": "yes",
               "index": "analyzed",
               "analyzer": "simple"
            },
            "product_name_noa": {
               "type": "string",
               "store": "yes",
               "index": "not_analyzed"
            },
            "product_name": {
               "type": "string",
               "store": "yes",
               "index": "analyzed",
               "analyzer": "ik"
            }
         }
      }
   }
}'
```