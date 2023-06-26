package io.github.dddplus.step;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReviseStepsExceptionTest {

    @Test
    void basic() {
        ReviseStepsException exception = new ReviseStepsException();
        List<String> steps = new ArrayList<>();
        steps.add("stepA");
        exception.withSubsequentSteps(steps);
        List<String> subsequences = exception.subsequentSteps();
        assertEquals(subsequences.get(0), "stepA");
    }
}
