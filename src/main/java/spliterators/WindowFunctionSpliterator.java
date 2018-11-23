package spliterators;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Spliterator;
import java.util.function.Consumer;

public class WindowFunctionSpliterator<T> implements Spliterator<List<T>> {

    private final Spliterator<T> source;
    private final int windowSize;
    private final Queue<T> buffer;
    private boolean sourceNotEmpty;

    private WindowFunctionSpliterator(Spliterator<T> source, int windowSize) {
        this.source = source;
        this.sourceNotEmpty = true;
        this.windowSize = windowSize;
        this.buffer = new ArrayDeque<>(windowSize);
    }

    public static <T> WindowFunctionSpliterator<T> from(Spliterator<T> source, int windowSize) {
        return new WindowFunctionSpliterator<>(source, windowSize);
    }

    @Override
    public boolean tryAdvance(Consumer<? super List<T>> action) {
        while (sourceNotEmpty && buffer.size() < windowSize) {
            sourceNotEmpty = source.tryAdvance(buffer::offer);
        }
        if (buffer.isEmpty()) {
            return false;
        }
        if (buffer.size() == 3) {
            action.accept(new ArrayList<>(buffer));
        }
        buffer.poll();
        return true;
    }

    @Override
    public Spliterator<List<T>> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return source.estimateSize();
    }

    @Override
    public int characteristics() {
        return source.characteristics();
    }
}
