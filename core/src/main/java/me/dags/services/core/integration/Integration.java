package me.dags.services.core.integration;

import org.slf4j.Logger;

public class Integration {

    private final Logger logger;
    private final String testForClass;
    private final String integrationClass;
    private UpdatableIntegration integration = null;

    public Integration(Logger logger, String s1, String s2) {
        this.logger = logger;
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
            logger.warn("DID NOT DETECT CLASS " + testForClass);
        }
    }

    public void update() {
        if (integration != null) {
            integration.update();
        }
    }
}
