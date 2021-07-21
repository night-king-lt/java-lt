package designPattern.Observer;

/**
 * @Author liu.teng
 * @Date 2021/7/20
 * @Version 1.0
 *
 *   一个具体观察者类Observer
 */
public class Observer implements IObserver{
    @Override
    public void refresh(String data) {
        System.out.println("I have received the data:" +data);
    }

    public static void main(String[] args) {
        IObserver obs = new Observer();    //定义观察者对象
        Subject subject = new Subject();
        //定义主题对象
        subject.register(obs);             //主题添加观察者
        subject.setData("hello");          //主题中心数据发生变动
        subject.notifyObservers();         //通知所有观察者进行数据响应

    }
}
