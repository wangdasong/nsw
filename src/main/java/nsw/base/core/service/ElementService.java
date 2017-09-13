package nsw.base.core.service;

import java.util.List;

import nsw.base.core.dao.entity.AttConfig;
import nsw.base.core.dao.entity.Element;
import nsw.base.core.utils.paging.Pagination;

public interface ElementService {
	/** 
	 * 获取自己属性列表
	 * @param pageNo 页数
	 * @param size 每页多少条记录 
	 * @param t 查询条件
	 * @return Pagination
	 * @throws ServiceException 
	 */
	public Pagination getMyAttrConfigPage(int pageNo, int size, String sort, Element element);
	public List<AttConfig> getMyAttConfigList(Element element);
	public Element getElementById(String id);
	public List<Element> getElementPath(String id);
	public void editElement(Element element);
	public void addElement(Element element);
	public void delElement(Element element);
	public Element saveOrUpdateElement(Element element);
	public Element copyElementAttsFromTemplet(Element templetElement, Element myElement);
}
