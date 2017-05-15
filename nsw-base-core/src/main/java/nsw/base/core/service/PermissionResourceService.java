package nsw.base.core.service;

import java.util.List;
import java.util.Set;

import nsw.base.core.dao.entity.PermissionResource;
import nsw.base.core.dao.entity.Resource;
import nsw.base.core.service.base.ExportService;
import nsw.base.core.service.base.ImportService;
import nsw.base.core.utils.paging.Pagination;


public interface PermissionResourceService extends ImportService<PermissionResource>, ExportService<PermissionResource>{
	/** 
	 * 获取用户-角色桥表列表
	 * @param pageNo 页数
	 * @param size 每页多少条记录 
	 * @param t 查询条件
	 * @return Pagination
	 * @throws ServiceException 
	 */
	public Pagination getPermissionResourcePage(int pageNo, int size, String sort, PermissionResource pres);
	public void edit(PermissionResource pres);
	public void add(PermissionResource pres);
	public void del(String roleResId);
	public void delByPermissionId(String permissionId);
	
	
	
	/**
	 * <p>方法描述: 创建新 用户-角色桥表 关联</p>
	 * <p>方法备注: </p>
	 * @param user
	 * @return
	 */
	public int create(PermissionResource pres);
	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param pid
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年3月6日 上午11:18:07</p>
	 */
	public List<Resource> getResourceByPid(String pid);
	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param resourceIdSet
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年3月6日 上午11:22:12</p>
	 */
	public Set<String> findPermissionsByResourceId(Set<String> resourceIdSet);
	

    
	
	
}
