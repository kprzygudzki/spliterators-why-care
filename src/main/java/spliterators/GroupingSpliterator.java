package spliterators;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GroupingSpliterator implements Spliterator<String> {

    private final Spliterator<String> source;
    private final int groupSize;
    private final ArrayList<String> buffer;
    private boolean sourceNotEmpty;

    private GroupingSpliterator(Spliterator<String> source, int groupSize) {
        this.source = source;
        this.sourceNotEmpty = true;
        this.groupSize = groupSize;
        this.buffer = new ArrayList<>(groupSize);
    }

    public static GroupingSpliterator from(Spliterator<String> source, int groupSize) {
        return new GroupingSpliterator(source, groupSize);
    }

    @Override
    public boolean tryAdvance(Consumer<? super String> action) {
        while (sourceNotEmpty && buffer.size() < groupSize) {
            sourceNotEmpty = source.tryAdvance(buffer::add);
        }
        if (buffer.isEmpty()) {
            return false;
        }
        action.accept(String.join("", buffer));
        buffer.clear();
        return true;
    }

    @Override
    public Spliterator<String> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return source.estimateSize() / groupSize;
    }

    @Override
    public int characteristics() {
        return source.characteristics();
    }
}
