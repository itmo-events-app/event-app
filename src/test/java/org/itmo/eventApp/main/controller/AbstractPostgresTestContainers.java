package org.itmo.eventApp.main.controller;

import io.minio.MinioClient;
import org.itmo.eventapp.main.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test-postgres")
@ContextConfiguration(classes = Main.class)
public abstract class AbstractPostgresTestContainers {
    @MockBean
    MinioClient minioClient;

    @Autowired
    private DataSource dataSource;

    @Autowired
    protected MockMvc mockMvc;

    private final static String POSTGRES_VERSION = "postgres:16.0";

    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_VERSION)
            .withUsername("test_user")
            .withPassword("test_password")
            .withDatabaseName("test_db")
            .withReuse(true);

    @BeforeAll
    public static void startPostgres() {
        postgreSQLContainer.start();
        System.setProperty("DB_URL", postgreSQLContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgreSQLContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgreSQLContainer.getPassword());
    }

    @BeforeEach
    public void cleanUp() {
        executeSqlScript("/sql/cleanTables.sql");
    }

    /**
     * @param pathToFile full path from project root directory with filename
     *                   Example: src/test/resources/json/sayHello.json
     * @return file content as string
     */
    protected String loadAsString(String pathToFile) {
        String everything;

        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return everything;
    }

    /**
     * @param sqlFileName full path from resources directory with filename.
     *                    Example /sql/cleanTables.sql
     */
    protected void executeSqlScript(String sqlFileName) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource(sqlFileName));
        resourceDatabasePopulator.setSeparator("@@");
        resourceDatabasePopulator.execute(dataSource);
    }
}
