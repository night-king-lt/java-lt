package effect.thread;

import effect.inheritance.ForwardingSet;
import org.apache.flink.runtime.executiongraph.Execution;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObservableSet<E> extends ForwardingSet<E> {

    public ObservableSet(Set<E> s) {
        super(s);
    }

    private final List<SetObserver<E>> observers = new ArrayList<>();

    public void addObserver(SetObserver<E> observer){
        synchronized(observers){
            observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserver<E> observer){
        synchronized(observers){
            return observers.remove(observer);
        }
    }

    private void notifyElementAdded(E element){
        synchronized(observers){
            for (SetObserver<E> observer: observers){
                observer.added(this, element);
            }
        }
    }

    @Override
    public boolean add(E e) {
        boolean added = super.add(e);
        if (added){
            notifyElementAdded(e);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element: c){
            result |= add(element);
        }
        return result;
    }

    public static void main(String[] args) {
        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());
//        set.addObserver((s, e) -> System.out.println(e));
//        set.addObserver(new SetObserver<Integer>() {
//            @Override
//            public void added(ObservableSet<Integer> set, Integer element) {
//                System.out.println(element + ": " + this);
//                if (element == 23){
//                    set.removeObserver(this);
//                }
//            }
//        });
        set.addObserver(new SetObserver<Integer>() {
            @Override
            public void added(ObservableSet<Integer> set, Integer element) {
                System.out.println(element + ": " + this);
                if (element == 23){
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try{
                        exec.submit(() -> set.removeObserver(this)).get();
                    }catch (ExecutionException | InterruptedException e){
                        throw new AssertionError(e);
                    }finally {
                        exec.shutdown();
                    }
                }
            }
        });
        for (int i = 0; i < 100; i++){
            set.add(i);
        }
    }
}
