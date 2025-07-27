package io.smallyaml;

import java.util.*;

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

    /**
     * Counts the number of elements within this path
     */
    public int countElements(YamlPath path) {
        Set<String> paths = new HashSet<>();
        for(var node : nodes) {
            path.directSubFolder(node.path()).ifPresent(paths::add);
        }
        return paths.size();
    }

    static Builder builder() {
        return new Builder();
    }

    /**
     * Friend class pattern to maintain the immutability of YamlObject records
     */
    static class Builder {
        private final YamlObject object = new YamlObject(new ArrayList<>());
        private String previousSpaces = "";
        private boolean scalar = true;

        public void addNode(Node node) {
            object.nodes.add(node);
            scalar = true;
        }

        public boolean containsNode(YamlPath path) {
            return object.exists(path);
        }

        public int countElements(YamlPath path) {
            return object.countElements(path);
        }

        public void spaces(String spaces) {
            this.previousSpaces = spaces;
        }

        public String previousSpaces() {
            return previousSpaces;
        }

        public boolean isEmpty() {
            return object.nodes.isEmpty();
        }

        public YamlObject build() {
            return object;
        }

        public boolean scalar() {
            return scalar;
        }

        public void unsetScalar() {
            scalar = false;
        }
    }
}
