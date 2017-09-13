package nsw.base.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.persistence.UserMapper;
import nsw.base.core.service.base.DataSrcService;
import nsw.base.core.service.impl.base.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 构造用户详细信息popup页面
 * @author qwe456
 *
 */
@Service
public class UserDetailSrcServiceImpl extends BaseServiceImpl<User> implements DataSrcService<User> {
	@Autowired
	UserMapper userMapper;

	@Override
	public List<DataSrcEntity> getDataSrcListByCondition(User t) {
		User user = userMapper.getById(t.getId());
		List<DataSrcEntity> dataSrcEntityList = new ArrayList<DataSrcEntity>();

		DataSrcEntity currrDataSrcEntity = new DataSrcEntity();
		currrDataSrcEntity.setLabel("POPUP_INPUT_ID");
		currrDataSrcEntity.setValue(user.getId());
		dataSrcEntityList.add(currrDataSrcEntity);
		currrDataSrcEntity = new DataSrcEntity();
		currrDataSrcEntity.setLabel("POPUP_INPUT_USERNAME");
		currrDataSrcEntity.setValue(user.getUsername());
		dataSrcEntityList.add(currrDataSrcEntity);
		return dataSrcEntityList;
	}

	@Override
	public UserMapper getCurrDaoMapper() {
		return userMapper;
	}

}
