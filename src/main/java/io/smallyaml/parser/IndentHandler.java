package io.smallyaml.parser;

import io.smallyaml.token.Token;

public final class IndentHandler implements Handler {
    @Override
    public Token.Type type() {
        return Token.Type.INDENT;
    }

    @Override
    public void handle(Parser parser, Token token) {
        parser.indent();
    }
}
