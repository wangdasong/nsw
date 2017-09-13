package nsw.base.core.dao.persistence;

import java.util.List;

import nsw.base.core.dao.entity.Element;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;

public interface ElementMapper extends BaseDaoMapper<Element>{
	public List<Element> getElementsByParentId(String parentId);

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void save(Element entity)throws DataAccessException;

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void update(Element entity)throws DataAccessException;

	@CacheEvict(value="pageDataCache", allEntries=true)
    public void deleteById(String id)throws DataAccessException;
}

