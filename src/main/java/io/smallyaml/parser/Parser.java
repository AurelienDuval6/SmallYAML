package io.smallyaml.parser;

import io.smallyaml.Node;
import io.smallyaml.YamlObject;
import io.smallyaml.YamlPath;
import io.smallyaml.token.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {

    private int indentLevel = 0;
    private final List<String> keys = new ArrayList<>();
    private final List<Integer> listIndex = new ArrayList<>();
    private final List<YamlObject> result = new ArrayList<>();
    private YamlObject.Builder builder = null;

    public void initialize() {
        resetIndentLevel();
        clearKeys();
        result.clear();
        builder = null;
        listIndex.clear();
    }

    public void parse(Token token) {
        var handler = Handler.implementation(token.type());
        handler.handle(this, token);
    }

    public void createNewListIndex() {
        listIndex.add(0);
    }

    public void updateListIndex() {
        if(isNewListLevel()) {
            createNewListIndex();
        }
        addKey("" + listIndex.getLast());
        listIndex.set(indentLevel - 1, listIndex.getLast() + 1);
    }

    public boolean isNewListLevel() {
        return listIndex.size() < indentLevel;
    }

    public void addKey(String token) {
        keys.add(token);
        createNewListIndex();
    }

    public void addNode(Token token) {
        if(builder == null) {
            builder = new YamlObject.Builder();
        }
        builder.addNode(Node.from(YamlPath.of(keys), token.value()));
    }

    public void buildObject() {
        if(builder != null) {
            result.add(builder.build());
            builder = null;
        }
    }

    public void indent() {
        ++indentLevel;
    }

    public void dedent() {
        while(listIndex.size() > indentLevel) {
            listIndex.removeLast();
        }

        while(keys.size() > indentLevel) {
            keys.removeLast();
        }
    }

    public List<YamlObject> terminate() {
        buildObject();
        return Collections.unmodifiableList(result);
    }

    public void resetIndentLevel() {
        indentLevel = 0;
    }

    public void clearKeys() {
        keys.clear();
    }

    public void endLine() {
        resetIndentLevel();
    }
}
