package designPattern.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liu.teng
 * @Date 2021/7/20
 * @Version 1.0
 *
 * （3）主题实现类Subject
 *   主题实现类Subject是观察者设计模式中最重要的一个类，包含了观察者对象的维护向量vec以及主题中心数据data变量与具体观察者对象的关联
 *     方法（通过nitofyObservers()）。也就是说，从此类出发，可以更深刻地理解ISubject为什么定义了3个方法、IObserver接口为什么定义了1个方法。
 */
public class Subject implements ISubject{

    private List<IObserver> list = new ArrayList<>();  // 观察者维集合
    private String data;                           //主题中心数据

    public String getData() {
        return data;
    }
    public void setData(String data) {   //主题注册（添加）
        this.data = data;
    }

    @Override
    public void register(IObserver obs) {   //主题注册（添加）观察者
        list.add(obs);
    }

    @Override
    public void unregister(IObserver obs) {  //主题撤销（删除）观察者
        list.remove(obs);
    }

    @Override
    public void notifyObservers() {    //主题通知所有观察者进行数据响应
        for(int i = 0; i < list.size(); i++){
            IObserver obs = list.get(i);
            obs.refresh(data);
        }
    }
}
