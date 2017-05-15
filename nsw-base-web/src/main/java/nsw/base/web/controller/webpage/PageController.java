package nsw.base.web.controller.webpage;

import nsw.base.core.dao.entity.Page;
import nsw.base.core.service.PageService;
import nsw.base.core.utils.Constants;
import nsw.base.core.utils.paging.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(Constants.REST_PAGE_PREFIX)
public class PageController {

	@Autowired
	PageService pageService;

	/**
	 * 构�?�页面请�?
	 */
	@RequestMapping(value = Constants.REST_USER_LIST)
	@ResponseBody
	public Pagination findPagePage(Pagination pagination, Page page){
		Pagination rePage = null;
		rePage = pageService.getPagePage(pagination.getPageNumber(), pagination.getPageSize(),pagination.getPageSort(), page);
		return rePage;
	}

	/**
	 * 保存容器数据
	 */
	@RequestMapping(value = Constants.REST_PAGE_SOU)
	@ResponseBody
	public Page saveOrUpdate(Page page){
		page = pageService.saveOrUpdatePage(page);
		return page;
	}

	/**
	 * 删除容器数据
	 */
	@RequestMapping(value = Constants.REST_PAGE_DEL)
	@ResponseBody
	public Page del(Page page){
		pageService.delPage(page);
		return page;
	}
	
	
}
