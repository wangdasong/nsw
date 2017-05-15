package nsw.base.core.service.base;

import java.util.List;

import nsw.base.core.dao.entity.base.BaseEntity;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface ExportService<T extends BaseEntity> {
	public HSSFWorkbook exportData(List<T> dataList, String tableId);
}
