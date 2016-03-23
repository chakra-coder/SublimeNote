package com.bizoss.trade.base;

import java.util.List;

public interface BaseDao<T> {
	public void save(T t);

	public void update(T t);
	
	public T get(String id);

	public List<T> getAll();

	public List<T> getAll(BaseQuery bqm);

	public Integer getCount(BaseQuery bqm);

	public List<T> getAll(BaseQuery bqm, Integer pageNum,
			Integer pageCount);

	public void delete(T t);
}
