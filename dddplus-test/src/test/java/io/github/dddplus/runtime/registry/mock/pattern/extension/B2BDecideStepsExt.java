package io.github.dddplus.runtime.registry.mock.pattern.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.ext.IDecideStepsExt;
import io.github.dddplus.ext.IIdentity;
import io.github.dddplus.runtime.registry.mock.pattern.B2BPattern;
import io.github.dddplus.runtime.registry.mock.step.Steps;
import lombok.NonNull;

import java.util.*;
import java.util.function.Supplier;

@Extension(code = B2BPattern.CODE)
public class B2BDecideStepsExt implements IDecideStepsExt {
    private static final List<String> emptySteps = Collections.emptyList();

    @Override
    public List<String> decideSteps(@NonNull IIdentity model, @NonNull String activityCode) {
        Supplier<List<String>> supplier = registry.get(activityCode);
        if (supplier != null) {
            return supplier.get();
        }

        return emptySteps;
    }

    private static List<String> submitSteps() {
        List<String> steps = new ArrayList<>();
        steps.add(Steps.Submit.BazStep);
        steps.add(Steps.Submit.FooStep);
        steps.add(Steps.Submit.BarStep);
        return steps;
    }

    private static List<String> cancelSteps() {
        List<String> steps = new ArrayList<>();
        return steps;
    }

    private static Map<String, Supplier<List<String>>> registry = new HashMap<>();
    static {
        registry.put(Steps.Submit.Activity, B2BDecideStepsExt::submitSteps);
        registry.put(Steps.Cancel.Activity, B2BDecideStepsExt::cancelSteps);
    }
}
