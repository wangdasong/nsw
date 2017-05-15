package nsw.base.core.service;

import java.util.List;
import java.util.Set;

import nsw.base.core.dao.entity.Permission;
import nsw.base.core.dao.entity.RolePermission;
import nsw.base.core.service.base.ExportService;
import nsw.base.core.service.base.ImportService;
import nsw.base.core.utils.paging.Pagination;


public interface RolePermissionService extends ImportService<RolePermission>, ExportService<RolePermission>{
	/** 
	 * 获取用户-角色桥表列表
	 * @param pageNo 页数
	 * @param size 每页多少条记录 
	 * @param t 查询条件
	 * @return Pagination
	 * @throws ServiceException 
	 */
	public Pagination getRolePermissionPage(int pageNo, int size, String sort, RolePermission rp);
	public void edit(RolePermission rp);
	public void add(RolePermission rp);
	public void del(String userRoleId);
	
	
	
	/**
	 * <p>方法描述: 创建新 用户-角色桥表 关联</p>
	 * <p>方法备注: </p>
	 * @param user
	 * @return
	 */
	public int create(RolePermission rp);
	
	List<Permission> getPermissionListByRoleId(String roleId);

	List<String> getRoleByPid(String pid);

	public List<Permission> getPermissionList(String username);

	public Set<String> findRoleByPid(Set<String> permissionIdSet);
    
	
	
}
