### `Elasticsearch`
1. **启动添加内存参数**
```bash
~/opt/elasticsearch-1.7.1/bin/elasticsearch -Xms512m -Xmx1g -d
```
+ **动态脚本支持**
```yml
script.disable_dynamic: false
#关闭marvel
marvel.agent.enabled: false
```
+ **b2b(测试)**
```json
curl -XPOST "http://192.168.1.126:9200/b2b" -d'
{
   "settings": {
      "number_of_replicas": "1",
      "number_of_shards": "1"
   },
   "mappings": {
      "mlq_shop_info": {
         "properties": {
            "shop_name": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "shop_desc": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "shop_logo": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "user_id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "gmt_create": {
               "type": "string"
            },
            "recom_modify": {
               "type": "string"
            },
            "business_license_deadline": {
               "type": "string"
            },
            "tag": {
               "type": "string"
            },
            "gmt_modify": {
               "type": "string"
            }
         }
      },
      "mlq_goods": {
         "_parent": {
            "type": "mlq_shop_info"
         },
         "_routing": {
            "required": true
         },
         "properties": {
            "audit_remark": {
               "type": "string"
            },
            "brand_id": {
               "type": "string"
            },
            "brand_name": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "brand_name_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "cat_code": {
               "type": "string",
               "store": true,
               "analyzer": "mmseg"
            },
            "category": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "distribution_mode": {
               "type": "long"
            },
            "gmt_create": {
               "type": "string"
            },
            "gmt_listing": {
               "type": "string"
            },
            "gmt_modify": {
               "type": "string"
            },
            "goods_desc": {
               "type": "string"
            },
            "goods_model": {
               "type": "string"
            },
            "goods_number": {
               "type": "string"
            },
            "goods_price": {
               "type": "string"
            },
            "goods_selling_point": {
               "type": "string"
            },
            "goods_status": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "img_large": {
               "type": "string"
            },
            "is_listing_now": {
               "type": "string"
            },
            "is_self": {
               "type": "long"
            },
            "logistics_type": {
               "type": "long"
            },
            "new_seller_nick": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "new_seller_nick_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "search_tag": {
               "type": "string"
            },
            "seller_id": {
               "type": "string"
            },
            "seller_nick": {
               "type": "string"
            },
            "shop_cat_id": {
               "type": "string"
            },
            "shop_id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "shop_recommended": {
               "type": "long"
            },
            "sort": {
               "type": "long"
            },
            "title": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "weight": {
               "type": "long"
            }
         }
      },
      "mlq_goods_dis": {
         "_parent": {
            "type": "mlq_shop_info"
         },
         "_routing": {
            "required": true
         },
         "properties": {
            "audit_remark": {
               "type": "string"
            },
            "brand_id": {
               "type": "string"
            },
            "brand_name": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "brand_name_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "cat_code": {
               "type": "string",
               "store": true,
               "analyzer": "mmseg"
            },
            "category": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "distribution_mode": {
               "type": "long"
            },
            "goods_desc": {
               "type": "string"
            },
            "goods_model": {
               "type": "string"
            },
            "goods_selling_point": {
               "type": "string"
            },
            "goods_status": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "goods_id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "is_listing_now": {
               "type": "string"
            },
            "gmt_listing": {
               "type": "string"
            },
            "shop_id": {
               "type": "string"
            },
            "is_self": {
               "type": "long"
            },
            "new_seller_nick": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "new_seller_nick_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "shop_recommended": {
               "type": "long"
            },
            "sort": {
               "type": "long"
            },
            "weight": {
               "type": "long"
            },
            "title": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "sale_price": {
               "type": "double"
            },
            "is_distribute": {
               "type": "string"
            }
         }
      },
      "mlq_product_dis": {
         "_parent": {
            "type": "mlq_goods_dis"
         },
         "_routing": {
            "required": true
         },
         "properties": {
            "delivery_day": {
               "type": "string"
            },
            "goods_id": {
               "type": "string"
            },
            "id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "min_quantity": {
               "type": "string"
            },
            "package_type": {
               "type": "string"
            },
            "product_id": {
               "type": "string"
            },
            "product_name": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "product_name_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "repository_region": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "repository_region_noa": {
               "type": "string",
               "store": true,
               "analyzer": "simple"
            },
            "repository_region_simple": {
               "type": "string",
               "store": true,
               "analyzer": "simple"
            },
            "sale_region": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "stock_quantity": {
               "type": "string"
            },
            "unit": {
               "type": "string"
            }
         }
      },
      "mlq_product": {
         "_parent": {
            "type": "mlq_goods"
         },
         "_routing": {
            "required": true
         },
         "properties": {
            "brand_price": {
               "type": "double"
            },
            "contract_id": {
               "type": "string"
            },
            "creater": {
               "type": "string"
            },
            "delivery_day": {
               "type": "string"
            },
            "gmt_create": {
               "type": "string"
            },
            "gmt_modify": {
               "type": "string"
            },
            "goods_id": {
               "type": "string"
            },
            "id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "min_quantity": {
               "type": "string"
            },
            "package_type": {
               "type": "string"
            },
            "product_id": {
               "type": "string"
            },
            "product_name": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "product_name_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "product_price": {
               "type": "double"
            },
            "repository_region": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "repository_region_noa": {
               "type": "string",
               "store": true,
               "analyzer": "simple"
            },
            "repository_region_simple": {
               "type": "string",
               "store": true,
               "analyzer": "simple"
            },
            "sale_region": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "stock_quantity": {
               "type": "string"
            },
            "unit": {
               "type": "string"
            }
         }
      }
   }
}'
```


