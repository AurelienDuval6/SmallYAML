package io.smallyaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
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
        List<String> levels = new ArrayList<>();
        List<String> path = new ArrayList<>();
        var root = YamlObject.builder();

        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                /**
                 * Ignore the weird start of YAML marker if there is one, or many... or whatever (as long as no value was added)
                 */
                if(line.startsWith("---") && root.isEmpty()) {
                    continue;
                }
                processLine(root, reader.getLineNumber(), line, levels, path);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read YAML file", e);
        }
        return root.build();
    }

    private static void processLine(YamlObject.Builder root, int lineNumber, String line, List<String> levels, List<String> path) {
        /**
         * Ignore blank lines
         */
        if (line.isBlank()) {
            return;
        }

        var stripped = line.stripLeading();

        /**
         * Ignore comments
         */
        if(stripped.startsWith("#")) {
            return;
        }

        var spaces = extractSpaces(line);

        String currentLevel = levels.isEmpty() ? "" : levels.getLast();

        if(doesNotMatchAnyLowerLevel(root, levels, spaces) || startsNewLevelOnScalarNode(root, spaces)) {
            throw new IllegalStateException("Spaces do not match on line " + lineNumber + " : " + line.trim());
        }

        root.unsetScalar();

        while(spaces.length() < currentLevel.length() && !levels.isEmpty()) {
            currentLevel = levels.removeLast();
            path.removeLast();
        }

        var separatorIndex = stripped.indexOf(":");
        var key = separatorIndex == -1 ? stripped : stripped.substring(0, separatorIndex).trim();
        var value = separatorIndex == -1 ? null : stripped.substring(separatorIndex + 1).trim();

        if(value != null && value.isEmpty()) {
            value = null;
        }

        /**
         * Lists handled here
         */
        if(key.startsWith("- ")) {
            var indexKey = root.countElements(new YamlPath(path)) + "";
            var keyAfterListMarker = key.substring(1);
            if(value != null) { // List of composite (generates two nodes : 1 for the index, 1 for the key value pair)
                levels.add(spaces);
                path.add(indexKey);
                spaces += " " + extractSpaces(keyAfterListMarker);
                key = keyAfterListMarker;
            }
            else { // List of scalar nodes
                key = indexKey;
                value = keyAfterListMarker;
            }
        }

        if(value == null) {
            levels.add(spaces);
            path.add(extractStringValue(key));
        }

        if(value != null){
            YamlPath nodePath = new YamlPath(path).append(extractStringValue(key));

            if(root.containsNode(nodePath)) {
                throw new IllegalStateException("A node is already defined for path " + nodePath + " at line " + lineNumber);
            }

            root.addNode(new Node(nodePath, extractStringValue(value)));
        }
        root.spaces(spaces);
    }

    /**
     * Tests if a new level is being created while the context was mapping scalar nodes
     * @param root The context
     * @param spaces The active spaces of this line
     * @return true when a new unauthorized level is being created
     */
    private static boolean startsNewLevelOnScalarNode(YamlObject.Builder root, String spaces) {
        return !root.previousSpaces().startsWith(spaces) && root.scalar();
    }

    /**
     * Tests if the context does not fall back to any previously known level
     * @param root The context
     * @param levels The currently active levels
     * @param spaces The active spaces of this line
     * @return true if the context does not fall back to any previously known level
     */
    private static boolean doesNotMatchAnyLowerLevel(YamlObject.Builder root, List<String> levels, String spaces) {
        return spaces.length() < root.previousSpaces().length() && !levels.contains(spaces);
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
