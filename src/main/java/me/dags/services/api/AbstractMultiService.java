package me.dags.services.api;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractMultiService<T extends NamedService> implements MultiService<T> {

    protected final Map<String, T> services = new HashMap<>();

    @Override
    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(services.values());
    }

    @Override
    public Optional<T> getService(String name) {
        return Optional.ofNullable(services.get(name));
    }

    @Override
    public boolean hasService(String name) {
        return services.containsKey(name);
    }

    @Override
    public void register(T service) {
        services.put(service.getName(), service);
    }
}
