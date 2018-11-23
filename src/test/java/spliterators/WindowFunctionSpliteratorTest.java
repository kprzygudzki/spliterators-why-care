package spliterators;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class WindowFunctionSpliteratorTest {

    @Test
    void shouldReturnSlidingWindowGroups() {
        Stream<String> source = Stream.of("a", "b", "c", "d", "e", "f");

        WindowFunctionSpliterator<String> windowFunctionSpliterator = WindowFunctionSpliterator.from(source.spliterator(), 3);
        Stream<List<String>> stream = StreamSupport.stream(windowFunctionSpliterator, false);

        assertThat(stream).containsExactly(
                List.of("a", "b", "c"),
                List.of("b", "c", "d"),
                List.of("c", "d", "e"),
                List.of("d", "e", "f")
        );
    }

    @Test
    void shouldReturnEmptyGroupIfUpstreamIsEmpty() {
        Stream<String> source = Stream.empty();

        WindowFunctionSpliterator<String> windowFunctionSpliterator = WindowFunctionSpliterator.from(source.spliterator(), 3);
        Stream<List<String>> stream = StreamSupport.stream(windowFunctionSpliterator, false);

        assertThat(stream).isEmpty();
    }

    @Test
    void shouldReturnEmptyGroupIfUpstreamDoesNotHaveEnoughElementsToFormASingleWindow() {
        Stream<String> source = Stream.of("a", "b");

        WindowFunctionSpliterator<String> windowFunctionSpliterator = WindowFunctionSpliterator.from(source.spliterator(), 3);
        Stream<List<String>> stream = StreamSupport.stream(windowFunctionSpliterator, false);

        assertThat(stream).isEmpty();
    }
}
