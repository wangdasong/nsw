package nsw.base.core.dao.entity.base;

import nsw.base.core.utils.PropertyUtils;

public interface BaseDbDialogConfig {
	public static String dbDialog = PropertyUtils.getJDBCPropertyValue("jdbc_dbDialog");
	public String getDbDialog();
}
