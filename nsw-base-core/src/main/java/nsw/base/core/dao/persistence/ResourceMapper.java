package nsw.base.core.dao.persistence;

import java.util.List;

import nsw.base.core.dao.entity.Resource;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.utils.paging.Pagination;

public interface ResourceMapper extends BaseDaoMapper<Resource>{

	
	List<Resource> findPageData(Pagination pagination);
	void save(Resource Resource);
	void update(Resource Resource);
	int deleteById(String id);
	Resource getById(String id);
	
	

	/**
	 * <p>方法描述: 查询全部资源列表</p>
	 * <p>方法备注: </p>
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月24日 下午2:06:04</p>
	 */
	List<Resource> findAll();
	/**
	 * <p>方法描述: 创建新资源</p>
	 * <p>方法备注: </p>
	 * @param resource
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月24日 下午2:23:48</p>
	 */
	int createResource(Resource resource);
	/**
	 * <p>方法描述: 有返回值的更新资源</p>
	 * <p>方法备注: </p>
	 * @param resource
	 * @return
	 * <p>创建人：wenjie.zhu</p>
	 * <p>创建时间：2017年2月24日 下午3:29:56</p>
	 */
	int updateResource(Resource resource);

}

