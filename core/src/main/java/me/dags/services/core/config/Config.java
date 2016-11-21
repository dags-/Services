package me.dags.services.core.config;

import me.dags.data.NodeAdapter;
import me.dags.data.node.Node;
import me.dags.data.node.NodeArray;
import me.dags.data.node.NodeObject;
import me.dags.services.core.Services;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author dags <dags@dags.me>
 */
public class Config {

    private final Path path;
    private final NodeObject data;

    public Config(String name) {
        path = Services.getInstance().configDir.resolve(name).resolve("config.conf");
        if (Files.exists(path)) {
            Node loaded = NodeAdapter.hocon().from(path);
            data = loaded.isPresent() && loaded.isNodeObject() ? loaded.asNodeObject() : new NodeObject();
        } else {
            data = new NodeObject();
        }
    }

    public Config set(String key, Object value) {
        data.put(key, value);
        NodeAdapter.hocon().to(data, path);
        return this;
    }

    public Node getNode(String key) {
        return data.get(key);
    }

    public NodeObject getObject(String key) {
        return data.getObject(key);
    }

    public NodeArray getArray(String key) {
        return data.getArray(key);
    }
}
