package nsw.base.core.controller;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsw.base.core.controller.base.BaseController;
import nsw.base.core.dao.entity.AttConfig;
import nsw.base.core.dao.entity.Container;
import nsw.base.core.dao.entity.Element;
import nsw.base.core.dao.entity.Page;
import nsw.base.core.dao.entity.Widget;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.entity.base.TreeDataEntity;
import nsw.base.core.service.AttConfigService;
import nsw.base.core.service.ContainerService;
import nsw.base.core.service.ElementService;
import nsw.base.core.service.PageService;
import nsw.base.core.service.WidgetService;
import nsw.base.core.service.base.DataSrcService;
import nsw.base.core.service.base.ExportService;
import nsw.base.core.service.base.ImportService;
import nsw.base.core.service.base.TreeDataService;
import nsw.base.core.utils.AopTargetUtils;
import nsw.base.core.utils.Constants;
import nsw.base.core.utils.RequestToBean;
import nsw.base.core.utils.WebContextFactoryUtil;
import nsw.base.core.utils.paging.Pagination;
import nsw.base.core.utils.paging.TablePagingService;
import nsw.base.core.utils.result.ResultString;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(Constants.REST_API_PREFIX)
public class CommonResController extends BaseController {
	@Autowired
	PageService pageService;
	@Autowired
	ContainerService containerService;
	@Autowired
	ElementService elementService;
	@Autowired
	WidgetService widgetService;
	@Autowired
	AttConfigService attConfigService;
	
	/**
	 * 构造页面请求
	 */
	@RequestMapping(value = Constants.PAGE_CONTAINERS)
	@ResponseBody
	public Page createPage(@PathVariable("pageId")String pageId){
		Page page = null;
		page = pageService.getPageDetailById(pageId);
		return page;
	}

