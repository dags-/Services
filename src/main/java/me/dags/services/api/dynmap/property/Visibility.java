package me.dags.services.api.dynmap.property;


public interface Visibility {

    default boolean hideByDefault() {
        return true;
    }
}
