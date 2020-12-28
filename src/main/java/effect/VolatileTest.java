package effect;

/**
 * @Author liu.teng
 * @Date 2020/12/28 15:52
 * @Version 1.0
 */
public class VolatileTest extends Thread{

//    volatile boolean flag = false; // v线程会退出
    boolean flag = false;  // v线程永远不会退出
    int i = 0;

    @Override
    public void run() {
        while (!flag){
            i++;
        }
    }

    /**
     *   首先 v 线程在运行的时候会把 变量 flag 与 i 从“主内存”  拷贝到 v线程栈内存
     *
     *   主线程将v.flag的值同样 从主内存中拷贝到自己的线程工作内存 然后修改flag=true. 然后再将新值回到主内存。
     *
     *   这就解释了为什么在主线程（main）中设置了vt.flag = true; 而vt线程在进行判断flag的时候拿到的仍然是false。
     *   那就是因为vt线程每次判断flag标记的时候是从它自己的“工作内存中”取值，而并非从主内存中取值！
     *
     *   这也是JVM为了提供性能而做的优化。那我们如何能让vt线程每次判断flag的时候都强制它去主内存中取值呢。这就是volatile关键字的作用。
     *
     *   在flag前面加上volatile关键字，强制线程每次读取该值的时候都去“主内存”中取值，v线程就可以正常退出了
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        VolatileTest v = new VolatileTest();
        v.start();
        Thread.sleep(2000);
        v.flag = true;
        System.out.println("stope" + v.i);
    }
}
