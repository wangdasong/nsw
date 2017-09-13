package nsw.base.core.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

public class RequestToBean {
	  
    /** 
     * ConvertUtilsBean convertUtils = new ConvertUtilsBean();    
     * DateConverter dateConverter = new DateConverter();    
     * convertUtils.register(dateConverter,Date.class);     
     * */  
      
      
    /** 
     * @param <T> 
     * @param newSource  现将要设置新值的对象 
     * @param source     源数据对�? 
     */  
    public static <T> void  beanConvert(T newSource,T source){  
        try {  
            BeanUtils.copyProperties(newSource,source);  
        } catch (IllegalAccessException e) {              
            e.printStackTrace();  
        } catch (InvocationTargetException e) {           
            e.printStackTrace();  
        }   
    }  
      
    /** 
     * @param request 请求对象 
     * @param obj     要设置Bean的类�?,传入试为: Bean.class 
     * @return 
     */  
    @SuppressWarnings("unchecked")    
    public static <T> T getBeanToRequest(HttpServletRequest request,Class<T> clazz){    
          
        //获取页面�?有的请求参数名称  
        Enumeration<String> enume = request.getParameterNames();     
        T beanObj = getObjectByClass(clazz);  
        try{     
            while(enume.hasMoreElements()){     
                //参数名称  
                String propertyName = enume.nextElement();                  
                //判断是否存在此属�?  
                if(isCheckBeanExitsPropertyName(clazz,propertyName)){  
                    //获取请求�?  
                    Object propertyValue = request.getParameter(propertyName);  
                    setProperties(beanObj,propertyName,propertyValue);  
                }  
                  
            }     
        }catch(Exception e){}    
             
        return beanObj;     
    }    
      
    private static <T> T getObjectByClass(Class<T> clazz){  
        T t = null;  
        try {  
            t = clazz.newInstance();  
        } catch (InstantiationException e1) {             
            e1.printStackTrace();  
        } catch (IllegalAccessException e1) {             
            e1.printStackTrace();  
        }  
        return t;  
    }  
      
      
      
    /** 
     * @param clazz           Class对象 
     * @param propertyName    属�?�名�? 
     * @return true || false  �?查对象中是否存在该属性名�? 
     */  
    private  static boolean isCheckBeanExitsPropertyName(Class<?> clazz,String propertyName){   
        boolean retValue = false;  
        try {  
            Field field =  clazz.getDeclaredField(propertyName);              
            if(null != field){  
                retValue = true;  
            }
        } catch (NoSuchFieldException e) {
        	if (isCheckBeanExitsPropertyName(clazz.getSuperclass(), propertyName)){
                retValue = true;  
            }
        }  
        return retValue;  
          
    }  
      
     /**   
     * 设置字段�?       
     * @param obj          实例对象   
     * @param propertyName 属�?�名   
     * @param value        新的字段�?   
     * @return             
     */     
    public static void setProperties(Object object, String propertyName,Object value) throws IntrospectionException,     
            IllegalAccessException, InvocationTargetException {     
        PropertyDescriptor pd = new PropertyDescriptor(propertyName,object.getClass());
        if(pd.getPropertyType().getName().equals("java.util.Date")){
        	DateTypeEditor dateTypeEditor = new DateTypeEditor();
        	dateTypeEditor.setAsText((String) value);
        	value = dateTypeEditor.getValue();
        }
        if(pd.getPropertyType().getName().equals("java.lang.Integer")){
        	value = Integer.parseInt((String) value);
        }
        if(pd.getPropertyType().getName().equals("java.lang.Double")){
        	value = Double.parseDouble((String) value);
        }
        Method methodSet = pd.getWriteMethod();
        methodSet.invoke(object,value);     
    }  
}
