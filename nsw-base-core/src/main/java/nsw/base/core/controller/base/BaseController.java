package nsw.base.core.controller.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import nsw.base.core.utils.PropertyUtils;

public class BaseController {

	protected void responseObject(HttpServletResponse response, HSSFWorkbook wkb){
		OutputStream output;
		try {
			output = response.getOutputStream();
			response.reset();
            response.setCharacterEncoding("utf-8");
			response.setHeader("Content-disposition", "attachment; filename=details.xls");
			response.setContentType("application/msexcel");
			wkb.write(output);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected String saveUploadFile(MultipartFile file){
		String uploadFilePath = null;
		String filePath = PropertyUtils.getPropertyValue("file_path");
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String strToday = sdf.format(today);
		uploadFilePath = filePath + strToday;
		UUID uuid = UUID.randomUUID();
		String strUUID = uuid.toString();
		for(String currStr : strUUID.split("-")){
			uploadFilePath = uploadFilePath + "/" + currStr;
		}
		try {
			saveFile(file.getInputStream(), uploadFilePath, file.getOriginalFilename());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String savedFile = uploadFilePath + "/" + file.getOriginalFilename();
		return savedFile;
	}
	
	private void saveFile(InputStream inputStream, String path, String fileName) {

        OutputStream os = null;
        try {
            // 2、保存到临时文件
            // 1K的数据缓�?
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文�?

            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            // �?始读�?
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链�?
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	
}
