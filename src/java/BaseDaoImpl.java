package com.bizoss.trade.base;

import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;  
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

@SuppressWarnings("deprecation")
public class BaseDaoImpl<T> extends SqlMapClientDaoSupport implements
		BaseDao<T> {
	private String className;
	{
		Type genType = getClass().getGenericSuperclass();   
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();   
		Class<T> clazz =  (Class)params[0];  
		className = clazz.getSimpleName();
	}
	

	public void save(T t) {
		getSqlMapClientTemplate().insert("insert" + className, t);
	}

	public List<T> getAll() {
		return getSqlMapClientTemplate().queryForList("queryAll" + className);
	}

	public void update(T t) {
		getSqlMapClientTemplate().update("update" + className, t);
	}

	public T get(String id) {
		return (T) getSqlMapClientTemplate().queryForObject("query"+className+"ById", id);
	}

	public List<T> getAll(BaseQuery bqm) {

		return getSqlMapClientTemplate().queryForList("queryAll" + className + "ByObj",
				bqm);
	}

	public Integer getCount(BaseQuery bqm) {
		// getCountByObj
		Object count = getSqlMapClientTemplate().queryForObject(
				"queryCountBy" + className, bqm);
		return Integer.parseInt(count.toString());
	}

	public List<T> getAll(BaseQuery bqm, Integer pageNum, Integer pageCount) {
		return getSqlMapClientTemplate().queryForList("queryAll" + className+"Page",
				bqm, pageNum, pageCount);
	}

	public void delete(T t) {
		getSqlMapClientTemplate().delete("delete" + className, t);
	}

}
