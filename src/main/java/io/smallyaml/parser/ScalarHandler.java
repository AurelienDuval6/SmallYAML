package io.smallyaml.parser;

import io.smallyaml.token.Token;

public final class ScalarHandler implements Handler {
    @Override
    public Token.Type type() {
        return Token.Type.SCALAR;
    }

    @Override
    public void handle(Parser parser, Token token) {
        parser.addNode(token);
    }
}
