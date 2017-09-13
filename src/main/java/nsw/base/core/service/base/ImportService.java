package nsw.base.core.service.base;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nsw.base.core.dao.entity.base.BaseEntity;

public interface ImportService<T extends BaseEntity> {
	public List<T> importData(HttpServletRequest request, String uploadFile, String tableId);
}
