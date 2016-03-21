package com.bizoss.es.createIndex.search;

import static com.google.common.base.Preconditions.checkElementIndex;
import io.searchbox.core.SearchResult.Hit;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.bizoss.es.createIndex.genQuery.GenQueryString;
import com.bizoss.es.createIndex.impl.Export_funcImpl;
import com.bizoss.es.createIndex.impl.Mlq_goods_funcImpl;
import com.bizoss.es.createIndex.search.bean.Es_Mlq_goods_dis_base;
import com.bizoss.es.createIndex.search.bean.Es_Mlq_product_dis;
import com.bizoss.es.createIndex.search.bean.Es_Mlq_product_dis_base;
import com.bizoss.es.createIndex.search.bean.FilterBean;
import com.bizoss.es.createIndex.search.bean.SearchResult;
import com.bizoss.frame.util.ObjUtils;
import com.bizoss.frame.util.PackToList;
import com.bizoss.trade.mlq_goods.Mlq_goods;
import com.bizoss.trade.mlq_product.Mlq_product;
import com.bizoss.trade.mlq_product.Mlq_productInfo;
import com.bizoss.trade.mlq_product_picture.Mlq_product_picture;
import com.bizoss.trade.ti_advinfo.BannerList;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.stream.JsonWriter;

public class Search {
	private static Logger logger = Logger.getLogger(Search.class);
	private GenQueryString gqs = new GenQueryString();
	private GetSearchAll gsa = new GetSearchAll();
	/**
	 * 移动端搜索
	 * @param method
	 * @param request
	 * @return
	 */
	public String mobileSearch(String method, HttpServletRequest request) {
		String str = "";
		switch(method){
			case("MobileFilterSearch"):
				str = getMobileFilterSearch(request);
			break;
			case("MobileDetailByProductId"):
				String product_id = request.getParameter("product_id");
				str = getMobileDetailByProductId(product_id);
			break;
			case("MobileProductNorm"):
				str = getMobileProductNorm(request.getParameter("product_id"));
			break;
			case("MobileFilter"):
				str = getMobileFilter(request);
			break;
		}
		return str;
	}
	/**
	 * Pc端搜索
	 * @param method
	 * @param request
	 * @return
	 */
	public SearchResult pcSearch(String method, HttpServletRequest request){
		SearchResult sr = null;
		switch(method){
			case("PcFilterSearch"):
				sr = getPcFilterSearch(request);
			break;
			case("CatFilterSearch"): //管理类目 过滤
				sr = getCatFilterSearch(request);
			break;
			case("PcDetailByProductId"):
				sr = getPcDetailByProductId(request.getParameter("product_id"));
			break;
		}
		return sr;
	}

	/**
	 * 筛选,搜索
	 * @param request
	 * @return
	 */
	private SearchResult getFilterSearch(HttpServletRequest request){
		String pageNo = request.getParameter("pageNo");
		String from = "";
		if(ObjUtils.empty(pageNo)){
			from = request.getParameter("page");
		}else{
			from = pageNo;
		}
		String cat_id = request.getParameter("cat_id");
		if(ObjUtils.empty(cat_id)){
			Object tmp_cat_id = request.getAttribute("cat_id");
			if(ObjUtils.notEmpty(tmp_cat_id)){
				cat_id = tmp_cat_id.toString();
			}
		}
//		logger.info(cat_id);
		String brand_name = request.getParameter("brand_name");
		String product_name = request.getParameter("product_name");
		String region = request.getParameter("region");
		String seller_nick = request.getParameter("seller_nick");
		String shop_id = request.getParameter("shop_id");
		String price = request.getParameter("price");
		//String priceFrom = request.getParameter("priceFrom");
		//String priceTo = request.getParameter("priceTo");
		String search_key = request.getParameter("search_key");
		String priceFrom = "";
		String priceTo = "";
		if(ObjUtils.notEmpty(price)){
			String[] prices = price.split("-");
			if(prices.length>1){
				priceFrom = prices[0];
				priceTo = prices[1];
			}else{
				priceFrom = prices[0];
				priceTo = "999999";
			}
		}

		FilterBean fb = new FilterBean();
		if(ObjUtils.notEmpty(from)){
			fb.setFrom(Integer.parseInt(from));
		}
		if(ObjUtils.notEmpty(priceFrom)){
			fb.setPriceFrom(Double.parseDouble(priceFrom));
		}
		if(ObjUtils.notEmpty(priceTo)){
			fb.setPriceTo(Double.parseDouble(priceTo));
		}
		fb.setCat_id(cat_id);
		fb.setBrand_name(brand_name);
		fb.setProduct_name(product_name);
		fb.setRegion(region);
		fb.setSeller_nick(seller_nick);
		fb.setSearchKey(search_key);
		fb.setShop_id(shop_id);
		fb.setAggsSize(20);


		//logger.info(fb);
		//生成查询语句
		String filterSearch = gqs.filterSearchWithScore(fb);
		logger.info(filterSearch);
		Map<String, Object> search = gsa.getSearch(Mlq_goods.class,filterSearch);
		List<Hit<Mlq_goods, Void>> hits = (List<Hit<Mlq_goods, Void>>) search.get("hits");
		Integer total = (Integer) search.get("total");
//		logger.info(total);
		List<Mlq_goods> mg_l = PackToList.packHit(hits, Mlq_goods.class, new Mlq_goods_funcImpl());
		//计算分页
		Integer totalPages = 1;

		if ((total % 10) == 0) {
			totalPages = total / 10;
        } else {
        	totalPages = total / 10 + 1;
        }

		SearchResult sr = new SearchResult();
		sr.setMg_l(mg_l);
		sr.setTotalPages(totalPages);
		sr.setTotalDataCount(total);
		return sr;
	}

