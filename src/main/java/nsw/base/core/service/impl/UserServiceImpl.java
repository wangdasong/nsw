package nsw.base.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.persistence.UserMapper;
import nsw.base.core.service.UserService;
import nsw.base.core.service.base.DataSrcService;
import nsw.base.core.service.impl.base.BaseServiceImpl;
import nsw.base.core.utils.paging.Pagination;
import nsw.base.core.utils.paging.TablePagingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements DataSrcService<User>,  UserService, TablePagingService{
	@Autowired
	UserMapper userMapper;

	@Override
	public UserMapper getCurrDaoMapper() {
		return userMapper;
	}

	@Override
	public int getCount(Pagination page) {
		return userMapper.getCount(page);
	}

	@Override
	public List findPageData(Pagination page) {
		List userList = userMapper.findPageData(page);
		return userList;
	}

	@Override
	public Pagination getUserPage(int pageNo, int size, String sort, User user) {
		Pagination pr = new Pagination(pageNo, size, sort, this);
		try {
			pr.doPage(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pr;
	}

	@Override
	public List<DataSrcEntity> getDataSrcListByCondition(User t) {
		List<User> userList = userMapper.getListByCondition(t);
		List<DataSrcEntity> dataSrcEntityList = new ArrayList<DataSrcEntity>();
		for(User currUser : userList){
			DataSrcEntity currrDataSrcEntity = new DataSrcEntity();
			currrDataSrcEntity.setId(currUser.getId());
			currrDataSrcEntity.setLabel(currUser.getUsername());
			currrDataSrcEntity.setValue(currUser.getId());
			dataSrcEntityList.add(currrDataSrcEntity);
		}
		return dataSrcEntityList;
	}

	@Override
	public void editUser(User user) {
		userMapper.update(user);
	}

	@Override
	public void addUser(User user) {
		userMapper.save(user);
	}

	@Override
	public void delUser(String userId) {
		userMapper.deleteById(userId);
	}

	@Override
	public int createUser(User user) {
		return userMapper.createUser(user);
	}

	@Override
	public User getUserByName(String userName) {
		User user = new User();
		user.setUsername(userName);
		List<User> users = userMapper.getListByCondition(user);
		if(users != null && users.size() > 0){
			return users.get(0);
		}
		return null;
	}


	


}
