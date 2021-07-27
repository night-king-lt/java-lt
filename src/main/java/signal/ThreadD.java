package signal;

/**
 * @Author liu.teng
 * @Date 2021/7/26
 * @Version 1.0
 *
 *   在这个例子中，线程C因调用了监控对象的wait()方法而挂起，线程D通过调用监控对象的notify()方法唤醒挂起的线程C。
 *   我们还可以看到，两个线程都是在同步块中调用的wait()和notify()方法。
 *   如果一个线程在没有获得对象锁的前提下调用了这个对象的wait()或notify()方法，方法调用时将会抛出 IllegalMonitorStateException异常。
 *
 *   注意，当一个线程调用一个对象的notify()方法，则会唤醒正在等待这个对象所有线程中的一个线程（唤醒的线程是随机的），
 *    当线程调用的是对象的notifyAll()方法，则会唤醒所有等待这个对象的线程（唤醒的所有线程中哪一个会执行也是不确定的）。
 */
public class ThreadD extends Thread{
    int count;
    MonitorObject mySignal;
    public ThreadD(MonitorObject mySignal){
        this.mySignal = mySignal;
    }
    @Override
    public void run(){
        for(int i=0;i<100;i++){
            count=count+1;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (mySignal){
            mySignal.notify(); //计算完成调用对象的notify()方法，唤醒因调用这个对象wait()方法而挂起的线程
        }
    }
}
