package io.smallyaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Umbrella class to hide the inner mechanics of our YAML object.
 * @param children The nodes of the YAML object.
 */
public record YamlObject(List<Node> children) implements Composite {

    public static YamlObject create() {
        return new YamlObject(new ArrayList<>());
    }

    @Override
    public void addComponent(Node node) {
        this.children.add(node);
    }

    @Override
    public int childrenCount() {
        return this.children.size();
    }

    @Override
    public Node lastChild() {
        return this.children.getLast();
    }

    /**
     * Finds a value via JSON Path like structure to avoid collision that usually occurs with
     * raw separators such as slashes and dots
     * @param path The path where the data is expected to be within the data structure
     * @return The raw String value or an empty optional if the path is not found
     */
    public Optional<String> findValue(YamlPath path) {
        Composite composite = this;
        for(var needle : path.needles()) {
            var foundElement = findNeedle(needle, composite);
            if(foundElement.isEmpty()) {
                return Optional.empty();
            }
            composite = foundElement.get();
        }
        if(composite instanceof Node node && node.isScalar()) {
            return Optional.of(node.value());
        }
        return Optional.empty();
    }

    /**
     * Finds a needle in a haystack, could be optimized with some caching techniques someday
     * @param needle The needle
     * @param composite The haystack
     * @return The needle in the haystack or an empty optional if the needle is not found
     */
    private static Optional<Composite> findNeedle(String needle, Composite composite) {
        for(var child : composite.children()) {
            if(child.name().equals(needle)) {
                return Optional.of(child);
            }
        }
        return Optional.empty();
    }
}
