package nsw.base.web.controller.urpl;

import nsw.base.core.controller.base.BaseController;
import nsw.base.core.dao.entity.UserRole;
import nsw.base.core.service.UserRoleService;
import nsw.base.core.utils.Constants;
import nsw.base.core.utils.paging.Pagination;
import nsw.base.core.utils.result.ResultString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 用户角色操作相关
 **/
@Controller
@RequestMapping(Constants.REST_USER_ROLE_PREFIX)
public class UserRoleController extends BaseController{
	
	@Autowired
	private UserRoleService userRoleService;

	@RequestMapping(value = "/getRoleList")
	@Transactional
	@ResponseBody
	public Pagination getRoleList(Pagination pagination, String userId){	
		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		Pagination rePage = null;
		rePage = userRoleService.getUserRolePage(pagination.getPageNumber(), pagination.getPageSize(), pagination.getPageSort(), userRole);		
		return rePage;		
	}	
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/del")
	@ResponseBody
	public UserRole del(UserRole userRole){
		userRoleService.delete(userRole.getId());
		return userRole;
	}
	
	 /**
     * 桥表 查询
     */
    @RequestMapping(value = "/listUserRole")
    @ResponseBody 
	public Pagination getRoleList(Pagination pagination, String[] ids){
		Pagination rePage = null;
		if(null==ids){
			return null;
		}
		String userId = ids[0];
		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		rePage = userRoleService.getUserRolePage(pagination.getPageNumber(), pagination.getPageSize(), pagination.getPageSort(), userRole);	
		return rePage;
	}
    
    @RequestMapping(value = "/addUserRole")
    @ResponseBody 
    public ResultString addUserRole(String[] userIds, String[] ids){
    	ResultString resultString = new ResultString();
		for(String userId : userIds){
			for(String roleId : ids){
				UserRole userRole = new UserRole();
				userRole.setUserId(userId);
				userRole.setRoleId(roleId);				
				userRoleService.addUserRole(userRole);
			}
		}		
		resultString.setStatus(ResultString.RESULT_STATUS_SUCCESS);
		resultString.setMsg("角色添加成功�?");
		return resultString;
    }
}
