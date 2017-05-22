package nsw.base.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constants {

	public static final Gson GSON;
	public static final Gson GSON_FULL;
	public static final Gson DEFAULT_GSON;
	
	public static final String REST_API_PREFIX = "/api";
	public static final String REST_IMPORT = "/import/{serviceName}/{tableId}";
	public static final String REST_EXPORT = "/export/{serviceName}";
	public static final String PAGE_CONTAINERS = "/pageContainers/{pageId}";
	public static final String SUB_CONTAINERS = "/subContainers/{containerId}";
	public static final String MENU_PATH = "/menuPath/{elementId}";
	public static final String WIDGET_DETAIL = "/widgetDetail/{widgetId}";
	public static final String ATT_CONFIG = "/attConfigs/{belongId}";
	public static final String DATA_SRC_LIST = "/dataSrcList/{serviceName}";
	public static final String TREE_DATA_LIST = "/treeDataList/{serviceName}/{parentId}";
	public static final String UPLOAD_FILE = "/uploadfile";
	/**
	 * 用户相关请求
	 */
	public static final String REST_USER_PREFIX = "/api/user";
	public static final String REST_USER_LIST = "/list";
	public static final String REST_USER_IMPORT = "/import/{tableId}";
	public static final String REST_USER_EXPORT = "/export";
	public static final String REST_USER_BATDEL = "/batdel";
	public static final String REST_USER_EDIT = "/edit";
	public static final String REST_USER_ADD = "/add";
	public static final String REST_USER_DEL = "/del";
	/**
	 * 子系统配置相关请求
	 */
	public static final String REST_SUBSYS_PREFIX = "/api/subsys";
	public static final String REST_SUBSYS_LIST = "/list";
	public static final String REST_SUBSYS_EDIT = "/edit";
	public static final String REST_SUBSYS_ADD = "/add";
	public static final String REST_SUBSYS_DEL = "/del";
	public static final String REST_SUBSYS_RELOAD = "/reload";
	/**
	 * 服务提供者配置相关请求
	 */
	public static final String REST_PROVIDER_PREFIX = "/api/provider";
	public static final String REST_PROVIDER_LIST = "/list";
	public static final String REST_PROVIDER_EDIT = "/edit";
	public static final String REST_PROVIDER_ADD = "/add";
	public static final String REST_PROVIDER_DEL = "/del";
	
	/**
	 * 用户角色相关请求
	 */
	public static final String REST_USER_ROLE_PREFIX = "/api/userrole";
	
	/**
	 * 容器相关请求
	 */
	public static final String REST_CONTAINER_PREFIX = "/api/container";
	public static final String REST_CONTAINER_LIST = "/list";
	public static final String REST_CONTAINER_SOU = "/saveorupdate";
	public static final String REST_CONTAINER_COPY_TEMPLETE = "/copytemplete";
	public static final String REST_CONTAINER_EDIT = "/edit";
	public static final String REST_CONTAINER_ADD = "/add";
	public static final String REST_CONTAINER_DEL = "/del";
	/**
	 * 页面编辑相关请求
	 */
	public static final String REST_PAGE_PREFIX = "/api/page";
	public static final String REST_PAGE_LIST = "/list";
	public static final String REST_PAGE_SOU = "/saveorupdate";
	public static final String REST_PAGE_COPY_TEMPLETE = "/copytemplete";
	public static final String REST_PAGE_EDIT = "/edit";
	public static final String REST_PAGE_ADD = "/add";
	public static final String REST_PAGE_DEL = "/del";
	/**
	 * 元素相关请求
	 */
	public static final String REST_ELEMENT_PREFIX = "/api/element";
	public static final String REST_ELEMENT_LIST = "/list";
	public static final String REST_ELEMENT_SOU = "/saveorupdate";
	public static final String REST_ELEMENT_COPY_TEMPLETE = "/copytemplete";
	public static final String REST_ELEMENT_EDIT = "/edit";
	public static final String REST_ELEMENT_ADD = "/add";
	public static final String REST_ELEMENT_DEL = "/del";

	/**
	 * 控件相关请求
	 */
	public static final String REST_WIDGET_PREFIX = "/api/widget";
	public static final String REST_WIDGET_LIST = "/list";
	public static final String REST_WIDGET_SOU = "/saveorupdate";
	public static final String REST_WIDGET_COPY_TEMPLETE = "/copytemplete";
	public static final String REST_WIDGET_EDIT = "/edit";
	public static final String REST_WIDGET_ADD = "/add";
	public static final String REST_WIDGET_DEL = "/del";
	/**
	 * 属�?�相关请�?
	 */
	public static final String REST_ATT_CONFIG_PREFIX = "/api/attribute";
	public static final String REST_ATT_CONFIG_LIST = "/list";
	public static final String REST_ATT_CONFIG_BATDEL = "/batdel";
	public static final String REST_ATT_CONFIG_EDIT = "/edit";
	public static final String REST_ATT_CONFIG_ADD = "/add";
	public static final String REST_ATT_CONFIG_DEL = "/del";

	/**
	 * 价格管理相关请求
	 */
	public static final String REST_PRICE_PREFIX = "/api/price";
	public static final String REST_PRICE_LIST = "/list";
	public static final String REST_PRICE_SOU = "/saveorupdate";
	public static final String REST_PRICE_EDIT = "/edit";
	public static final String REST_PRICE_ADD = "/add";
	public static final String REST_PRICE_DEL = "/del";
	public static final String REST_PRICE_ACT = "/active";


	/**
	 * 包裹查看相关请求
	 */
	public static final String REST_PACKAGE_PREFIX = "/api/package";
	public static final String REST_PACKAGE_LIST = "/list";
	public static final String REST_PACKAGE_SOU = "/saveorupdate";
	public static final String REST_PACKAGE_EDIT = "/edit";
	public static final String REST_PACKAGE_ADD = "/add";
	public static final String REST_PACKAGE_DEL = "/del";
	/**
	 * 包裹错误费用调整查看相关请求
	 */
	public static final String REST_PACKAGEERROR_PREFIX = "/api/packageerror";
	public static final String REST_PACKAGEERROR_LIST = "/list";
	public static final String REST_PACKAGEERROR_SOU = "/saveorupdate";
	public static final String REST_PACKAGEERROR_EDIT = "/edit";
	public static final String REST_PACKAGEERROR_ADD = "/add";
	public static final String REST_PACKAGEERROR_DEL = "/del";
	
	/**
	 * 导入费用管理相关请求
	 */
	public static final String REST_AMOUNT_PREFIX = "/api/amount";
	public static final String REST_AMOUNT_LIST = "/list";
	public static final String REST_AMOUNT_SOU = "/saveorupdate";
	public static final String REST_AMOUNT_EDIT = "/edit";
	public static final String REST_AMOUNT_ADD = "/add";
	public static final String REST_AMOUNT_DEL = "/del";

	/**
	 * 应收应付费用管理相关请求
	 */
	public static final String REST_VIEW_AMOUNT_PREFIX = "/api/viewamount";
	public static final String REST_VIEW_AMOUNT_LIST = "/list";
	public static final String REST_VIEW_AMOUNT_STAT = "/stat";
	public static final String REST_VIEW_AMOUNT_CREATE_SETTLEMENT = "/createsettle";

	/**
	 * 结算单管理相关请�?
	 */
	public static final String REST_SETTLEMENT_PREFIX = "/api/settlement";
	public static final String REST_SETTLEMENT_LIST = "/list";
	public static final String REST_SETTDETAIL_LIST = "/detaillist";
	public static final String REST_SETTLEMENT_CREATE = "/create";
	public static final String REST_SETTLEMENT_STAT = "/stat";
	public static final String REST_SETTLEMENT_EDIT = "/edit";
	public static final String REST_SETTLEMENT_ADD = "/add";
	public static final String REST_SETTLEMENT_DEL = "/del";
	public static final String REST_SETTLEMENT_AUTH = "/auth";
	public static final String REST_SETTLEMENT_PAIDIN = "/paidin";
	public static final String REST_SETTLEMENT_BATDEL = "/batdel";

	/**
	 * 空港与城市表
	 */
	public static final String CITY_AIRPORT_PREFIX = "/api/city";
	public static final String CITY_AIRPORT_LIST = "/list";
	public static final String CITY_AIRPORT_EDIT = "/edit";
	public static final String CITY_AIRPORT_DEL = "/del";
	
	
	/**
	 * 客户
	 */
	public static final String CITY_CUSTOMER_PREFIX = "/api/customer";
	public static final String CITY_CUSTOMER_LIST = "/list";
	public static final String CITY_CUSTOMER_EDIT = "/edit";
	public static final String CITY_CUSTOMER_DEL = "/del";
	public static final String CITY_CUSTOMER_INIT = "/init";
	
	/**
	 * 应付发票
	 */
	public static final String INVOICE_PAYABLE_PREFIX = "/api/invoicePayable";
	public static final String INVOICE_PAYABLE_LIST = "/list";
	public static final String INVOICE_PAYABLE_EDIT = "/edit";
	public static final String INVOICE_PAYABLE_DEL = "/del";
	
	/**
	 * 应收发票
	 */
	public static final String INVOICE_RECEIVEABLE_PREFIX = "/api/invoiceReceiveable";
	public static final String INVOICE_RECEIVEABLE_LIST = "/list";
	public static final String INVOICE_RECEIVEABLE_EDIT = "/edit";
	public static final String INVOICE_RECEIVEABLE_DEL = "/del";

	/**
	 * 客户航班管理相关请求
	 */
	public static final String REST_AIR_LINE_PREFIX = "/api/airline";
	public static final String REST_AIR_LINE_LIST = "/list";
	public static final String REST_AIR_LINE_SOU = "/saveorupdate";
	public static final String REST_AIR_LINE_EDIT = "/edit";
	public static final String REST_AIR_LINE_ADD = "/add";
	public static final String REST_AIR_LINE_DEL = "/del";
	static {

		DEFAULT_GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();

		GSON = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		GSON_FULL = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}
}
