package io.smallyaml;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

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
    public void benchBigYaml() {
        YamlParser.parse(this.getClass().getClassLoader().getResourceAsStream("big.yaml"));
    }
}