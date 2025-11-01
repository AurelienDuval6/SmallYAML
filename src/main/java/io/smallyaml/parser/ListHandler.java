package io.smallyaml.parser;

import io.smallyaml.token.Token;

public final class ListHandler implements Handler {
    @Override
    public Token.Type type() {
        return Token.Type.LIST_START;
    }

    @Override
    public void handle(Parser parser, Token token) {
        parser.dedent();
        parser.updateListIndex();
        parser.indent();

    }
}
