package com.example.chat.pojo;

import java.util.List;
import java.util.Map;

public class JSONUtil {
    //转换Map类型为JSON对象类型
    public static String JSONStringify(Map<String,Object> body){
        StringBuilder jsonBody = new StringBuilder("{");
        for (Object entry : body.entrySet()){
            Map.Entry m = (Map.Entry) entry;
            if (m.getValue() instanceof List)
                jsonBody.append("\""+m.getKey()+"\":"+JSONStringify((List<Object>) m.getValue()));
            if (m.getValue() instanceof Map)
                jsonBody.append("\""+m.getKey()+"\":"+JSONStringify((Map<String, Object>) m.getValue()));
            if (m.getValue() instanceof Integer)
                jsonBody.append("\""+m.getKey()+"\":"+m.getValue());
            if (m.getValue() instanceof String)
                jsonBody.append("\""+m.getKey()+"\":"+"\""+m.getValue()+"\"");
            jsonBody.append(",");
        }
        int index = jsonBody.lastIndexOf(",");
        if (index != -1)jsonBody.delete(index,index+1);
        jsonBody.append("}");
        return jsonBody.toString();
    }
    //转换List类型为JSON数组类型
    public static String JSONStringify(List<Object> list){
        StringBuilder jsonBody = new StringBuilder("[");
        for (Object item : list){
            if (item instanceof Map)
                jsonBody.append(JSONStringify((Map<String, Object>) item));
            if (item instanceof Integer)
                jsonBody.append(item);
            if (item instanceof String)
                jsonBody.append("\""+item+"\"");
            if (item instanceof List)
                jsonBody.append(JSONStringify((List<Object>) item));
            jsonBody.append(",");
        }
        int index = jsonBody.lastIndexOf(",");
        if (index != -1)jsonBody.delete(index,index+1);
        jsonBody.append("]");
        return jsonBody.toString();
    }
}
