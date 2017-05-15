package nsw.base.core.service;

import java.util.List;

import nsw.base.core.dao.entity.ProviderConfig;

public interface ProviderConfigService {
	public List<ProviderConfig> getProviderConfigsByCode(String providerConfigCode);
}
