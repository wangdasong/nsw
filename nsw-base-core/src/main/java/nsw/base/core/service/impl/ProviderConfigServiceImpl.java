package nsw.base.core.service.impl;

import java.util.List;

import nsw.base.core.dao.entity.ProviderConfig;
import nsw.base.core.dao.persistence.ProviderConfigMapper;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.service.ProviderConfigService;
import nsw.base.core.service.impl.base.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderConfigServiceImpl extends BaseServiceImpl<ProviderConfig> implements ProviderConfigService {

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

}
