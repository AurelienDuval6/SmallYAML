package io.smallyaml.token;

import java.util.regex.Pattern;

public record Token(Type type, String value, int length) {

    public enum Type {
        COMMENT(Pattern.compile("^\\s*#\\s*(.*)\\s*$"), new int[]{1}),
        START_DOCUMENT(Pattern.compile("^\\s*(---)\\s*$"), new int[]{1}),
        INDENT(Pattern.compile("^( {2})"), new int[]{1}),
        LIST_START(Pattern.compile("(-\\s+)"), new int[]{1}),
        KEY(Pattern.compile("^[\"']?(\\w+)[\"']?:(\\s+|$)"), new int[]{1}),
        SCALAR(Pattern.compile("^(\"?(\\w+)\"?|'?(\\w+)'?)\\s*"), new int[]{2, 3});

        private final Pattern pattern;
        private final int[] groups;
        Type(Pattern pattern, int[] groups) {
            this.pattern = pattern;
            this.groups = groups;
        }

        public Pattern pattern() {
            return pattern;
        }

        public int[] groups() {
            return groups;
        }
    }

}
