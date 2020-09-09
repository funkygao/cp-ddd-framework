package org.ddd.cp.ddd.runtime.registry.mock.pattern.extension;

import org.ddd.cp.ddd.annotation.Extension;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IReviseStepsExt;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;
import org.ddd.cp.ddd.runtime.registry.mock.pattern.RedecideStepsPattern;
import org.ddd.cp.ddd.runtime.registry.mock.step.Steps;

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
