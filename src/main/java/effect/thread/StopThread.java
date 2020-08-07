package effect.thread;

import java.util.concurrent.TimeUnit;

public class StopThread {
    private static boolean stopRequested;

    private static synchronized void setStopRequested(){
        stopRequested = true;
    }

    private static synchronized boolean isStopRequested(){
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while(!isStopRequested()){
                i++;
            }
        } );
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
//        stopRequested = true;
        setStopRequested();
    }
}
