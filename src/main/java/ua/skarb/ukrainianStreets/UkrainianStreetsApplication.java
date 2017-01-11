package ua.skarb.ukrainianStreets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ua.skarb.ukrainianStreets.service.MainService;
import java.io.IOException;

/**
 * Main class, that start to retrieve data from Overpass API and store to <b>Oracle database</b>.
 * Before start the app create schema "UKRAINIANSTREETS" and run schema.sql file
 *
 * @author Vynohradov Evgeniy
 */
@SpringBootApplication
public class UkrainianStreetsApplication {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = SpringApplication.run(UkrainianStreetsApplication.class, args);

        MainService mainService = context.getBean(MainService.class);
        mainService.fillDB();

    }
}
