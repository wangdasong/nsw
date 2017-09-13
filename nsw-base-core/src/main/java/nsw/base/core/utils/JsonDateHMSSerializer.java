package nsw.base.core.utils;

import java.io.IOException; 
import java.text.SimpleDateFormat; 
import java.util.Date; 

import org.codehaus.jackson.JsonGenerator; 
import org.codehaus.jackson.JsonProcessingException; 
import org.codehaus.jackson.map.JsonSerializer; 
import org.codehaus.jackson.map.SerializerProvider; 
import org.springframework.stereotype.Component; 


/**
 * @author 王大�?  创建时间2016-12-16
 * 功能�?
 * @修改�?     日期      版本      描述
 */

@Component 
public class JsonDateHMSSerializer extends JsonSerializer<Date>{ 

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Override 
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
             throws IOException, JsonProcessingException { 
        String formattedDate = dateFormat.format(date); 
        gen.writeString(formattedDate); 
    } 
} 

