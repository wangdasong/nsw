package nsw.base.core.service;

import nsw.base.core.dao.entity.User;
import nsw.base.core.service.base.ExportService;
import nsw.base.core.service.base.ImportService;
import nsw.base.core.utils.paging.Pagination;


public interface UserService extends ImportService<User>, ExportService<User>{
	/** 
	 * 获取用户列表
	 * @param pageNo 页数
	 * @param size 每页多少条记录 
	 * @param t 查询条件
	 * @return Pagination
	 * @throws ServiceException 
	 */
	public Pagination getUserPage(int pageNo, int size, String sort, User user);
	public void editUser(User user);
	public void addUser(User user);
	public void delUser(String userId);
	public User getUserByName(String userName);
	
	
	
	/**
	 * <p>方法描述: 用户注册，创建新用户</p>
	 * <p>方法备注: </p>
	 * @param user
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年1月23日 下午3:47:29</p>
	 */
	public int createUser(User user);
	
	

    
    
	
	
}
