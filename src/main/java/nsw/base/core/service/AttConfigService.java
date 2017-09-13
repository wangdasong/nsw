package nsw.base.core.service;

import java.util.List;

import nsw.base.core.dao.entity.AttConfig;

public interface AttConfigService {
	public List<AttConfig> getAttConfigsByBelongId(String belongId);
	public void editAttConfig(AttConfig attConfig);
	public void addAttConfig(AttConfig attConfig);
	public void delAttConfig(AttConfig attConfig);
}
