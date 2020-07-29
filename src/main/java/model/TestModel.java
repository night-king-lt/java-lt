package model;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author liuteng
 * @Date 2020/7/24 3:08 下午
 * @Version 1.0
 */
public class TestModel {
    public long time;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static void main(String[] args) {
        TestModel t = new TestModel();
        System.out.println(t);
        System.out.println("time: " + t.time);
    }
}
