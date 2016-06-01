package me.dags.services.api.query;


public class Query<T extends Property> {

    final Class<T> type;

    public Query(Class<T> type) {
        this.type = type;
    }
}
