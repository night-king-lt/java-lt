package effect.thread;

/**
 * @Author liu.teng
 * @Date 2020/8/7 17:26
 * @Version 1.0
 */
public interface SetObserver<E> {
    void added(ObservableSet<E> set, E element);
}
