package nsw.base.core.service.impl;

import java.util.List;

import nsw.base.core.dao.entity.ProviderConfig;
import nsw.base.core.dao.persistence.ProviderConfigMapper;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.service.ProviderConfigService;
import nsw.base.core.service.impl.base.BaseServiceImpl;
import nsw.base.core.utils.paging.Pagination;
import nsw.base.core.utils.paging.TablePagingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderConfigServiceImpl extends BaseServiceImpl<ProviderConfig> implements ProviderConfigService , TablePagingService {

	@Autowired
	ProviderConfigMapper providerConfigMapper;
	@Override
	public BaseDaoMapper<ProviderConfig> getCurrDaoMapper() {
		return providerConfigMapper;
	}
	@Override
	public List<ProviderConfig> getProviderConfigsByCode(
			String providerConfigCode) {
		ProviderConfig providerConfig = new ProviderConfig();
		providerConfig.setCode(providerConfigCode);
		return providerConfigMapper.getListByCondition(providerConfig);
	}
	@Override
	public int getCount(Pagination page) {
		return providerConfigMapper.getCount(page);
	}
	@Override
	public List<ProviderConfig> findPageData(Pagination page) {
		return providerConfigMapper.findPageData(page);
	}
	@Override
	public Pagination getProviderConfigPage(int pageNo, int size, String sort,
			ProviderConfig providerConfig) {
		Pagination pr = new Pagination(pageNo, size, sort, this);
		try {
			pr.doPage(providerConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pr;
	}
	@Override
	public void add(ProviderConfig providerConfig) {
		providerConfigMapper.save(providerConfig);
	}
	@Override
	public void edit(ProviderConfig providerConfig) {
		providerConfigMapper.update(providerConfig);
	}
	@Override
	public void del(String pcid) {
		providerConfigMapper.deleteById(pcid);
	}

}
