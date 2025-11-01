package io.smallyaml;

import io.smallyaml.parser.Parser;
import io.smallyaml.token.Token;
import io.smallyaml.token.Tokenizer;
import io.smallyaml.token.TransitionConfiguration;
import io.smallyaml.token.TransitionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public final class YamlParser {

    private static final TransitionManager transitionManager;

    static {
        transitionManager = new TransitionConfiguration().transitionManager();
    }

    public static YamlObject parse(InputStream inputStream) {
        var parser = new Parser();
        var tokenizer = new Tokenizer();
        var possibleTokens = transitionManager.getTransitionsForType(Token.Type.START_DOCUMENT);
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = line;
                while(!processedLine.isBlank()) {
                    var token = tokenizer.tokenize(processedLine, possibleTokens);

                    if(token.isEmpty()) {
                        throw new IllegalStateException("Failed to parse line " + (reader.getLineNumber()) + " : " + line);
                    }

                    var unwrappedToken = token.get();
                    possibleTokens = transitionManager.getTransitionsForType(unwrappedToken.type());

                    //System.out.println(unwrappedToken.type() + "(" + unwrappedToken.value() + ")");

                    processedLine = processedLine.substring(unwrappedToken.length());

                    parser.parse(unwrappedToken);
                }
                parser.endLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read YAML file", e);
        }
        return parser.terminate().getFirst();
    }
}
