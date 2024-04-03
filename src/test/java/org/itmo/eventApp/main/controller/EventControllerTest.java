package org.itmo.eventApp.main.controller;

import io.minio.BucketExistsArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTest extends AbstractTestContainers {
    // TODO: Add Test for event controller here
    private boolean isImageExist(String object) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket("event-images")
                    .object(object).build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @BeforeEach
    public void setup() {
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event.sql");
    }

    @Test
    void addProperEvent() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("placeId", "1");
        params.add("start", "2024-03-28T09:00:00");
        params.add("end", "2024-03-28T18:00:00");
        params.add("title", "itmo-event");
        params.add("shortDescription", "This is a short description.");
        params.add("fullDescription", "This is a full description of the event.");
        params.add("format", "OFFLINE");
        params.add("status", "PUBLISHED");
        params.add("registrationStart", "2024-03-01T00:00:00");
        params.add("registrationEnd", "2024-03-25T23:59:59");
        params.add("parent", "1");
        params.add("participantLimit", "50");
        params.add("participantAgeLowest", "18");
        params.add("participantAgeHighest", "50");
        params.add("preparingStart", "2024-03-20T00:00:00");
        params.add("preparingEnd", "2024-03-27T23:59:59");
        ClassPathResource imageResource = new ClassPathResource("/images/itmo.jpeg");
        byte[] content = imageResource.getInputStream().readAllBytes();
        MockMultipartFile image = new MockMultipartFile("image", "itmo.jpeg", MediaType.IMAGE_JPEG_VALUE, content);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/activity")
                        .file(image)
                        .params(params)
                        .contentType("multipart/form-data"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("event-images").build());
        boolean isImageExists = isImageExist("3.jpeg");
        assertThat(isBucketExists).isTrue();
        assertThat(isImageExists).isTrue();
    }

    @Test
    void addPlaceNotFoundInvalidEvent() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("placeId", "10000000");
        params.add("start", "2024-03-28T09:00:00");
        params.add("end", "2024-03-28T18:00:00");
        params.add("title", "itmo-event");
        params.add("shortDescription", "This is a short description.");
        params.add("fullDescription", "This is a full description of the event.");
        params.add("format", "OFFLINE");
        params.add("status", "PUBLISHED");
        params.add("registrationStart", "2024-03-01T00:00:00");
        params.add("registrationEnd", "2024-03-25T23:59:59");
        params.add("parent", "1");
        params.add("participantLimit", "50");
        params.add("participantAgeLowest", "18");
        params.add("participantAgeHighest", "50");
        params.add("preparingStart", "2024-03-20T00:00:00");
        params.add("preparingEnd", "2024-03-27T23:59:59");
        ClassPathResource imageResource = new ClassPathResource("/images/itmo.jpeg");
        byte[] content = imageResource.getInputStream().readAllBytes();
        MockMultipartFile image = new MockMultipartFile("image", "itmo.jpeg", MediaType.IMAGE_JPEG_VALUE, content);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/activity")
                        .file(image)
                        .params(params)
                        .contentType("multipart/form-data"))
                .andExpect(status().is(404))
                .andExpect(content().string(containsString("Place not found")));;
    }

    @Test
    void addEmptyTitleInvalidEvent() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("placeId", "1");
        params.add("start", "2024-03-28T09:00:00");
        params.add("end", "2024-03-28T18:00:00");
        params.add("title", "");
        params.add("shortDescription", "This is a short description.");
        params.add("fullDescription", "This is a full description of the event.");
        params.add("format", "OFFLINE");
        params.add("status", "PUBLISHED");
        params.add("registrationStart", "2024-03-01T00:00:00");
        params.add("registrationEnd", "2024-03-25T23:59:59");
        params.add("parent", "1");
        params.add("participantLimit", "50");
        params.add("participantAgeLowest", "18");
        params.add("participantAgeHighest", "50");
        params.add("preparingStart", "2024-03-20T00:00:00");
        params.add("preparingEnd", "2024-03-27T23:59:59");
        ClassPathResource imageResource = new ClassPathResource("/images/itmo.jpeg");
        byte[] content = imageResource.getInputStream().readAllBytes();
        MockMultipartFile image = new MockMultipartFile("image", "itmo.jpeg", MediaType.IMAGE_JPEG_VALUE, content);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/activity")
                        .file(image)
                        .params(params)
                        .contentType("multipart/form-data"))
                .andExpect(status().is(400));
    }
    @Test
    void getAllEventsTest() throws Exception {
        mockMvc.perform(get("/api/events")
                        .param("page", "0")
                        .param("size", "15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getEventByIdTest() throws Exception {
        String expectedEventJson = """
                {
                  "id": 1,
                  "placeId": 1,
                  "start": "2024-03-30T21:32:23.536819",
                  "end": "2024-03-30T21:32:23.536819",
                  "title": "party",
                  "shortDescription": "cool party",
                  "fullDescription": "very cool party",
                  "format": "OFFLINE",
                  "status": "PUBLISHED",
                  "registrationStart": "2024-03-30T21:32:23.536819",
                  "registrationEnd": "2024-03-30T21:32:23.536819",
                  "parent": null,
                  "participantLimit": 10,
                  "participantAgeLowest": 5,
                  "participantAgeHighest": 7,
                  "preparingStart": "2024-03-30T21:32:23.536819",
                  "preparingEnd": "2024-03-30T21:32:23.536819"
                }""";

        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedEventJson));
    }

    @Test
    void updateEventTest() throws Exception {
        // add one event for updating later
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("placeId", "1");
        params.add("start", "2024-03-28T09:00:00");
        params.add("end", "2024-03-28T18:00:00");
        params.add("title", "itmo-event");
        params.add("shortDescription", "This is a short description.");
        params.add("fullDescription", "This is a full description of the event.");
        params.add("format", "OFFLINE");
        params.add("status", "PUBLISHED");
        params.add("registrationStart", "2024-03-01T00:00:00");
        params.add("registrationEnd", "2024-03-25T23:59:59");
        params.add("parent", "1");
        params.add("participantLimit", "50");
        params.add("participantAgeLowest", "18");
        params.add("participantAgeHighest", "50");
        params.add("preparingStart", "2024-03-20T00:00:00");
        params.add("preparingEnd", "2024-03-27T23:59:59");
        ClassPathResource imageResource = new ClassPathResource("/images/itmo.jpeg");
        byte[] content = imageResource.getInputStream().readAllBytes();
        MockMultipartFile image = new MockMultipartFile("image", "itmo.jpeg", MediaType.IMAGE_JPEG_VALUE, content);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/activity")
                        .file(image)
                        .params(params)
                        .contentType("multipart/form-data"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("event-images").build());
        boolean isObjectExists = isImageExist("3.jpeg");
        assertThat(isBucketExists).isTrue();
        assertThat(isObjectExists).isTrue();
        // update
        String expectedEventJson = """
                {
                  "id": 3,
                  "placeId": 2,
                  "start": "2024-04-02T14:00:00",
                  "end": "2024-04-02T16:00:00",
                  "title": "New updated test title",
                  "shortDescription": "Short Description",
                  "fullDescription": "Full Description",
                  "format": "ONLINE",
                  "status": "DRAFT",
                  "registrationStart": "2024-03-16T00:00:00",
                  "registrationEnd": "2024-03-31T23:59:59",
                  "participantLimit": 30,
                  "parent": 2,
                  "participantAgeLowest": 10,
                  "participantAgeHighest": 90,
                  "preparingStart": "2024-03-26T14:00:00",
                  "preparingEnd": "2024-03-31T14:00:00"
                }""";
        MultiValueMap<String, String> updatedParams = new LinkedMultiValueMap<>();
        updatedParams.add("placeId", "2");
        updatedParams.add("start", "2024-04-02T14:00:00");
        updatedParams.add("end", "2024-04-02T16:00:00");
        updatedParams.add("title", "New updated test title");
        updatedParams.add("shortDescription", "Short Description");
        updatedParams.add("fullDescription", "Full Description");
        updatedParams.add("format", "ONLINE");
        updatedParams.add("status", "DRAFT");
        updatedParams.add("registrationStart", "2024-03-16T00:00:00");
        updatedParams.add("registrationEnd", "2024-03-31T23:59:59");
        updatedParams.add("parent", "2");
        updatedParams.add("participantLimit", "30");
        updatedParams.add("participantAgeLowest", "10");
        updatedParams.add("participantAgeHighest", "90");
        updatedParams.add("preparingStart", "2024-03-26T14:00:00");
        updatedParams.add("preparingEnd", "2024-03-31T14:00:00");
        ClassPathResource updatedImageResource = new ClassPathResource("/images/itmo.png");
        byte[] updatedContent = updatedImageResource.getInputStream().readAllBytes();
        MockMultipartFile updatedImage = new MockMultipartFile("image", "itmo.png", MediaType.IMAGE_PNG_VALUE, updatedContent);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/events/3")
                        .file(updatedImage)
                        .params(updatedParams)
                        .contentType("multipart/form-data"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedEventJson));
        boolean isNewImageExists = isImageExist("3.png");
        boolean isOldImageExists = isImageExist("3.jpeg");
        assertThat(isNewImageExists).isTrue();
        assertThat(isOldImageExists).isFalse();
    }
}
