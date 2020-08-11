package effect.inheritance;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 */
public class InstrumentedHashSet<E> extends HashSet<E> {
    private int addCount = 0;

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount(){
        return addCount;
    }

    /**
     *  InstrumentedHashSet 的 addAll 方法首先会给 addCount 增加 3，
     *   然后利用 supper.addAll 来调用 HashSet 的 addAll实现，
     *   然后又依次调用了被 InstrumentedHashSet 覆盖了的 add方法，所以总共增加了 6
     * @param args
     */
    public static void main(String[] args) {
        InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
        s.addAll(Arrays.asList("snap", "pop", "sample"));
        System.out.println(s.getAddCount()); // 6
    }
}
