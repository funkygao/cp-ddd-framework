package org.cdf.ddd.runtime.registry;

import com.google.caliper.Benchmark;
import com.google.caliper.api.VmOptions;
import com.google.caliper.runner.CaliperMain;
import org.cdf.ddd.runtime.StepsExecTemplate;
import org.springframework.core.ResolvableType;

@VmOptions({"-Xms4g", "-Xmx4g", "-XX:-TieredCompilation"})
public class ResolvableTypeBenchmark {
    
    public static void main(String[] args) {
        CaliperMain.main(ResolvableTypeBenchmark.class, args);
    }

    // 400 ns
    @Benchmark
    public void stepExecTemplateResolveStepExType(int N) throws Exception {
        for (int i = 0; i < N; i++) {
            resolveStepEx();
        }
    }

    private void resolveStepEx() {
        ResolvableType t = ResolvableType.forClass(StepsExecTemplate.class);
        ResolvableType stepType = t.getGeneric(0);
        ResolvableType stepExceptionType = stepType.getGeneric(1);
        stepExceptionType.resolve();
    }
}
