package signal;

/**
 * @Author liu.teng
 * @Date 2021/7/26
 * @Version 1.0
 */
public class MySignal {
    private boolean hasDataToProcess;

    public synchronized void setHasDataToProcess(boolean hasData){
        this.hasDataToProcess=hasData;
    }
    public synchronized boolean hasDataToProcess(){
        return this.hasDataToProcess;
    }
}
