package nsw.base.core.dao.entity.base;

import nsw.base.core.utils.PropertyUtils;

public interface FrameworkDbDialogConfig extends BaseDbDialogConfig {
	public static String dbDialog = PropertyUtils.getJDBCPropertyValue("core.jdbc_dbDialog");
}
