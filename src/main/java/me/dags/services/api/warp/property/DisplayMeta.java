package me.dags.services.api.warp.property;

import java.util.List;

import me.dags.services.api.query.Property;
import me.dags.services.api.warp.Warp;

public interface DisplayMeta extends Property<Warp> {

    List<String> htmlLines();
}
