package nsw.base.core.service;

import java.util.List;

import nsw.base.core.dao.entity.ProviderConfig;
import nsw.base.core.utils.paging.Pagination;

public interface ProviderConfigService {
	public List<ProviderConfig> getProviderConfigsByCode(String providerConfigCode);
	public Pagination getProviderConfigPage(int pageNo, int size, String sort, ProviderConfig providerConfig);
	public void add(ProviderConfig providerConfig);
	public void edit(ProviderConfig providerConfig);
	public void del(String pcid);
}
