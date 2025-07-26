package io.smallyaml;

import java.util.ArrayList;
import java.util.List;

/**
 * The description of a node. Can be a composite or scalar node, as stated by YAML specification.
 *
 * @param spaces The spaces before the name of the node. Acts as a path and will be useful when validation will be done
 * @param name The name of the current node. Converted as integer to manipulate lists in order to reduce complexity
 * @param children The children components of my composite when the node is not a scalar node.
 * @param value The value of the node when it is not a composite node. Could easily work with labels and references in a future version.
 */
record Node(String spaces, String name, List<Node> children, String value) implements Composite {
    public static Node from(String spaces, String name, String value) {
        return new Node(spaces, name, new ArrayList<>(), value);
    }

    public boolean isScalar() {
        return this.children.isEmpty();
    }

    public boolean isComposite() {
        return !this.children.isEmpty();
    }

    @Override
    public void addComponent(Node child) {
        this.children.add(child);
    }

    @Override
    public int childrenCount() {
        return this.children.size();
    }

    @Override
    public Node lastChild() {
        return this.children.getLast();
    }

}
