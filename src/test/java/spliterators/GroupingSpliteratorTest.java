package spliterators;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class GroupingSpliteratorTest {

    @Test
    void shouldReturnGrouped() {
        Stream<String> source = Stream.of("a", "b", "c", "d", "e", "f");

        GroupingSpliterator groupingSpliterator = GroupingSpliterator.from(source.spliterator(), 3);
        Stream<String> stream = StreamSupport.stream(groupingSpliterator, false);

        assertThat(stream).containsExactly("abc", "def");
    }

    @Test
    void shouldReturnIncompleteGroupIfNotEnoughElementsAvailableUpstream() {
        Stream<String> source = Stream.of("a", "b", "c", "d", "e");

        GroupingSpliterator groupingSpliterator = GroupingSpliterator.from(source.spliterator(), 3);
        Stream<String> stream = StreamSupport.stream(groupingSpliterator, false);

        assertThat(stream).containsExactly("abc", "de");
    }

    @Test
    void shouldReturnEmptyGroupIfUpstreamIsEmpty() {
        Stream<String> source = Stream.of();

        GroupingSpliterator groupingSpliterator = GroupingSpliterator.from(source.spliterator(), 3);
        Stream<String> stream = StreamSupport.stream(groupingSpliterator, false);

        assertThat(stream).isEmpty();
    }
}
