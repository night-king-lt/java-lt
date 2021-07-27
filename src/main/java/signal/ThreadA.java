package signal;

/**
 * @Author liu.teng
 * @Date 2021/7/26
 * @Version 1.0
 *
 *    很明显，采用共享对象方式通信的线程A和线程B必须持有同一个MySignal对象的引用，这样它们才能彼此检测到对方设置的信号。
 *    当然，信号也可存储在共享内存buffer中，它和实例是分开的。
 */
public class ThreadA extends Thread{

    MySignal mySignal;
    ThreadB threadB;
    public ThreadA(MySignal mySignal, ThreadB threadB){
        this.mySignal=mySignal;
        this.threadB=threadB;
    }

    /**
     *  线程A一直在等待数据就绪，或者说线程A一直在等待线程B设置hasDataToProcess的信号值为true
     *  为什么说是忙等呢？因为下面代码一直在执行循环，直到hasDataToProcess被设置为true。
     *
     * 忙等意味着线程还处于运行状态，一直在消耗CPU资源，所以，忙等不是一种很好的现象。
     * 那么能不能让线程在等待信号时释放CPU资源进入阻塞状态呢？
     * 其实java.lang.Object提供的wait()、notify()、notifyAll()方法就可以解决忙等问题。
     */
    @Override
    public void run() {
        while(true){
            if (mySignal.hasDataToProcess()){
                System.out.println("线程B计算结果为:"+threadB.count);
                break;
            }
        }
    }


    public static void main(String[] args) {
        MySignal mySignal = new MySignal();
        ThreadB b = new ThreadB(mySignal);
        ThreadA a = new ThreadA(mySignal, b);
        b.start();
        a.start();
    }
}
