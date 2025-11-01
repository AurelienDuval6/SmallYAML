package io.smallyaml.parser;

import io.smallyaml.token.Token;

public final class StartDocumentHandler implements Handler {
    @Override
    public Token.Type type() {
        return Token.Type.START_DOCUMENT;
    }

    @Override
    public void handle(Parser parser, Token token) {
        parser.buildObject();
        parser.resetIndentLevel();
        parser.clearKeys();
    }
}
