package nsw.base.core.dao.persistence;

import java.util.List;

import nsw.base.core.dao.entity.AttConfig;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.utils.paging.Pagination;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;

public interface AttConfigMapper extends BaseDaoMapper<AttConfig>{
	@Cacheable(value="pageDataCache")
	List<AttConfig> getByBelongId(String belongId);

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
	List<AttConfig> findPageData(Pagination pagination);

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void save(AttConfig entity)throws DataAccessException;

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void update(AttConfig entity)throws DataAccessException;

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void deleteById(String id)throws DataAccessException;
}

