package io.smallyaml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record YamlPath(List<String> needles) {

    public static YamlPath of(String... needles) {
        return of(Arrays.asList(needles));
    }

    public static YamlPath of(List<String> needles) {
        return new YamlPath(new ArrayList<>(needles));
    }

    @Override
    public String toString() {
        return String.join(".", this.needles);
    }
}
