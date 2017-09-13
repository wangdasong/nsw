package nsw.base.core.dao.persistence;

import java.util.List;

import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.utils.paging.Pagination;

import org.springframework.stereotype.Component;

//@Repository
@Component("userMapper") 
public interface UserMapper extends BaseDaoMapper<User>{

	/**
	 * 获取用户总数目
	 * @param pagination
	 * @return int
	 */
	int getCount(Pagination pagination);

	/**
	 * 获取某一页用户的集合
	 * @param pagination
	 * @return 某页用户集合
	 */
	List<User> findPageData(Pagination pagination);
	
	/**
	 * 
	 * <p>方法描述: 根据username和password来查询User信息</p>
	 * <p>方法备注: </p>
	 * @param user
	 * @return
	 */
	User selectByUsernamePassword(User user);

	/**
	 * <p>方法描述: 根据username查询User信息</p>
	 * <p>方法备注: </p>
	 * @param username
	 * @return
	 */
	User selectByUsername(String username);
	

	/**
	 * <p>方法描述: 用户注册功能</p>
	 * <p>方法备注: </p>
	 * @param user
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年1月22日 上午11:54:46</p>
	 */
	int createUser(User user);
	
	
	int editPwd(User user);

	
}

