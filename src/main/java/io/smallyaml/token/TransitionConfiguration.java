package io.smallyaml.token;

public final class TransitionConfiguration {

    private final TransitionManager transitionManager;

    public TransitionConfiguration() {
        transitionManager = new TransitionManager();
        transitionManager.addTransitions(Token.Type.START_DOCUMENT,
                Token.Type.KEY,
                Token.Type.COMMENT,
                Token.Type.START_DOCUMENT
        );
        transitionManager.addTransitions(Token.Type.LIST_START,
                Token.Type.KEY,
                Token.Type.SCALAR
        );
        transitionManager.addTransitions(Token.Type.INDENT,
                Token.Type.INDENT,
                Token.Type.KEY,
                Token.Type.SCALAR,
                Token.Type.LIST_START
        );
        transitionManager.addTransitions(Token.Type.COMMENT,
                Token.Type.KEY,
                Token.Type.SCALAR,
                Token.Type.INDENT,
                Token.Type.COMMENT,
                Token.Type.START_DOCUMENT
        );
        transitionManager.addTransitions(Token.Type.KEY,
                Token.Type.SCALAR,
                Token.Type.INDENT,
                Token.Type.COMMENT,
                Token.Type.LIST_START
        );
        transitionManager.addTransitions(Token.Type.SCALAR,
                Token.Type.KEY,
                Token.Type.INDENT,
                Token.Type.COMMENT,
                Token.Type.START_DOCUMENT
        );

    }

    public TransitionManager transitionManager() {
        return transitionManager;
    }

}
