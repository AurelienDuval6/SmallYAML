package io.smallyaml.token;

import java.util.Optional;
import java.util.SortedSet;
import java.util.regex.Matcher;

public class Tokenizer {


    public Optional<Token> tokenize(String line, SortedSet<Token.Type> possibleTokens) {
        for (var type : possibleTokens) {
            var matcher = type.pattern().matcher(line);
            if (matcher.find()) {
                var match = findMatch(type, matcher);
                var token = new Token(type, match, matcher.group().length());
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    private static String findMatch(Token.Type type, Matcher matcher) {
        for(var group : type.groups()) {
            var match = matcher.group(group);
            if(match != null) {
                return match;
            }
        }
        throw new IllegalStateException("Cannot find any match for given token, please report a bug ticket");
    }
}
