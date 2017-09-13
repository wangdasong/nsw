package nsw.base.core.service;

import java.util.List;
import java.util.Set;

import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.entity.base.BaseEntity;

import org.springframework.stereotype.Service;



/**
 * 项目名称：finance<br>
 * *********************************<br>
 * <P>类名称：LoginService</P>
 * *********************************<br>
 * <P>类描述：</P>
 * 创建人：wenjie.zhu<br>
 * 创建时间：2016年12月27日 上午11:15:44<br>
 * 修改人：wenjie.zhu<br>
 * 修改时间：2016年12月27日 上午11:15:44<br>
 * 修改备注：<br>
 * @version 1.0<br>    
 */
@Service("iLoginService")
public interface LoginService {

	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param user
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2016年12月27日 上午11:15:57</p>
	 * @return 
	 */
	User loginCheck(User user);

	/**
	 * <p>方法描述: </p>
	 * <p>方法备注: </p>
	 * @param username
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年1月3日 下午4:42:46</p>
	 */
	User loginCheck2(String username);

	/**
	 * <p>方法描述: 获取某一用户的全部角色</p>
	 * <p>方法备注: Set集合</p>
	 * @param username
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月28日 下午2:23:13</p>
	 */
	Set<String> findRolesByUname(String username);

	/**
	 * <p>方法描述: 获取某一用户的所有权限</p>
	 * <p>方法备注: Set集合</p>
	 * @param username
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月28日 下午3:03:23</p>
	 */
	Set<BaseEntity> findPermissionsByUname(String username);
	
	
	
	/**
	 * 
	 * <p>方法描述: 获取某一用户的所有权限List</p>
	 * <p>方法备注: </p>
	 * @param username
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月28日 下午3:46:27</p>
	 */
	List<String> getPermissionStrList(String username);
	
	
	
	
	
	/**
	 * <p>方法描述: 密码修改功能</p>
	 * <p>方法备注: </p>
	 * @param user
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年3月7日 上午11:06:08</p>
	 */
	int editPassword(User user);
	
	
	
	
	
	
	
	
	
	

}
