package com.company.customerinfo.benchmark.test;


import com.company.customerinfo.benchmark.impl.CustomerOrderServiceBenchMark;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CustomerOrderServiceIntegrationBenchMarkTest {

    private final static Integer MEASUREMENT_ITERATIONS = 3;
    private final static Integer WARMUP_ITERATIONS = 1;

    @Test
    public void saveCustomerWithOrdersTest() throws RunnerException {

        Options opt = new OptionsBuilder()
                .include("\\." + CustomerOrderServiceBenchMark.class.getSimpleName() + "\\.")
                .warmupIterations(WARMUP_ITERATIONS)
                .measurementIterations(MEASUREMENT_ITERATIONS)
                // do not use forking or the benchmark methods will not see references stored within its class
                .forks(0)
                // do not use multiple threads
                .threads(1)
                // single shot for each iteration:
                .warmupTime(TimeValue.NONE)
                .measurementTime(TimeValue.NONE)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                .result(buildResultsFileName())
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build();

        new Runner(opt).run();
    }

    private String buildResultsFileName() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm-dd-yyyy-hh-mm-ss");
        String suffix = ".json";
        return String.format("target/%s%s%s", "jmh-result-", date.format(formatter), suffix);
    }

}
