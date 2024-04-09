package org.itmo.eventApp.main.controller;


import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.repository.PlaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlaceControllerTest extends AbstractTestContainers {
    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceControllerTest(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }


    @AfterEach
    public void cleanUp() {
        executeSqlScript("/sql/clean_tables.sql");
    }

    @Test
    void placeGetTest() throws Exception {
        executeSqlScript("/sql/insert_place.sql");

        String expectedJson = """
                {
                  "id": 1,
                  "name": "itmo place",
                  "format": "OFFLINE",
                  "address": "itmo university",
                  "room": "13",
                  "description": "this is itmo place, you can do whatever you want",
                  "latitude":11.11,
                  "longitude":22.22,
                  "renderInfo":"render_info"
                }
                """;

        mockMvc.perform(get("/api/places/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void placeGetAllOrFilteredTest() throws Exception {
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_place_2.sql");

        String expectedJson = """
                [
                    {
                        "id": 2,
                        "name": "itmo place 2",
                        "format": "ONLINE",
                        "address": "itmo university 2",
                        "room": "101",
                        "description": "this is itmo 2 place, you can do whatever you want",
                        "latitude": 33.33,
                        "longitude": 44.44,
                        "renderInfo":"render_info"
                    }
                ]
                """;

        mockMvc.perform(get("/api/places").param("name", "itmo place 2"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void placeGetInvalidIdTest() throws Exception {
        mockMvc.perform(get("/api/places/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("placeGet.id: Параметр id не может быть меньше 1!")));
    }

    @Test
    void placeAddTest() throws Exception {
        String name = "name";
        String address = "address";
        String format = "ONLINE";
        String description = "description";
        String room = "room";
        float longitude = 100.2f;
        float latitude = 50.3f;

        String taskJson = """
                {
                    "name": "name",
                    "address": "address",
                    "format": "ONLINE",
                    "description": "description",
                    "room": "room",
                    "longitude": 100.2,
                    "latitude": 50.3
                }
                """;

        mockMvc.perform(post("/api/places")
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("1")));

        Place place = placeRepository.findById(1).orElseThrow();
        Assertions.assertAll(
                () -> Assertions.assertEquals(name, place.getName()),
                () -> Assertions.assertEquals(address, place.getAddress()),
                () -> Assertions.assertEquals(format, place.getFormat().toString()),
                () -> Assertions.assertEquals(room, place.getRoom()),
                () -> Assertions.assertEquals(description, place.getDescription()),
                () -> Assertions.assertEquals(latitude, place.getLatitude()),
                () -> Assertions.assertEquals(longitude, place.getLongitude()),
                () -> Assertions.assertNull(place.getRenderInfo())
        );
    }

    @Test
    void placeAddInvalidTest() throws Exception {
        String taskJson = """
                {
                  "longitude": 10000
                }""";

        mockMvc.perform(post("/api/tasks")
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void taskEditTest() throws Exception {
        executeSqlScript("/sql/insert_place.sql");
        String name = "name";
        String address = "address";
        String format = "ONLINE";
        String description = "description";
        String room = "room";
        float longitude = 100.2f;
        float latitude = 50.3f;

        String taskJson = """
                {
                    "name": "name",
                    "address": "address",
                    "format": "ONLINE",
                    "description": "description",
                    "room": "room",
                    "longitude": 100.2,
                    "latitude": 50.3
                }
                """;

        mockMvc.perform(put("/api/places/1")
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Place place = placeRepository.findById(1).orElseThrow();
        Assertions.assertAll(
                () -> Assertions.assertEquals(name, place.getName()),
                () -> Assertions.assertEquals(address, place.getAddress()),
                () -> Assertions.assertEquals(format, place.getFormat().toString()),
                () -> Assertions.assertEquals(room, place.getRoom()),
                () -> Assertions.assertEquals(description, place.getDescription()),
                () -> Assertions.assertEquals(latitude, place.getLatitude()),
                () -> Assertions.assertEquals(longitude, place.getLongitude()),
                () -> Assertions.assertNull(place.getRenderInfo())
        );
    }

    @Test
    void placeEditInvalidTest() throws Exception {
        executeSqlScript("/sql/insert_place.sql");
        Place notEdited = placeRepository.findById(1).orElseThrow();
        String taskJson = """
                {
                    "longitude": 100200.2,
                }
                """;

        mockMvc.perform(put("/api/places/1")
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Place place = placeRepository.findById(1).orElseThrow();
        Assertions.assertAll(
                () -> Assertions.assertEquals(notEdited.getName(), place.getName()),
                () -> Assertions.assertEquals(notEdited.getAddress(), place.getAddress()),
                () -> Assertions.assertEquals(notEdited.getFormat().toString(), place.getFormat().toString()),
                () -> Assertions.assertEquals(notEdited.getRoom(), place.getRoom()),
                () -> Assertions.assertEquals(notEdited.getDescription(), place.getDescription()),
                () -> Assertions.assertEquals(notEdited.getLatitude(), place.getLatitude()),
                () -> Assertions.assertEquals(notEdited.getLongitude(), place.getLongitude())
        );
    }

    @Test
    void placeDeleteTest() throws Exception {
        executeSqlScript("/sql/insert_place.sql");

        Assertions.assertTrue(placeRepository.findById(1).isPresent());

        mockMvc.perform(delete("/api/places/1"))
                .andExpect(status().isNoContent());

        Assertions.assertFalse(placeRepository.findById(1).isPresent());
    }
}