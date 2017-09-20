package nsw.base.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;


/**
 * @author Tom
 */
public class PropertyUtils implements BeanFactoryAware {
	
	public List<String> getList(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				list.add(properties.getProperty(key));
			}
		}
		return list;
	}
	
	public Set<String> getSet(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptySet();
		}
		Set<String>set=new TreeSet<String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				set.add(properties.getProperty(key));
			}
		}
		return set;
	}
	

	public Map<String, String> getMap(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyMap();
		}
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		int len = prefix.length();
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				map.put(key.substring(len), properties.getProperty(key));
			}
		}
		return map;
	}

	public Properties getProperties(String prefix) {
		Properties props = new Properties();
		if (properties == null || prefix == null) {
			return props;
		}
		Enumeration<?> en = properties.propertyNames();
		String key;
		int len = prefix.length();
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				props.put(key.substring(len), properties.getProperty(key));
			}
		}
		return props;
	}
	
	public String getPropertiesString(String prefix) {
		String property = "";
		if (properties == null || prefix == null) {
			return property;
		}
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.equals(prefix)) {
				return properties.getProperty(key);
			}
		}
		return property;
	}

	public Map<String, Object> getBeanMap(String prefix) {
		Map<String, String> keyMap = getMap(prefix);
		if (keyMap.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(keyMap.size());
		String key, value;
		for (Map.Entry<String, String> entry : keyMap.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			resultMap.put(key, beanFactory.getBean(value, Object.class));
		}
		return resultMap;
	}
	
	public static Properties getProperties(File file) {
		Properties props = new Properties();
		InputStream in;
		try {
			in = new FileInputStream(file);
			props.load(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return props;
	}
	
	public static String getPropertyValue(File file,String key) {
		Properties props=getProperties(file);
		return (String) props.get(key);
	}

	private BeanFactory beanFactory;
	private Properties properties;

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	/**
	 * 从中读取�?
	 * @param key
	 * @return
	 */
	public static String getPropertyValue(String key){
		try {
			StringBuffer path = new StringBuffer();
			path.append(PropertyUtils.class.getClassLoader().getResource("").toURI().toString().replace("classes/", "").replace("file:", ""));
			path.append("classes");
			Properties props = new Properties();
			InputStream fis = new FileInputStream(URLDecoder.decode(path.toString(),"utf-8")+"/conf/config.properties");
			props.load(fis);
			fis.close();
			if(props.getProperty(key) == null){
				return getCorePropertyValue(key);
			}else{
	            return props.getProperty(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 从中读取�?
	 * @param key
	 * @return
	 */
	public static String getCorePropertyValue(String key){
		try {
//			StringBuffer path = new StringBuffer();
//			path.append(PropertyUtils.class.getClassLoader().getResource("").toURI().toString().replace("classes/", "").replace("file:", ""));
//			path.append("classes");
			Properties props = new Properties();
//			InputStream fis = new FileInputStream(URLDecoder.decode(path.toString(),"utf-8")+"/conf/core.config.properties");
			InputStream fis = new PropertyUtils().getClass().getResourceAsStream("/conf/core.config.properties");
			props.load(fis);
			fis.close();
            return props.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 从中读取数据库配置
	 * @param key
	 * @return
	 */
	public static String getJDBCPropertyValue(String key){
		try {
			StringBuffer path = new StringBuffer();
			path.append(PropertyUtils.class.getClassLoader().getResource("").toURI().toString().replace("classes/", "").replace("file:", ""));
			path.append("classes");
			Properties props = new Properties();
			InputStream fis = new FileInputStream(URLDecoder.decode(path.toString(),"utf-8")+"/conf/jdbc.properties");
			props.load(fis);
			fis.close();
			if(props.getProperty(key) == null){
				return getCoreJDBCPropertyValue(key);
			}else{
	            return props.getProperty(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 从中读取框架数据库配置
	 * @param key
	 * @return
	 */
	public static String getCoreJDBCPropertyValue(String key){
		try {
			Properties props = new Properties();
			InputStream fis = new PropertyUtils().getClass().getResourceAsStream("/conf/core.jdbc.properties");
			props.load(fis);
			fis.close();
            return props.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static boolean checkInput(String strText) {
		//String strRule = "[`~!@#$%^&*+=|{}':;',\\[\\]<>/?~！@#�?%…�??&*—�??+|{}【�?��?�；：�?��?��?��?�，？]";
		String strRule = "''‘�??";
		String tempRule = "";
		int intStrLen;
		int intI;
		boolean flag = false;
		intStrLen = strRule.length();
		for (intI = 1; intI <= intStrLen; intI++) {
			tempRule = getMidW(strRule, intI, 1);
			if (strText.indexOf(tempRule) >= 0) {
				flag = true;
				return flag;
			}
		}
		return flag;
	}	
	/**
	 * 用于�?验非法字�?
	 * 
	 * @param stringIn
	 * @param startIn
	 * @param lenIn
	 * @return
	 */
	public static String getMidW(String stringIn, int startIn, int lenIn) {
		StringBuffer midString;
		StringBuffer stringBufferTemp;

		try {
			if (stringIn == null) {
				return null;
			}

			stringBufferTemp = new StringBuffer(stringIn);
			if (startIn > stringBufferTemp.length()) {

				return "";

			}
			if (lenIn > (stringBufferTemp.length() - startIn + 1)) {
				return getMidW(stringBufferTemp.toString(), startIn);
			}
			midString = new StringBuffer(stringBufferTemp.substring(startIn - 1, startIn + lenIn - 1));
			return midString.toString();
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 用于�?验非法字�?
	 * 
	 * @param stringIn
	 * @param startIn
	 * @return midString
	 */
	public static String getMidW(String stringIn, int startIn) {
		StringBuffer midString;
		StringBuffer stringBufferTemp;

		try {
			if (stringIn == null) {
				return null;
			}
			if (startIn > stringIn.length()) {
				return "";
			}
			stringBufferTemp = new StringBuffer(stringIn);
			midString = new StringBuffer(stringBufferTemp.substring(startIn - 1));
			return midString.toString();
		} catch (Exception e) {
			return null;
		}
	}	
}
