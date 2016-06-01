package me.dags.services.api.property.dynmap;


public interface Visibility {

    default boolean hideByDefault() {
        return true;
    }
}
