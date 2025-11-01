package io.smallyaml.token;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class TransitionManager {

    private final Map<Token.Type, SortedSet<Token.Type>> transitions = new HashMap<>();

    public void addTransitions(Token.Type origin, Token.Type... targets) {
        var transition = this.transitions.getOrDefault(origin, new TreeSet<>());
        transition.addAll(List.of(targets));
        this.transitions.put(origin, transition);
    }

    public SortedSet<Token.Type> getTransitionsForType(Token.Type type) {
        return Collections.unmodifiableSortedSet(transitions.getOrDefault(type, Collections.emptySortedSet()));
    }
}
