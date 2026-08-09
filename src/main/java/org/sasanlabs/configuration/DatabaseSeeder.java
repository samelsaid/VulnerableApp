package org.sasanlabs.configuration;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

/**
 * Runs all {@link ModuleSeeder} implementations while the context is still refreshing, ensuring the
 * database is populated before the app accepts requests.
 */
@Component
public class DatabaseSeeder implements SmartInitializingSingleton {

    private static final transient Logger LOGGER = LogManager.getLogger(DatabaseSeeder.class);

    //  Finds every @Component that implements ModuleSeeder
    private final List<ModuleSeeder> seeders;

    public DatabaseSeeder(List<ModuleSeeder> seeders) {
        this.seeders = seeders;
    }

    /**
     * ApplicationReadyEvent is published after the servlet container has already bound its port,
     * so seeding used to overlap with live traffic. Bcrypt-hashing the vault takes seconds, and
     * every request arriving in that window reached a level whose table was still empty and got
     * an HTTP 500 back. Running from {@code afterSingletonsInstantiated} finishes inside the
     * refresh, before the connector opens, which is what this class already claimed to do.
     */
    @Override
    public void afterSingletonsInstantiated() {
        LOGGER.info("Starting Global Database Seeding");

        for (ModuleSeeder seeder : seeders) {
            try {
                if (!seeder.isSeeded()) {
                    seeder.seed();
                    LOGGER.info(
                            "{} seeded module: {} (Table: {})",
                            seeder.toString(),
                            seeder.getModuleName(),
                            seeder.getModuleTable());
                }
            } catch (Exception e) {
                LOGGER.error(
                        "{} failed to seed module: {} (Table: {}). Aborting startup.",
                        seeder.toString(),
                        seeder.getModuleName(),
                        seeder.getModuleTable(),
                        e);
                throw new IllegalStateException(
                        "Aborting startup: seeding failed for " + seeder.getModuleName(), e);
            }
        }

        LOGGER.info("Seeding complete. Processed {} modules", seeders.size());
    }
}
