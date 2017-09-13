package nsw.base.core.dao.persistence;

import java.util.List;

import nsw.base.core.dao.entity.RolePermission;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.utils.paging.Pagination;

public interface RolePermissionMapper extends BaseDaoMapper<RolePermission>{
	
	
	RolePermission getById(String id);
	int deleteById(String id);
	void save(RolePermission RolePermission);
	void update(RolePermission RolePermission);
	List<RolePermission> findPageData(Pagination pagination);
	
	

	


	
	/**
	 * <p>方法描述: 根据角色ID获取权限集合</p>
	 * <p>方法备注: </p>
	 * @param roleId
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月23日 下午4:48:33</p>
	 */
	List<String> getPermissionIdList(String roleId);
	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param pid
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月24日 下午5:37:38</p>
	 */
	List<String> getRoleByPid(String pid);
	
	
	
	/**
	 * <p>方法描述: 角色表与权限表之间桥表的新增，创建。</p>
	 * <p>方法备注: </p>
	 * @param rp
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年3月3日 上午11:24:41</p>
	 */
	int create(RolePermission rp);

	
}

