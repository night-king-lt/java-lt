package signal;

/**
 * @Author liu.teng
 * @Date 2021/7/26
 * @Version 1.0
 *
 *   wait()、notify()、notifyAll()
 * Java提供了一种内联机制可以让线程在等待信号时进入非运行状态。
 * 当一个线程调用任何对象上的wait()方法时便会进入非运行状态，
 * 直到另一个线程调用同一个对象上的notify()或notifyAll()方法。
 *
 * 为了能够调用一个对象的wait()、notify()方法，调用线程必须先获得这个对象的锁。
 * 因为线程只有在同步块中才会占用对象的锁，所以线程必须在同步块中调用wait()、notify()方法。
 *
 */
public class ThreadC extends Thread{
    MonitorObject mySignal;
    ThreadD threadD;
    public ThreadC(MonitorObject mySignal, ThreadD threadD){
        this.mySignal=mySignal;
        this.threadD=threadD;
    }

    /**
     *  既然调用对象wait()方法的线程需要获得这个对象的锁，那么这会不会阻塞其它线程调用这个对象的notify()方法呢？
     *  答案是不会阻塞，当一个线程调用监控对象的wait()方法时，它便会释放掉这个监控对象锁，以便让其它线程能够调用这个对象的notify()方法或者wait()方法。
     */
    @Override
    public void run(){
        synchronized (mySignal){
            try {
                mySignal.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程B计算结果为:" + threadD.count);
        }
    }

    /**
     *  另外，当一个线程被唤醒时不会立刻退出wait()方法，只有当调用notify()的线程退出它的同步块为止。
     */
    public static void main(String[] args) {
        MonitorObject mySignal=new MonitorObject();
        ThreadD threadD=new ThreadD(mySignal);
        ThreadC threadC=new ThreadC(mySignal,threadD);
        threadC.start();
        threadD.start();
    }
}
