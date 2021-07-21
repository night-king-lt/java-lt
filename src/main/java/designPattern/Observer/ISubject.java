package designPattern.Observer;

/**
 * @Author liu.teng
 * @Date 2021/7/20
 * @Version 1.0
 *
 *  （2）主题接口ISubject
 */
public interface ISubject {
    public void register(IObserver obs);       //注册观察者
    public void unregister(IObserver obs);     //撤销观察者
    public void notifyObservers();             //通知所有观察者
}
