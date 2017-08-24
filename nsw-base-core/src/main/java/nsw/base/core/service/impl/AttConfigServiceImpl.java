package nsw.base.core.service.impl;

import java.util.List;

import nsw.base.core.dao.entity.AttConfig;
import nsw.base.core.dao.persistence.AttConfigMapper;
import nsw.base.core.service.AttConfigService;
import nsw.base.core.service.DynamicElementsService;
import nsw.base.core.utils.WebContextFactoryUtil;
import nsw.base.core.utils.paging.Pagination;
import nsw.base.core.utils.paging.TablePagingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttConfigServiceImpl implements TablePagingService, AttConfigService{
	@Autowired
	AttConfigMapper attConfigMapper;

	@Override
	public List<AttConfig> getAttConfigsByBelongId(String belongId) {
		if(belongId.contains("dynamicElement_")){
			String realBelongId = belongId.split("_")[2];
			String dynamicElementsServiceName = belongId.split("_")[1];
			DynamicElementsService dynamicElementsService = (DynamicElementsService)WebContextFactoryUtil.getBean(dynamicElementsServiceName);
			List<AttConfig> attConfigs = dynamicElementsService.getAttConfigsByElementId(realBelongId);
			return attConfigs;
		}
		return attConfigMapper.getByBelongId(belongId);
	}

	@Override
	public int getCount(Pagination page) {
		return attConfigMapper.getCount(page);
	}

	@Override
	public List findPageData(Pagination page) {
		return attConfigMapper.findPageData(page);
	}

	@Override
	public void editAttConfig(AttConfig attConfig) {
		attConfigMapper.update(attConfig);
	}

	@Override
	public void addAttConfig(AttConfig attConfig) {
		if("".equals(attConfig.getId())){
			attConfig.setId(null);
		}
		if(attConfig.getNullAble()== null || "".equals(attConfig.getNullAble())){
			attConfig.setNullAble("1");
		}
		attConfigMapper.save(attConfig);
	}

	@Override
	public void delAttConfig(AttConfig attConfig) {
		attConfigMapper.deleteById(attConfig.getId());
	}

}
