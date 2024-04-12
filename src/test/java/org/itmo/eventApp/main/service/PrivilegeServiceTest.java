package org.itmo.eventApp.main.service;

import org.itmo.eventApp.main.controller.AbstractTestContainers;
import org.itmo.eventapp.main.model.entity.Privilege;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeType;
import org.itmo.eventapp.main.service.PrivilegeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.itmo.eventapp.main.model.entity.enums.PrivilegeName.*;
import static org.junit.jupiter.api.Assertions.*;

public class PrivilegeServiceTest extends AbstractTestContainers {
    // ASSIGN_ORGANIZER_ROLE is both SYSTEM & EVENT type privilege
    private static final List<PrivilegeName> basicSystemPrivileges =
            Arrays.asList(APPROVE_REGISTRATION_REQUEST, REJECT_REGISTRATION_REQUEST, MODIFY_PROFILE_DATA,
                    VIEW_OTHER_USERS_PROFILE, VIEW_ALL_EVENTS, SEARCH_EVENTS_AND_ACTIVITIES, CREATE_EVENT,
                    VIEW_EVENT_PLACE, VIEW_ROUTE_BETWEEN_ROOMS, REVOKE_ORGANIZER_ROLE, CREATE_EVENT_VENUE,
                    DELETE_EVENT_VENUE, EDIT_EVENT_VENUE, CREATE_ROLE, DELETE_ROLE, EDIT_ROLE,
                    ASSIGN_SYSTEM_ROLE, REVOKE_SYSTEM_ROLE);
    private static final List<PrivilegeName> basicEventPrivileges =
            Arrays.asList(ASSIGN_ORGANIZER_ROLE, EDIT_EVENT_INFO, ASSIGN_ASSISTANT_ROLE, REVOKE_ASSISTANT_ROLE,
                    VIEW_ORGANIZER_USERS, VIEW_ASSISTANT_USERS, CREATE_EVENT_ACTIVITIES, DELETE_EVENT_ACTIVITIES,
                    EDIT_EVENT_ACTIVITIES, VIEW_EVENT_ACTIVITIES, CREATE_TASK, DELETE_TASK, EDIT_TASK,
                    CHANGE_TASK_STATUS, ASSIGN_TASK_EXECUTOR, REPLACE_TASK_EXECUTOR, DELETE_TASK_EXECUTOR,
                    ASSIGN_ORGANIZATIONAL_ROLE, REVOKE_ORGANIZATIONAL_ROLE, VIEW_ALL_EVENT_TASKS,
                    CHANGE_ASSIGNED_TASK_STATUS, ASSIGN_SELF_AS_TASK_EXECUTOR, DECLINE_TASK_EXECUTION,
                    IMPORT_PARTICIPANT_LIST_XLSX, EXPORT_PARTICIPANT_LIST_XLSX, WORK_WITH_PARTICIPANT_LIST);

    final static int SYSTEM_PRIVILEGE_COUNT = basicSystemPrivileges.size();
    final static int EVENT_PRIVILEGE_COUNT = basicEventPrivileges.size();

    final static int PRIVILEGE_COUNT = basicSystemPrivileges.size() + basicEventPrivileges.size();


    @Autowired
    private PrivilegeService privilegeService;


    @Test
    @DisplayName("[getAll]-(Pos) Getting all privileges")
    void getAllPrivilegesTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();

        // ==========----  Execution  ----==========
        List<Privilege> privileges = privilegeService.getPrivileges(null);

