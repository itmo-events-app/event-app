package org.itmo.eventApp.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import org.itmo.eventapp.main.Main;
import org.itmo.eventapp.main.model.dto.request.LoginRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = Main.class)
public abstract class AbstractTestContainers {
    @Autowired
    private DataSource dataSource;

    @Autowired
    protected MockMvc mockMvc;

    private final static String POSTGRES_VERSION = "postgres:16.0";

    /**
     * Class for converting java object to json format string
     */
    protected ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    /**
     * In tests create bucket only with this name
     * After each test bucket with this name will be deleted
     */
    public final static String MINIO_BUCKET = "test-bucket";

    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_VERSION)
        .withUsername("test_user")
        .withPassword("test_password")
        .withDatabaseName("test_db")
        .withReuse(true);

    private static final MinIOContainer minioContainer = new MinIOContainer("minio/minio")
        .withUserName("test_minio_admin")
        .withPassword("test_minio_admin")
        .withReuse(true);

    MinioClient minioClient = MinioClient
        .builder()
        .endpoint(minioContainer.getS3URL())
        .credentials(minioContainer.getUserName(), minioContainer.getPassword())
        .build();

    @BeforeAll
    public static void startPostgres() {
        postgreSQLContainer.start();
        System.setProperty("DB_URL", postgreSQLContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgreSQLContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgreSQLContainer.getPassword());
    }

    @BeforeAll
    public static void startMinio() {
        minioContainer.start();
        System.setProperty("MINIO_URL", minioContainer.getS3URL());
        System.setProperty("MINIO_ACCESS_KEY", minioContainer.getUserName());
        System.setProperty("MINIO_SECRET_KEY", minioContainer.getPassword());
    }

    @BeforeEach
    public void cleanUp() throws Exception {
        try {
            executeSqlScript("/sql/clean_tables.sql");
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(MINIO_BUCKET).build());
        } catch (Exception ignored) {

        }
    }

    /**
     * @param pathToFile full path from resources directory with filename.
     *                   Example: /json/sayHello.json
     * @return file content as string
     */
    protected String loadAsString(String pathToFile) {
        String everything;

        try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/" + pathToFile))) {
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
//        resourceDatabasePopulator.setSeparator("@@");
        resourceDatabasePopulator.setSqlScriptEncoding("UTF-8");
        resourceDatabasePopulator.execute(dataSource);
    }

    /**
     * Sends request to /login endpoint and returns Bearer token
     *
     * @param login    - login
     * @param password - unencrypted password
     * @return token
     */
    protected String getToken(String login, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(login, password);
        String content = objectWriter.writeValueAsString(loginRequest);

        String token = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andReturn()
            .getResponse()
            .getContentAsString();

        return token;
    }
}
