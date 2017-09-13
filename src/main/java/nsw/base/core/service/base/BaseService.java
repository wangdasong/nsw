package nsw.base.core.service.base;

import java.util.List;

import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.utils.paging.Pagination;

public interface BaseService<T extends BaseEntity>  {
	public T addEntity(T t);
	public T editEntity(T t);
	public T delEntity(String id);
	public T getEntityById(String id);
	public T getEntityDetailById(String id);
	public List<T> getEntityListByCondition(T t);
	public Pagination getEntityPage(int pageNo, int size, String sort, T t);
}
