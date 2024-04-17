package org.itmo.eventApp.main.controller;


import io.minio.BucketExistsArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.repository.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest extends AbstractTestContainers {
    private boolean isImageExist(String imageName) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                .bucket("event-images")
                .object(imageName).build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private final EventRepository eventRepository;

    @Autowired
    public EventControllerTest(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private void setUpEventData() {
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_event_role.sql");
    }

    private void setUpEventsByRoleData() {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_2.sql");
        executeSqlScript("/sql/insert_event_3.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
    }

    private UserLoginInfo getUserLoginInfo() {
        UserLoginInfo userDetails = new UserLoginInfo();
        userDetails.setLogin("test_mail@itmo.ru");
        User dummyUser = new User();
        dummyUser.setId(1);
        userDetails.setUser(dummyUser);
        return userDetails;
    }

    private void setUpActivityData() {
        executeSqlScript("/sql/insert_activity.sql");
    }

    private void setUpUserData() {
        executeSqlScript("/sql/insert_user.sql");
    }

    @Test
    void addProperEvent() throws Exception {
        setUpEventData();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("placeId", "1");
        params.add("startDate", "2030-03-28T09:00:00");
        params.add("endDate", "2030-03-28T18:00:00");
        params.add("title", "itmo-event");
        params.add("shortDescription", "This is a short description.");
        params.add("fullDescription", "This is a full description of the event.");
        params.add("format", "OFFLINE");
        params.add("status", "PUBLISHED");
        params.add("registrationStart", "2030-03-01T00:00:00");
        params.add("registrationEnd", "2030-03-25T23:59:59");
        params.add("parent", "1");
        params.add("participantLimit", "50");
        params.add("participantAgeLowest", "18");
        params.add("participantAgeHighest", "50");
        params.add("preparingStart", "2030-03-20T00:00:00");
        params.add("preparingEnd", "2030-03-27T23:59:59");
        ClassPathResource imageResource = new ClassPathResource("/images/itmo.jpeg");
        byte[] content = imageResource.getInputStream().readAllBytes();
        MockMultipartFile image = new MockMultipartFile("image", "itmo.jpeg", MediaType.IMAGE_JPEG_VALUE, content);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/activity")
                .file(image)
                .params(params)
                .contentType("multipart/form-data")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isCreated())
            .andExpect(content().string("3"));
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("event-images").build());
        boolean isImageExists = isImageExist("3.jpeg");
        assertThat(isBucketExists).isTrue();
        assertThat(isImageExists).isTrue();
        assertThat(eventRepository.findById(3).isPresent()).isTrue();
    }

    @Test
    void addPlaceNotFoundInvalidEvent() throws Exception {
        setUpEventData();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("placeId", "10000000");
        params.add("startDate", "2030-03-28T09:00:00");
        params.add("endDate", "2030-03-28T18:00:00");
        params.add("title", "itmo-event");
        params.add("shortDescription", "This is a short description.");
        params.add("fullDescription", "This is a full description of the event.");
        params.add("format", "OFFLINE");
        params.add("status", "PUBLISHED");
        params.add("registrationStart", "2030-03-01T00:00:00");
        params.add("registrationEnd", "2030-03-25T23:59:59");
        params.add("parent", "1");
        params.add("participantLimit", "50");
        params.add("participantAgeLowest", "18");
        params.add("participantAgeHighest", "50");
        params.add("preparingStart", "2030-03-20T00:00:00");
        params.add("preparingEnd", "2030-03-27T23:59:59");
        ClassPathResource imageResource = new ClassPathResource("/images/itmo.jpeg");
        byte[] content = imageResource.getInputStream().readAllBytes();
        MockMultipartFile image = new MockMultipartFile("image", "itmo.jpeg", MediaType.IMAGE_JPEG_VALUE, content);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/activity")
                .file(image)
                .params(params)
                .contentType("multipart/form-data")
                .with(user(getUserLoginInfo())))
            .andExpect(status().is(404))
            .andExpect(content().string(containsString("Площадка не найдена")));
        assertThat(eventRepository.findById(3).isEmpty()).isTrue();
    }

    @Test
    void addEmptyTitleInvalidEvent() throws Exception {
        setUpEventData();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("placeId", "1");
        params.add("startDate", "2030-03-28T09:00:00");
        params.add("endDate", "2030-03-28T18:00:00");
        params.add("title", "");
        params.add("shortDescription", "This is a short description.");
        params.add("fullDescription", "This is a full description of the event.");
        params.add("format", "OFFLINE");
        params.add("status", "PUBLISHED");
        params.add("registrationStart", "2030-03-01T00:00:00");
        params.add("registrationEnd", "2030-03-25T23:59:59");
        params.add("parent", "1");
        params.add("participantLimit", "50");
        params.add("participantAgeLowest", "18");
        params.add("participantAgeHighest", "50");
        params.add("preparingStart", "2030-03-20T00:00:00");
        params.add("preparingEnd", "2030-03-27T23:59:59");
        ClassPathResource imageResource = new ClassPathResource("/images/itmo.jpeg");
        byte[] content = imageResource.getInputStream().readAllBytes();
        MockMultipartFile image = new MockMultipartFile("image", "itmo.jpeg", MediaType.IMAGE_JPEG_VALUE, content);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/activity")
                .file(image)
                .params(params)
                .contentType("multipart/form-data")
                .with(user(getUserLoginInfo())))
            .andExpect(status().is(400));
        assertThat(eventRepository.findById(3).isEmpty()).isTrue();
    }

    @Test
    void getAllOrFilteredEventsTest() throws Exception {
        setUpEventData();
        mockMvc.perform(get("/api/events")
                .param("title", "party")
                .param("format", "OFFLINE")
                .param("status", "PUBLISHED")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.total").exists())
            .andExpect(jsonPath("$.total").isNumber())
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items[0].title").value("party"))
            .andExpect(jsonPath("$.items[0].format").value("OFFLINE"))
            .andExpect(jsonPath("$.items[0].status").value("PUBLISHED"));
    }

    @Test
    void filterActivityTest() throws Exception {
        setUpEventData();
        setUpActivityData();
        mockMvc.perform(get("/api/events")
                .param("parentId", "1")
                .param("format", "OFFLINE")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.total").exists())
            .andExpect(jsonPath("$.total").isNumber())
            .andExpect(jsonPath("$.items").isNotEmpty())
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items[0].title").value("partys activity"))
            .andExpect(jsonPath("$.items[0].format").value("OFFLINE"))
            .andExpect(jsonPath("$.items[0].status").value("PUBLISHED"));
    }

    @Test
    void doNotGetActivityInEventFilteringTest() throws Exception {
        setUpEventData();
        setUpActivityData();
        mockMvc.perform(get("/api/events")
                .param("format", "OFFLINE")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.total").exists())
            .andExpect(jsonPath("$.total").isNumber())
            // Two activities from setUpEventData;
            .andExpect(jsonPath("$.total").value(2))
            .andExpect(jsonPath("$.items").isNotEmpty())
            .andExpect(jsonPath("$.items").isArray());

    }

    @Test
    void getAllEventsTest() throws Exception {
        setUpEventData();
        mockMvc.perform(get("/api/events")
                .param("page", "0")
                .param("size", "15")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.total").exists())
            .andExpect(jsonPath("$.total").isNumber())
            .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    void addProperEventByOrganizer() throws Exception {
        setUpUserData();
        String eventJson = """
            {
                "userId": 1,
                "title": "test event"
            }""";
        mockMvc.perform(
                post("/api/events")
                    .content(eventJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user(getUserLoginInfo()))
            )
            .andExpect(status().is(201));
        assertThat(eventRepository.findById(1).isPresent()).isTrue();
        Event event = eventRepository.findById(1).get();
        assertThat(event.getTitle().equals("test event")).isTrue();
    }

    @Test
    void addEventByOrganizerUserNotFound() throws Exception {
        String eventJson = """
            {
                "userId": 42,
                "title": "test event"
            }""";
        mockMvc.perform(
                post("/api/events")
                    .content(eventJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user(getUserLoginInfo()))
            )
            .andExpect(status().is(404))
            .andExpect(content().string(containsString("Пользователь не найден")));
        assertThat(eventRepository.findById(1).isEmpty()).isTrue();
    }

    @Test
    void addEventByOrganizerNotNull() throws Exception {
        String eventJson = """
            {
                "userId": 1,
            }""";
        mockMvc.perform(
                post("/api/events")
                    .content(eventJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user(getUserLoginInfo()))
            )
            .andExpect(status().is(400));

        eventJson = """
            {
                "title": "test event"
            }""";
        mockMvc.perform(
                post("/api/events")
                    .content(eventJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user(getUserLoginInfo()))
            )
            .andExpect(status().is(400));
        assertThat(eventRepository.findById(1).isEmpty()).isTrue();
    }

    @Test
    void getEventByIdTest() throws Exception {
        setUpEventData();
        String expectedEventJson = """
            {
              "id": 1,
              "placeId": 1,
              "startDate": "2100-03-30T21:32:23.536819",
              "endDate": "2101-03-30T21:32:23.536819",
              "title": "party",
              "shortDescription": "cool party",
              "fullDescription": "very cool party",
              "format": "OFFLINE",
              "status": "PUBLISHED",
              "registrationStart": "2100-03-30T21:32:23.536819",
              "registrationEnd": "2101-03-30T21:32:23.536819",
              "participantLimit": 10,
              "participantAgeLowest": 5,
              "participantAgeHighest": 7,
              "preparingStart": "2100-03-30T21:32:23.536819",
              "preparingEnd": "2101-03-30T21:32:23.536819"
            }""";

        mockMvc.perform(get("/api/events/1")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedEventJson));
    }

    @Test
    void updateEventTest() throws Exception {
        setUpEventData();
        UserLoginInfo userLoginInfo = getUserLoginInfo();
        // add one event for updating later
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("placeId", "1");
        params.add("startDate", "2030-03-28T09:00:00");
        params.add("endDate", "2030-03-28T18:00:00");
        params.add("title", "itmo-event");
        params.add("shortDescription", "This is a short description.");
        params.add("fullDescription", "This is a full description of the event.");
        params.add("format", "OFFLINE");
        params.add("status", "PUBLISHED");
        params.add("registrationStart", "2030-03-01T00:00:00");
        params.add("registrationEnd", "2030-03-25T23:59:59");
        params.add("parent", "1");
        params.add("participantLimit", "50");
        params.add("participantAgeLowest", "18");
        params.add("participantAgeHighest", "50");
        params.add("preparingStart", "2030-03-20T00:00:00");
        params.add("preparingEnd", "2030-03-27T23:59:59");
        ClassPathResource imageResource = new ClassPathResource("/images/itmo.jpeg");
        byte[] content = imageResource.getInputStream().readAllBytes();
        MockMultipartFile image = new MockMultipartFile("image", "itmo.jpeg", MediaType.IMAGE_JPEG_VALUE, content);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/activity")
                .file(image)
                .params(params)
                .contentType("multipart/form-data")
                .with(user(userLoginInfo)))
            .andExpect(status().isCreated())
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
              "startDate": "2030-04-02T14:00:00",
              "endDate": "2030-04-02T16:00:00",
              "title": "New updated test title",
              "shortDescription": "Short Description",
              "fullDescription": "Full Description",
              "format": "ONLINE",
              "status": "DRAFT",
              "registrationStart": "2030-03-16T00:00:00",
              "registrationEnd": "2030-03-31T23:59:59",
              "participantLimit": 30,
              "parent": 2,
              "participantAgeLowest": 10,
              "participantAgeHighest": 90,
              "preparingStart": "2030-03-26T14:00:00",
              "preparingEnd": "2030-03-31T14:00:00"
            }""";
        MultiValueMap<String, String> updatedParams = new LinkedMultiValueMap<>();
        updatedParams.add("placeId", "2");
        updatedParams.add("startDate", "2030-04-02T14:00:00");
        updatedParams.add("endDate", "2030-04-02T16:00:00");
        updatedParams.add("title", "New updated test title");
        updatedParams.add("shortDescription", "Short Description");
        updatedParams.add("fullDescription", "Full Description");
        updatedParams.add("format", "ONLINE");
        updatedParams.add("status", "DRAFT");
        updatedParams.add("registrationStart", "2030-03-16T00:00:00");
        updatedParams.add("registrationEnd", "2030-03-31T23:59:59");
        updatedParams.add("parent", "2");
        updatedParams.add("participantLimit", "30");
        updatedParams.add("participantAgeLowest", "10");
        updatedParams.add("participantAgeHighest", "90");
        updatedParams.add("preparingStart", "2030-03-26T14:00:00");
        updatedParams.add("preparingEnd", "2030-03-31T14:00:00");
        ClassPathResource updatedImageResource = new ClassPathResource("/images/itmo.png");
        byte[] updatedContent = updatedImageResource.getInputStream().readAllBytes();
        MockMultipartFile updatedImage = new MockMultipartFile("image", "itmo.png", MediaType.IMAGE_PNG_VALUE, updatedContent);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/events/3")
                .file(updatedImage)
                .params(updatedParams)
                .contentType("multipart/form-data")
                .with(user(userLoginInfo)))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedEventJson));
        boolean isNewImageExists = isImageExist("3.png");
        boolean isOldImageExists = isImageExist("3.jpeg");
        assertThat(isNewImageExists).isTrue();
        assertThat(isOldImageExists).isFalse();
    }


    @Test
    void deleteEventByIdTest() throws Exception {
        setUpEventData();
        UserLoginInfo userLoginInfo = getUserLoginInfo();
        // add one event with image for updating later
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("placeId", "1");
        params.add("startDate", "2030-03-28T09:00:00");
        params.add("endDate", "2030-03-28T18:00:00");
        params.add("title", "itmo-event");
        params.add("shortDescription", "This is a short description.");
        params.add("fullDescription", "This is a full description of the event.");
        params.add("format", "OFFLINE");
        params.add("status", "PUBLISHED");
        params.add("registrationStart", "2030-03-01T00:00:00");
        params.add("registrationEnd", "2030-03-25T23:59:59");
        params.add("parent", "1");
        params.add("participantLimit", "50");
        params.add("participantAgeLowest", "18");
        params.add("participantAgeHighest", "50");
        params.add("preparingStart", "2030-03-20T00:00:00");
        params.add("preparingEnd", "2030-03-27T23:59:59");
        ClassPathResource imageResource = new ClassPathResource("/images/itmo.jpeg");
        byte[] content = imageResource.getInputStream().readAllBytes();
        MockMultipartFile image = new MockMultipartFile("image", "itmo.jpeg", MediaType.IMAGE_JPEG_VALUE, content);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/activity")
                        .file(image)
                        .params(params)
                        .contentType("multipart/form-data")
                        .with(user(userLoginInfo)))
                .andExpect(status().isCreated())
                .andExpect(content().string("3"));
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("event-images").build());
        boolean isObjectExists = isImageExist("3.jpeg");
        assertThat(isBucketExists).isTrue();
        assertThat(isObjectExists).isTrue();
        mockMvc.perform(delete("/api/events/3")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isNoContent());
        Optional<Event> deletedEvent = eventRepository.findById(3);
        Assertions.assertFalse(deletedEvent.isPresent());
        isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("event-images").build());
        isObjectExists = isImageExist("3.jpeg");
        assertThat(isBucketExists).isTrue();
        assertThat(isObjectExists).isFalse();
    }

    @Test
    void getUsersHavingRolesByEventSimple() throws Exception {
        setUpEventData();
        mockMvc.perform(
                get("/api/events/1/organizers")
                    .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getUsersHavingRolesByEvent() throws Exception {
        setUpUserData();
        String eventJson = """
            {
                "userId": 1,
                "title": "test event"
            }""";
        mockMvc.perform(
            post("/api/events")
                .content(eventJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(getUserLoginInfo()))
        );

        String expectedJson = """
            [{"id":1,"name":"test","surname":"user","login":"test_mail@itmo.ru","roleName":"Организатор"}]
            """;
        mockMvc.perform(
                get("/api/events/1/organizers")
                    .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(content().json(expectedJson));
    }

    @Test
    void copyEventTest() throws Exception {
        setUpEventData();
        UserLoginInfo userLoginInfo = getUserLoginInfo();
        // add one event for updating later
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("placeId", "1");
        params.add("startDate", "2030-03-28T09:00:00");
        params.add("endDate", "2030-03-28T18:00:00");
        params.add("title", "itmo-event");
        params.add("shortDescription", "This is a short description.");
        params.add("fullDescription", "This is a full description of the event.");
        params.add("format", "OFFLINE");
        params.add("status", "PUBLISHED");
        params.add("registrationStart", "2030-03-01T00:00:00");
        params.add("registrationEnd", "2030-03-25T23:59:59");
        params.add("parent", "1");
        params.add("participantLimit", "50");
        params.add("participantAgeLowest", "18");
        params.add("participantAgeHighest", "50");
        params.add("preparingStart", "2030-03-20T00:00:00");
        params.add("preparingEnd", "2030-03-27T23:59:59");
        ClassPathResource imageResource = new ClassPathResource("/images/itmo.jpeg");
        byte[] content = imageResource.getInputStream().readAllBytes();
        MockMultipartFile image = new MockMultipartFile("image", "itmo.jpeg", MediaType.IMAGE_JPEG_VALUE, content);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/activity")
                .file(image)
                .params(params)
                .contentType("multipart/form-data")
                .with(user(userLoginInfo)))
            .andExpect(status().isCreated())
            .andExpect(content().string("3"));
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("event-images").build());
        boolean isObjectExists = isImageExist("3.jpeg");
        assertThat(isBucketExists).isTrue();
        assertThat(isObjectExists).isTrue();
        // copy
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/events/3/copy")
                .with(user(userLoginInfo)))
            .andExpect(status().isCreated())
            .andExpect(content().string("4"));
        boolean isNewImageExists = isImageExist("4.jpeg");
        assertThat(isNewImageExists).isTrue();
        assertThat(eventRepository.findById(4).isPresent()).isTrue();
    }

    @Test
    @WithMockUser(username = "test_mail@test_mail.com")
    void getEventsByRoleTest() throws Exception {
        setUpEventsByRoleData();

        mockMvc.perform(get("/api/roles/3/events")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[0].title").value("party"))
            .andExpect(jsonPath("$[0].shortDescription").value("cool party"))
            .andExpect(jsonPath("$[0].format").value("OFFLINE"))
            .andExpect(jsonPath("$[0].status").value("PUBLISHED"))
            .andDo(print());
    }

    @Test
    @WithMockUser(username = "test_mail@test_mail.com")
    void getEventsByRoleTestEmptyArray() throws Exception {
        setUpEventsByRoleData();

        mockMvc.perform(get("/api/roles/10/events")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty())
            .andDo(print());
    }
}
