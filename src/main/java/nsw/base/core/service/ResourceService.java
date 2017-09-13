package nsw.base.core.service;

import java.util.List;
import java.util.Set;

import nsw.base.core.dao.entity.Resource;
import nsw.base.core.service.base.ExportService;
import nsw.base.core.service.base.ImportService;
import nsw.base.core.utils.paging.Pagination;

public interface ResourceService extends ImportService<Resource>, ExportService<Resource> {
	
	
	Pagination getPermissionPage(int pageNumber, int pageSize, String pageSort, Resource resource);
	
	Resource findById(String resourceId); 
	
	
	
	
	
	
	
	
	
	//得到资源对应的权限字符串
    //Set<String> findPermissions(Set<String> resourceIds);  
    
    
    //根据用户权限得到菜单 (即权限内可以看到的菜单列表)
    //List<Resource> findMenus(Set<String> permissions);


	/**
	 * <p>方法描述: 获取全部资源列表</p>
	 * <p>方法备注: </p>
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月17日 下午4:33:25</p>
	 */
	List<Resource> findAll();
	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param username
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年3月6日 上午11:30:45</p>
	 */
	List<Resource> getResourceList(String username);
	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param username
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年3月8日 上午11:11:10</p>
	 */
	Set<String> getResourceSet(String username);


    
    
}  