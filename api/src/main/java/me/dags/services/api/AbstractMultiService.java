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
    public Optional<T> getService(String identifier) {
        return Optional.ofNullable(services.get(identifier));
    }

    @Override
    public boolean hasService(String identifier) {
        return services.containsKey(identifier);
    }

    @Override
    public void register(T service) {
        services.put(service.getIdentifier(), service);
    }
}
