package org.cdf.ddd.runtime.registry;

import com.google.caliper.Benchmark;
import com.google.caliper.api.VmOptions;
import com.google.caliper.runner.CaliperMain;
import org.cdf.ddd.runtime.registry.mock.step.SubmitStepsExec;
import org.springframework.core.ResolvableType;

@VmOptions({"-Xms4g", "-Xmx4g", "-XX:-TieredCompilation"})
public class ResolvableTypeBenchmark {

    public static void main(String[] args) {
        CaliperMain.main(ResolvableTypeBenchmark.class, args);
    }

    // 300 ns
    @Benchmark
    public void stepExecTemplateResolveStepExType(int N) throws Exception {
        ChildStepsExec stepsExec = new ChildStepsExec();
        for (int i = 0; i < N; i++) {
            stepsExec.resolve();
        }
    }

    static class ChildStepsExec extends SubmitStepsExec {
        Class resolve() {
            ResolvableType stepsExecTemplateT = ResolvableType.forClass(this.getClass());
            ResolvableType stepT = stepsExecTemplateT.getSuperType().getSuperType().getGeneric(0); // 父类的父类，才能拿到泛型声明
            Class stepExType = stepT.getInterfaces()[0].getGeneric(1).resolve(); // will be FooException
            return stepExType;
        }
    }
}
