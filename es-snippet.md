<!-- toc -->
# es-snippet

### **new snippets**
1. **索引分片设置**
```json
PUT /b2b
{
      "settings":{
        "number_of_shards":1,
        "number_of_replicas":1
      }
}
//查询b2b分片信息
GET /b2b/_settings
```

#### mlq_goods
1. **`mlq_goods`的`mapping`设置**
```json
PUT /b2b/mlq_goods/_mapping
{
    "mlq_goods":{
      "properties": {
        "id": {
          "type":"string",
          "store":"yes",
          "index":"not_analyzed"
        },
        "title": {
           "type": "string",
           "store":"yes",
           "index":"analyzed",
           "analyzer":"ik_analyzer"
          }
        }
    }
}
//查询mlq_goods的mapping信息
GET /b2b/mlq_goods/_mapping
```
+ **从数据库导入`mlq_goods`表到`ES`配置信息**
```json
PUT /_river/mlq_goods/_meta 
{
    "type" : "jdbc",
    "jdbc" : {
       "url" : "jdbc:mysql://localhost:3306/b2b",
       "user" : "root",
       "password" : "111111",
       "sql" : "SELECT mg.id AS _id, tranCat (LEFT(cat_code, LENGTH(cat_code) - 1)) AS category, (SELECT brand_name FROM mlq_brand WHERE id = brand_id) brand_name, (SELECT tm.cust_name FROM ti_member tm WHERE tm.cust_id = mg.seller_id) new_seller_nick, mg.* FROM mlq_goods mg",
       "index": "b2b",
       "type": "mlq_goods"
      }
}
//查询mlq_goods数据
GET /b2b/mlq_goods/_search
```

#### mlq_product
1. **`mlq_product`的`mapping`设置**
```json
PUT /b2b/mlq_product/_mapping
{
    "mlq_product":{
      "_parent": {"type": "mlq_goods"},
      "properties": {
        "id": {
          "type":"string",
          "store":"yes",
          "index":"not_analyzed"
        },
        "sale_region": {
           "type": "string",
           "store":"yes",
           "index":"analyzed",
           "analyzer":"ik_analyzer"
        },
        "repository_region": {
           "type": "string",
           "store":"yes",
           "index":"analyzed",
           "analyzer":"ik_analyzer"
        },
        "product_name": {
           "type": "string",
           "store":"yes",
           "index":"analyzed",
           "analyzer":"ik_analyzer"
          }
       }
    }
}
//查询mlq_product映射maping信息
GET /b2b/mlq_product/_mapping
```
+ **从数据库导入`mlq_product`表到`ES`配置信息**
```json
PUT /_river/mlq_product/_meta 
{
"type" : "jdbc",
"jdbc" : {
   "url" : "jdbc:mysql://localhost:3306/b2b",
   "user" : "root",
   "password" : "111111",
   "sql" : "SELECT mp.id as _id, mp.goods_id as _parent,mp.*,mpa.* FROM mlq_product mp, (SELECT mpa.product_id , tranAddr(MAX(CASE mpa.attr_name WHEN '销售地区' THEN mpa.attr_value ELSE 0 END ),'-') sale_region, tranAddr(MAX(CASE mpa.attr_name WHEN '出库地区' THEN mpa.attr_value ELSE 0 END ),'-') repository_region, MAX(CASE mpa.attr_name WHEN '最小起订量' THEN mpa.attr_value ELSE 0 END ) min_quantity, MAX(CASE mpa.attr_name WHEN '库存量' THEN mpa.attr_value ELSE 0 END ) stock_quantity, MAX(CASE mpa.attr_name WHEN '单位' THEN mpa.attr_value ELSE 0 END ) unit, MAX(CASE mpa.attr_name WHEN '交货天数' THEN mpa.attr_value ELSE 0 END ) delivery_day, MAX(CASE mpa.attr_name WHEN '包装类型' THEN mpa.attr_value ELSE 0 END ) package_type FROM mlq_product_attr mpa GROUP BY mpa.product_id) mpa WHERE mp.id = mpa.product_id",
   "index": "b2b",
   "type": "mlq_product"
  }
}
//查询mlq_product 数据
GET /b2b/mlq_product/_search
```

### **tmp snippets**

