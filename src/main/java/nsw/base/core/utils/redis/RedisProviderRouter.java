package nsw.base.core.utils.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsw.base.core.dao.entity.ProviderConfig;
import nsw.base.core.dao.entity.SubsysConfig;
import nsw.base.core.service.ProviderConfigService;
import nsw.base.core.service.SubsysConfigService;
import nsw.base.core.utils.PropertyUtils;
import nsw.base.core.utils.SimpleProxy;
import nsw.base.core.utils.ThreadVariable;
import nsw.base.core.utils.WebContextFactoryUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RedisProviderRouter {
	public static final String REDIS_PROVIDERS_KEY = "providers";
	public String providerCode;
	public HashMap<String, List<ProviderConfig>> providerConfigMap = new HashMap<String, List<ProviderConfig>>();
	private HashMap<String, ProviderConfig> providerConfigAllMap = new HashMap<String, ProviderConfig>();
	private HashMap<String, List<String>> providerConfigRandomIdMap = new HashMap<String, List<String>>();

	private HashMap<String, List<ProviderConfig>> providerConfigMapNew = new HashMap<String, List<ProviderConfig>>();
	private HashMap<String, ProviderConfig> providerConfigAllMapNew = new HashMap<String, ProviderConfig>();
	private HashMap<String, List<String>> providerConfigRandomIdMapNew = new HashMap<String, List<String>>();
	private boolean useNew = false;
	
	//加载服务提供者
	public void reloadProviders(){
		//给每个提供者进行注册
		SubsysConfigService subsysConfigService = (SubsysConfigService) WebContextFactoryUtil.getBean("subsysConfigServiceImpl");
		List<SubsysConfig> subsysConfigList = subsysConfigService.getAllList();
		for(SubsysConfig subsysConfig : subsysConfigList){
			this.registProvider(subsysConfig.getCode());
		}
	}
	
	
	//每5分钟执行一次
	@Scheduled(cron = "0 1/5 * * * ?")
	public void reloadJob(){
		System.out.println("################################systemdate =" + new Date());
		String serverType = PropertyUtils.getPropertyValue("serverType");
		if("customer".equals(serverType)){
			this.providerConfigMapNew = getAllProvider();
			Iterator<String> iter = this.providerConfigMapNew.keySet().iterator();
			while (iter.hasNext()) {
				String currProviderCode = iter.next();
				List<ProviderConfig> providerConfigList = this.providerConfigMapNew.get(currProviderCode);
				List<String> providerConfigRandomIdList = new ArrayList<String>();
				for(ProviderConfig providerConfig : providerConfigList){
					this.providerConfigAllMapNew.put(providerConfig.getId(), providerConfig);
					for(int i = 0; i < providerConfig.getWeight(); i ++ ){
						providerConfigRandomIdList.add(providerConfig.getId());
					}					
				}
				this.providerConfigRandomIdMapNew.put(currProviderCode, providerConfigRandomIdList);
			}
			useNew = true;
			this.providerConfigMap = this.providerConfigMapNew;
			this.providerConfigAllMap = this.providerConfigAllMapNew;
			this.providerConfigRandomIdMap = this.providerConfigRandomIdMapNew;
			useNew = false;
		}
	}
	public RedisProviderRouter(){
		String serverType = PropertyUtils.getPropertyValue("serverType");
		if("customer".equals(serverType)){
			this.providerConfigMap = getAllProvider();
			Iterator<String> iter = this.providerConfigMap.keySet().iterator();
			while (iter.hasNext()) {
				String currProviderCode = iter.next();
				List<ProviderConfig> providerConfigList = this.providerConfigMap.get(currProviderCode);
				List<String> providerConfigRandomIdList = new ArrayList<String>();
				for(ProviderConfig providerConfig : providerConfigList){
					providerConfigAllMap.put(providerConfig.getId(), providerConfig);
					for(int i = 0; i < providerConfig.getWeight(); i ++ ){
						providerConfigRandomIdList.add(providerConfig.getId());
					}					
				}
				providerConfigRandomIdMap.put(currProviderCode, providerConfigRandomIdList);
			}
		}else{
			registProvider();
		}
	}
	/**
	 * 服务发现
	 * 发现当前启动服务提供者的所有服务器，并注册到redis上
	 */
	public void registProvider(){
		ProviderConfigService providerConfigService = (ProviderConfigService) WebContextFactoryUtil.getBean("providerConfigServiceImpl");
		List<ProviderConfig> providerConfigList = providerConfigService.getProviderConfigsByCode(providerCode);
		if(providerConfigList != null && providerConfigList.size() > 0){
			HashMap<String, List<ProviderConfig>> providerConfigMap = getAllProvider();
			if(providerConfigMap == null){
				providerConfigMap = new HashMap<String, List<ProviderConfig>>();
			}
			providerConfigMap.put(providerCode, providerConfigList);
			this.providerConfigMap = providerConfigMap;
			RedisDb.setObject(REDIS_PROVIDERS_KEY.getBytes(), RedisDb.providerConfigMapToByte(providerConfigMap));
		}
	}
	/**
	 * 服务发现
	 * 发现当前启动服务提供者的所有服务器，并注册到redis上
	 */
	public void registProvider(String providerCode){
		ProviderConfigService providerConfigService = (ProviderConfigService) WebContextFactoryUtil.getBean("providerConfigServiceImpl");
		List<ProviderConfig> providerConfigList = providerConfigService.getProviderConfigsByCode(providerCode);
		if(providerConfigList != null && providerConfigList.size() > 0){
			HashMap<String, List<ProviderConfig>> providerConfigMap = getAllProvider();
			if(providerConfigMap == null){
				providerConfigMap = new HashMap<String, List<ProviderConfig>>();
			}
			providerConfigMap.put(providerCode, providerConfigList);
			this.providerConfigMap = providerConfigMap;
			RedisDb.setObject(REDIS_PROVIDERS_KEY.getBytes(), RedisDb.providerConfigMapToByte(providerConfigMap));
		}
	}

	public static HashMap<String, List<ProviderConfig>> getAllProvider(){
		HashMap<String, List<ProviderConfig>> providerConfigMap = null;
		byte[] bytes = RedisDb.getObject(REDIS_PROVIDERS_KEY.getBytes());
        if(bytes != null && bytes.length > 0){
        	providerConfigMap = (HashMap<String, List<ProviderConfig>>) RedisDb.byteToProviderConfigMap(bytes); 
        }
        return providerConfigMap;
	}

	public static List<ProviderConfig> getProvider(String providerCode){
		HashMap<String, List<ProviderConfig>> providerConfigMap = getAllProvider();
		if(providerConfigMap == null){
			return null;
		}
		return providerConfigMap.get(providerCode);
	}

	public List<ProviderConfig> getProvider(){
		HashMap<String, List<ProviderConfig>> providerConfigMap = getAllProvider();
		if(providerConfigMap == null){
			return null;
		}
		return providerConfigMap.get(this.providerCode);
	}
	public String getProviderCode() {
		return providerCode;
	}
	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	//按照权重随机抽取服务提供者
	private ProviderConfig randomProviderConfig(String providerCode){
		HashMap<String, ProviderConfig> providerConfigAllMapUse = null;
		HashMap<String, List<String>> providerConfigRandomIdMapUse = null;
		if(useNew){
			providerConfigAllMapUse = providerConfigAllMapNew;
			providerConfigRandomIdMapUse = providerConfigRandomIdMapNew;
		}else{
			providerConfigAllMapUse = providerConfigAllMap;
			providerConfigRandomIdMapUse = providerConfigRandomIdMap;
		}
		List<String> idList = providerConfigRandomIdMapUse.get(providerCode);
		int max = idList.size();
        int min = 0;
        Random random = new Random();
        int radomIndex = random.nextInt(max)%(max-min+1) + min;
        String randomId = idList.get(radomIndex);
        return providerConfigAllMapUse.get(randomId);
	}
	/** 
	 * 将map转换成url 
	 * @param requestMap 
	 * @return 
	 */  
	public static String getUrlParamsByMap(Map<String, String[]> requestMap) {  
	    if (requestMap == null) {  
	        return "";  
	    }  
	    StringBuffer sb = new StringBuffer();  
	    for (Entry<String, String[]> entry : requestMap.entrySet()) {
	    	String[] valueList = entry.getValue();
	    	if(valueList.length == 1){
		        sb.append(entry.getKey() + "=" + valueList[0]);
		        sb.append("&");  
	    	}else if(valueList.length > 1){
	    		int index = 0;
	    		for(String currValue : valueList){
			        sb.append(entry.getKey() + "[" + index + "] = " + currValue);
			        sb.append("&");  
			        index ++;
	    		}
	    	}
	    }  
	    String s = sb.toString();  
	    if (s.endsWith("&")) {  
	        s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");  
	    }  
	    return s;  
	}  
	public void redirectToProvider(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String reqUrl = request.getRequestURI();
		String providerCode = reqUrl.substring(reqUrl.indexOf("providerCode_") + 13, reqUrl.indexOf("_", reqUrl.indexOf("providerCode_") + 13));
		String providerIp = "127.0.0.1";
		String providerPort = "80";

		String serverType = ThreadVariable.getServerTypeVariable();
		if("1".equals(serverType)){
			if(!StringUtils.isEmpty(ThreadVariable.getServerIpVariable())){
				providerIp = ThreadVariable.getServerIpVariable();
			}
			if(!StringUtils.isEmpty(ThreadVariable.getServerPortVariable())){
				providerPort = ThreadVariable.getServerPortVariable();
			}
		}else{
			ProviderConfig providerConfig = randomProviderConfig(providerCode);
			if(!StringUtils.isEmpty(providerConfig.getIp())){
				providerIp = providerConfig.getIp();
			}
			if(!StringUtils.isEmpty(providerConfig.getPort())){
				providerPort = providerConfig.getPort();
			}
		}
		String strUrl= "http://" + providerIp + ":"+ providerPort + reqUrl;
		String queryString = request.getQueryString();
		if(queryString!=null&&("".equals(queryString.trim()))==false){
			strUrl+="?"+queryString;
		}
		strUrl = strUrl.replace("providerCode_" + providerCode+"_", "");
		System.out.println(strUrl);
		SimpleProxy.doServer(request, response, strUrl);
		
//	        HttpUtils.getOutputContext(strUrl, cookiesStr, requestUrl, outputStream);
//	        outputStream.close();
	}
}
