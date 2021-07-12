package net.intelie.challenges;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * Class generated to AutoLoad Events on the Repository for Test Purposes.
 */
@Configuration
@EnableTransactionManagement //Enables Transactions
public class LoadEvents {

  private static final Logger log = LoggerFactory.getLogger(LoadEvents.class);

  /*
   * Starts Memory Database Preloading Events.
   */
  @Bean
  CommandLineRunner initDatabase(EventRepository repository) {
    return args -> {
      log.info("Preloading " + repository.save(new Event("Start", System.currentTimeMillis())));
      log.info("Preloading " + repository.save(new Event("Hello", System.currentTimeMillis())));
    };
  }
}