package nsw.base.web.controller.urpl;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import nsw.base.core.controller.base.BaseController;
import nsw.base.core.dao.entity.User;
import nsw.base.core.service.UserService;
import nsw.base.core.utils.Constants;
import nsw.base.core.utils.MD5Util;
import nsw.base.core.utils.paging.Pagination;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户操作相关
 **/
@Controller
@RequestMapping(Constants.REST_USER_PREFIX)
public class UserController extends BaseController{
	@Autowired
	private UserService userService;
	

	/**
	 * 构�?�页面请�?
	 */
	@RequestMapping(value = Constants.REST_USER_LIST)
	@ResponseBody
	public Pagination findUserPage(Pagination page, User user){
		Pagination rePage = null;
		rePage = userService.getUserPage(page.getPageNumber(), page.getPageSize(),page.getPageSort(), user);
		return rePage;
	}

	/**
	 * 导出页面请求
	 */
	@RequestMapping(value = Constants.REST_USER_EXPORT)
	public void exportUserPage(HttpServletResponse response, Pagination page, String[] ids, String tableId, User user){
		Pagination rePage = null;
		//如果输入为�?�中的表格行
		if(ids != null){
			String idStr = "";
			for(String currId : ids){
				idStr = idStr + "," + currId;
			}
			if(idStr.length() > 1){
				idStr = idStr.substring(1);
			}
			user.setId(idStr);
		}
		rePage = userService.getUserPage(1, 10000, null, user);
		HSSFWorkbook wkb = userService.exportData((List<User>) rePage.getList(), tableId);
		//处理Excel响应
		responseObject(response, wkb);
	}
//	
//	/**
//	 * 构�?�页面请�?
//	 */
//	@RequestMapping(value = Constants.REST_USER_IMPORT)
//	@ResponseBody
//	public Pagination importUserPage(Pagination page, String uploadFile, User user){
//		Pagination rePage = null;
//		String tableId = page.getTableId();
//		if(tableId != null && uploadFile != null){
//			File file = new File(uploadFile);
//			if(file.exists()){
//				userService.importData(uploadFile, tableId);
//			}
//		}
//		rePage = userService.getUserPage(page.getPageNumber(), page.getPageSize(),page.getPageSort(), user);
//		return rePage;
//	}
	
	/**
	 * 批量删除请求
	 */
	@RequestMapping(value = Constants.REST_USER_BATDEL)
	@ResponseBody
	public Pagination batDel(Pagination page, User user, String[] ids){
		Pagination rePage = null;
		for(String id : ids){
			System.out.println("id = "+ id);
		}
		rePage = userService.getUserPage(page.getPageNumber(), page.getPageSize(),page.getPageSort(), user);
		return rePage;
	}
	/**
	 * 修改
	 */
	@RequestMapping(value = Constants.REST_USER_EDIT)
	@ResponseBody
	public User edit(User user){
		userService.editUser(user);
		return user;
	}
	/**
	 * 新增
	 */
	@RequestMapping(value = Constants.REST_USER_ADD)
	@ResponseBody
	public User add(User user){
		if("".equals(user.getId())){
			user.setId(null);
		}	
		if("".equals(user.getPassword()) || null == user.getPassword()){
			user.setPassword(MD5Util.getMd5Value("123456"));
		}
		userService.addUser(user);
		return user;
	}
	/**
	 * 删除
	 */
	@RequestMapping(value = Constants.REST_USER_DEL)
	@ResponseBody
	public User del(User user){
		userService.delUser(user.getId());
		return user;
	}
	
	
	
	
}