```json
GET _search
{
  "query": {
    "match_all": {}
  }
}

DELETE /_river/mlq_product
DELETE /_river/mlq_goods
DELETE /b2b
DELETE /b2b/mlq_goods

PUT /b2b/mlq_product/_mapping
{
  "mlq_product": {
    "_parent":{
      "type":"mlq_goods"
    },
    "properties": {
      "addr": {
        "type": "string",
        "store": true,
        "indexAnalyzer": "ik",
        "searchAnalyzer": "ik"
      }
    }
  }
}


PUT /_river/mlq_product_attr/_meta
{
  "type" : "jdbc",
    "jdbc" : {
        "url" : "jdbc:mysql://localhost:3306/b2b",
        "user" : "root",
        "password" : "111111",
        "sql" : "SELECT mpa.id AS _id,mpa.product_id AS _parent,mpa.* FROM mlq_product_attr mpa",
        "index" : "b2b",
        "type" : "mlq_product_attr"
    }
}

GET /b2b/mlq_product_attr/_mapping
GET /b2b/mlq_product_attr/_search

GET /b2b/mlq_goods/_mapping

GET /b2b/mlq_product/_mapping

PUT /b2b/mlq_goods/_mapping
{
  "mlq_goods": {
    "properties": {
      "title": {
        "type": "string",
        "store": true,
        "analyzer": "ik_max_word"
      },
      "category": {
        "type": "string",
        "store": true,
        "analyzer": "ik_max_word"
      }
    }
  }
}

PUT /_river/mlq_goods/_meta
{
  "type" : "jdbc",
    "jdbc" : {
        "url" : "jdbc:mysql://localhost:3306/b2b",
        "user" : "root",
        "password" : "111111",
        "sql" : "SELECT mg.id as _id,tranCat(mg.cat_code) category,mg.* from mlq_goods mg",
        "index" : "b2b",
        "type" : "mlq_goods"
    }
}


PUT /b2b
{
  "settings": {
    "number_of_shards": 1
    , "number_of_replicas": 1
  }
}

GET /b2b/mlq_goods/_search
{
  "from": 0,
  "size": 5
}

GET /b2b/mlq_goods/_search
{
  "query": {
    "has_child": {
      "type": "mlq_product",
      "query": {
        "term": {
          "addr": {
            "value": "华中地区"
          }
        }
      }
    }
  }
}

GET /b2b/mlq_product/_search
{
  "from": 1,
  "size": 3, 
  "query": {
    "match": {
      "addr": "华中地区"
    }
  }
}

GET /b2b/mlq_product/_search
{
 "query": {
   "term": {
     "addr": {
        "value": "华中地区"
      }
   }
 } 
} 


GET /b2b/mlq_product_attr/_mapping

GET /b2b/mlq_goods/_search

GET /b2b/mlq_goods/_search
{
  "query": {
    "term": {
      "category": {
        "value": "改性"
      }
    }
  }
}

GET /b2b/mlq_goods/_search
{
  "query": {
    "filtered": {
      "query": {
          "has_child": {
          "type": "mlq_product",
          "query": {
            "term": {
              "addr": {
                "value": "华中地区"
              }
            }
          }
        }
      }, 
      "filter": {
        "term": {
          "category": "改性"
        }
      }
    }
  }
}

GET /b2b/mlq_product/_search
{
  "query": {
    "filtered": {
      "query": {
          "has_parent": {
          "parent_type": "mlq_goods",
          "query": {
            "term": {
              "id": "20150914JPCi4n8"
            }
          }
        }
      }
    }
  }
}

GET /b2b/mlq_product/_search
{
  "query": {
    "filtered": {
      "query": {
        "has_parent": {
          "parent_type": "mlq_goods",
          "query": {
            "term": {
              "id": "20150915y2W535n"
            }
          }
        }
      }
    }
  }
}

GET /twitter/tweet/_mapping





GET /twitter/tweet/_search
{
  "query": {
    "filtered": {
      "query": {
        "term": {
          "author": "John"
        }
      }
    }
  }
}
```

## 2015-12-29 snippets

```json
GET /b2b/mlq_goods/_search
{
  "fields": [
    "id"
  ],
  "query": {
    "match": {
      "cat_code": "20150914w7L7oG2"
    }
  },
  "aggs": {
    "district": {
      "terms": {
        "field": "brand_id",
        "size": 0
      },
      "aggs": {
        "tops": {
          "top_hits": {
            "_source": {
              "include": [
                "brand_id",
                "brand_name"
              ]
            },
            "size": 1
          }
        }
      }
    },
    "to-product": {
      "children": {
        "type": "mlq_product"
      },
      "aggs": {
        "top-names": {
          "terms": {
            "field": "id",
            "size": 0
          },
          "aggs": {
            "tops": {
              "top_hits": {
                "_source": {
                  "include": [
                    "id",
                    "product_name"
                  ]
                },
                "size": 1
              }
            }
          }
        }
      }
    }
  }
}

```

### `AND`查询

+ 
```json
GET /b2b/mlq_goods/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": { "cat_code": "20150914w7L7oG2"}},
        {"match": { "brand_id": "20150914C4ie5oO"}}
      ]
    }
  }
}
```

### `Range`查询

+ 
```json
GET /b2b/mlq_goods/_search
{
  "query": {
    "filtered": {
      "query": {
        "match_all": {}
      },
      "filter": {
        "has_child": {
          "type": "mlq_product",
          "query": {
            "range": {
              "mlq_product.product_price": {
                "gte": 3600,
                "lte": 4000
              }
            }
          }
        }
      }
    }
  }
}
```

### 多条件查询

