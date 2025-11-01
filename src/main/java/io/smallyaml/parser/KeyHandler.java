package io.smallyaml.parser;

import io.smallyaml.token.Token;

public final class KeyHandler implements Handler {
    @Override
    public Token.Type type() {
        return Token.Type.KEY;
    }

    @Override
    public void handle(Parser parser, Token token) {
        parser.dedent();
        parser.addKey(token.value());
    }
}
