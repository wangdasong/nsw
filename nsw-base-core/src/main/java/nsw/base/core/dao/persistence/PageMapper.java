package nsw.base.core.dao.persistence;

import java.util.List;

import nsw.base.core.dao.entity.Page;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.utils.paging.Pagination;

public interface PageMapper extends BaseDaoMapper<Page>{

	/**
	 * 获取页面总数目
	 * @param pagination
	 * @return int
	 */
	int getCount(Pagination pagination);

	/**
	 * 获取某一页页面信息的集合
	 * @param pagination
	 * @return 某页面信息集合
	 */
	List<Page> findPageData(Pagination pagination);
}

