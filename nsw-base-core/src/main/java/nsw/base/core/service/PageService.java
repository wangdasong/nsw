package nsw.base.core.service;

import nsw.base.core.dao.entity.Page;
import nsw.base.core.utils.paging.Pagination;

public interface PageService {
	public Pagination getPagePage(int pageNo, int size, String sort, Page page);
	public Page getPageDetailById(String pageId);
	public void editPage(Page page);
	public void addPage(Page page);
	public void delPage(Page page);
	public Page saveOrUpdatePage(Page page);
}
