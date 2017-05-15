package nsw.base.core.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 项目名称：finance<br>
 * *********************************<br>
 * <P>类名称：fileUtils</P>
 * *********************************<br>
 * <P>类描述：将输入流生成文件的工具类�?</P>
 * 创建人：wenjie.zhu<br>
 * 创建时间�?2016-6-12 上午11:16:49<br>
 * 修改人：wenjie.zhu<br>
 * 修改时间�?<br>
 * 修改备注�?<br>
 * @version 1.0<br>
 */
public class fileUtils {

	private static Log logger = LogFactory.getLog(fileUtils.class);
	
	
	public static void inputStreamToFile(InputStream ins,File file) throws Exception {
		long start = System.currentTimeMillis();
		logger.info("�?**********inputStreamToFile�?�?**********】文件名�?" + file.getName() + "\t生成文件路径:" + file.getAbsolutePath());
		  try {
		   OutputStream os = new FileOutputStream(file);
		   int bytesRead = 0;
		   byte[] buffer = new byte[8192];
		   while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
		    os.write(buffer, 0, bytesRead);
		   }
		   os.close();
		   ins.close();
		  } catch (Exception e) {
			logger.error("�?*********生成文件异常**********�?" + e.getMessage());
		   throw e;
		  }
		  logger.info("�?**********inputStreamToFile结束**********】共花费:�?"+ (System.currentTimeMillis() - start) + "】毫秒~");
		 }
	
	
}
