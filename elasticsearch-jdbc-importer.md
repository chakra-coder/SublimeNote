# elasticsearch-jdbc-importer

### `mysql`数据导入`elasticsearch`

1. **设置映射**
```json
PUT /b2b
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
}
```
+ **import导入文件**
```bash
#!/bin/sh
#"schedule" : "0 0 22 * * ?",
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
bin=${DIR}/../../bin
lib=${DIR}/../../lib
#
echo '
{
    "type" : "jdbc",
	"jdbc" : {
     "url" : "jdbc:mysql://192.168.137.1:3306/b2b",
     "user" : "root",
     "password" : "111111",
	 "sql" :[
		{
			"statement" : "SELECT \"b2b\" AS _index, \"mlq_goods\" as _type, mg.id AS _id, tranCat (LEFT(cat_code, LENGTH(cat_code) - 1)) AS category, (SELECT brand_name FROM mlq_brand WHERE id = brand_id) brand_name, (SELECT tm.cust_name FROM ti_member tm WHERE tm.cust_id = mg.seller_id) new_seller_nick, mg.* FROM mlq_goods mg"
		},
		{
			"statement" : "SELECT \"b2b\" AS _index, \"mlq_product\" as _type, mp.id as _id, mp.goods_id as _parent,mp.*,mpa.* FROM mlq_product mp, (SELECT mpa.product_id , tranAddr(MAX(CASE mpa.attr_name WHEN \"销售地区\" THEN mpa.attr_value ELSE 0 END ),\"-\") sale_region, tranAddr(MAX(CASE mpa.attr_name WHEN \"出库地区\" THEN mpa.attr_value ELSE 0 END ),\"-\") repository_region, MAX(CASE mpa.attr_name WHEN \"最小起订量\" THEN mpa.attr_value ELSE 0 END ) min_quantity, MAX(CASE mpa.attr_name WHEN \"库存量\" THEN mpa.attr_value ELSE 0 END ) stock_quantity, MAX(CASE mpa.attr_name WHEN \"单位\" THEN mpa.attr_value ELSE 0 END ) unit, MAX(CASE mpa.attr_name WHEN \"交货天数\" THEN mpa.attr_value ELSE 0 END ) delivery_day, MAX(CASE mpa.attr_name WHEN \"包装类型\" THEN mpa.attr_value ELSE 0 END ) package_type FROM mlq_product_attr mpa GROUP BY mpa.product_id) mpa WHERE mp.id = mpa.product_id"
		}
	],
	"elasticsearch" : {
			"cluster" : "elasticsearch",
			"host" : "192.168.137.107",
			"port" : 9300
		}
    }
}
' | java \
    -cp "${lib}/*" \
    -Dlog4j.configurationFile=${bin}/log4j2.xml \
    org.xbib.tools.Runner \
    org.xbib.tools.JDBCImporter
```