package io.smallyaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Umbrella class to hide the inner mechanics of our YAML object.
 * @param nodes The nodes of the YAML object.
 */
public record YamlObject(List<Node> nodes) {

    /**
     * Finds a value via JSON Path like structure to avoid collision that usually occurs with
     * raw separators such as slashes and dots
     * @param path The path where the data is expected to be within the data structure
     * @return The raw String value or an empty optional if the path is not found
     */
    public Optional<String> findValue(YamlPath path) {
        for(var node : nodes) {
            if(node.path().equals(path)) {
                return Optional.of(node.value());
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if the context containes a node at the given path
     * @param path The path where the data is expected to be within the data structure
     * @return true if a node exists
     */
    public boolean exists(YamlPath path) {
        for(var node : nodes) {
            if(node.path().equals(path)) {
                return true;
            }
        }
        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Friend class pattern to maintain the immutability of YamlObject records
     */
    public static class Builder {
        private final YamlObject object = new YamlObject(new ArrayList<>());

        public void addNode(Node node) {
            object.nodes.add(node);
        }

        public boolean containsNode(YamlPath path) {
            return object.exists(path);
        }

        public YamlObject build() {
            return object;
        }
    }
}
