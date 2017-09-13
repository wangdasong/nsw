/**
 * 
 */
package nsw.base.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nsw.base.core.dao.entity.Permission;
import nsw.base.core.dao.entity.Role;
import nsw.base.core.dao.entity.RolePermission;
import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.persistence.PermissionMapper;
import nsw.base.core.dao.persistence.RoleMapper;
import nsw.base.core.dao.persistence.RolePermissionMapper;
import nsw.base.core.dao.persistence.UserMapper;
import nsw.base.core.dao.persistence.UserRoleMapper;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.service.RolePermissionService;
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
 * <P>类名称：UserRoleServiceImpl</P>
 * *********************************<br>
 * <P>类描述：</P>
 * 创建人：wenjie.zhu<br>
 * 创建时间：2017年3月6日 上午9:41:49<br>
 * 修改人：wenjie.zhu<br>
 * 修改时间：2017年3月6日 上午9:41:49<br>
 * 修改备注：<br>
 * @version 1.0<br>    
 */
@Service
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermission> implements DataSrcService<RolePermission>,  RolePermissionService, TablePagingService{
	@Autowired
	private RolePermissionMapper rpMapper;
	
	@Autowired
	private PermissionMapper pMapper;
	
	@Autowired
	private UserRoleMapper urMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
		
	
	
	@Override
	public BaseDaoMapper<RolePermission> getCurrDaoMapper() {
		return rpMapper;
	}
	
	
	@Override
	public int getCount(Pagination page) {
		return rpMapper.getCount(page);
	}
	
	
	
	@Override
	public List findPageData(Pagination page) {
		List userRoleList = rpMapper.findPageData(page);
		return userRoleList;
	}

	@Override
	public Pagination getRolePermissionPage(int pageNo, int size, String sort,RolePermission rp) {
		Pagination pr = new Pagination(pageNo, size, sort, this);
		try {
			pr.doPage(rp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pr;
	}
	
	
	
	
	@Override
	public List<DataSrcEntity> getDataSrcListByCondition(RolePermission t) {
		Assert.notNull(t, ExceptionCode.ROLEPERMISSION_IS_NULL.getCode(), ExceptionCode.ROLEPERMISSION_IS_NULL.getMsg());
		List<RolePermission> userRoleList = rpMapper.getListByCondition(t);
		List<DataSrcEntity> dataSrcEntityList = new ArrayList<DataSrcEntity>();
		for(RolePermission currUserRole : userRoleList){
			DataSrcEntity currrDataSrcEntity = new DataSrcEntity();
			currrDataSrcEntity.setId(currUserRole.getId());
			currrDataSrcEntity.setLabel(currUserRole.getRoleId());
			currrDataSrcEntity.setValue(currUserRole.getPermissionId());
			dataSrcEntityList.add(currrDataSrcEntity);
		}
		return dataSrcEntityList;
	}

	
	


	
	@Override
	public void edit(RolePermission rp) {
		rpMapper.update(rp);
	}

	@Override
	public void add(RolePermission rp) {
		rpMapper.save(rp);
	}

	@Override
	public void del(String userRoleId) {
		rpMapper.deleteById(userRoleId);
	}

	@Override
	public int create(RolePermission rp) {
		return rpMapper.create(rp);
	}


	@Override
	public List<String> getRoleByPid(String pid) {
		return rpMapper.getRoleByPid(pid);
	}


	@Override
	@Transactional
	public List<Permission> getPermissionListByRoleId(String roleId) {
		Assert.notNull(roleId, ExceptionCode.ROLEID_IS_NULL.getCode(), ExceptionCode.ROLEID_IS_NULL.getMsg());
		
		List<Permission> permissionList = new ArrayList<Permission>();
		List<String> pidList = rpMapper.getPermissionIdList(roleId);
		for (String pid : pidList) {
			Permission permission = pMapper.getById(pid);
			permissionList.add(permission);
		}
		return permissionList;
		
		
	}


	@Override
	@Transactional
	public List<Permission> getPermissionList(String username) {
		Assert.notNull(username, ExceptionCode.USERNAME_IS_NULL.getCode(), ExceptionCode.USERNAME_IS_NULL.getMsg());
		List<Permission> permissionList = new ArrayList<Permission>();
		
		User user = userMapper.selectByUsername(username);
		
		List<Role> roleList = new ArrayList<Role>();
		List<String> roleIds = urMapper.getRoleId(user.getId());
		for (String rid : roleIds) {
			Role role = roleMapper.getById(rid);
			roleList.add(role);
		}
		
		for (Role role : roleList) {
			List<Permission> pList = getPermissionListByRoleId(role.getId());
			permissionList.addAll(pList);
		}
		
		return permissionList;
	}


	
	
	@Override
	@Transactional
	public Set<String> findRoleByPid(Set<String> permissionIdSet) {
		Assert.notNull(permissionIdSet, ExceptionCode.PERMISSIONIDSET_IS_NULL.getCode(), ExceptionCode.PERMISSIONIDSET_IS_NULL.getMsg());
		Set<String> retRoleSet = new HashSet<String>();
    	
    	List<String> roleStrList = new ArrayList<String>();
    	
    	for (String pid : permissionIdSet) {
    		roleStrList = getRoleByPid(pid);
    		for (String rid : roleStrList) {
    			Role role = roleMapper.getById(rid);
    			retRoleSet.add(role.getRoleType());
			}
		}
    	
    	return retRoleSet;
	}




}
