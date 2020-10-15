package io.github.dddplus.runtime;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class ReducerTest {

    private List<String> fixture = new ArrayList<>();

    @Before
    public void setUp() {
        fixture.clear();
        fixture.add("1");
        fixture.add("2");
        fixture.add("3");
    }

    @Test
    public void allOf() {
        IReducer<String> reducer = Reducer.all(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return "2".equals(s);
            }
        });

        assertFalse(reducer.shouldStop(fixture));
        assertEquals("2", reducer.reduce(fixture));
    }

    @Test
    public void nullPredicate() {
        IReducer<String> reducer = Reducer.all(null);
        assertNull(reducer.reduce(fixture));
    }

    @Test
    public void nothingMatched() {
        IReducer<String> reducer = Reducer.all(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return "10".equals(s);
            }
        });

        List<String> fixture = new ArrayList<>();
        fixture.add("1");
        fixture.add("2");
        fixture.add("3");
        assertNull(reducer.reduce(fixture));
        assertFalse(reducer.shouldStop(fixture));
    }

}