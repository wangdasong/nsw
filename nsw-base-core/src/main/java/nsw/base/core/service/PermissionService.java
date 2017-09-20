package nsw.base.core.service;

import java.util.List;

import nsw.base.core.dao.entity.Permission;
import nsw.base.core.service.base.ExportService;
import nsw.base.core.service.base.ImportService;
import nsw.base.core.utils.paging.Pagination;

public interface PermissionService extends ImportService<Permission>, ExportService<Permission>{
	
	Pagination getPermissionPage(int pageNumber, int pageSize, String pageSort, Permission permission);
	public void add(Permission permission);
	public void edit(Permission permission);
	public void del(String pid);


	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月17日 下午4:33:25</p>
	 */
	List<Permission> findAll();


	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param id
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月23日 下午4:44:43</p>
	 */
	Permission findById(String id);



	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param permission
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月23日 下午5:45:14</p>
	 */
	int createPermission(Permission permission);
	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param permission
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月23日 下午5:57:10</p>
	 */
	int update(Permission permission);
	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param id
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月23日 下午5:58:52</p>
	 */
	int lockPermission(String id); 
    
    
}  