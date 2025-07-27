package io.smallyaml;

/**
 * The description of a node. Can be a composite or scalar node, as stated by YAML specification.
 *
 * @param path The path to this node within the YAML document.
 * @param value The value of the node when it is not a composite node. Could easily work with labels and references in a future version.
 */
record Node(YamlPath path, String value) {
    public static Node from(YamlPath path, String value) {
        return new Node(path, value);
    }

}
