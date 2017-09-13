package nsw.base.core.utils.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nsw.base.core.dao.entity.ProviderConfig;
import nsw.base.core.utils.PropertyUtils;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDb {
    private static JedisPool jedisPool;
    // session 在redis过期时间是30分钟30*60
    private static int expireTime = 1800;
    // 计数器的过期时间默认2天
    private static int countExpireTime = 2*24*3600; 
    private static String password = null;
    private static String redisIp = PropertyUtils.getPropertyValue("core.redisIp");
    private static int redisPort = Integer.parseInt(PropertyUtils.getPropertyValue("core.redisPort"));
    private static int timeOut = Integer.parseInt(PropertyUtils.getPropertyValue("core.timeOut"));
    private static int maxActive = Integer.parseInt(PropertyUtils.getPropertyValue("core.maxActive"));
    private static int maxIdle = Integer.parseInt(PropertyUtils.getPropertyValue("core.maxIdle"));
    private static long maxWait = Integer.parseInt(PropertyUtils.getPropertyValue("core.maxWait"));
    private static Logger logger = Logger.getLogger(RedisDb.class);

    static {
        initPool();        
    }
    // 初始化连接池
    public static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        config.setTestOnBorrow(false);
        jedisPool = new JedisPool(config, redisIp, redisPort, timeOut, password, 2);
    }
    // 从连接池获取redis连接
    public static Jedis getJedis(){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
//            jedis.auth(password);
        } catch(Exception e){
            e.printStackTrace();
        }
        
        return jedis;
    }
    // 回收redis连接
    public static void recycleJedis(Jedis jedis){
        if(jedis != null){
            try{
                jedis.close();
            } catch(Exception e){
                e.printStackTrace();
            }
        }        
    }
    // 保存字符串数据
    public static void setString(String key, String value){
        Jedis jedis = getJedis();
        if(jedis != null){
            try{
                jedis.set(key, value);
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                recycleJedis(jedis);
            }
        }
        
    } 
    // 获取字符串类型的数据
    public static String getString(String key){
        Jedis jedis = getJedis();
        String result = "";
        if(jedis != null){
            try{
                result = jedis.get(key);
            }catch(Exception e){
                e.printStackTrace();
            } finally{
                recycleJedis(jedis);
            }
        }
        
        return result;
    }
    // 删除字符串数据
    public static void delString(String key){
        Jedis jedis = getJedis();
        if(jedis != null){
            try{
                jedis.del(key);
            }catch(Exception e){
                e.printStackTrace();
            } finally{
                recycleJedis(jedis);
            }
        }
    }
    // 保存byte类型数据
    public static void setObject(byte[] key, byte[] value, Integer expireTime){
        Jedis jedis = getJedis();
        String result = "";
        if(jedis != null){
            try{
            	jedis.set(key, value);
                // redis中session过期时间
            	if(expireTime == null){
            		expireTime = RedisDb.expireTime;
            	}
                jedis.expire(key, expireTime);
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                recycleJedis(jedis);
            }
        }    
    } 
    // 保存byte类型数据
    public static void setObject(byte[] key, byte[] value){
        Jedis jedis = getJedis();
        String result = "";
        if(jedis != null){
            try{
            	jedis.set(key, value);
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                recycleJedis(jedis);
            }
        }    
    }
    // 获取byte类型数据
    public static byte[] getObject(byte[] key){
        Jedis jedis = getJedis();
        byte[] bytes = null;
        if(jedis != null){
            try{
                bytes = jedis.get(key);;
            }catch(Exception e){
            	e.printStackTrace();
            } finally{
                recycleJedis(jedis);
            }
        }    
        return bytes;
        
    }
    
    // 更新byte类型的数据，主要更新过期时间
    public static void updateObject(byte[] key){
        Jedis jedis = getJedis();
        if(jedis != null){
            try{
                // redis中session过期时间
                jedis.expire(key, expireTime);
            }catch(Exception e){
                e.printStackTrace();
            } finally{
                recycleJedis(jedis);
            }
        }    
        
    }
    
    // 更新byte类型的数据，主要更新过期时间
    public static void setObjectForever(byte[] key){
        Jedis jedis = getJedis();
        if(jedis != null){
            try{
                // redis中session过期时间
                jedis.expire(key, expireTime);
            }catch(Exception e){
                e.printStackTrace();
            } finally{
                recycleJedis(jedis);
            }
        }    
        
    }
    
    // key对应的整数value加1
    public static void inc(String key){
        Jedis jedis = getJedis();
        if(jedis != null){
            try{
                if(!jedis.exists(key)){
                    jedis.set(key, "1");
                    jedis.expire(key, countExpireTime);
                } else {
                    // 加1
                    jedis.incr(key);
                }
            }catch(Exception e){
                e.printStackTrace();
            } finally{
                recycleJedis(jedis);
            }
        }    
    }
    
    // 获取所有keys
    public static Set<String> getAllKeys(String pattern){
        Jedis jedis = getJedis();
        if(jedis != null){
            try{
                return jedis.keys(pattern);
            }catch(Exception e){
                e.printStackTrace();
            } finally{
                recycleJedis(jedis);
            }
        }
        return null;
    }

    // 把byte还原为session
    public static HashMap<String, List<ProviderConfig>> byteToProviderConfigMap(byte[] bytes){
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream in;
        HashMap<String, List<ProviderConfig>> providerConfigMap = null;
        try {
            in = new ObjectInputStream(bi);
            providerConfigMap = (HashMap<String, List<ProviderConfig>>) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return providerConfigMap;
    }
    public static byte[] providerConfigMapToByte(HashMap<String, List<ProviderConfig>> providerConfigMap){
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(providerConfigMap);
            bytes = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}