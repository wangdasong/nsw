package nsw.base.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nsw.base.core.dao.entity.AttConfig;
import nsw.base.core.dao.entity.Container;
import nsw.base.core.dao.entity.Element;
import nsw.base.core.dao.entity.Widget;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.persistence.WidgetMapper;
import nsw.base.core.service.AttConfigService;
import nsw.base.core.service.ContainerService;
import nsw.base.core.service.ElementService;
import nsw.base.core.service.WidgetService;
import nsw.base.core.service.base.DataSrcService;
import nsw.base.core.utils.ThreadVariable;
import nsw.base.core.utils.paging.Pagination;
import nsw.base.core.utils.paging.TablePagingService;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WidgetServiceImpl implements DataSrcService<Widget>, WidgetService {
	@Autowired
	WidgetMapper widgetMapper;
	@Autowired
	AttConfigService attConfigService;
	@Autowired
	ContainerService containerService;
	@Autowired
	ElementService elementService;
	protected Widget menuPermissionFilter(Widget widget, Set<String> permission){
		Set<Element> dealElementSet = new HashSet<Element>();
		for(Element element : widget.getElements()){
			if(!permission.contains(element.getId())){
				dealElementSet.add(element);
			}
			Set<Element> dealElementSub2Set = new HashSet<Element>();
			for(Element elementSub2 : element.getChildElements()){
				if(!permission.contains(elementSub2.getId())){
					dealElementSub2Set.add(elementSub2);
				}
				Set<Element> dealElementSub3Set = new HashSet<Element>();
				for(Element elementSub3 : elementSub2.getChildElements()){
					if(!permission.contains(elementSub3.getId())){
						dealElementSub3Set.add(elementSub3);
					}
					Set<Element> dealElementSub4Set = new HashSet<Element>();
					for(Element elementSub4 : elementSub3.getChildElements()){
						if(!permission.contains(elementSub4.getId())){
							dealElementSub4Set.add(elementSub4);
						}
					}
					for(Element dealElementSub4 : dealElementSub4Set){
						elementSub3.getChildElements().remove(dealElementSub4);
					}
				}
				for(Element dealElementSub3 : dealElementSub3Set){
					elementSub2.getChildElements().remove(dealElementSub3);
				}
			}
			for(Element dealElementSub2 : dealElementSub2Set){
				element.getChildElements().remove(dealElementSub2);
			}
		}
		for(Element dealElement : dealElementSet){
			widget.getElements().remove(dealElement);
		}
		return widget;
	}
	protected Widget elementPermissionFilter(Widget widget, Set<String> permission){
		//编辑页面不控制权限
		Container container = containerService.getContainerById(widget.getContainerId());
		if(container.getContainerId().length() < 32){
			return widget;
		}
		Set<Element> dealElementSet = new HashSet<Element>();
		for(Element element : widget.getElements()){
			String currMenuId = ThreadVariable.getCurrMenuVariable();
			if(!permission.contains(currMenuId + ":" + widget.getContainerId() + ":" + widget.getId() + ":" + element.getId())){
				dealElementSet.add(element);
			}
		}
		for(Element dealElement : dealElementSet){
			widget.getElements().remove(dealElement);
		}
		return widget;
	}
	@Override
	public Widget getWidgetDetailById(String widgetId) {
		Widget widget = widgetMapper.getDetailById(widgetId);
		if(SecurityUtils.getSubject().hasRole("sysadmin") || ThreadVariable.getPopupWidgetVariable() != null){
			return widget;
		}
		//如果是读取菜单信息，检查菜单权限
		if("systemmenu".equals(widget.getContainerId())){
			Set<String> menuPermission = ThreadVariable.getMenuPermissionSetVariable();
			return menuPermissionFilter(widget, menuPermission);
		}else {
			Set<String> elementPermission = ThreadVariable.getElementPermissionSetVariable();
			return elementPermissionFilter(widget, elementPermission);
		}
	}

	@Override
	public Widget saveOrUpdateWidget(Widget widget) {
		if(widget.getId() == null || "".equals(widget.getId())){
			addWidget(widget);
		}else{
			editWidget(widget);
		}
		return widget;
	}

	@Override
	public void editWidget(Widget widget) {
		widgetMapper.update(widget);
	}

	@Override
	public void addWidget(Widget widget) {
		BaseEntity user = ThreadVariable.getUser();
		widget.setCreateDate(new Date());
		widget.setUpdateDate(new Date());
		if(user != null){
			widget.setCreateUserId(user.getId());
			widget.setUpdateUserId(user.getId());
		}
		widget.setTmpFlg("1");
		widgetMapper.save(widget);
	}

	@Override
	public Pagination getMyAttConfigPage(int pageNo, int size, String sort,
			Widget widget) {
		Pagination pr = new Pagination(pageNo, size, sort, (TablePagingService)attConfigService);
		try {
			AttConfig attConfig = new AttConfig();
			attConfig.setBelongId(widget.getId());
			pr.doPage(attConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pr;
	}

	@Override
	public List<AttConfig> getMyAttConfigList(Widget widget) {
		return attConfigService.getAttConfigsByBelongId(widget.getId());
	}

	@Override
	public List<DataSrcEntity> getDataSrcListByCondition(Widget t) {
		List<DataSrcEntity> dataSrcEntityList = new ArrayList<DataSrcEntity>();
		//取得元素字段信息
		if(t.getId() != null){
			Widget widget = widgetMapper.getById(t.getId());
			DataSrcEntity dataSrcEntity;
			//设置ID
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(widget.getId() + "id");
			dataSrcEntity.setLabel("EDIT_WIDGET_FORM_INPUT_ID");
			dataSrcEntity.setValue(widget.getId());
			dataSrcEntityList.add(dataSrcEntity);
			//设置CODE
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(widget.getId() + "code");
			dataSrcEntity.setLabel("EDIT_WIDGET_FORM_INPUT_CODE");
			dataSrcEntity.setValue(widget.getCode());
			dataSrcEntityList.add(dataSrcEntity);
			//设置NAME
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(widget.getId() + "name");
			dataSrcEntity.setLabel("EDIT_WIDGET_FORM_INPUT_NAME");
			dataSrcEntity.setValue(widget.getName());
			dataSrcEntityList.add(dataSrcEntity);
			//设置类型
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(widget.getId() + "parentId");
			dataSrcEntity.setLabel("EDIT_WIDGET_FORM_INPUT_TYPE");
			dataSrcEntity.setValue(widget.getType());
			dataSrcEntityList.add(dataSrcEntity);
			//设置所属容器
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(widget.getId() + "widgetId");
			dataSrcEntity.setLabel("EDIT_WIDGET_FORM_INPUT_CONATAINER_ID");
			dataSrcEntity.setValue(widget.getContainerId());
			dataSrcEntityList.add(dataSrcEntity);
			//设置加载顺序
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(widget.getId() + "sort");
			dataSrcEntity.setLabel("EDIT_WIDGET_FORM_INPUT_SORT");
			dataSrcEntity.setValue(widget.getSort() + "");
			dataSrcEntityList.add(dataSrcEntity);
			
		}
		return dataSrcEntityList;
	}

	@Override
	public Widget copyElementAttsFromTemplet(Widget templetWidget,
			Widget myWidget) {
		List<AttConfig> tempAttConfigs = this.getMyAttConfigList(templetWidget);
		List<AttConfig> attConfigs = new ArrayList<AttConfig>();
		for(AttConfig attConfig : tempAttConfigs){
			attConfig.setId("");
			attConfig.setCreateDate(new Date());
			attConfig.setUpdateDate(new Date());
			attConfig.setBelongId(myWidget.getId());
			attConfigService.addAttConfig(attConfig);
			attConfigs.add(attConfig);
		}
		myWidget.setAttConfigs(attConfigs);
		return myWidget;
	}

	@Override
	public void delWidget(Widget widget) {
		Widget widgetDetail = this.getWidgetDetailById(widget.getId());
		for(Element element : widgetDetail.getElements()){
			elementService.delElement(element);
		}
		for(AttConfig attConfig : widgetDetail.getAttConfigs()){
			attConfigService.delAttConfig(attConfig);
		}
		widgetMapper.deleteById(widget.getId());
	}

}
