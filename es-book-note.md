# ES-book-note

### 1、基本查询
>  词条查询

+ 
```json
{
  "query":{
    "term":{
      "title":{
        "value":"crime"
      }
    }
  }
}
```

 >  多词条查询

 + 
```json
{
  "query":{
    "terms":{
      //novel,book 字段
      "tags":["novel","book"],
      //至少一条需要匹配
      "minimum_match":1
    }
  }
}
```

 >  match_all 查询

 + 
```json
{
  "query":{
    "match_all":{ }
  }
}
```

 >  常用词查询

 + 
```json
{
  "query":{
    "common":{
      "title":{
        "query":"crime and punishment",
        "cutoff_frequncy":0.001
      }
    }
  }
}
```
__查询可能使用下列参数__
    - query:这个参数定义了实际的查询内容
    - cutoff_frequency: 这个参数定义一个百分比

 >  match查询

 + 
```json
{
  "query":{
    "match":{
      "title":"crime and punishment"
    }
  }
}
```

 >  布尔值匹配查询

 + 
```json
{
  "query":{
    "match":{
      "title":{
        "query":"crime and punishment",
        "operator":"and"
      }
    }
  }
}
```

 >  match_phrase 查询

 + 
```json
{
  "query":{
    "match":{
      "title":{
        "query":"crime and punishment",
        "slop":"1"
      }
    }
  }
}
```

 >  multi_match查询

 + 
```json
{
  "query":{
    "multi_match":{
      "query":"crime punishment",
      "fields":["title","otitle"]
    }
  }
}
```

 >  query_string 查询

 + 
```json
{
  "query":{
    "query_string":{
      "query":"title:crime^10 +title:punishment -otitle:cat +author:(+Fyodor +dostoevsky)",
      "default_field":"title"
    }
  }
}
//针对多字段的query_string查询
{
  "query":{
    "query_string":{
      "query":"crime punishment",
      "fields":["title","otitle"],
      "use_dis_max":true
    }
  }
}
```

 >  标识符查询

 + 
```json
{
  "query":{
    "ids":{
      "type":"book",
      "values":["10","11","12","13"]
    }
  }
}
```