curl -XPUT "http://192.168.1.126:9200/b2b" -d'
{
   "settings": {
      "number_of_replicas": "1",
      "number_of_shards": "1"
   },
   "mappings": {
      "mlq_shop_info": {
         "properties": {
            "shop_name": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "shop_desc": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "shop_logo": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "user_id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "gmt_create": {
               "type": "string"
            },
            "recom_modify": {
               "type": "string"
            },
            "business_license_deadline": {
               "type": "string"
            },
            "gmt_modify": {
               "type": "string"
            }
         }
      },
      "mlq_goods": {
         "_parent": {
            "type": "mlq_shop_info"
         },
         "_routing": {
            "required": true
         },
         "properties": {
            "audit_remark": {
               "type": "string"
            },
            "brand_id": {
               "type": "string"
            },
            "brand_name": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "brand_name_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "cat_code": {
               "type": "string",
               "store": true,
               "analyzer": "mmseg"
            },
            "category": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "distribution_mode": {
               "type": "long"
            },
            "gmt_create": {
               "type": "string"
            },
            "gmt_listing": {
               "type": "string"
            },
            "gmt_modify": {
               "type": "string"
            },
            "goods_desc": {
               "type": "string"
            },
            "goods_model": {
               "type": "string"
            },
            "goods_number": {
               "type": "string"
            },
            "goods_price": {
               "type": "string"
            },
            "goods_selling_point": {
               "type": "string"
            },
            "goods_status": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "img_large": {
               "type": "string"
            },
            "is_listing_now": {
               "type": "string"
            },
            "is_self": {
               "type": "long"
            },
            "logistics_type": {
               "type": "long"
            },
            "new_seller_nick": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "new_seller_nick_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "search_tag": {
               "type": "string"
            },
            "seller_id": {
               "type": "string"
            },
            "seller_nick": {
               "type": "string"
            },
            "shop_cat_id": {
               "type": "string"
            },
            "shop_id": {
               "type": "string"
            },
            "shop_recommended": {
               "type": "long"
            },
            "sort": {
               "type": "long"
            },
            "title": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "weight": {
               "type": "long"
            }
         }
      },
      "mlq_goods_dis": {
         "_parent": {
            "type": "mlq_shop_info"
         },
         "_routing": {
            "required": true
         },
         "properties": {
            "audit_remark": {
               "type": "string"
            },
            "brand_id": {
               "type": "string"
            },
            "brand_name": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "brand_name_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "cat_code": {
               "type": "string",
               "store": true,
               "analyzer": "mmseg"
            },
            "category": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "distribution_mode": {
               "type": "long"
            },
            "goods_desc": {
               "type": "string"
            },
            "goods_model": {
               "type": "string"
            },
            "goods_selling_point": {
               "type": "string"
            },
            "goods_status": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "goods_id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "is_listing_now": {
               "type": "string"
            },
            "gmt_listing": {
               "type": "string"
            },
            "shop_id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "is_self": {
               "type": "long"
            },
            "new_seller_nick": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "new_seller_nick_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "shop_recommended": {
               "type": "long"
            },
            "sort": {
               "type": "long"
            },
            "weight": {
               "type": "long"
            },
            "title": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "sale_price": {
               "type": "double"
            },
            "is_distribute": {
               "type": "string"
            }
         }
      },
      "mlq_product_dis": {
         "_parent": {
            "type": "mlq_goods_dis"
         },
         "_routing": {
            "required": true
         },
         "properties": {
            "delivery_day": {
               "type": "string"
            },
            "goods_id": {
               "type": "string"
            },
            "id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "min_quantity": {
               "type": "string"
            },
            "package_type": {
               "type": "string"
            },
            "product_id": {
               "type": "string"
            },
            "product_name": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "product_name_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "repository_region": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "repository_region_noa": {
               "type": "string",
               "store": true,
               "analyzer": "simple"
            },
            "repository_region_simple": {
               "type": "string",
               "store": true,
               "analyzer": "simple"
            },
            "sale_region": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "stock_quantity": {
               "type": "string"
            },
            "unit": {
               "type": "string"
            }
         }
      },
      "mlq_product": {
         "_parent": {
            "type": "mlq_goods"
         },
         "_routing": {
            "required": true
         },
         "properties": {
            "brand_price": {
               "type": "double"
            },
            "contract_id": {
               "type": "string"
            },
            "creater": {
               "type": "string"
            },
            "delivery_day": {
               "type": "string"
            },
            "gmt_create": {
               "type": "string"
            },
            "gmt_modify": {
               "type": "string"
            },
            "goods_id": {
               "type": "string"
            },
            "id": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "min_quantity": {
               "type": "string"
            },
            "package_type": {
               "type": "string"
            },
            "product_id": {
               "type": "string"
            },
            "product_name": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "product_name_noa": {
               "type": "string",
               "index": "not_analyzed",
               "store": true
            },
            "product_price": {
               "type": "double"
            },
            "repository_region": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "repository_region_noa": {
               "type": "string",
               "store": true,
               "analyzer": "simple"
            },
            "repository_region_simple": {
               "type": "string",
               "store": true,
               "analyzer": "simple"
            },
            "sale_region": {
               "type": "string",
               "store": true,
               "analyzer": "ik"
            },
            "stock_quantity": {
               "type": "string"
            },
            "unit": {
               "type": "string"
            }
         }
      }
   }
}'

