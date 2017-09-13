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
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.persistence.PermissionMapper;
import nsw.base.core.dao.persistence.PermissionResourceMapper;
import nsw.base.core.dao.persistence.RoleMapper;
import nsw.base.core.dao.persistence.RolePermissionMapper;
import nsw.base.core.dao.persistence.UserMapper;
import nsw.base.core.dao.persistence.UserRoleMapper;
import nsw.base.core.service.LoginService;
import nsw.base.core.utils.Assert;
import nsw.base.core.utils.ExceptionCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

/**
 * 项目名称：finance<br>
 * *********************************<br>
 * <P>类名称：LoginServiceImpl</P>
 * *********************************<br>
 * <P>类描述：</P>
 * 创建人：wenjie.zhu<br>
 * 创建时间：2016年12月27日 下午1:24:32<br>
 * 修改人：wenjie.zhu<br>
 * 修改时间：2016年12月27日 下午1:24:32<br>
 * 修改备注：<br>
 * @version 1.0<br>    
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

	
	private Log logger = LogFactory.getLog(super.getClass());
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	UserRoleMapper userRoleMapper;
	
	@Autowired
	RoleMapper roleMapper;
	
	@Autowired
	RolePermissionMapper rpMapper;
	
	@Autowired
	PermissionMapper pMapper;
	@Autowired
	PermissionResourceMapper permissionResourceMapper;
	
	
	
	
	
	



	/*
	 * 根据username和password查询User信息
	 */
	@Override
	public User loginCheck(User user) {
		Assert.notNull(user, ExceptionCode.USER_IS_NULL.getCode(), ExceptionCode.USER_IS_NULL.getMsg());
		User reU = userMapper.selectByUsernamePassword(user);
		
		return reU;
	}



	/*
	 * 根据username查询User信息
	 */
	@Override
	public User loginCheck2(String username) {
		Assert.notNull(username, ExceptionCode.USERNAME_IS_NULL.getCode(), ExceptionCode.USERNAME_IS_NULL.getMsg());
		User reU = userMapper.selectByUsername(username);
		
		return reU;
	}



	
	/**
	 * 查角色 Set
	 * <p>方法描述: 根据用户名获取角色Set（SYS_ROLE表中的ROLE_TYPE字段的Set集合）</p>
	 * <p>方法备注: </p>
	 * @param username
	 * @return
	 */
	@Override
	public Set<String> findRolesByUname(String username) {
		Assert.notNull(username, ExceptionCode.USERNAME_IS_NULL.getCode(), ExceptionCode.USERNAME_IS_NULL.getMsg());
		Set<String> roleSet = new HashSet<String>();
		User user = loginCheck2(username);
		List<Role> roleList = new ArrayList<Role>();
		List<String> roleIds = userRoleMapper.getRoleId(user.getId());
		for (String rid : roleIds) {
			Role role = roleMapper.getById(rid);
			roleList.add(role);
		}
		for (Role role : roleList) {
			String roleType = role.getRoleType();
			roleSet.add(roleType);
		}
		
		return roleSet;
	}


	

	/**
	 * 查权限 Set
	 * <p>方法描述: 通过用户名username查询一个账户的所有权限(SYS_PERMISSION表中的PERMISSION字段的集合Set)</p>
	 * <p>方法备注: </p>
	 * @param username
	 * @return
	 */
	@Override
	@CacheEvict(value="pageDataCache", allEntries=true)
	public Set<BaseEntity> findPermissionsByUname(String username) {
		Assert.notNull(username, ExceptionCode.USERNAME_IS_NULL.getCode(), ExceptionCode.USERNAME_IS_NULL.getMsg());
		Set<BaseEntity> permissionSet = new HashSet<BaseEntity>();
		
		User user = loginCheck2(username);
		
		List<Role> roleList = new ArrayList<Role>();
		List<String> roleIds = userRoleMapper.getRoleId(user.getId());
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
			for (Permission permission : pList) {
				List<Resource> resourceList = permissionResourceMapper.getResourceList(permission.getId());
				for(Resource resource : resourceList){
					permissionSet.add(resource);
				}
			}
		}
		
		return permissionSet;
	}
	
	
	
	
	
	/**
	 *  查权限 List
	 * <p>方法描述: 通过用户名username查询一个账户的所有权限(SYS_PERMISSION表中的PERMISSION字段的集合List)</p>
	 * <p>方法备注: </p>
	 * @param username
	 * @return
	 */
	public List<String> getPermissionStrList(String username){
		Assert.notNull(username, ExceptionCode.USERNAME_IS_NULL.getCode(), ExceptionCode.USERNAME_IS_NULL.getMsg());
		List<String> retPermissionList = new ArrayList<String>();
		
		User user = loginCheck2(username);
		
		List<Role> roleList = new ArrayList<Role>();
		List<String> roleIds = userRoleMapper.getRoleId(user.getId());
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
			for (Permission permission : pList) {
				List<Resource> resourceList = permissionResourceMapper.getResourceList(permission.getId());
				for(Resource resource : resourceList){
					retPermissionList.add(resource.getId());
				}
			}
		}
		return retPermissionList;
	}



	/**
	 * 密码修改
	 */
	@Override
	public int editPassword(User user) {
		Assert.notNull(user, ExceptionCode.USER_IS_NULL.getCode(), ExceptionCode.USER_IS_NULL.getMsg());
		return userMapper.editPwd(user);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
