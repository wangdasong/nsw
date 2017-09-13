package nsw.base.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import nsw.base.core.dao.entity.Container;
import nsw.base.core.dao.entity.Page;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.persistence.PageMapper;
import nsw.base.core.service.ContainerService;
import nsw.base.core.service.PageService;
import nsw.base.core.service.base.DataSrcService;
import nsw.base.core.utils.paging.Pagination;
import nsw.base.core.utils.paging.TablePagingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl implements DataSrcService<Page>, PageService, TablePagingService {
	@Autowired
	PageMapper pageMapper;
	@Autowired
	ContainerService containerService;

	@Override
	public Page getPageDetailById(String pageId) {
		return pageMapper.getDetailById(pageId);
	}

	@Override
	public void editPage(Page page) {
		pageMapper.update(page);
	}

	@Override
	public void addPage(Page page) {
		pageMapper.save(page);
	}

	@Override
	public void delPage(Page page) {
		page = pageMapper.getDetailById(page.getId());
		List<Container> containerList = page.getContainers();
		for(Container container : containerList){
			containerService.delContainer(container);
		}
		pageMapper.deleteById(page.getId());
	}

	@Override
	public Page saveOrUpdatePage(Page page) {
		if(page.getId() == null || "".equals(page.getId())){
			addPage(page);
			//增加主内容容器（固定）
			Container container = new Container();
			container.setCode("MAIN_CONTENT");
			container.setName("内容容器");
			container.setPageId(page.getId());
			container.setContainerId(page.getId());
			container.setTmpFlg("1");
			containerService.addContainer(container);
		}else{
			editPage(page);
		}
		page = this.getPageDetailById(page.getId());
		return page;
	}

	@Override
	public List<DataSrcEntity> getDataSrcListByCondition(Page t) {
		List<DataSrcEntity> dataSrcEntityList = new ArrayList<DataSrcEntity>();
		//取得元素字段信息
		if(t.getId() != null){
			Page page = pageMapper.getById(t.getId());
			DataSrcEntity dataSrcEntity;
			//设置ID
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(page.getId() + "id");
			dataSrcEntity.setLabel("EDIT_PAGE_FORM_INPUT_ID");
			dataSrcEntity.setValue(page.getId());
			dataSrcEntityList.add(dataSrcEntity);
			//设置CODE
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(page.getId() + "code");
			dataSrcEntity.setLabel("EDIT_PAGE_FORM_INPUT_CODE");
			dataSrcEntity.setValue(page.getCode());
			dataSrcEntityList.add(dataSrcEntity);
			//设置NAME
			dataSrcEntity = new DataSrcEntity();
			dataSrcEntity.setId(page.getId() + "name");
			dataSrcEntity.setLabel("EDIT_PAGE_FORM_INPUT_NAME");
			dataSrcEntity.setValue(page.getName());
			dataSrcEntityList.add(dataSrcEntity);
			
		}
		return dataSrcEntityList;
	}

	@Override
	public Pagination getPagePage(int pageNo, int size, String sort, Page page) {
		Pagination pr = new Pagination(pageNo, size, sort, this);
		try {
			pr.doPage(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pr;
	}

	@Override
	public int getCount(Pagination pagination) {
		return pageMapper.getCount(pagination);
	}

	@Override
	public List<Page> findPageData(Pagination pagination) {
		return pageMapper.findPageData(pagination);
	}

}