1. 筛选 
```json
POST /b2b/mlq_goods/_search
{
  "from": 0,
  "size": 10,
  "query": {
    "bool": {
      "must": [
        {
          "bool": {
          "should": [
            {
              "match": {
                "brand_name": "宁波博盈"
              }
            },
            {
              "match": {
                "title": "宁波博盈"
              }
            },
            {
              "match": {
                "new_seller_nick": "宁波博盈"
              }
            }
          ]
          }
        },
        {
          "match": {
            "cat_code": "20150914w7L7oG2"
          }
        },
        {
          "match": {
            "new_seller_nick": "宁波博盈石油化工有限公司"
          }
        },
        {
          "term": {
            "brand_name_noa": "泰普克"
          }
        },
        {
          "has_child": {
            "type": "mlq_product",
            "query": {
              "range": {
                "mlq_product.product_price": {
                  "from": 1000.0,
                  "to": 3000.0
                }
              }
            }
          }
        },
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
            "type": "mlq_product",
            "query": {
              "match": {
                "repository_region": "华东"
              }
            }
          }
        }
      ]
    }
  }
}
```

### 父子查询
> 父:mlq_goods - 子:mlq_product

1. 查询子
```json
POST /b2b/mlq_product/_search
{
    "query": {
        "has_parent": {
            //父类型
            "parent_type": "mlq_goods",
            "query": {
                "term": {
                   //父id
                   "id": {
                      "value": "20151127y344nRq"
                   }
                }
            }
        }
    }
}
```
2. 查询父
```json
POST /b2b/mlq_goods/_search
{
   "query": {
      "has_child": {
         //子类型
         "type": "mlq_product",
         "query": {
            "term": {
               //子id
               "id": {
                  "value": "20151127c3De6K2"
               }
            }
         }
      }
   }
}
```

### 2016年1月24日
1. 加权
```
POST /b2b/mlq_goods/_search
{
   "query": {
      "function_score": {
         "query": {
            "match": {
               "cat_code": "20150914q0asfEw"
            }
         },
         "functions": [
            {
               "boost_factor": 20
            }
         ]
      }
   }
}
```
2. 商品,单品 `weight` 相加 得到 `score`
```json
POST /b2b/mlq_goods/_search
{
   "query": {
      "function_score": {
         "query": {
            "has_child": {
               "type": "mlq_product",
               "score_type": "sum",
               "query": {
                  "function_score": {
                     "functions": [
                        {
                           "script_score": {
                              "script": "doc['mlq_product.weight'].value"
                           }
                        }
                     ],
                     "query": {
                        "match": {
                           "repository_region_noa": "甘肃"
                        }
                     },
                     "boost_mode": "replace"
                  }
               }
            }
         },
         "functions": [
            {
               "script_score": {
                  "script": "doc['mlq_goods.weight'].value"
               }
            }
         ],
         "boost_mode": "sum"
      }
   }
}
```
+ 商品,单品,店铺 `weight` 相加 得到 score
```json
POST /b2b/mlq_goods/_search
{
   "query": {
      "function_score": {
         "query": {
            "bool": {
               "must": [
                  {
                     "has_child": {
                        "type": "mlq_product",
                        "score_type": "sum",
                        "query": {
                           "function_score": {
                              "functions": [
                                 {
                                    "script_score": {
                                       "script": "doc['mlq_product.weight'].value"
                                    }
                                 }
                              ],
                              "query": {
                                 "match": {
                                    "repository_region_noa": "甘肃"
                                 }
                              },
                              "boost_mode": "replace"
                           }
                        }
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
                                       "script": "doc['mlq_shop_info.weight'].value"
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
                  "script": "doc['mlq_goods.weight'].value"
               }
            }
         ],
         "score_mode": "sum",
         "boost_mode": "sum"
      }
   }
}
```
+ 查询,得分,聚合
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
                     "match": {
                        "cat_code": "201509140qX823u"
                     }
                  },
                  {
                     "term": {
                        "goods_status": "c"
                     }
                  },
                  {
                     "has_child": {
                        "type": "mlq_product",
                        "query": {
                           "range": {
                              "mlq_product.product_price": {
                                 "from": 1000,
                                 "to": 3000
                              }
                           }
                        }
                     }
                  },
                  {
                     "has_child": {
                        "type": "mlq_product",
                        "score_type": "max",
                        "query": {
                           "function_score": {
                              "functions": [
                                 {
                                    "script_score": {
                                       "script": "doc['mlq_product.weight'].value"
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
                                       "script": "doc['mlq_shop_info.weight'].value"
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
                  "script": "doc['mlq_goods.weight'].value"
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
            "size": 10
         }
      },
      "seller_nicks": {
         "terms": {
            "field": "new_seller_nick_noa",
            "size": 10
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
                  "size": 10
               }
            }
         }
      }
   }
}
```

### 2016-01-26
1. `script`更新 `weight` 
```json
POST /b2b/mlq_shop_info/2015091404A3ol8/_update
{
   "script": "weight = ctx._source.weight; if (weight == null) {ctx._source.weight=0 }; ctx._source.weight+=count;",
   "params": {
      "count": 7
   }
}
```
