package nsw.base.core.utils;
  
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
  
/** 
 * Excel组件 
 *  
 * @author wangdasong 
 * @version 1.0 
 * @since 1.0 
 */  
public abstract class ExcelHelper {  
  
    /** 
     * Excel 2003 
     */  
    private final static String XLS = "xls";  
    /** 
     * Excel 2007 
     */  
    private final static String XLSX = "xlsx";  
    /** 
     * 分隔�? 
     */  
    private final static String SEPARATOR = "|";  
  
    /** 
     * 由Excel文件的Sheet导出至List 
     *  
     * @param file 
     * @param sheetNum 
     * @return 
     */  
    public static List<List<Cell>> exportListFromExcel(File file, int sheetNum)  
            throws IOException {  
        return exportListFromExcel(new FileInputStream(file),  
                FilenameUtils.getExtension(file.getName()), sheetNum);  
    }  
  
    /** 
     * 由Excel流的Sheet导出至List 
     *  
     * @param is 
     * @param extensionName 
     * @param sheetNum 
     * @return 
     * @throws IOException 
     */  
    public static List<List<Cell>> exportListFromExcel(InputStream is,  
            String extensionName, int sheetNum) throws IOException {  
  
        Workbook workbook = null;  
  
        if (extensionName.toLowerCase().equals(XLS)) {  
            workbook = new HSSFWorkbook(is);  
        } else if (extensionName.toLowerCase().equals(XLSX)) {  
            workbook = new XSSFWorkbook(is);  
        }  
  
        return exportListFromExcel(workbook, sheetNum);  
    }  
  
    public static String getStringFromCell(Cell cell){
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper()  
                .createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        if(cellValue == null){
        	return null;
        }
//        if(cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC){
//        	return String.valueOf(cellValue.getNumberValue());
//        }
//        if(cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN){
//        	return cellValue.getBooleanValue() + "";
//        }
        return cellValue.getStringValue();    	
    }

    public static Date getDateFromCell(Cell cell){
        return cell.getDateCellValue();
    }

    public static Double getDoubleFromCell(Cell cell) throws Exception{
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper()  
                .createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        try{
        	if(cellValue == null){
        		return null;
        	}
        	String strReturn = cellValue.getStringValue();
        	if(strReturn != null){
        		return Double.parseDouble(strReturn);
        	}
            return (double)cellValue.getNumberValue();
        }catch(Exception e){
        	e.printStackTrace();
        	throw e;
        }
    }

    public static Integer getIntegerFromCell(Cell cell) throws Exception{
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper()  
                .createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        try{
        	if(cellValue == null){
        		return null;
        	}
        	String strReturn = cellValue.getStringValue();
        	if(strReturn != null){
        		return Integer.parseInt(strReturn);
        	}
            return (int)cellValue.getNumberValue();
        }catch(Exception e){
        	e.printStackTrace();
        	throw e;
        }
    }

    public static Boolean getBooleanFromCell(Cell cell) throws Exception{
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper()  
                .createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        try{
        	if(cellValue == null){
        		return null;
        	}
        	String strReturn = cellValue.getStringValue();
        	if(strReturn != null){
        		if("true".equals(strReturn)){
            		return true;
            	}else if("false".equals(strReturn)){
            		return false;
            	}else{
            		throw new Exception();
            	}
        	}
            return cellValue.getBooleanValue();
        }catch(Exception e){
        	e.printStackTrace();
        	throw e;
        }
    }
    
    
    /** 
     * 由指定的Sheet导出至List 
     *  
     * @param workbook 
     * @param sheetNum 
     * @return 
     * @throws IOException 
     */  
    private static List<List<Cell>> exportListFromExcel(Workbook workbook,  
            int sheetNum) {  
  
        Sheet sheet = workbook.getSheetAt(sheetNum);  
  
        // 解析公式结果  
  
        List<List<Cell>> list = new ArrayList<List<Cell>>();  
  
        int minRowIx = sheet.getFirstRowNum();  
        int maxRowIx = sheet.getLastRowNum();  
        for (int rowIx = minRowIx; rowIx <= maxRowIx; rowIx++) {  
            Row row = sheet.getRow(rowIx);  
            StringBuilder sb = new StringBuilder();
            List<Cell> dataList = new ArrayList<Cell>();
  
            short minColIx = row.getFirstCellNum();  
            short maxColIx = row.getLastCellNum();  
            for (short colIx = minColIx; colIx <= maxColIx; colIx++) {  
                Cell cell = row.getCell(new Integer(colIx));  
                dataList.add(cell);
            }  
            list.add(dataList);  
        }  
        return list;  
    }  

	// 导出Excel
	public static HSSFWorkbook exportExcel(String sheetName, List<List<Object>> list){
		
		
		//创建HSSFWorkbook对象(excel的文档对�?)  
	    HSSFWorkbook wkb = new HSSFWorkbook();  
		//建立新的sheet对象（excel的表单）  
	    if(sheetName == null || "".equals(sheetName)){
	    	sheetName = "SheetData";
	    }
		HSSFSheet sheet=wkb.createSheet(sheetName);  
		//在sheet里创建第�?行，参数为行索引(excel的行)，可以是0�?65535之间的任何一�?  
		HSSFRow row1=sheet.createRow(0);  
		//创建单元格（excel的单元格，参数为列索引，可以�?0�?255之间的任何一�?  
		HSSFCell cell=row1.createCell(0);  
		      //设置单元格内�?  
		cell.setCellValue("�?" + sheetName + "】数据一�?");  
		//合并单元格CellRangeAddress构�?�参数依次表示起始行，截至行，起始列�? 截至�?  
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,list.get(0).size()));
		//创建单元格并设置单元格内�?  
		Iterator<List<Object>> iterator = list.iterator();
		int index = 0;
		HSSFCellStyle cellStyle = wkb.createCellStyle();
        HSSFDataFormat format= wkb.createDataFormat();
		while (iterator.hasNext()){
			List<Object> currRowList = iterator.next();
			HSSFRow currRow=sheet.createRow(index + 1);
			int colIndex = 0;
			for(Object currCellObject : currRowList){
				if(currCellObject instanceof String){
					currRow.createCell(colIndex).setCellValue((String)currCellObject);
				}
				if(currCellObject instanceof Date){
					HSSFCell currCell = currRow.createCell(colIndex);
		            currCell.setCellValue((Date)currCellObject);
		            cellStyle.setDataFormat(format.getFormat("yyyy年m月d�?"));
		            currCell.setCellStyle(cellStyle);
				}
				if(currCellObject instanceof Integer){
					currRow.createCell(colIndex).setCellValue((Integer)currCellObject);
				}
				if(currCellObject instanceof Double){
					currRow.createCell(colIndex).setCellValue((Double)currCellObject);
				}
				if(currCellObject instanceof Float){
					currRow.createCell(colIndex).setCellValue((Float)currCellObject);
				}
				colIndex ++;
			}
			index ++;
		}
		
		return wkb;
	}
}