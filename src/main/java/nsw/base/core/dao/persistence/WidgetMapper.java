package nsw.base.core.dao.persistence;

import nsw.base.core.dao.entity.Widget;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;

public interface WidgetMapper extends BaseDaoMapper<Widget>{
	@Cacheable(value="pageDataCache")
    public Widget getDetailById(String id)throws DataAccessException;

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void save(Widget entity)throws DataAccessException;

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void update(Widget entity)throws DataAccessException;

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void deleteById(String id)throws DataAccessException;
}

