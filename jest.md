## Jest-API

### `JestClient`单例
+ 
```java
public class InitClient {    
    private static class LazyHolder {    
       static final JestClientFactory factory = new JestClientFactory();
       static JestClient client;
       static{
    	   factory.setHttpClientConfig(
                   new HttpClientConfig
                           .Builder("http://localhost:9200")
                           .multiThreaded(true).build()
           );
    	   client = factory.getObject();
       }
    }    
    private InitClient (){}    
    public static final JestClient getInstance() { 
    	return LazyHolder.client;
    }    
}   
```