package org.itmo.eventApp.main.controller;

import io.minio.BucketExistsArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestControllerTest extends AbstractTestContainers {
    @Test
    @WithMockUser(username = "test_mail@test_mail.com")
    void sayHelloTest() throws Exception {

        executeSqlScript("/sql/insert_user.sql");

        mockMvc.perform(get("/hello").param("s", "test"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Hello, test!")));
    }

    @Test
    @WithMockUser(username = "test_mail@test_mail.com")
    void uploadTest() throws Exception {
        String originalFilename = "upload-test";

        executeSqlScript("/sql/insert_user.sql");

        // name should be multipartFile as param name
        MockMultipartFile jsonFile = new MockMultipartFile(
            "multipartFile",
            originalFilename,
            "",
            "{\"upload\": \"test\"}".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/upload/" + MINIO_BUCKET)
                .file(jsonFile)
                .contentType("multipart/form-data"))
            .andExpect(status().isOk());

        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(MINIO_BUCKET).build());
        boolean isObjectExists = isObjectExist(originalFilename);

        assertThat(isBucketExists).isTrue();
        assertThat(isObjectExists).isTrue();
    }

    @Test
    @WithMockUser(username = "test_mail@test_mail.com")
    void deleteTest() throws Exception {

        executeSqlScript("/sql/insert_user.sql");

        String originalFilename = "upload-test";

        // name should be multipartFile as param name
        MockMultipartFile jsonFile = new MockMultipartFile(
            "multipartFile",
            originalFilename,
            "",
            "{\"upload\": \"test\"}".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/upload/" + MINIO_BUCKET)
                .file(jsonFile)
                .contentType("multipart/form-data"))
            .andExpect(status().isOk());

        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(MINIO_BUCKET).build());
        boolean isObjectExists = isObjectExist(originalFilename);

        assertThat(isBucketExists).isTrue();
        assertThat(isObjectExists).isTrue();

        mockMvc.perform(delete("/delete/" + MINIO_BUCKET + "/" + originalFilename))
            .andExpect(status().is(204));

        isObjectExists = isObjectExist(originalFilename);

        assertThat(isObjectExists).isFalse();
    }

    private boolean isObjectExist(String object) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                .bucket(AbstractTestContainers.MINIO_BUCKET)
                .object(object).build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
