package nsw.base.core.service.base;

import java.util.List;

import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.DataSrcEntity;

public interface DataSrcService<T extends BaseEntity> {

	List<DataSrcEntity> getDataSrcListByCondition(T t);
}
