package io.smallyaml;

import java.util.List;

/**
 * Simple composite interface to maipulate a tree of nodes.
 */
interface Composite {
    void addComponent(Node node);
    int childrenCount();
    Node lastChild();
    List<Node> children();
}
