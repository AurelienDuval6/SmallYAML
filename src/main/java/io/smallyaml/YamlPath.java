package io.smallyaml;

import java.util.Arrays;
import java.util.List;

public record YamlPath(List<String> needles) {

    public static YamlPath of(String... needles) {
        return new YamlPath(Arrays.asList(needles));
    }

}
