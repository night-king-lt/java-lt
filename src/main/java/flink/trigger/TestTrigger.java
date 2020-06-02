package flink.trigger;

import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.util.HashMap;
import java.util.Map;

public class TestTrigger extends Trigger<Tuple2<String, Integer>, TimeWindow> {

    private Map<String, Integer> flagMap = new HashMap<>();
//    private int flag = 0;

    @Override
    public TriggerResult onElement(Tuple2<String, Integer> t, long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        System.out.println("timeWindow " + timeWindow + "  onElement : "+ t);
        if (flagMap.containsKey(t.f0)){
            flagMap.put(t.f0, flagMap.get(t.f0) + 1);
        }else{
            flagMap.put(t.f0, 1);
        }

        if(flagMap.get(t.f0) > 1){
            flagMap.put(t.f0, 0);
            return TriggerResult.FIRE_AND_PURGE;
        }
        long current = System.currentTimeMillis();
        long end = timeWindow.getEnd();
        long dif = end - RandomUtils.nextInt(0, 240000);
        if (current > dif){
            return TriggerResult.FIRE_AND_PURGE;
        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {

        return TriggerResult.FIRE;
    }

    @Override
    public TriggerResult onEventTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {

    }

}
