package me.dags.services.api.query;


public class Query<Q, T extends Property<Q>> {

    final Class<T> type;

    public Query(Class<T> type) {
        this.type = type;
    }
}
