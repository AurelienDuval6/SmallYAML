package io.smallyaml.parser;

import io.smallyaml.token.Token;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public sealed interface Handler permits CommentHandler, IndentHandler, KeyHandler, ListHandler, ScalarHandler, StartDocumentHandler {

    Handler[] IMPLEMENTATIONS = new Handler[]{
            new CommentHandler(),
            new StartDocumentHandler(),
            new KeyHandler(),
            new ListHandler(),
            new ScalarHandler(),
            new IndentHandler(),
    };

    Map<Token.Type, Handler> TYPE_MAP = Arrays.stream(IMPLEMENTATIONS)
            .collect(Collectors.toMap(
                    Handler::type,
                    Function.identity()
            ));


    Token.Type type();

    void handle(Parser parser, Token token);

    static Handler implementation(Token.Type type) {
        return TYPE_MAP.get(type);
    }

}
