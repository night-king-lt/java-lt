package signal;

/**
 * @Author liu.teng
 * @Date 2021/7/26
 * @Version 1.0
 */
public class ThreadB extends Thread{

    int count;
    MySignal mySignal;
    public ThreadB(MySignal mySignal){
        this.mySignal=mySignal;
    }

    @Override
    public void run() {
        for(int i=0;i<100;i++){
            count = count + 1;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mySignal.setHasDataToProcess(true);
    }
}
