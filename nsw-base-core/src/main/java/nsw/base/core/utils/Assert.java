package nsw.base.core.utils;


import java.util.Collection;
import java.util.Map;

import nsw.base.core.utils.exception.BusinessException;

public class Assert{
  public static void notNull(Object obj){
    try{
      org.springframework.util.Assert.notNull(obj, "系统出错，请联系管理员！");
    }
    catch (IllegalArgumentException e){
      throw new BusinessException("999", "系统出错，请联系管理员！", e);
    }
  }

  public static void notNull(Object obj, String code, String msg){
    try {
      org.springframework.util.Assert.notNull(obj, msg);
    }catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }

  public static void notNull(String str) {
    notNull(str, "999", "系统出错，请联系管理员！");
  }

  public static void notNull(String str, String code, String msg){
    try {
      org.springframework.util.Assert.notNull(str, msg);
      if ("".equals(str)) throw new IllegalArgumentException(msg);
    }catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }

  public static void notNull(Integer value){
    notNull(value, "999", "系统出错，请联系管理员！");
  }

  public static void notNull(Integer value, String code, String msg){
    try {
      org.springframework.util.Assert.notNull(value, msg);
      if (value.intValue() <= 0) throw new IllegalArgumentException(msg);
    }catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }

  public static void notNull(Double value){
    notNull(value, "999", "系统出错，请联系管理员！");
  }

  public static void notNull(Double value, String code, String msg){
    try {
      org.springframework.util.Assert.notNull(value, msg);
      if (value.doubleValue() <= 0.0D) throw new IllegalArgumentException(msg);
    }catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }

  public static void notNull(Float value){
    notNull(value, "999", "系统出错，请联系管理员！");
  }

  public static void notNull(Float value, String code, String msg){
    try {
      org.springframework.util.Assert.notNull(value, msg);
      if (value.floatValue() <= 0.0F) throw new IllegalArgumentException(msg);
    }catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }

  public static void notNull(int value){
    notNull(value, "999", "系统出错，请联系管理员！");
  }

  public static void notNull(int value, String code, String msg){
    try {
      if (value <= 0) throw new IllegalArgumentException(msg); 
    }catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }

  public static void notNull(double value){
    notNull(value, "999", "系统出错，请联系管理员！");
  }

  public static void notNull(double value, String code, String msg){
    try {
      if (value <= 0.0D) throw new IllegalArgumentException(msg);
    }catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }

  public static void notNull(float value){
    notNull(value, "999", "系统出错，请联系管理员！");
  }

  public static void notNull(float value, String code, String msg){
    try {
      if (value <= 0.0F) throw new IllegalArgumentException(msg); 
    }catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }

  public static void notEmpty(Collection collection){
    notEmpty(collection, "999", "系统出错，请联系管理员！");
  }

  public static void notEmpty(Collection collection, String code, String msg){
    try{
      org.springframework.util.Assert.notEmpty(collection, msg);
    }
    catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }

  public static void notEmpty(Object[] objArray){
    notEmpty(objArray, "999", "系统出错，请联系管理员！");
  }

  public static void notEmpty(Object[] objArray, String code, String msg){
    try {
      org.springframework.util.Assert.notEmpty(objArray, msg);
    }
    catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }

  public static void notEmpty(Map map){
    notEmpty(map, "999", "系统出错，请联系管理员！");
  }

  public static void notEmpty(Map map, String code, String msg){
    try{
      org.springframework.util.Assert.notEmpty(map, msg);
    }catch (IllegalArgumentException e){
      throw new BusinessException(code, msg, e);
    }
  }
}