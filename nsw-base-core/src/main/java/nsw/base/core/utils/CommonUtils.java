package nsw.base.core.utils;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 
 * 项目名称：finance<br>
 * *********************************<br>
 * <P>类名称：CommonUtils</P>
 * *********************************<br>
 * <P>类描述：主要作用是转换毫秒为标准日期类型，因为alibaba的fastjson有bug，在传输日期类时会将Date类型的标志位去掉，只取其中的fasttime�?
 * 另一种方法是通过注解�?
 * JSONField (format="yyyy-MM-dd HH:mm:ss")
 * public Date birthday;
 * 
 * 其他乱码问题后续再在此类中追加�??
 * </P>
 * 创建人：wenjie.zhu<br>
 * 创建时间�?2016�?12�?21�? 下午4:14:30<br>
 * 修改人：wenjie.zhu<br>
 * 修改时间�?2016�?12�?21�? 下午4:14:30<br>
 * 修改备注�?<br>
 * @version 1.0<br>
 */
public class CommonUtils {

	private static final String dateFormat = "yyyy-MM-dd";

	/**
	 * 把毫秒转化成日期
	 * 
	 * @param millSec
	 *            (日期格式，例如：MM/ dd/yyyy HH:mm:ss)
	 * @return millSec
	 */
	public static String transferLongToDate(String millSec) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		if (millSec != null && !millSec.equals("")) {
			Date date = new Date(Long.valueOf(millSec));
			return sdf.format(date);
		} else {
			return millSec;
		}

	}



	public static JSONObject getReturnJson(boolean success, String msg, String detail, Object data) {
		return net.sf.json.JSONObject.fromObject(getReturnJsonStr(success, msg, detail, data));
	}

	public static String getReturnJsonStr(boolean success, String msg, String detail, Object data) {
		String dataStr = "\"\"";
		if (data instanceof String) {
			dataStr = "\"" + data + "\"";
		} else if (data instanceof List) {
			dataStr = JSONArray.fromObject(data).toString();
		} else {
			dataStr = JSONObject.fromObject(data).toString();
		}
		String separator = System.getProperty("line.separator");
		msg = msg.replace(separator, "").replace("\"", "");
		String retStr = null;
		if (!success) {
			retStr = "{success: false, code: \"500\", msg: \"" + msg + "\", detail: \"" + detail + "\", data:" + dataStr + "}";
		} else {
			retStr = "{success: true, code: \"200\", msg: \"" + msg + "\", detail: \"" + detail + "\", data:" + dataStr + "}";
		}
		return retStr;
	}
	/**
     * 对象转byte[]
     * @param obj
     * @return byte[]
	 * @throws IOException
     */
    public static byte[] object2Bytes(Object obj) throws IOException{
        ByteArrayOutputStream bo=new ByteArrayOutputStream();
        ObjectOutputStream oo=new ObjectOutputStream(bo);
        oo.writeObject(obj);
        byte[] bytes=bo.toByteArray();
        bo.close();
        oo.close();
        return bytes;
    }
    /**
     * byte[]转对象
     * @param bytes
     * @return
     * @throws Exception
     */
    public static Object bytes2Object(byte[] bytes) throws Exception{
        ByteArrayInputStream in=new ByteArrayInputStream(bytes);
        ObjectInputStream sIn=new ObjectInputStream(in);
        return sIn.readObject();
    }
}
