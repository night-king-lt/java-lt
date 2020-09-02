package fastJson;

import com.alibaba.fastjson.JSONObject;
import model.ActionData;

public class ObjectToJSON {
    public static void main(String[] args) {
        ActionData t = new ActionData();
        t.setType(1);
        //Java对象转化为JSON对象
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(t);
        jsonObject.put("name", "video");
        System.out.println("Java对象转化为JSON对象\n" + jsonObject);
    }
}
