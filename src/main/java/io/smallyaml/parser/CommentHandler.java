package io.smallyaml.parser;

import io.smallyaml.token.Token;

public final class CommentHandler implements Handler {
    @Override
    public Token.Type type() {
        return Token.Type.COMMENT;
    }

    @Override
    public void handle(Parser parser, Token token) {
        // Nothing to do
    }
}
