package nsw.base.core.utils;


import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * 
 * 项目名称：finance<br>
 * *********************************<br>
 * <P>类名称：DecryptDruidSource2</P>
 * *********************************<br>
 * <P>类描述：druid数据源在1.0.16以后的版本中在连接数据库时解密用户名并赋值的类（解密方式稍有不同�?</P>
 * 创建人：wenjie.zhu<br>
 * 创建时间�?2016�?12�?21�? 下午4:11:40<br>
 * 修改人：wenjie.zhu<br>
 * 修改时间�?2016�?12�?21�? 下午4:11:40<br>
 * 修改备注�?<br>
 * @version 1.0<br>
 */
@SuppressWarnings("all")
public class DecryptDruidSource extends DruidDataSource{
	
//	
//	@Override
//	public void setUsername(String username) {		
//		try {
//			String publicKey = PropertyUtils.getJDBCPropertyValue("publickey");
//			String jdbc_username = PropertyUtils.getJDBCPropertyValue("jdbc_username");
//			String jdbc_password = PropertyUtils.getJDBCPropertyValue("jdbc_password");
//			username = ConfigTools.decrypt(publicKey, jdbc_username);
//			super.setUsername(username);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}

