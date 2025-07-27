package io.smallyaml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record YamlPath(List<String> needles) {

    public YamlPath {
        Objects.requireNonNull(needles);
    }

    public static YamlPath of(String... needles) {
        return new YamlPath(Arrays.asList(needles));
    }

    public YamlPath append(String needle) {
        List<String> newPath = new ArrayList<>(this.needles);
        newPath.add(needle);
        return new YamlPath(newPath);
    }

    public Optional<String> directSubFolder(YamlPath child) {
        if(child.needles().size() <= this.needles.size()) {
            return Optional.empty();
        }

        if(!child.needles().subList(0, this.needles.size()).equals(this.needles)) {
            return Optional.empty();
        }

        return Optional.of(child.needles.get(this.needles.size()));
    }

    @Override
    public String toString() {
        return String.join(".", this.needles);
    }
}
