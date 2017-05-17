package nsw.base.core.service;

import nsw.base.core.dao.entity.SubsysConfig;
import nsw.base.core.utils.paging.Pagination;

public interface SubsysConfigService {
	public Pagination getSubsysConfigPage(int pageNo, int size, String sort, SubsysConfig subsysConfig);
	public void add(SubsysConfig subsysConfig);
	public void edit(SubsysConfig subsysConfig);
	public void del(String scid);
}
