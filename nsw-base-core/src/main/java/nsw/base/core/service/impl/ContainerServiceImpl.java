package nsw.base.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import nsw.base.core.dao.entity.Container;
import nsw.base.core.dao.entity.Widget;
import nsw.base.core.dao.entity.base.AdditionalParameters;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.entity.base.TreeDataEntity;
import nsw.base.core.dao.entity.base.TreeDataEntity.TypeEnum;
import nsw.base.core.dao.persistence.ContainerMapper;
import nsw.base.core.service.ContainerService;
import nsw.base.core.service.ElementService;
import nsw.base.core.service.WidgetService;
import nsw.base.core.service.base.DataSrcService;
import nsw.base.core.service.base.TreeDataService;
import nsw.base.core.utils.ThreadVariable;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContainerServiceImpl implements DataSrcService<Container>, ContainerService, TreeDataService<Container>{
	@Autowired
	ContainerMapper containerMapper;
	@Autowired
	WidgetService widgetService;
	@Autowired
	ElementService elementService;

	@Override
	public List<Container> getSubContainers(String containerId) {
		//取得容器列表
		List<Container> containerList = containerMapper.getListByParentId(containerId);
		//角色未系统管理员或编辑视图功能
		for(Container container : containerList){
			List<Widget> needDealList = new ArrayList<Widget>();
			for(Widget widget : container.getWidgets()){
				String subsysCode = ThreadVariable.getSubsysCodeVariable();
				if("menu".equals(widget.getType()) && !subsysCode.equals(widget.getCode())){
					needDealList.add(widget);
				}
			}
			for(Widget widget : needDealList){
				container.getWidgets().remove(widget);
			}
		}
		if(SecurityUtils.getSubject().hasRole("sysadmin") || containerId.length() < 32){
			return containerList;
		}
		//校验权限
		Set<String> containerPermissionSet= ThreadVariable.getContainerPermissionSetVariable();
		Set<String> widgetPermissionSet= ThreadVariable.getWidgetPermissionSetVariable();
		String currMenuId = ThreadVariable.getCurrMenuVariable();
		String currPopupWidgetId = ThreadVariable.getPopupWidgetVariable();
		String currPopupContainerId = null;
		if(currPopupWidgetId != null && !"".equals(currPopupWidgetId)){
			if(!"none".equals(currPopupWidgetId)){
				currPopupContainerId = widgetService.getWidgetDetailById(currPopupWidgetId).getContainerId();
				Set<String> pcPermissionSet= ThreadVariable.getPcPermissionSetVariable();
				Set<String> pwPermissionSet= ThreadVariable.getPwPermissionSetVariable();
				List<Container> dealContainerList = new ArrayList<Container>();
				for(Container currContainer : containerList){
					if(!pcPermissionSet.contains(currMenuId + ":" + currPopupContainerId + ":" + currPopupWidgetId + ":" + currContainer.getId())){
						dealContainerList.add(currContainer);
					}else{
						List<Widget> dealWidgetList = new ArrayList<Widget>();
						for(Widget currWidget : currContainer.getWidgets()){
							if(!pwPermissionSet.contains(currMenuId + ":" + currPopupContainerId + ":" + currPopupWidgetId + ":" + currContainer.getId() + ":" + currWidget.getId())){
								dealWidgetList.add(currWidget);
							}
						}
						for(Widget dealWidget : dealWidgetList){
							currContainer.getWidgets().remove(dealWidget);
						}
					}
				}
				for(Container dealContainer : dealContainerList){
					containerList.remove(dealContainer);
				}
			}
		}else{
			List<Container> dealContainerList = new ArrayList<Container>();
			for(Container currContainer : containerList){
				if(!containerPermissionSet.contains(currMenuId + ":" + currContainer.getId())){
					dealContainerList.add(currContainer);
				}else{
					List<Widget> dealWidgetList = new ArrayList<Widget>();
					for(Widget currWidget : currContainer.getWidgets()){
						if(!widgetPermissionSet.contains(currMenuId + ":" + currContainer.getId() + ":" + currWidget.getId())){
							dealWidgetList.add(currWidget);
						}
					}
					for(Widget dealWidget : dealWidgetList){
						currContainer.getWidgets().remove(dealWidget);
					}
				}
			}
			for(Container dealContainer : dealContainerList){
				containerList.remove(dealContainer);
			}			
		}
		return containerList;
	}

	@Override
	public TreeDataEntity getTreeDataListByParentId(String parentId, Container queryCondition) {
		List<Container> containers = containerMapper.getListByParentId(parentId);
		Container parentContainer = containerMapper.getById(parentId);
		List<TreeDataEntity> treeDataEntitys = new ArrayList<TreeDataEntity>();
		for(Container container : containers){
			TreeDataEntity currTreeDataEntity = new TreeDataEntity();
			currTreeDataEntity.setName(container.getCode()+":"+container.getName());
			if(container.getSubCount() > 0){
				currTreeDataEntity.setTypeEnum(TreeDataEntity.TypeEnum.FOLDER);
				AdditionalParameters adp = new AdditionalParameters();  
                adp.setId(container.getId());
                currTreeDataEntity.setAdditionalParameters(adp);  
			}else{
				AdditionalParameters adp = new AdditionalParameters();
				adp.setId(container.getId());
                adp.setItemSelected(false);
                currTreeDataEntity.setAdditionalParameters(adp);
				currTreeDataEntity.setTypeEnum(TreeDataEntity.TypeEnum.ITEM);//无子节点 
			}
			treeDataEntitys.add(currTreeDataEntity);
		}
		TreeDataEntity treeDataEntity = new TreeDataEntity();
		treeDataEntity.setTypeEnum(TypeEnum.FOLDER);
		AdditionalParameters additionalParameters = new AdditionalParameters();
		additionalParameters.setChildren(treeDataEntitys);
		if(parentContainer == null){
			treeDataEntity.setName("");
			additionalParameters.setId("");
		}else{
			treeDataEntity.setName(parentContainer.getCode()+":"+parentContainer.getName());
			additionalParameters.setId(parentContainer.getId());
		}
		treeDataEntity.setAdditionalParameters(additionalParameters);
		return treeDataEntity;
	}

	@Override
	public List<DataSrcEntity> getDataSrcListByCondition(Container t) {
		List<DataSrcEntity> dataSrcEntityList = new ArrayList<DataSrcEntity>();
		//取得元素字段信息
		if(t.getId() != null){
			Container container = containerMapper.getById(t.getId());
			DataSrcEntity dataSrcEntity;
			//设置ID
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(container.getId() + "id");
			dataSrcEntity.setLabel("EDIT_CONTAINER_FORM_INPUT_ID");
			dataSrcEntity.setValue(container.getId());
			dataSrcEntityList.add(dataSrcEntity);
			//设置CODE
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(container.getId() + "code");
			dataSrcEntity.setLabel("EDIT_CONTAINER_FORM_INPUT_CODE");
			dataSrcEntity.setValue(container.getCode());
			dataSrcEntityList.add(dataSrcEntity);
			//设置NAME
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(container.getId() + "name");
			dataSrcEntity.setLabel("EDIT_CONTAINER_FORM_INPUT_NAME");
			dataSrcEntity.setValue(container.getName());
			dataSrcEntityList.add(dataSrcEntity);
			//设置页面ID
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(container.getId() + "pageId");
			dataSrcEntity.setLabel("EDIT_CONTAINER_FORM_INPUT_PAGE_ID");
			dataSrcEntity.setValue(container.getPageId());
			dataSrcEntityList.add(dataSrcEntity);
			//设置父容器ID
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(container.getId() + "containerId");
			dataSrcEntity.setLabel("EDIT_CONTAINER_FORM_INPUT_CONTAINER_ID");
			dataSrcEntity.setValue(container.getContainerId());
			dataSrcEntityList.add(dataSrcEntity);
			//设置排序
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(container.getId() + "sort");
			dataSrcEntity.setLabel("EDIT_CONTAINER_FORM_INPUT_SORT");
			dataSrcEntity.setValue(container.getSort() + "");
			dataSrcEntityList.add(dataSrcEntity);
			//设置宽度
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(container.getId() + "width");
			dataSrcEntity.setLabel("EDIT_CONTAINER_FORM_INPUT_WIDTH");
			dataSrcEntity.setValue(container.getWidth() + "");
			dataSrcEntityList.add(dataSrcEntity);
			
			
		}
		return dataSrcEntityList;
	}

	@Override
	public void editContainer(Container container) {
		containerMapper.update(container);
	}

	@Override
	public void addContainer(Container container) {
		//默认内容容器
		if(container.getType() == null || "".equals(container.getType())){
			container.setType("3");
		}
		BaseEntity user = ThreadVariable.getUser();
		if(user != null){
			container.setCreateUserId(user.getId());
			container.setUpdateUserId(user.getId());
		}
		container.setCreateDate(new Date());
		container.setUpdateDate(new Date());
		containerMapper.save(container);
	}

	@Override
	public void delContainer(Container container) {
		container = containerMapper.getDetailById(container.getId());
		List<Widget> widgetList = container.getWidgets();
		for(Widget widget : widgetList){
			widgetService.delWidget(widget);
		}
		containerMapper.deleteById(container.getId());
	}

	@Override
	public Container saveOrUpdateContainer(Container container) {
		if(container.getId() == null || "".equals(container.getId())){
			addContainer(container);
		}else{
			editContainer(container);
		}
		return container;
	}

	@Override
	public Container getContainerById(String containerId) {
		return containerMapper.getById(containerId);
	}

}
