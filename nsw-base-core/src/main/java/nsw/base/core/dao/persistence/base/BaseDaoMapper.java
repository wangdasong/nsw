package nsw.base.core.dao.persistence.base;
import java.util.List;

import org.springframework.dao.DataAccessException;

import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.utils.paging.Pagination;


public interface BaseDaoMapper<T extends BaseEntity> {
	/**
	 * 根据ID取得对象
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
    public <PK> T getById(PK id)throws DataAccessException;
    
    /**
     * 根据ID取得对象以及对象中的成员
     * @param id
     * @return
     * @throws DataAccessException
     */
    public <PK> T getDetailById(PK id)throws DataAccessException;

    public void save(T entity)throws DataAccessException;

    public void update(T entity)throws DataAccessException;

    public <PK> void deleteById(PK id)throws DataAccessException;

    public List<T> getAllData() throws DataAccessException;

    public List<T> getListByCondition(T t) throws DataAccessException;
    
	public int getCount(Pagination pagination) throws DataAccessException ;
	
	public List<T> findPageData(Pagination pagination) throws DataAccessException ;
	
}
