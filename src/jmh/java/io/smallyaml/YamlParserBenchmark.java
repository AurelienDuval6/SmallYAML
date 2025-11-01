package io.smallyaml;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class YamlParserBenchmark {

    @Setup(Level.Invocation)
    public void setUp() {
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1, time = 10)
    @Measurement(iterations = 2, time = 20)
    @Benchmark
    @BenchmarkMode(Mode.All)
    public void benchBigYaml(YamlParserBenchmark plan) {
        YamlParser.parse(this.getClass().getClassLoader().getResourceAsStream("big.yaml"));
    }
}