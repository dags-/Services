package me.dags.services.api.property;

import me.dags.services.api.property.action.Build;
import me.dags.services.api.property.action.Damage;
import me.dags.services.api.property.action.Interact;
import me.dags.services.api.property.action.Traverse;
import me.dags.services.api.property.action.UseItem;
import me.dags.services.api.property.meta.Description;
import me.dags.services.api.property.meta.Owner;

public final class Query<T extends Property> {

    public static final Query<Build> BUILD = new Query<>(Build.class);
    public static final Query<Damage> DAMAGE = new Query<>(Damage.class);
    public static final Query<Description> DESCRIPTION = new Query<>(Description.class);
    public static final Query<Interact> INTERACT = new Query<>(Interact.class);
    public static final Query<Owner> OWNER = new Query<>(Owner.class);
    public static final Query<Traverse> TRAVERSE = new Query<>(Traverse.class);
    public static final Query<UseItem> USE_ITEM = new Query<>(UseItem.class);

    final Class<T> type;

    public Query(Class<T> type) {
        this.type = type;
    }
}
