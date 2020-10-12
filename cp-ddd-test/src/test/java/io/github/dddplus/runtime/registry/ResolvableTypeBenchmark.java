package io.github.dddplus.runtime.registry;

import com.google.caliper.Benchmark;
import com.google.caliper.api.VmOptions;
import com.google.caliper.runner.CaliperMain;
import io.github.dddplus.step.IDomainStep;
import io.github.dddplus.runtime.registry.mock.step.SubmitStepsExec;
import org.springframework.core.ResolvableType;

@VmOptions({"-Xms4g", "-Xmx4g", "-XX:-TieredCompilation"})
public class ResolvableTypeBenchmark {

    public static void main(String[] args) {
        CaliperMain.main(ResolvableTypeBenchmark.class, args);
    }

    // 400 ns
    @Benchmark
    public void stepExecTemplateResolveStepExType(int N) throws Exception {
        ChildStepsExec stepsExec = new ChildStepsExec();
        for (int i = 0; i < N; i++) {
            stepsExec.resolve();
        }
    }

    static class ChildStepsExec extends SubmitStepsExec {
        Class resolve() {
            ResolvableType stepsExecType = ResolvableType.forClass(this.getClass());
            ResolvableType templateType = stepsExecType.getSuperType();
            while (templateType.getGenerics().length == 0) {
                templateType = templateType.getSuperType();
            }
            ResolvableType stepType = templateType.getGeneric(0);

            // Step实现多个接口的场景
            for (ResolvableType stepInterfaceType : stepType.getInterfaces()) {
                if (IDomainStep.class.isAssignableFrom(stepInterfaceType.resolve())) {
                    return stepInterfaceType.getGeneric(1).resolve();
                }
            }

            // should never happen
            return null;
        }
    }
}
