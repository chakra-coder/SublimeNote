# ES-issues

### `Doc`更新出错
+ error
```json
{
   "error": "ElasticsearchIllegalArgumentException[failed to execute script]; nested: ScriptException[scripts of type [inline], operation [update] and lang [groovy] are disabled]; ",
   "status": 400
}
```
+ fix
```bash
#vim ${ES_HOME}/config/elasticsearch.yml
#添加
script.disable_dynamic: false
```

### `jdbc-importer`错误
+ `ERROR`
```
java.sql.SQLException: No suitable driver found for jdbc:mysql://192.168.137.1:3306/mailiqing
```
+ Fix

    将`mysql-connector-java-5.1.33.jar`放到`$JAVA_HOME/jre/lib/ext`路径下