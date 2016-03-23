package com.bizoss.trade.base;

import java.util.List;

public class BaseServiceWarper<T> implements BaseService<T>{
	public void save(T t) {
		
	}

	public void update(T t) {
	}
	
	public T get(String id) {
		return null;
	}

	public List<T> getAll() {
		return null;
	}

	public List<T> getAll(BaseQuery bqm) {
		return null;
	}

	public Integer getCount(BaseQuery bqm) {
		return null;
	}

	public List<T> getAll(BaseQuery bqm, Integer pageNum,
			Integer pageCount) {
		return null;
	}

	public void delete(T t) {
		
	}
}
