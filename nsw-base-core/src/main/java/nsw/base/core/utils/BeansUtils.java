package nsw.base.core.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;

import nsw.base.core.utils.exception.BusinessException;


/**
 * 
 * 项目名称：finance<br>
 * *********************************<br>
 * <P>类名称：BeansUtils</P>
 * *********************************<br>
 * <P>类描述：POJO/Bean/Model 类克隆工具，适用于DTO-Model之间互相转换</P>
 * 创建人：wenjie.zhu<br>
 * 创建时间�?2016�?12�?21�? 下午5:01:19<br>
 * 修改人：wenjie.zhu<br>
 * 修改时间�?2016�?12�?21�? 下午5:01:19<br>
 * 修改备注�?<br>
 * @version 1.0<br>
 */
public class BeansUtils
{
  public static <T> T copyProperties(Class<T> targetClass, Object source)
  {
    Assert.notNull(targetClass, "copyProperties", "targetClass is not exist!~");
    Assert.notNull(source, "copyProperties", "source is not exist!~");

    Object target = null;
    try
    {
      target = Class.forName(targetClass.getName()).newInstance();
      BeanUtils.copyProperties(source, target);
    }
    catch (Exception e) {
      throw new BeanCreationException(e.getMessage(), e);
    }

    return (T) target;
  }

  public static <T> List<T> copyProperties(Class<T> targetClass, List sources)
  {
    Assert.notNull(targetClass, "copyProperties", "targetClass is not exist!~");
    Assert.notNull(sources, "copyProperties", "sources is not exist!~");

    List targetList = new ArrayList();

    for (Iterator localIterator = sources.iterator(); localIterator.hasNext(); ) { Object source = localIterator.next();

      Object target = null;
      try
      {
        target = Class.forName(targetClass.getName()).newInstance();
        BeanUtils.copyProperties(source, target);
      }
      catch (Exception e) {
        throw new BeanCreationException(e.getMessage(), e);
      }

      targetList.add(target);
    }

    return targetList;
  }

  public static void copyProperties(Object source, Object target)
  {
    BeanUtils.copyProperties(source, target);
  }

  public static boolean valid(Object obj)
  {
    return obj != null;
  }

  public static boolean valid(Collection collection)
  {
    try {
      Assert.notEmpty(collection);
      return true;
    } catch (BusinessException e) {
    }
    return false;
  }

  public static boolean valid(Map map)
  {
    try
    {
      Assert.notEmpty(map);
      return true;
    } catch (BusinessException e) {
    }
    return false;
  }

  public static boolean valid(Object[] objArray)
  {
    try
    {
      Assert.notEmpty(objArray);
      return true;
    } catch (BusinessException e) {
    }
    return false;
  }

  public static boolean valid(String str)
  {
    try
    {
      Assert.notNull(str);
      return true;
    } catch (BusinessException e) {
    }
    return false;
  }

  public static String getClassFFilePath(Class objClass)
  {
    String classpath = getClassPath(objClass);

    return classpath.replace("file:\\", "").replace("file:/", "");
  }

  public static String getClassPath(Class objClass)
  {
    Assert.notNull(objClass, "999", "系统出错，请联系管理员！");

    return objClass.getResource("/").toString();
  }
  
  public static Class getPropertyTypeFromClass(Class clzz, String propertyName){
	Class<?> reClazz = null;
	try {
		reClazz = clzz.getDeclaredField(propertyName).getType();
	} catch (NoSuchFieldException e) {
		if(clzz.getSuperclass() != null){
			reClazz = getPropertyTypeFromClass(clzz.getSuperclass(),propertyName);
		}else {
			e.printStackTrace();
		}
	} catch (SecurityException e) {
		e.printStackTrace();
	}
	return  reClazz;
  }
  //merge two bean by discovering differences
  public static <M> void merge(M target, M destination) throws Exception {
      BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
      // Iterate over all the attributes
      for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
          // Only copy writable attributes
          if (descriptor.getWriteMethod() != null) {
              Object originalValue = descriptor.getReadMethod()
                      .invoke(target);
              // Only copy values values where the destination values is null
              if (originalValue == null) {
                  Object defaultValue = descriptor.getReadMethod().invoke(
                          destination);
                  descriptor.getWriteMethod().invoke(target, defaultValue);
              }

          }
      }
  }
}