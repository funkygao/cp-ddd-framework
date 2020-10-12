package io.github.dddplus.runtime.registry.mock.pattern.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.step.Steps;
import io.github.dddplus.runtime.registry.mock.ext.IReviseStepsExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.runtime.registry.mock.pattern.RedecideStepsPattern;

import java.util.ArrayList;
import java.util.List;

@Extension(code = RedecideStepsPattern.CODE)
public class ReviseStepsExt implements IReviseStepsExt {

    @Override
    public List<String> reviseSteps(FooModel model) {
        List<String> result = new ArrayList<>();
        result.add(Steps.Submit.BazStep);
        result.add(Steps.Submit.HamStep);
        return result;
    }
}
