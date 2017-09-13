/**
 * 
 */
package nsw.base.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nsw.base.core.dao.entity.Permission;
import nsw.base.core.dao.entity.Resource;
import nsw.base.core.dao.entity.Role;
import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.persistence.PermissionMapper;
import nsw.base.core.dao.persistence.PermissionResourceMapper;
import nsw.base.core.dao.persistence.ResourceMapper;
import nsw.base.core.dao.persistence.RoleMapper;
import nsw.base.core.dao.persistence.RolePermissionMapper;
import nsw.base.core.dao.persistence.UserMapper;
import nsw.base.core.dao.persistence.UserRoleMapper;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.service.ResourceService;
import nsw.base.core.service.base.DataSrcService;
import nsw.base.core.service.impl.base.BaseServiceImpl;
import nsw.base.core.utils.Assert;
import nsw.base.core.utils.ExceptionCode;
import nsw.base.core.utils.paging.Pagination;
import nsw.base.core.utils.paging.TablePagingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目名称：finance<br>
 * *********************************<br>
 * <P>类名称：ResourceServiceImpl</P>
 * *********************************<br>
 * <P>类描述：</P>
 * 创建人：wenjie.zhu<br>
 * 创建时间：2017年2月20日 上午9:59:14<br>
 * 修改人：wenjie.zhu<br>
 * 修改时间：2017年2月20日 上午9:59:14<br>
 * 修改备注：<br>
 * @version 1.0<br>    
 */

@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource>  implements ResourceService, DataSrcService<Resource>, TablePagingService {
	
	@Autowired
	ResourceMapper resMapper;
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	UserRoleMapper urMapper;
	
	@Autowired
	RoleMapper roleMapper;
	
	@Autowired
	RolePermissionMapper rpMapper;
	
	@Autowired
	PermissionMapper pMapper;
	
	@Autowired
	PermissionResourceMapper presMapper;

	

	@Override
	public BaseDaoMapper<Resource> getCurrDaoMapper() {
		return resMapper;
	}
	
	@Override
	public int getCount(Pagination page) {
		return resMapper.getCount(page);
	}
	
	@Override
	public List<Resource> findAll() {
		return resMapper.findAll();
	}
	
	@Override
	public Resource findById(String resourceId) {
		return resMapper.getById(resourceId);
	}

	@Override
	public Pagination getPermissionPage(int pageNo, int size, String sort, Resource resource) {
		Pagination pr = new Pagination(pageNo, size, sort, this);
		try {
			pr.doPage(resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pr;
	}

	@Override
	public List<Resource> findPageData(Pagination page) {
		List<Resource> resList = resMapper.findPageData(page);
		return resList;
	}

	@Override
	public List<DataSrcEntity> getDataSrcListByCondition(Resource t) {
		List<Resource> resList = resMapper.getListByCondition(t);
		List<DataSrcEntity> dataSrcEntityList = new ArrayList<DataSrcEntity>();
		for(Resource currResource : resList){
			DataSrcEntity currrDataSrcEntity = new DataSrcEntity();
			currrDataSrcEntity.setId(currResource.getId());
			currrDataSrcEntity.setLabel(currResource.getName());
			currrDataSrcEntity.setValue(currResource.getId());
			dataSrcEntityList.add(currrDataSrcEntity);
		}
		return dataSrcEntityList;
	}

	
	
	
	@Override
	public Set<String> getResourceSet(String username) {
		Assert.notNull(username, ExceptionCode.USERNAME_IS_NULL.getCode(), ExceptionCode.USERNAME_IS_NULL.getMsg());
		Set<String> resourceSet = new HashSet<String>();
		List<Resource> resourceList = getResourceList(username);
		for (Resource resource : resourceList) {
			resourceSet.add(resource.getCode());
		}
		
		return resourceSet;
	}

	
	
	
	
	@Override
	@Transactional
	public List<Resource> getResourceList(String username) {
		Assert.notNull(username, ExceptionCode.USERNAME_IS_NULL.getCode(), ExceptionCode.USERNAME_IS_NULL.getMsg());
		List<Resource> retResList = new ArrayList<Resource>();
		
		List<Permission> permissionList = new ArrayList<Permission>();
		
		User user = userMapper.selectByUsername(username);
		
		List<Role> roleList = new ArrayList<Role>();
		List<String> roleIds = urMapper.getRoleId(user.getId());
		for (String rid : roleIds) {
			Role role = roleMapper.getById(rid);
			roleList.add(role);
		}
		
		for (Role role : roleList) {
			List<Permission> pList = new ArrayList<Permission>();
			List<String> pidList = rpMapper.getPermissionIdList(role.getId());
			for (String pid : pidList) {
				Permission permission = pMapper.getById(pid);
				pList.add(permission);
			}
			permissionList.addAll(pList);
		}
		for (Permission permission : permissionList) {
			List<Resource> resourceList = presMapper.getResourceList(permission.getId());
			retResList.addAll(resourceList);
		}
		return retResList;
	}





	



}