	/**
	 * 取得子容器列�?
	 */
	@RequestMapping(value = Constants.SUB_CONTAINERS)
	@ResponseBody
	public List<Container> getSubContainers(@PathVariable("containerId")String containerId){
		List<Container> containers = null;
		containers = containerService.getSubContainers(containerId);
		return containers;
	}
	/**
	 * 取得菜单路径列表
	 */
	@RequestMapping(value = Constants.MENU_PATH)
	@ResponseBody
	public List<Element> getMenuPath(@PathVariable("elementId")String elementId){
		List<Element> elements = elementService.getElementPath(elementId);
		return elements;
	}
	/**
	 * 取得控件详细内容
	 */
	@RequestMapping(value = Constants.WIDGET_DETAIL)
	@ResponseBody
	public Widget getWidgetDetail(@PathVariable("widgetId")String widgetId){
		Widget widget = null;
		widget = widgetService.getWidgetDetailById(widgetId);
		return widget;
	}
	/**
	 * 根据�?属ID查询属�?�列�?
	 */
	@RequestMapping(value = Constants.ATT_CONFIG)
	@ResponseBody
	public List<AttConfig> getAttConfigsByBelongId(@PathVariable("belongId")String belongId){
		List<AttConfig> attConfigs = null;
		attConfigs = attConfigService.getAttConfigsByBelongId(belongId);
		return attConfigs;
	}
	/**
	 * 根据条件查询控件元素数据
	 */
	@RequestMapping(value = Constants.DATA_SRC_LIST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<DataSrcEntity> getDataSrcList(@PathVariable("serviceName")String serviceName, String json){
		List<DataSrcEntity> dataSrcEntityList = null;
		try {
			List<DataSrcEntity> objectList = null;
			DataSrcService dataSrcService = (DataSrcService)WebContextFactoryUtil.getBean(serviceName);
			ParameterizedType parameterizedType = (ParameterizedType) AopTargetUtils.getTarget(dataSrcService).getClass().getGenericInterfaces()[0];
			Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];
			BaseEntity baseEntity;
			baseEntity = (BaseEntity) clazz.newInstance();
			if(!"[]".equals(json)){
				baseEntity = Constants.GSON_FULL.fromJson(json, clazz);
			}
			dataSrcEntityList = dataSrcService.getDataSrcListByCondition(baseEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dataSrcEntityList;
	}

	/**
	 * 根据父ID取得树控件列�?
	 */
	@RequestMapping(value = Constants.TREE_DATA_LIST)
	@ResponseBody
	public TreeDataEntity getTreeDataList(@PathVariable("serviceName")String serviceName, @PathVariable("parentId")String parentId, String json){
		TreeDataEntity treeDataEntity = null;
		try {
			BaseEntity baseEntity = null;
			TreeDataService treeDataService = (TreeDataService)WebContextFactoryUtil.getBean(serviceName);
			Type[] parameterizedTypeList = AopTargetUtils.getTarget(treeDataService).getClass().getGenericInterfaces();
			ParameterizedType parameterizedType = null;
			for(Type currType : parameterizedTypeList){
				if(currType.toString().indexOf("TreeDataService") > -1){
					parameterizedType = (ParameterizedType)currType;
				}
			}
			Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];
			baseEntity = (BaseEntity) clazz.newInstance();
			if(!"\"non\"".equals(json)){
				baseEntity = Constants.GSON_FULL.fromJson(json, clazz);
			}
			treeDataEntity = treeDataService.getTreeDataListByParentId(parentId, baseEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return treeDataEntity;
	}
	
	

	/**
	 * 上传文件
	 */
	@RequestMapping(value = Constants.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	public String uploadfile(final @RequestParam("file") MultipartFile file){
		String uploadfile = null;
		uploadfile = super.saveUploadFile(file);
		if(uploadfile == null){
			uploadfile = "fail";
		}
		return uploadfile;
	}

	/**
	 * 导入Excel
	 */
	@RequestMapping(value = Constants.REST_IMPORT)
	@ResponseBody
	public ResultString importFile(HttpServletRequest request, String uploadImportFile, @PathVariable("serviceName")String serviceName, @PathVariable("tableId")String tableId){

		ResultString rs = new ResultString();
		try {
			ImportService importService = (ImportService)WebContextFactoryUtil.getBean(serviceName);
			if(tableId != null && uploadImportFile != null){
				File file = new File(uploadImportFile);
				if(file.exists()){
					List<BaseEntity> importDataList = importService.importData(request, uploadImportFile, tableId);
					String errorMsg = "";
					for(BaseEntity currBaseEntity : importDataList){
						if(currBaseEntity.getHasError()){
							errorMsg = errorMsg + "<br/>" + currBaseEntity.getErrors().get(0);
						}
					}
					if(!"".equals(errorMsg)){
						rs.setMsg(errorMsg);
						rs.setStatus(ResultString.RESULT_STATUS_FAILED);
						return rs;
					}
				}else {
					rs.setMsg("未找到导入文件！");
					rs.setStatus(ResultString.RESULT_STATUS_FAILED);
					return rs;
				}
			}else{
				rs.setMsg("未找到导入文件！");
				rs.setStatus(ResultString.RESULT_STATUS_FAILED);
				return rs;
			}
		} catch (Exception e) {
			e.printStackTrace();
			rs.setMsg("文件导入时发生系统异常，请及时联系管理员�?");
			rs.setStatus(ResultString.RESULT_STATUS_FAILED);
			return rs;
		}
		rs.setMsg("导入成功！");
		rs.setStatus(ResultString.RESULT_STATUS_SUCCESS);
		return rs;
	}
	

	/**
	 * 导出请求
	 */
	@RequestMapping(value = Constants.REST_EXPORT)
	public void exportDataTable(HttpServletRequest request, HttpServletResponse response, Pagination page, String[] ids, String tableId, @PathVariable("serviceName")String serviceName){

		Pagination rePage = null;
		ExportService exportService = (ExportService)WebContextFactoryUtil.getBean(serviceName);
		
		TablePagingService tablePagingService = (TablePagingService)exportService;
		Pagination pr = new Pagination(1, 1, null, tablePagingService);
		try {
			pr.doPage(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List dataList = pr.getList();
		if(dataList == null || dataList.size() == 0){
			return;
		}
		Object currEntity = dataList.get(0);
		Object queryBean = RequestToBean.getBeanToRequest(request, currEntity.getClass());
		pr = new Pagination(1, 10000, null, tablePagingService);
		try {
			pr.doPage((BaseEntity)queryBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataList = pr.getList();
		HSSFWorkbook wkb = exportService.exportData(dataList, tableId);
		//处理Excel响应
		responseObject(response, wkb);
	}
}