        // ==========---- Assertions  ----==========
        assertNotNull(privileges,
                "Got NULL after calling getAll()");
        assertFalse(privileges.isEmpty(),
                "Got EMPTY list after calling getAll()");
        assertEquals(PRIVILEGE_COUNT, privileges.size(),
                "Returned privileges list size [%d] does not match expected [%d]".formatted(privileges.size(), PRIVILEGE_COUNT));
    }

    @Test
    @DisplayName("[getAll]-(Neg) Getting all privileges which are NOT created")
    void getAllPrivilegesNotCreatedTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();

        // ==========----  Execution  ----==========
        List<Privilege> privileges = privilegeService.getPrivileges(null);

        // ==========---- Assertions  ----==========
        assertNotNull(privileges,
                "Got NULL after calling getAll()");
        assertTrue(privileges.isEmpty(),
                "Returned privileges list size is [%d] but expected to be empty".formatted(privileges.size()));
    }


    @Test
    @DisplayName("[findById]-(Pos) Getting privileges by Id")
    void findPrivilegeByIdTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();

        for (int i = 0; i < PRIVILEGE_COUNT; i++) {
            int privilegeId = i + 1;

            // ==========----  Execution  ----==========
            Privilege privilege = privilegeService.findById(privilegeId);

            // ==========---- Assertions  ----==========
            assertNotNull(privilege,
                    "Got NULL after calling findById() for an EXISTING privilege id: %d"
                            .formatted(privilegeId));
            assertEquals(privilegeId, privilege.getId(),
                    "Returned privileges id [%d] does not match expected [%d]"
                            .formatted(privilege.getId(), privilegeId));
        }
    }

    @Test
    @DisplayName("[findById]-(Neg) Getting NON-existing privileges by Id")
    void findNonExistPrivilegeByIdTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();

        final int nonExistPrivilegeId = PRIVILEGE_COUNT + 1;

        // ====----  Execution & Assertions ----====
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> privilegeService.findById(nonExistPrivilegeId),
                        "NON-existing role can not be accessed");
        assertTrue(exception.getMessage().contains("не найдена"),
                exception.getReason());
    }


    @Test
    @DisplayName("[getPrivilegeByType]-(Pos) Getting SYSTEM privileges by type")
    void getSystemPrivilegeByTypeTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();

        // ==========----  Execution  ----==========
        List<Privilege> systemPrivileges = privilegeService.getPrivileges(PrivilegeType.SYSTEM);

        // ==========---- Assertions  ----==========
        assertNotNull(systemPrivileges,
                "Got NULL after calling getPrivilegeByType()");
        assertEquals(SYSTEM_PRIVILEGE_COUNT, systemPrivileges.size(),
                "Returned SYSTEM privileges list size [%d] in NOT equal expected [%d]"
                        .formatted(systemPrivileges.size(), SYSTEM_PRIVILEGE_COUNT));
    }

    @Test
    @DisplayName("[getPrivilegeByType]-(Pos) Getting EVENT privileges by type")
    void getEventPrivilegeByTypeTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();

        // ==========----  Execution  ----==========
        List<Privilege> eventPrivileges = privilegeService.getPrivileges(PrivilegeType.EVENT);

        // ==========---- Assertions  ----==========
        assertNotNull(eventPrivileges,
                "Got NULL after calling getPrivilegeByType()");
        assertEquals(EVENT_PRIVILEGE_COUNT, eventPrivileges.size(),
                "Returned EVENT privileges list size [%d] in NOT equal expected [%d]"
                        .formatted(eventPrivileges.size(), EVENT_PRIVILEGE_COUNT));
    }

    @Test
    @DisplayName("[getNotCreatedPrivilegeByTypeTest]-(Neg) Getting privileges by type which are NOT created")
    void getNotCreatedPrivilegeByTypeTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();

        // ==========----  Execution  ----==========
        List<Privilege> systemPrivileges = privilegeService.getPrivileges(PrivilegeType.SYSTEM);
        List<Privilege> eventPrivileges = privilegeService.getPrivileges(PrivilegeType.EVENT);

        // ==========---- Assertions  ----==========
        assertNotNull(systemPrivileges,
                "Got NULL after calling getPrivilegeByType() for type %s".formatted(PrivilegeType.SYSTEM));
        assertNotNull(eventPrivileges,
                "Got NULL after calling getPrivilegeByType() for type %s".formatted(PrivilegeType.EVENT));

        assertTrue(systemPrivileges.isEmpty(),
                "Returned SYSTEM privilege list is NOT EMPTY but it IS EXPECTED");
        assertTrue(eventPrivileges.isEmpty(),
                "Returned EVENT privilege list is NOT EMPTY but it IS EXPECTED");
    }


    private void cleanRolesPrivileges() {
        executeSqlScript("/sql/clean_roles_privileges.sql");
    }

    private void insertBasicFilling() {
        executeSqlScript("/db/test-migration/V0_3__fill_tables_test.sql");
    }
}
