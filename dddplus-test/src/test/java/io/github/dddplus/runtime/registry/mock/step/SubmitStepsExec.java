package io.github.dddplus.runtime.registry.mock.step;

import io.github.dddplus.runtime.StepsExecTemplate;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import org.springframework.stereotype.Component;

@Component
public class SubmitStepsExec extends StepsExecTemplate<SubmitStep, FooModel> {
}
