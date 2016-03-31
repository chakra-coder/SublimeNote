<!-- toc -->
# JavaAPI

### ES-API

> **生成索引**

```java
public class IndexTest {

	JestClientFactory factory;
	JestClient client;
	@Before
	public void before() {

		factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(
				"http://localhost:9200").multiThreaded(true).build());
		client = factory.getObject();
	}
	
	@Test
	public void index_t1() throws IOException {

		ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
		settings.put("number_of_shards",1);
		settings.put("number_of_replicas",1);
		
		client.execute(new CreateIndex.Builder("articles").settings(settings.build().getAsMap()).build());
	}
	
	@Test
	public void type_t1() throws IOException {
		Article source = new Article();
		source.setId("1");
		source.setAuthor("John Ronald Reuel Tolkien");
		source.setContent("The Lord of the Rings is an epic high fantasy novel");
		
		
		Index index = new Index.Builder(source).index("articles").type("tweet").build();
		client.execute(index);
	}

}

```

### ES GenJson
>JavaAPI 生成Json

```java
package cn.dishui;

import java.io.IOException;

import org.junit.Test;
import org.elasticsearch.common.xcontent.XContentBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
public class GenJson {
	/*
	 * {
		 "type" : "jdbc",
		 "jdbc" : {
		     "url" : "jdbc:mysql://localhost:3306/b2b",
		     "user" : "root",
		     "password" : "111111",
		     "sql" : "select ma.id as \"_id\", ma.* from mlq_attribute ma",
		     "index": "b2b",
		     "type": "mlq_brand",
		     "type_mapping": {
		         "properties": {
		            "brand_name": {
		               "type": "string","store":"yes","index":"analyzed","analyzer":"ik_analyzer"
		            }
		         }
		      }
		 }
		}
	 * */
	 
 	JestClientFactory factory;
	JestClient client;
	@Before
	public void before() {

		factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(
				"http://localhost:9200").multiThreaded(true).build());
		client = factory.getObject();
	}
	@Test
	public void test_json1() throws IOException{
		XContentBuilder builder = jsonBuilder()
			    .startObject()
			        .field("type", "jdbc")
			        .field("jdbc")
			        	.startObject()
			        		.field("url","jdbc:mysql://localhost:3306/b2b")
			        		.field("user","root")
			        		.field("password","111111")
			        		.field("sql","select ma.id as \"_id\", ma.* from mlq_attribute ma")
			        		.field("index","b2b")
			        		.field("type","mlq_attribute")
			        		.field("type_mapping")
			        			.startObject()
			        				.field("properties")
					        			.startObject()
					        				.field("brand_name")
							        			.startObject()
							        				.field("type","string")
							        				.field("store","yes")
							        				.field("index","analyzed")
							        				.field("analyzer","ik_analyzer")
							        			.endObject()
					        			.endObject()
			        			.endObject()
			        	.endObject()
			    .endObject();
		System.out.println(builder.string());
		String index = "_river";
		String type = "mlq_attribute";
		String id = "_meta";
        DocumentResult result = client.execute(
                new Index.Builder(builder.string())
                        .index(index)
                        .type(type)
                        .id(id)
                        .refresh(true)
                        .build()
        );
        
        System.out.println(result);
	}

```

> parent/child


```java
package cn.dishui;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Before;
import org.junit.Test;
public class GenJson {
	JestClientFactory factory;
	JestClient client;
	@Before
	public void before() {

		factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(
				"http://localhost:9200").multiThreaded(true).build());
		client = factory.getObject();
	}
	/**
	 *  PUT /b2b/mlq_product/_mapping
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
	 */
	@Test
	public void test_maping(){
		XContentBuilder builder = null;
		try {
			builder = jsonBuilder()
				    .startObject()
				        .startObject("mlq_product")
				        	.startObject("_parent")
				        		.field("type","mlq_goods")
				        	.endObject()
				        	.startObject("properties")
				        		.startObject("product_name")
				        			.field("type","string")
				        			.field("store","yes")
				        			.field("index","analyzed")
				        			.field("analyzer","ik_analyzer")
				        		.endObject()
				        	.endObject()
			        	.endObject()
				    .endObject();
			System.out.println(builder.string());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String jsonBuild(String sql,String index,String type){
		XContentBuilder builder = null;
		try {
			builder = jsonBuilder()
				    .startObject()
				        .field("type", "jdbc")
				        .field("jdbc")
				        	.startObject()
				        		.field("url","jdbc:mysql://localhost:3306/b2b")
				        		.field("user","root")
				        		.field("password","111111")
				        		.field("sql",sql)
				        		.field("index",index)
				        		.field("type",type)
				        	.endObject()
				    .endObject();
			if(builder != null){
				return builder.string();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Test
	public void indexTest1(){
		XContentBuilder mapping = null;
		String sql = "select mb.id as \"_id\", mb.* from mlq_brand mb";
		String index = "b2b";
		String type = "mlq_brand";
		try {
			mapping = jsonBuilder()
					.startObject()
						.field("brand_name")
							.startObject()
								.field("type","string")
								.field("store","yes")
								.field("index","analyzed")
								.field("analyzer","ik_analyzer")
							.endObject()
					.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//String jsonBuild = jsonBuild(sql,index,type,mapping);
		//System.out.println(jsonBuild);
	}
	
	@Test
	public void indexTest() throws IOException{
		XContentBuilder builder = jsonBuilder()
			    .startObject()
			        .field("type", "jdbc")
			        .field("jdbc")
			        	.startObject()
			        		.field("url","jdbc:mysql://localhost:3306/b2b")
			        		.field("user","root")
			        		.field("password","111111")
			        		.field("sql","select ma.id as \"_id\", ma.* from mlq_attribute ma")
			        		.field("index","b2b")
			        		.field("type","mlq_attribute")
			        		.field("type_mapping")
			        			.startObject()
			        				.field("properties")
					        			.startObject()
					        				.field("attr_name")
							        			.startObject()
							        				.field("type","string")
							        				.field("store","yes")
							        				.field("index","analyzed")
							        				.field("analyzer","ik_analyzer")
							        			.endObject()
					        			.endObject()
			        			.endObject()
			        	.endObject()
			    .endObject();
		System.out.println(builder.string());
		String index = "_river";
		String type = "mlq_attribute";
		String id = "_meta";
        DocumentResult result = client.execute(
                new Index.Builder(builder.string())
                        .index(index)
                        .type(type)
                        .id(id)
                        .refresh(true)
                        .build()
        );
        
        System.out.println(result);
	}
	
}

```