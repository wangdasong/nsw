/**
 * 
 */
package nsw.base.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import nsw.base.core.dao.entity.Role;
import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.entity.UserRole;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.persistence.RoleMapper;
import nsw.base.core.dao.persistence.UserMapper;
import nsw.base.core.dao.persistence.UserRoleMapper;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.service.UserRoleService;
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
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements DataSrcService<UserRole>,  UserRoleService, TablePagingService{
	@Autowired
	private UserRoleMapper userRoleMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	
	
	
	
	@Override
	public BaseDaoMapper<UserRole> getCurrDaoMapper() {
		return userRoleMapper;
	}
	
	
	@Override
	public int getCount(Pagination page) {
		return userRoleMapper.getCount(page);
	}
	
	
	
	@Override
	public List findPageData(Pagination page) {
		List userRoleList = userRoleMapper.findPageData(page);
		return userRoleList;
	}

	@Override
	public Pagination getUserRolePage(int pageNo, int size, String sort,UserRole userRole) {
		Pagination pr = new Pagination(pageNo, size, sort, this);
		try {
			pr.doPage(userRole);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pr;
	}
	
	
	
	
	@Override
	public List<DataSrcEntity> getDataSrcListByCondition(UserRole t) {
		List<UserRole> userRoleList = userRoleMapper.getListByCondition(t);
		List<DataSrcEntity> dataSrcEntityList = new ArrayList<DataSrcEntity>();
		for(UserRole currUserRole : userRoleList){
			DataSrcEntity currrDataSrcEntity = new DataSrcEntity();
			currrDataSrcEntity.setId(currUserRole.getId());
			currrDataSrcEntity.setLabel(currUserRole.getRoleId());
			currrDataSrcEntity.setValue(currUserRole.getUserId());
			dataSrcEntityList.add(currrDataSrcEntity);
		}
		return dataSrcEntityList;
	}

	
	


	
	@Override
	public void editUserRole(UserRole userRole) {
		userRoleMapper.update(userRole);
	}

	@Override
	public void addUserRole(UserRole userRole) {
		userRoleMapper.save(userRole);
	}

	@Override
	public void delUserRole(String userRoleId) {
		userRoleMapper.deleteById(userRoleId);
	}

	@Override
	public int create(UserRole userRole) {
		return userRoleMapper.create(userRole);
	}


	@Override
	public List<String> getRoleId(String userId) {
		return userRoleMapper.getRoleId(userId);
	}




	@Override
	public int delete(String userRoleId) {
		return userRoleMapper.deleteById(userRoleId);
	}


	@Override
	@Transactional
	public List<Role> getRoleListByUser(String username) {
		Assert.notNull(username, ExceptionCode.USERNAME_IS_NULL.getCode(), ExceptionCode.USERNAME_IS_NULL.getMsg());
		User user = userMapper.selectByUsername(username);
		List<Role> roleList = getRoleListByUserId(user);
		return roleList;
	}
	
	
	
	@Override
	@Transactional
	public List<Role> getRoleListByUserId(User user) {
		Assert.notNull(user, ExceptionCode.USER_IS_NULL.getCode(), ExceptionCode.USER_IS_NULL.getMsg());
		List<Role> roleList = new ArrayList<Role>();
		
		List<String> roleIds = userRoleMapper.getRoleId(user.getId());
		for (String rid : roleIds) {
			Role role = roleMapper.getById(rid);
			roleList.add(role);
		}
		return roleList;
		
	}


	@Override
	@Transactional
	public List<String> getRoleTypeListByUsername(String username) {
		Assert.notNull(username, ExceptionCode.USERNAME_IS_NULL.getCode(), ExceptionCode.USERNAME_IS_NULL.getMsg());
		User user = userMapper.selectByUsername(username);
		List<Role> roleList = getRoleListByUserId(user);
		List<String> roleTypeList = new ArrayList<String>();
		for (Role role : roleList) {
			String roleType = role.getRoleType();
			roleTypeList.add(roleType);
		}
		
		return roleTypeList;
	}


	@Override
	public List<Role> getRoleListByUserId(String userId) {
		User user = userMapper.getById(userId);
		List<Role> roleList = getRoleListByUserId(user);
		return roleList;
	}


	

}
