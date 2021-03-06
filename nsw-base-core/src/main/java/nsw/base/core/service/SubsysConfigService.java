package nsw.base.core.service;

import java.util.List;

import nsw.base.core.dao.entity.SubsysConfig;
import nsw.base.core.utils.paging.Pagination;

public interface SubsysConfigService {
	public Pagination getSubsysConfigPage(int pageNo, int size, String sort, SubsysConfig subsysConfig);
	public List<SubsysConfig> getAllList();
	public SubsysConfig getById(String subsysConfigId);
	public void add(SubsysConfig subsysConfig);
	public void edit(SubsysConfig subsysConfig);
	public void del(String scid);
}
