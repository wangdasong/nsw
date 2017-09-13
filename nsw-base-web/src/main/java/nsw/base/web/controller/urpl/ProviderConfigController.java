package nsw.base.web.controller.urpl;

import nsw.base.core.controller.base.BaseController;
import nsw.base.core.dao.entity.ProviderConfig;
import nsw.base.core.service.ProviderConfigService;
import nsw.base.core.utils.Constants;
import nsw.base.core.utils.paging.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 子系统配置操作相关
 **/
@Controller
@RequestMapping(Constants.REST_PROVIDER_PREFIX)
public class ProviderConfigController extends BaseController {
	@Autowired
	ProviderConfigService providerConfigService;
	

	/**	
	 * 取得全部
	 */
	@RequestMapping(value = Constants.REST_PROVIDER_LIST)
	@ResponseBody
	public Pagination findUserPage(Pagination page, ProviderConfig providerConfig){
		Pagination rePage = null;
		rePage = providerConfigService.getProviderConfigPage(page.getPageNumber(), page.getPageSize(),page.getPageSort(), providerConfig);
		return rePage;
	}
	/**
	 * 修改
	 */
	@RequestMapping(value = Constants.REST_PROVIDER_EDIT)
	@ResponseBody
	public ProviderConfig edit(ProviderConfig providerConfig){
		providerConfigService.edit(providerConfig);
		return providerConfig;
	}
	/**
	 * 新增
	 */
	@RequestMapping(value = Constants.REST_PROVIDER_ADD)
	@ResponseBody
	public ProviderConfig add(ProviderConfig providerConfig){
		if("".equals(providerConfig.getId())){
			providerConfig.setId(null);
		}
		providerConfigService.add(providerConfig);
		return providerConfig;
	}
	/**
	 * 删除
	 */
	@RequestMapping(value = Constants.REST_PROVIDER_DEL)
	@ResponseBody
	public ProviderConfig del(ProviderConfig providerConfig){
		providerConfigService.del(providerConfig.getId());
		return providerConfig;
	}
	
}
