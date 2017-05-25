package nsw.base.core.service.impl.base;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import nsw.base.core.dao.entity.AttConfig;
import nsw.base.core.dao.entity.Element;
import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.entity.Widget;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.service.ElementService;
import nsw.base.core.service.WidgetService;
import nsw.base.core.service.base.BaseService;
import nsw.base.core.service.base.DataSrcService;
import nsw.base.core.service.base.ExportService;
import nsw.base.core.service.base.ImportService;
import nsw.base.core.utils.AopTargetUtils;
import nsw.base.core.utils.BeansUtils;
import nsw.base.core.utils.Constants;
import nsw.base.core.utils.ExcelHelper;
import nsw.base.core.utils.RequestToBean;
import nsw.base.core.utils.ThreadVariable;
import nsw.base.core.utils.WebContextFactoryUtil;
import nsw.base.core.utils.paging.Pagination;
import nsw.base.core.utils.paging.TablePagingService;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T>, ImportService<T>, ExportService<T> , TablePagingService{
	@Autowired
	WidgetService widgetService;
	@Autowired
	ElementService elementService;
	private Class<T> persistentClass;

	public abstract BaseDaoMapper<T> getCurrDaoMapper();

	/** 
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找�?, 返回Object.class. 
     *  
     *@param clazz 
     *            clazz The class to introspect 
     * @param index 
     *            the Index of the generic ddeclaration,start from 0. 
     * @return the index generic declaration, or Object.class if cannot be 
     *         determined 
     */  
    @SuppressWarnings("unchecked")  
    public static Class getSuperClassGenricType(final Class clazz, final int index) {  
          
        //返回表示�? Class �?表示的实体（类�?�接口�?�基本类型或 void）的直接超类�? Type�?  
        Type genType = clazz.getGenericSuperclass();  
  
        if (!(genType instanceof ParameterizedType)) {  
           return Object.class;  
        }  
        //返回表示此类型实际类型参数的 Type 对象的数组�??  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
  
        if (index >= params.length || index < 0) {  
                     return Object.class;  
        }  
        if (!(params[index] instanceof Class)) {  
              return Object.class;  
        }  
  
        return (Class) params[index];  
    }

	public List<T> importData(HttpServletRequest request, String uploadImportFile, String tableId) {
		File importFile = new File(uploadImportFile);
		Widget tableWidget = widgetService.getWidgetDetailById(tableId);
		Map<String, String> labelNameMap = new HashMap<String, String>();
		this.persistentClass = (Class<T>)getSuperClassGenricType(getClass(), 0);
		Map<String,HashMap<String,Object>> dataConverMap = new HashMap<String, HashMap<String,Object>>();
		List<T> resultList = new ArrayList<T>();
		//构�?�导入用Mapper数据结构请参�?
		/**
		 * 1、�?�labelNameMap】表格元素标签名称对照表
		 *    1-1、Key：元素标�?
		 *    1-2、Value：元素name属�??
		 *    1-3、作用：取得excel中的数据时，
		 *    能够方便的找到当前数据映射到entity对象的那个属性中
		 * 2、�?�dataConverMap】数据转换对照表
		 *    2-1、Key：元素name属�??
		 *    2-2、Value：当前元素存储�?�与表现值的对应Map
		 *         2-2-1、Key：数据存储�??
		 *         2-2-2、Value：数据表现�??
		 *    2-3、作用：将导入数据的表现值，转化为存储�?�放入对象属性中
		 */
		try {
			for(Element element : tableWidget.getElements()){
				String elementLabel = null;
				String elementName = null;
				String elementType = null;
				String elementFormatter = null;
				String elementFormatoptions = null;
				if(element.getName() != null){
					elementName = element.getName();
				}
				for(AttConfig attConfig : elementService.getMyAttConfigList(element)){
					if("label".equals(attConfig.getType())){
						elementLabel = attConfig.getAttValue();
					}
					if("name".equals(attConfig.getType())){
						elementName = attConfig.getAttValue();
					}
					if("formatter".equals(attConfig.getType())){
						elementFormatter = attConfig.getAttValue();
					}
					if("type".equals(attConfig.getType())){
						elementType = attConfig.getAttValue();
					}
					if("formatoptions".equals(attConfig.getType())){
						elementFormatoptions = attConfig.getAttValue();
					}
				}
				if("select".equals(elementFormatter)){
					JsonParser parse =new JsonParser();  //创建json解析�?
					JsonObject json=(JsonObject) parse.parse(elementFormatoptions);
					HashMap<String,Object> dataSrcMapper = new HashMap<String, Object>();
					String serviceName = null;
					JsonObject value = null;
					if(json.get("serviceName") != null){
						serviceName = json.get("serviceName").getAsString();
					}
					if(json.get("value") != null){
						value = json.get("value").getAsJsonObject();
					}
					if(serviceName != null){
						//根据json参数取得数据源Map（参数为动�?�的时�?�把json放入Map�?
						dataSrcMapper = this.getDataSrcFromJson(json, false);
					}else if(value != null){
						HashMap<String,String> dataSrcCovMapper = Constants.GSON_FULL.fromJson(value.toString(), new TypeToken<HashMap<String, String>>() {}.getType());
						Iterator iter = dataSrcCovMapper.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							String key = (String)entry.getKey();
							String val = (String)entry.getValue();
							dataSrcMapper.put(val, key);
						}
						
					}
					dataConverMap.put(elementName, dataSrcMapper);
				}
				labelNameMap.put(elementLabel, elementName);
				
			}
			//取得Excel中的数据并进行Mapping，映射到Entity对象数组�?
			List<List<Cell>> excelDataList = ExcelHelper.exportListFromExcel(importFile, 0); 
			int index = 0;
			Map<Integer, Object> excelDataIndex = new HashMap<Integer, Object>();
			for(List<Cell> currDataList : excelDataList){
				int colIndex = 0;
				//首行，标�?
				if(index == 0){
					for(Cell currDataNote : currDataList){
						if(currDataNote == null){
							break;
						}
						excelDataIndex.put(colIndex, ExcelHelper.getStringFromCell(currDataNote));
						colIndex ++;
					}
				//数据内容�?
				}else{
					T t = persistentClass.newInstance();
					for(Cell currDataNote : currDataList){
						Object currLabel = excelDataIndex.get(colIndex);
						if(currLabel == null){
							continue;
						}
						if(labelNameMap.containsKey(currLabel.toString())){
							String currName = labelNameMap.get(currLabel);
							Class currItemClass = BeansUtils.getPropertyTypeFromClass(persistentClass, currName);
							if(currItemClass == null){
								continue;
							}							
							Object currValue = currDataNote;
							if(currValue == null){
								PropertyUtils.setProperty(t, currName, currValue);
							}else{
								try{
									if(currItemClass.equals(Date.class)){
										currValue = ExcelHelper.getDateFromCell(currDataNote);
									}else if(currItemClass.equals(Integer.class)){
										currValue = ExcelHelper.getIntegerFromCell(currDataNote);
									}else if(currItemClass.equals(Double.class)){
										currValue = ExcelHelper.getDoubleFromCell(currDataNote);
									}else if(currItemClass.equals(Boolean.class)){
										currValue = ExcelHelper.getBooleanFromCell(currDataNote);
									}else{
										currValue = ExcelHelper.getStringFromCell(currDataNote);
									}
									HashMap<String,Object> dataSrcMapper = dataConverMap.get(currName);
									if(dataSrcMapper != null){
										if(dataSrcMapper.get(currValue) == null){
											if(dataSrcMapper.get("json") == null && colIndex == 0){
												throw new Exception();
											}
										}else{
											currValue = dataSrcMapper.get(currValue);
										}
									}
									PropertyUtils.setProperty(t, currName, currValue);
								}catch(Exception e){
									t.addError("属�?�：" + currLabel + "的第"+ index +"行数据在加载数据时发生异常！");
								}
							}
						}
						colIndex ++;
					}
					colIndex = 0;
					for(Cell currDataNote : currDataList){
						Object currLabel = excelDataIndex.get(colIndex);
						if(currLabel == null){
							continue;
						}
						if(labelNameMap.containsKey(currLabel.toString())){
							String currName = labelNameMap.get(currLabel);
							//重新设置Entity的�??
							try{
								resetEntityValue(t, currName, dataConverMap);
							}catch(Exception e){
								t.addError("属�?�：" + currLabel + "的第"+ index +"行数据在加载数据时发生异常！");
							}
						}
						colIndex ++;
					}
					resultList.add(t);
				}
				index ++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//将Entity数组中的有效数据保存到数据库
		int index = 0;
		BaseEntity user = ThreadVariable.getUser();
		for(T t : resultList){			
			try {
				//取得接口传入数据作为导入的�?�用数据
				Object commBean = RequestToBean.getBeanToRequest(request, persistentClass);
				BeansUtils.merge(t, commBean);
				if(t.getCreateDate() == null){
					t.setCreateDate(new Date());
					if(user != null){
						t.setCreateUserId(user.getId());
					}
				}
				//没有发生错误的数据才可以保存数据�?
				if(!t.getHasError()){
					this.getCurrDaoMapper().save(t);
				}
			} catch (Exception e) {
				t.addError("�?" + index + "行数据在保存DB的过程中发生错误，请�?查数据是否存在异常！");
				e.printStackTrace();
			}
			index ++;
		}
		return resultList;
	}
	
	private void resetEntityValue(T t, String currName, Map<String,HashMap<String,Object>> dataConverMap) throws Exception{
		HashMap<String,Object> dataSrcMapper = dataConverMap.get(currName);
        Object currObject = PropertyUtils.getProperty(t, currName);
		if(dataSrcMapper != null && dataSrcMapper.get("json") != null){
			JsonObject json=(JsonObject) dataSrcMapper.get("json");
			//替换json中括号内�?
			String dataUrl = json.toString();
			Pattern pattern = Pattern.compile("\\[(.+?)\\]");
	        Matcher matcher = pattern.matcher(dataUrl);
	        StringBuffer sb = new StringBuffer();
	        while(matcher.find()) {
	        	String mg = matcher.group(1);
	        	HashMap<String,Object> parentDataSrcMapper = dataConverMap.get(currName);
	        	if(parentDataSrcMapper != null && parentDataSrcMapper.get("json") != null){
					resetEntityValue(t, mg, dataConverMap);
	        	}
	        	Object mgObject = PropertyUtils.getProperty(t, mg);
	        	if(mgObject == null){
	        		return;
	        	}
	            matcher.appendReplacement(sb, mgObject.toString());
	        }
	        matcher.appendTail(sb);
			JsonParser parse =new JsonParser();
	        json=(JsonObject) parse.parse(sb.toString());
			dataSrcMapper = this.getDataSrcFromJson(json, false);
			if(dataSrcMapper.get(currObject) == null ){
				if(!dataSrcMapper.containsValue(currObject)){
					throw(new Exception());
				}
			}else {
				PropertyUtils.setProperty(t, currName, dataSrcMapper.get(currObject));
			}
//			dataConverMap.put(currName, dataSrcMapper);
		}
	}
	
	private HashMap<String, Object> getDataSrcFromJson(JsonObject json, boolean exportFlag) throws Exception{
		HashMap<String,Object> dataSrcMapper = new HashMap<String, Object>();
		String serviceName = json.get("serviceName").getAsString();
		JsonObject queryCondition = json.get("queryCondition").getAsJsonObject();
		DataSrcService dataSrcService = (DataSrcService)WebContextFactoryUtil.getBean(serviceName);
		ParameterizedType parameterizedType = null;
		if(dataSrcService != null){
			parameterizedType = (ParameterizedType) AopTargetUtils.getTarget(dataSrcService).getClass().getGenericInterfaces()[0];
		}
		if(parameterizedType != null){
			Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];
			BaseEntity baseEntity;
			baseEntity = (BaseEntity) clazz.newInstance();
			baseEntity = Constants.GSON_FULL.fromJson(queryCondition, clazz);
			List<DataSrcEntity> dataSrcEntityList = null;
			dataSrcEntityList = dataSrcService.getDataSrcListByCondition(baseEntity);
			if(dataSrcEntityList == null || dataSrcEntityList.size() < 1){
				dataSrcMapper.put("json", json);
			}
			for(DataSrcEntity dataSrcEntity : dataSrcEntityList){
				if(exportFlag){
					dataSrcMapper.put(dataSrcEntity.getValue(), dataSrcEntity.getLabel());
				}else{
					dataSrcMapper.put(dataSrcEntity.getLabel(), dataSrcEntity.getValue());
				}
			}
		}
		return dataSrcMapper;
	}
	public HSSFWorkbook exportData(List<T> dataList, String tableId){
		Widget tableWidget = widgetService.getWidgetDetailById(tableId);
		Map<String, String> nameLabelMap = new HashMap<String, String>();
		List<Object> nameList = new LinkedList<Object>();
		List<Object> labelList = new LinkedList<Object>();
		List<List<Object>> exportList = new ArrayList<List<Object>>();
		Map<String,HashMap<String,Object>> dataConverMap = new HashMap<String, HashMap<String,Object>>();
		List<Integer> nameListIndex = new LinkedList<Integer>();
		Integer loopIndex = 0;
		try {
			for(Element element : tableWidget.getElements()){
				String elementLabel = null;
				String elementName = null;
				String elementOrder = null;
				String elementType = null;
				String elementHidden = null;
				String elementFormatter = null;
				String elementFormatoptions = null;
				if(element.getName() != null){
					elementName = element.getName();
				}
				for(AttConfig attConfig : elementService.getMyAttConfigList(element)){
					if("label".equals(attConfig.getType())){
						elementLabel = attConfig.getAttValue();
					}
					if("name".equals(attConfig.getType())){
						elementName = attConfig.getAttValue();
					}
					if("order".equals(attConfig.getType())){
						elementOrder = attConfig.getAttValue();
					}
					if("formatter".equals(attConfig.getType())){
						elementFormatter = attConfig.getAttValue();
					}
					if("type".equals(attConfig.getType())){
						elementType = attConfig.getAttValue();
					}
					if("hidden".equals(attConfig.getType())){
						elementHidden = attConfig.getAttValue();
					}
					if("formatoptions".equals(attConfig.getType())){
						elementFormatoptions = attConfig.getAttValue();
					}
				}
				if("hidden".equals(elementType) || "true".equals(elementHidden)){
					continue;
				}
				if("select".equals(elementFormatter)){
					JsonParser parse =new JsonParser();  //创建json解析�?
					JsonObject json=(JsonObject) parse.parse(elementFormatoptions);
					HashMap<String,Object> dataSrcMapper = new HashMap<String, Object>();
					String serviceName = null;
					JsonObject value = null;
					if(json.get("serviceName") != null){
						serviceName = json.get("serviceName").getAsString();
					}
					if(json.get("value") != null){
						value = json.get("value").getAsJsonObject();
					}
					if(serviceName != null){
						dataSrcMapper = this.getDataSrcFromJson(json, true);
					}else if(value != null){
						dataSrcMapper = Constants.GSON_FULL.fromJson(value.toString(), new TypeToken<HashMap<String, String>>() {}.getType());
					}
					dataConverMap.put(elementName, dataSrcMapper);
				}
				nameLabelMap.put(elementName, elementLabel);
//				nameList.add(elementName);
//				labelList.add(elementLabel);				

				Integer currIndex = null;
				try{
					if(elementOrder == null){
						currIndex = loopIndex;
					}else{
						currIndex = Integer.parseInt(elementOrder);
					}
				}catch(Exception e){
					currIndex = loopIndex;
				}
				Integer lastIndex = 0;
				for(Integer currNameListIndex : nameListIndex){
					if(currIndex > currNameListIndex){
						lastIndex ++; 
					}
				}
				nameList.add(lastIndex, elementName);
				labelList.add(lastIndex, elementLabel);
				nameListIndex.add(lastIndex, currIndex);
				loopIndex ++;
			}
			//加入标题
			exportList.add(labelList);
			for(T currDataBean : dataList){
				List dataListForExport = new LinkedList<Object>();
				for(Object currName : nameList){
					String currNameStr = currName.toString();
					try{
						ObjectMapper mapper = new ObjectMapper();
						String jsonString = mapper.writeValueAsString(currDataBean);
						HashMap<String, Object> dataMap= mapper.readValue(jsonString, HashMap.class);
//						Object currValue = PropertyUtils.getProperty(currDataBean, currNameStr);
						Object currValue = dataMap.get(currNameStr);
						HashMap<String,Object> dataSrcMapper = dataConverMap.get(currNameStr);
						if(dataSrcMapper != null){
							if(dataSrcMapper.size() == 1 && dataSrcMapper.get("json") != null){
								JsonObject json=(JsonObject) dataSrcMapper.get("json");
								//替换json中括号内�?
								String dataUrl = json.toString();
								Pattern pattern = Pattern.compile("\\[(.+?)\\]");
						        Matcher matcher = pattern.matcher(dataUrl);
						        StringBuffer sb = new StringBuffer();
						        while(matcher.find()) {
						        	String mg = matcher.group(1);
						        	Object mgObject = PropertyUtils.getProperty(currDataBean, mg);						        	
						            matcher.appendReplacement(sb, mgObject.toString());
						        }
						        matcher.appendTail(sb);
								JsonParser parse =new JsonParser();
						        json=(JsonObject) parse.parse(sb.toString());
								dataSrcMapper = this.getDataSrcFromJson(json, true);
							}
							currValue = dataSrcMapper.get(currValue);
						}
						dataListForExport.add(currValue);
					}catch (Exception e){
						//去除
						dataListForExport.add("");
						e.printStackTrace();
					}
				}
				exportList.add(dataListForExport);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ExcelHelper.exportExcel(null, exportList);
	}

	@Override
	public T addEntity(T t) {
		this.getCurrDaoMapper().save(t);
		return t;
	}

	@Override
	public T editEntity(T t) {
		Date currTime = new Date();
		User loginUser = null;
		loginUser = (User) ThreadVariable.getUser();
		t.setCreateDate(currTime);
		t.setUpdateDate(currTime);
		if(loginUser != null){
			t.setCreateUserId(loginUser.getCreateUserId());
			t.setUpdateUserId(loginUser.getCreateUserId());
		}
		this.getCurrDaoMapper().update(t);
		return t;
	}

	@Override
	public T getEntityById(String id){
		return this.getCurrDaoMapper().getById(id);
	}
	@Override
	public T delEntity(String id) {
		//获取删除前的Entity
		T t = this.getCurrDaoMapper().getById(id);
		//按照ID删除Entity
		this.getCurrDaoMapper().deleteById(id);
		return t;
	}

	@Override
	public List<T> getEntityListByCondition(T t) {
		return this.getCurrDaoMapper().getListByCondition(t);
	}

	@Override
	public Pagination getEntityPage(int pageNo, int size, String sort, T t) {
		Pagination pr = new Pagination(pageNo, size, sort, this);
		try {
			pr.doPage(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pr;
	}

	@Override
	public int getCount(Pagination page) {
		return this.getCurrDaoMapper().getCount(page);
	}

	@Override
	public List findPageData(Pagination page) {
		return this.getCurrDaoMapper().findPageData(page);
	}
}