	/**
	 * 移动端 筛选,搜索
	 * @param request
	 * @return json
	 */
	private String getMobileFilterSearch(HttpServletRequest request){
		SearchResult sr = getFilterSearch(request);
	    StringWriter sw = new StringWriter();
	    JsonWriter jw = new JsonWriter(sw);
	    try {
			jw.beginObject();
	        jw.name("totalPages");
	        jw.value(sr.getTotalPages());
		    jw.name("goods");
		    jw.beginArray();
		    for(Mlq_goods mg:sr.getMg_l()){
		      if(mg.getMp_l()!=null && mg.getMp_l().size()>0){
			      jw.beginObject();
			        jw.name("img");
					jw.value(BannerList.serverUrl+mg.getImg_large());
			        jw.name("name");
			        jw.value(mg.getTitle());
			        jw.name("address");
			        jw.value(mg.getMp_l().get(0).getRepository_region());
			        jw.name("price");
			        jw.value(mg.getMp_l().get(0).getProduct_price());
			        jw.name("unit");
			        jw.value(mg.getMp_l().get(0).getUnit());
			        jw.name("numb");
			        jw.value(mg.getMp_l().get(0).getStock_quantity());
			        jw.name("url");
			        jw.value(BannerList.serverUrl+"/supplyDetail.html?product_id="+mg.getMp_l().get(0).getId());
			        jw.name("product_id");
			        jw.value(mg.getMp_l().get(0).getId());
			      jw.endObject();
		      }
	    	}
		    jw.endArray();
		jw.endObject();
	    } catch (IOException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}


	/**
	 * PC端 筛选,搜索
	 * @param request
	 * @return
	 */
	private SearchResult getPcFilterSearch(HttpServletRequest request){
		SearchResult sr = getFilterSearch(request);
		SearchResult sr_filter = getFilter(request);

		sr.setBrandNameList(sr_filter.getBrandNameList());
		sr.setProductNameList(sr_filter.getProductNameList());
		sr.setSellerNickList(sr_filter.getSellerNickList());
		return sr;
	}
	/**
	 * PC端 管理类目 过滤
	 * @param request
	 * @return
	 */
	private SearchResult getCatFilterSearch(HttpServletRequest request) {
		SearchResult sr = getFilterSearch(request);
		return sr;
	}

	/**
	 * 手机端商品详情
	 * @param pUrl
	 * @return
	 */
	private String getMobileDetailByProductId(String product_id){

		//logger.info(product_id);
		//测试写死
		//product_id = "201509151s76W6W";
		GetById gbi = new GetById();
		Mlq_goods mg = null;
		Mlq_product mp = gbi.getMlq_productById(product_id);

		StringWriter sw = new StringWriter();
		JsonWriter jw = new JsonWriter(sw);
		try {
		//判断是否为空
		if(!Optional.fromNullable(mp).isPresent()){

		}else{
			if(!Strings.isNullOrEmpty(mp.getGoods_id())){
				mg = gbi.getMlq_goodsById(mp.getGoods_id());

				jw.beginObject();
				jw.name("imgOriginal");
				jw.beginArray();
					for(Mlq_product_picture mpp : MoreObjects.firstNonNull(mg.getMpp_l(), Collections.<Mlq_product_picture> emptyList())){
						jw.value(BannerList.serverUrl+mpp.getImg_original());
					}
					try {
						checkElementIndex(0,mg.getMpp_l().size());
					} catch (Exception e) {
						jw.value(BannerList.serverUrl+mg.getImg_large());
					}
				jw.endArray();
				    jw.name("shopName");
				    jw.value(mg.getNew_seller_nick());
				    jw.name("title");
				    jw.value(mg.getTitle());
				    jw.name("productPrice");
				    jw.value(mp.getProduct_price());
				    jw.name("stockQuantity");
				    jw.value(mp.getStock_quantity());
				    jw.name("packType");
				    jw.value(mp.getPackage_type());
				    jw.name("deliveryDay");
				    jw.value(mp.getDelivery_day());
				    jw.name("saleRegion");
				    jw.value(mp.getSale_region());
				    jw.name("minQuantity");
				    jw.value(mp.getMin_quantity());
				    jw.name("repositoryRegion");
				    jw.value(mp.getRepository_region());
				jw.endObject();
				}
			}
	    } catch (IOException e) {
			e.printStackTrace();
		}
		//logger.info(sw.toString());
		return sw.toString();
	}
	/**
	 * Pc端,商品详情
	 * @param product_id
	 * @return
	 */
	private SearchResult getPcDetailByProductId(String product_id){
		GetById gbi = new GetById();
		Mlq_goods mg = null;
		SearchResult sr = new SearchResult();
		Es_Mlq_product_dis_base empdb = gbi.getEs_Mlq_product_disById(product_id);
		if(ObjUtils.notEmpty(empdb)){ //分销单品
			Es_Mlq_goods_dis_base emgdb = gbi.getEs_Mlq_goods_disById(product_id);
			sr.setEs_Mlq_goods_dis_base(emgdb);
			sr.setEs_Mlq_product_dis_base(empdb);
		}else{
			Mlq_product mp = gbi.getMlq_productById(product_id);
			if(ObjUtils.notEmpty(mp) && ObjUtils.notEmpty(mp.getGoods_id())){
				mg = gbi.getMlq_goodsById(mp.getGoods_id());
			}
			sr.setMg(mg);
			sr.setMp(mp);
		}
		return sr;
	}

	/**
	 * 获取商品规格参数
	 * @param product_id
	 * @return
	 */
	private String getMobileProductNorm(String product_id){
		Mlq_productInfo mpi = new Mlq_productInfo();
		return mpi.getProductNormStr(product_id);
	}

	/**
	 * 现货市场列表 | 筛选
	 * @param request
	 * @return
	 */
	private String getMobileFilter(HttpServletRequest request){
		SearchResult sr = getFilter(request);

	    StringWriter sw = new StringWriter();
	    JsonWriter jw = new JsonWriter(sw);
	    try {
	    	jw.beginObject();
	    	  //品牌
	    	  jw.name("ret1");
	    	  jw.beginArray();
	    	  for(String tmp_bn:sr.getBrandNameList()){
	    	    jw.beginObject();
	    	      jw.name("name");
	    	      jw.value(tmp_bn);
	    	    jw.endObject();
	    	  }
	    	  jw.endArray();
	    	  //型号
	    	  jw.name("ret2");
	    	  jw.beginArray();
	    	  for(String tmp_pn:sr.getProductNameList()){
	    	    jw.beginObject();
	    	      jw.name("name");
	    	      jw.value(tmp_pn);
	    	    jw.endObject();
	    	  }
	    	  jw.endArray();
	    	  //供应商
	    	  jw.name("ret4");
	    	  jw.beginArray();
	    	  for(String tmp_sn:sr.getSellerNickList()){
	    	    jw.beginObject();
	    	      jw.name("name");
	    	      jw.value(tmp_sn);
	    	    jw.endObject();
	    	  }
	    	  jw.endArray();
	    	jw.endObject();
	    } catch (IOException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	private SearchResult getFilter(HttpServletRequest request){
		String from = request.getParameter("page");
		String cat_id = request.getParameter("cat_id");
		String brand_name = request.getParameter("brand_name");
		String region = request.getParameter("region");
		String seller_nick = request.getParameter("seller_nick");
		String price = request.getParameter("price");
		String search_key = request.getParameter("search_key");
		String product_name = request.getParameter("product_name");
		String priceFrom = "";
		String priceTo = "";
		if(ObjUtils.notEmpty(price)){
			String[] prices = price.split("-");
			if(prices.length>1){
				priceFrom = prices[0];
				priceTo = prices[1];
			}else{
				priceFrom = prices[0];
				priceTo = "999999";
			}
		}
		FilterBean fb = new FilterBean();
		if(ObjUtils.notEmpty(from)){
			fb.setFrom(Integer.parseInt(from));
		}
		if(ObjUtils.notEmpty(priceFrom)){
			fb.setPriceFrom(Double.parseDouble(priceFrom));
		}
		if(ObjUtils.notEmpty(priceTo)){
			fb.setPriceTo(Double.parseDouble(priceTo));
		}
		fb.setCat_id(cat_id);
		fb.setBrand_name(brand_name);
		fb.setProduct_name(product_name);
		fb.setRegion(region);
		fb.setSeller_nick(seller_nick);
		fb.setSearchKey(search_key);
		fb.setAggsSize(20);
		//logger.info(fb);
		//生成查询语句
		String filterSearch = gqs.filterSearch(fb);
		//logger.info(filterSearch);
		Map<String, Object> map = gsa.getSearch(filterSearch);
		List<String> brandName_l = (List<String>) map.get("brand_names");
		List<String> productName_l = (List<String>) map.get("product_names");
		List<String> sellerNick_l = (List<String>) map.get("seller_nicks");

		SearchResult sr = new SearchResult();
		sr.setBrandNameList(brandName_l);
		sr.setProductNameList(productName_l);
		sr.setSellerNickList(sellerNick_l);
		return sr;
	}
	/**
	 * 导出所有商品
	 * @return
	 */
	public List<Map<String,Object>> getExport(){
		List<Map<String,Object>> ll = new ArrayList<Map<String,Object>>();
		//生成查询语句
		String exportSearch = gqs.genExport();
//		logger.info(filterSearch);
		List<Hit<Mlq_goods, Void>> hits = gsa.getExportSearch(exportSearch);
		List<Mlq_goods> mg_l = PackToList.packHit(hits, Mlq_goods.class, new Export_funcImpl());

	    Function<Mlq_goods, List<Map<String,Object>>> function = new Function<Mlq_goods, List<Map<String,Object>>>() {
			public List<Map<String,Object>> apply(Mlq_goods mg) {
				List<Map<String,Object>> m_l = new ArrayList<Map<String,Object>>();
				if(ObjUtils.notEmpty(mg.getMp_l())){
					for(Mlq_product mp: mg.getMp_l()){
						Map<String,Object> tmp_m = new HashMap<String, Object>();
						tmp_m.put("title", mg.getTitle());
						tmp_m.put("brand_name", mg.getBrand_name());
						tmp_m.put("seller_nick", mg.getNew_seller_nick());
						tmp_m.put("is_could_sale", mp.getIs_could_sale());
						tmp_m.put("brand_price", mp.getBrand_price());
						tmp_m.put("product_name", mp.getProduct_name());
						tmp_m.put("sale_region", mp.getSale_region());
						tmp_m.put("sitory_region", mp.getRepository_region());
						tmp_m.put("delivery_day", mp.getDelivery_day());
						tmp_m.put("min_quantity", mp.getMin_quantity());
						tmp_m.put("stock_quantity", mp.getStock_quantity());
						tmp_m.put("brand_price", mp.getPackage_type());
						tmp_m.put("unit", mp.getUnit());

						m_l.add(tmp_m);
					}
				}
				return m_l;
			}

	    };
	    List<List<Map<String, Object>>> transform_l = Lists.transform(mg_l, function);

	    for(List<Map<String, Object>> tm_l:transform_l){
	    	for(Map<String, Object> tm: tm_l){
	    		ll.add(tm);
	    	}
	    }
		return ll;
	}

}
