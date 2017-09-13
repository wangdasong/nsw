package nsw.base.core.service.base;

import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.TreeDataEntity;

public interface TreeDataService<T extends BaseEntity>{
	TreeDataEntity getTreeDataListByParentId(String parentId, T queryCondition);
}
