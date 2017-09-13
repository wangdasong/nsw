package nsw.base.core.service;

import java.util.List;

import nsw.base.core.dao.entity.AttConfig;
import nsw.base.core.dao.entity.Widget;
import nsw.base.core.utils.paging.Pagination;

public interface WidgetService {
	public Pagination getMyAttConfigPage(int pageNo, int size, String sort, Widget widget);
	public List<AttConfig> getMyAttConfigList(Widget widget);
	public Widget getWidgetDetailById(String widgetId);
	public List<Widget> getWidgetByCondition(Widget widget);
	public void editWidget(Widget widget);
	public void addWidget(Widget widget);
	public void delWidget(Widget widget);
	public Widget saveOrUpdateWidget(Widget widget);
	public Widget copyElementAttsFromTemplet(Widget templetWidget, Widget myWidget);
}
