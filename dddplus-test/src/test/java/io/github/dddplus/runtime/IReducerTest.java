package io.github.dddplus.runtime;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IReducerTest {

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
        IReducer<String> reducer = IReducer.allOf(s -> "2".equals(s));

        assertFalse(reducer.shouldStop(fixture));
        assertEquals("2", reducer.reduce(fixture));
    }

    @Test
    public void nullPredicate() {
        IReducer<String> reducer = IReducer.allOf(null);
        assertNull(reducer.reduce(fixture));
    }

    @Test
    public void nothingMatched() {
        IReducer<String> reducer = IReducer.allOf(s -> "10".equals(s));

        List<String> fixture = new ArrayList<>();
        fixture.add("1");
        fixture.add("2");
        fixture.add("3");
        assertNull(reducer.reduce(fixture));
        assertFalse(reducer.shouldStop(fixture));
    }

    @Test
    public void stopOnFirstMatch() {
        IReducer<String> reducer = IReducer.stopOnFirstMatch(s -> s.equals("2"));
        List<String> results = new ArrayList<>();
        assertNull(reducer.reduce(results));

        for (String s : fixture) {
            results.add(s);
            if (reducer.shouldStop(results)) {
                break;
            }
        }

        assertEquals("2", reducer.reduce(results));

        reducer = IReducer.stopOnFirstMatch(s -> s.equals("never happen"));
        results.clear();
        for (String s : fixture) {
            results.add(s);
            if (reducer.shouldStop(results)) {
                break;
            }
        }

        assertEquals("3", reducer.reduce(results));
    }

}