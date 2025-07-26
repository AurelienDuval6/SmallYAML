package io.smallyaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Parses an input YAML stream to a YAML object
 * It was kept as permissive as possible for the moment as multiple "---" characters will be considered as valid keys
 * and duplicate values will take the last defined value.
 *
 * If strict validation is needed, another class could handle it in order to keep a standardized way to parse YAML,
 * as defined below. The parser should stay as agnostic as possible in order to keep it simple.
 */
public final class YamlParser {

    /**
     * Regex to extract string from a string with escaped characters and stuffs
     */
    private static final Pattern STRING_EXTRACTOR = Pattern.compile("(?:\"((?:[^\"\\\\]|\\\\.)*)\"|'((?:[^'\\\\]|\\\\.)*)'|([^#\\n]+))");

    private YamlParser() {
        // hidden
    }

    public static YamlObject parse(InputStream inputStream) {

        var root = YamlObject.create();

        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                /**
                 * Ignore the weird start of YAML marker if there is one, or many... or whatever (as long as no value was added)
                 */
                if(line.startsWith("---") && root.children().isEmpty()) {
                    continue;
                }
                processLine(root, reader.getLineNumber(), line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read YAML file", e);
        }
        return root;
    }

    private static void processLine(YamlObject root, int lineNumber, String line) {
        /**
         * Ignore blank lines
         */
        if (line.isBlank()) {
            return;
        }

        var currentRoot = findRoot(root, line, lineNumber).orElse(root);

        var stripped = line.stripLeading();

        /**
         * Ignore comments
         */
        if(stripped.startsWith("#")) {
            return;
        }

        var separatorIndex = stripped.indexOf(":");

        var key = separatorIndex == -1 ? stripped : stripped.substring(0, separatorIndex).trim();
        var spaces = extractSpaces(line);
        var value = separatorIndex == -1 ? null : stripped.substring(separatorIndex + 1).trim();

        /**
         * Lists handled here
         */
        if(key.startsWith("- ")) {
            var indexKey = currentRoot.childrenCount() + "";
            var keyAfterListMarker = key.substring(1);
            if(value != null) { // List of composite (generates two nodes : 1 for the index, 1 for the key value pair)
                addNode(currentRoot, spaces, indexKey, null);
                currentRoot.lastChild().addComponent(Node.from(spaces + " " + extractSpaces(keyAfterListMarker), keyAfterListMarker.trim(), value));
            }
            else { // List of scalar nodes
                addNode(currentRoot, spaces, indexKey, keyAfterListMarker.trim());
            }
        }
        else {
            addNode(currentRoot, spaces, key, value);
        }

    }

    /**
     * Helper function to remove the leading spaces before starting our work
     * @param line
     * @return
     */
    private static String extractSpaces(String line) {
        return line.substring(0, line.length() - line.stripLeading().length());
    }

    /**
     * Finds the root where to place the current node.
     * @param root The real root of our YAML object
     * @param line
     * @param lineNumber
     * @return The node that should be used as root for this line
     */
    private static Optional<Composite> findRoot(YamlObject root, String line, int lineNumber) {

        if (root.children().isEmpty() || line.stripLeading().equals(line)) {
            return Optional.empty();
        }

        var currentRoot = root.children().getLast();

        while(line.startsWith(currentRoot.spaces())) {
            if (currentRoot.children().isEmpty()) {
                return Optional.of(currentRoot);
            }
            Node nextRoot = currentRoot.children().getLast();
            if(nextRoot.spaces().equals(extractSpaces(line))) {
                return Optional.of(currentRoot);
            }
            currentRoot = nextRoot;
        }
        /**
         * Strange YAML structure with incorrect tabs usually. There are probably more edge cases
         */
        throw new IllegalStateException("Syntax error on line " + lineNumber);
    }

    /**
     * Add nodes and clean its data
     * @param currentRoot
     * @param spaces
     * @param key
     * @param value
     */
    private static void addNode(Composite currentRoot, String spaces, String key, String value) {
        var extractedKey = extractStringValue(key);
        var extractedValue = extractStringValue(value);

        if(key.startsWith("-")) {
            extractedKey = currentRoot.childrenCount() + "";
            extractedValue = extractStringValue(extractedKey.substring(1));
        }

        currentRoot.addComponent(Node.from(spaces, extractedKey, extractedValue));
    }

    /**
     * Clean the data of a String and removes the comments
     * @param value
     * @return cleaned up string
     */
    private static String extractStringValue(String value) {
        if (value == null) {
            return null;
        }
        var valueMatcher = STRING_EXTRACTOR.matcher(value.trim());
        var extractedValue = valueMatcher.find() ? valueMatcher.group(1) : null;
        if(extractedValue == null) {
            extractedValue = value.split("#")[0].trim();
        }
        return extractedValue;
    }

}
