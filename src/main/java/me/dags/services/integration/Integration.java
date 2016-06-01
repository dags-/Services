package me.dags.services.integration;

public class Integration {

    private final String testForClass;
    private final String integrationClass;
    private UpdatableIntegration integration = null;

    public Integration(String s1, String s2) {
        this.testForClass = s1;
        this.integrationClass = s2;
    }

    public void init() {
        try {
            if (Class.forName(testForClass) != null) {
                Class<?> target = Class.forName(integrationClass);
                Object object = target.newInstance();
                if (UpdatableIntegration.class.isInstance(object)) {
                    integration = UpdatableIntegration.class.cast(object);
                    integration.init();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (integration != null) {
            integration.update();
        }
    }
}
