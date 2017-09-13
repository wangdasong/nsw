package nsw.base.core.dao.persistence;

import java.util.List;

import nsw.base.core.dao.entity.Container;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;

public interface ContainerMapper extends BaseDaoMapper<Container>{
	@Cacheable(value="pageDataCache")
	List<Container> getListByParentId(String containerId);

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void save(Container entity)throws DataAccessException;

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void update(Container entity)throws DataAccessException;

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void deleteById(String id)throws DataAccessException;
}