+ **查询**
```
curl -XGET "http://192.168.1.121:9200/b2b/mlq_goods_dis/_search"
```

+ **删除**
```json
//测试
curl -XDELETE "http://192.168.1.126:9200/b2b"
//删除单个
curl -XDELETE "http://192.168.1.121:9200/b2b/mlq_goods_dis/AVNVz3ZQ1j-GBO6UpGU8"
```
+ **映射**
```json
curl -XGET "http://192.168.1.121:9200/b2b/mlq_goods_dis/_mapping"
```

+ **tmp-b2b**
```json
{
   "from": 0,
   "size": 10,
   "query": {
      "function_score": {
         "query": {
            "bool": {
               "must": [
                  {
                     "multi_match": {
                        "query": "夏邑县乐晒防水涂料销售部",
                        "fields": [
                           "new_seller_nick",
                           "brand_name",
                           "title"
                        ]
                     }
                  },
                  {
                     "match": {
                        "cat_code": "23245asdf"
                     }
                  },
                  {
                     "term": {
                        "new_seller_nick_noa": "中物振华"
                     }
                  },
                  {
                     "match": {
                        "shop_id": "seefwefwef"
                     }
                  },
                  {
                     "match": {
                        "shop_cat": "xxxeewef333"
                     }
                  },
                  {
                     "term": {
                        "brand_name_noa": "国产"
                     }
                  },
                  {
                     "term": {
                        "goods_status": "c"
                     }
                  },
                  {
                     "bool": {
                        "should": [
                           {
                              "has_child": {
                                 "type": "mlq_product",
                                 "query": {
                                    "range": {
                                       "mlq_product.product_price": {
                                          "from": 2000,
                                          "to": 3000
                                       }
                                    }
                                 }
                              }
                           },
                           {
                               "range": {
                                  "sale_price": {
                                     "from": 2000,
                                     "to": 3000
                                  }
                               }
                           }
                        ]
                     }
                  },
                  {
                     "bool": {
                        "should": [
                           {
                              "has_child": {
                                 "type": "mlq_product",
                                 "query": {
                                    "term": {
                                       "mlq_product.product_name_noa": "70#"
                                    }
                                 }
                              }
                           },
                           {
                              "has_child": {
                                 "type": "mlq_product_dis",
                                 "query": {
                                    "term": {
                                       "mlq_product_dis.product_name_noa": "SBS"
                                    }
                                 }
                              }
                           }
                        ]
                     }
                  },
                  {
                     "bool": {
                        "should": [
                           {
                              "has_child": {
                                 "type": "mlq_product",
                                 "query": {
                                    "match": {
                                       "mlq_product.repository_region_noa": "华北地区"
                                    }
                                 }
                              }
                           },
                           {
                              "has_child": {
                                 "type": "mlq_product_dis",
                                 "query": {
                                    "term": {
                                       "mlq_product_dis.repository_region_noa": "华北地区"
                                    }
                                 }
                              }
                           }
                        ]
                     }
                  },
                  {
                     "has_parent": {
                        "parent_type": "mlq_shop_info",
                        "score_type": "score",
                        "query": {
                           "function_score": {
                              "functions": [
                                 {
                                    "script_score": {
                                       "script": "doc['mlq_shop_info.weight'].value*0.02"
                                    }
                                 }
                              ],
                              "query": {
                                 "match_all": {}
                              },
                              "boost_mode": "replace"
                           }
                        }
                     }
                  }
               ]
            }
         },
         "functions": [
            {
               "script_score": {
                  "script": "doc['mlq_goods.weight'].value*0.01"
               }
            }
         ],
         "score_mode": "sum",
         "boost_mode": "sum"
      }
   },
   "aggs": {
      "brand_names": {
         "terms": {
            "field": "brand_name_noa",
            "size": 20
         }
      },
      "seller_nicks": {
         "terms": {
            "field": "new_seller_nick_noa",
            "size": 20
         }
      },
      "product_names": {
         "children": {
            "type": "mlq_product"
         },
         "aggs": {
            "tops": {
               "terms": {
                  "field": "product_name_noa",
                  "size": 20
               }
            }
         }
      }
   }
}
```
+ **mlq_goods_dis**
```json
"_index" "b2b",
"_type" "mlq_goods_dis",
"_id" "201603043Va5aTT",
   "id" "201603043Va5aTT",
   "is_distribute" "1",
   "sale_price" 2311,
   "goods_id" "20160129717104j",
   "title" "55550sbsssssss",
   "goods_status" "1",
   "category" "沥青添加剂,改性沥青添加剂,SBS",
   "brand_name" "天津LG",
   "brand_name_noa" "天津LG",
   "new_seller_nick" "中粮集团04",
   "new_seller_nick_noa" "中粮集团04",
   "cat_code" "20150914i3J058e,20150914p8880q7,20150914wUem680,",
   "brand_id" "2015091401A25fR",
   "goods_model" "sbsssssss",
   "goods_desc" "<p>阿三地方</p>",
   "goods_selling_point" "sbsssssss",
   "is_listing_now" "y",
   "distribution_mode" 1,
   "shop_recommended" null,
   "admin_recommended" null,
   "is_self" null,
   "sort" null,
   "weight" 0
}

_index, _type, _id, _parent, id, is_distribute, sale_price, goods_id, title, goods_status, category, brand_name, brand_name_noa, new_seller_nick, new_seller_nick_noa, cat_code, brand_id, goods_model, goods_desc, goods_selling_point, is_listing_now, distribution_mode, shop_recommended, admin_recommended, is_self, sort, weight

"_index": "b2b",
"_type": "mlq_product_dis",
"_id": "201603043Va5aTT",
"_parent": "201603043Va5aTT",
"goods_id": "201603043Va5aTT",
"product_name_noa": "SBS",
"product_name": "SBS",
"product_id": "2016012985X71k3",
"sale_region": "华中地区",
"repository_region": "华北地区",
"repository_region_noa": "华北地区",
"min_quantity": "23",
"stock_quantity": "11",
"unit": "台",
"delivery_day": "23",
"package_type": "散装"


_index, _type, _id, _parent, goods_id, product_name_noa, product_name, product_id, sale_region, repository_region, repository_region_noa, min_quantity, stock_quantity, unit, delivery_day, package_type

```

+ **tmp**
```
curl -XGET "http://192.168.1.121:9200/b2b/mlq_product/_search"
```

### `Linux`
#### `iptables`
1 **打开80端口**
```bash
#打开主动模式21端口
iptables -A INPUT -p tcp --dport 80 -j ACCEPT
```

+ **注册service服务**
```
service 操作的命令 相当于在 /etc/init.d/目录下的命令
```