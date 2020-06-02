import dao.ActionData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        testLongString();
    }

    public static void testDouble(){
        List<Integer>  list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(1);

        double rate = list.stream().collect(Collectors.averagingInt(Integer::intValue));
        double c = Double.parseDouble(String.format("%.4f", rate));
        System.out.println(rate);
        System.out.println(c);
    }

    public static void testException(){
        Map<String, String> map = new HashMap<>();
        map.put("user", "liuteng ");
        System.out.println(map);
        ActionData a = new ActionData();
        a.setUserId("hahah");
        byte[] bytes = a.toByteArray(a);
        for (byte b : bytes) {
            System.out.print(b);
        }
        ActionData b = a.toObject(bytes);
        System.out.println(b);

    }

    public static void testLongString(){
        Map<String, Integer> map1 = new HashMap<>();
        String key = "864738036816361#1583982588340_ddf6152298e349908f25a1110efd009f#20389487#6172";
        for (int i=0; i< 10000; i ++){
            map1.put(RandomStringUtils.randomAlphanumeric(50), RandomUtils.nextInt(0, 5));
        }
        map1.put(key, 1);

        long start = System.currentTimeMillis();
        if (map1.containsKey(key)){
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        }
    }
}
