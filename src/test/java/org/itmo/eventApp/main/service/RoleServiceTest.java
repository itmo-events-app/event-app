package org.itmo.eventApp.main.service;

import org.assertj.core.util.Lists;
import org.itmo.eventApp.main.controller.AbstractTestContainers;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.dto.request.RoleRequest;
import org.itmo.eventapp.main.model.entity.Role;
import org.itmo.eventapp.main.model.entity.enums.RoleType;
import org.itmo.eventapp.main.service.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


// TODO - test assignSystemRole
// TODO - test revokeSystemRole


public class RoleServiceTest extends AbstractTestContainers {
    private static final List<String> basicRolesNames
            = Arrays.asList("Администратор", "Читатель", "Организатор", "Помощник");
    private static final List<String> fakeRolesNames
            = Arrays.asList("Фэйк_Роль_1", "Фэйк_Роль_2", "Фэйк_Роль_3", "Фэйк_Роль_4", "Фэйк_Роль_5");
    private static final int BASIC_ROLES_COUNT = 4;
    private static final int FAKE_ROLES_COUNT = 5;
    private static final int EVENT_ROLES_COUNT = 2 + 3;


    @Autowired
    private RoleService roleService;


    @Test
    @DisplayName("[createRole]-(Pos) Creating a new role")
    void createRoleTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        // ==========----  Execution  ----==========
        Role createdRole = roleService.createRole(roleRequest);

        // ==========---- Assertions  ----==========
        assertNotNull(createdRole,
                "Got NULL after calling createRole() but expected NEW role: %s".formatted(roleRequest.name()));
        assertAll(
                "Role returned by createRole() is NOT equal role-request: %s".formatted(roleRequest.name()),
                () -> assertEquals(roleRequest.name(), createdRole.getName()),
                () -> assertEquals(roleRequest.description(), createdRole.getDescription()),
                () -> assertEquals(RoleType.EVENT, createdRole.getType()),
                () -> assertEquals(privileges.size(), createdRole.getPrivileges().size())
        );
    }

    @Test
    @DisplayName("[createRole]-(Neg) Creating a new role using a NON-unique name")
    void createRoleNonUniNameTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        RoleRequest roleRequest = new RoleRequest(
                "Фэйк_Роль_1",
                "Фэйк_Описание_1",
                true,
                privileges);

        // ====----  Execution & Assertions ----====
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> roleService.createRole(roleRequest),
                        "NO excepting thrown after calling createRole() for NON-unique role name: %s".formatted(roleRequest.name()));
        assertEquals(ExceptionConst.ROLE_EXIST_MESSAGE,
                exception.getReason(),
                "Exception message does not match expected");
    }


    @Test
    @DisplayName("[findByName]-(Pos) Getting BASIC roles by name")
    void findBasicRoleByNameTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        for (String basicRoleName : basicRolesNames) {
            // ==========----  Execution  ----==========
            Role basicRole = roleService.findByName(basicRoleName);

            // ==========---- Assertions  ----==========
            assertNotNull(basicRole,
                    "Got NULL after calling findByName() for an EXISTING BASIC role: %s".formatted(basicRoleName));
            assertEquals(basicRoleName, basicRole.getName(),
                    "Role returned by findByName() is NOT equal ACTUAL BASIC role: %s".formatted(basicRoleName));
        }
    }

    @Test
    @DisplayName("[findByName]-(Pos) Getting FAKE roles by name")
    void findFakeRoleByNameTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        for (String fakeRoleName : fakeRolesNames) {
            // ==========----  Execution  ----==========
            Role fakeRole = roleService.findByName(fakeRoleName);

            // ==========---- Assertions  ----==========
            assertNotNull(fakeRole,
                    "Got NULL after calling findByName() for an EXISTING FAKE role: %s".formatted(fakeRoleName));
            assertEquals(fakeRoleName, fakeRole.getName(),
                    "Role returned by findByName() is NOT equal ACTUAL FAKE role: %s".formatted(fakeRoleName));
        }
    }

    @Test
    @DisplayName("[get(Basic)Role]-(Pos) Getting BASIC roles")
    void findBasicRolesTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        // ==========----  Execution  ----==========
        Role adminRole = roleService.getAdminRole();
        Role readerRole = roleService.getReaderRole();
        Role organizerRole = roleService.getOrganizerRole();
        Role assistantRole = roleService.getAssistantRole();

        // ==========---- Assertions  ----==========
        assertNotNull(adminRole, "Got NULL after calling getAdminRole()");
        assertEquals(basicRolesNames.get(0), adminRole.getName(),
                "Role returned by getAdminRole() is NOT equal actual ADMIN role");

        assertNotNull(readerRole, "Got NULL after calling getReaderRole()");
        assertEquals(basicRolesNames.get(1), readerRole.getName(),
                "Role returned by getReaderRole() is NOT equal actual READER role");

        assertNotNull(organizerRole, "Got NULL after calling getOrganizerRole()");
        assertEquals(basicRolesNames.get(2), organizerRole.getName(),
                "Role returned by getOrganizerRole() is NOT equal actual ORGANIZER role");

        assertNotNull(assistantRole, "Got NULL after calling getAssistantRole()");
        assertEquals(basicRolesNames.get(3), assistantRole.getName(),
                "Role returned by getAssistantRole() is NOT equal actual ASSISTANT role");
    }


    @Test
    @DisplayName("[editRole]-(Pos) Editing role")
    void editRoleTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        final int editingRoleId = BASIC_ROLES_COUNT + 1;  // id of first fake role : Фэйк_Роль_1

        // ==========----  Execution  ----==========
        Role editedRole = roleService.editRole(editingRoleId, roleRequest);

        // ==========---- Assertions  ----==========
        assertNotNull(editedRole,
                "Got NULL after calling editRole()");
        assertAll(
                "Role returned by editRole() is NOT equal role-request: %s".formatted(roleRequest.name()),
                () -> assertEquals(roleRequest.name(), editedRole.getName()),
                () -> assertEquals(roleRequest.description(), editedRole.getDescription()),
                () -> assertEquals(RoleType.EVENT, editedRole.getType()),
                () -> assertEquals(privileges.size(), editedRole.getPrivileges().size())
        );
    }

    @Test
    @DisplayName("[editRole]-(Neg) Editing role which is BASIC & can NOT be modified")
    void editBasicRoleTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        for (String basicRolesName : basicRolesNames) {
            Role basicRole = roleService.findByName(basicRolesName);

            final int editingBasicRoleId = basicRole.getId();

            // ====----  Execution & Assertions ----====
            ResponseStatusException exception =
                    assertThrows(ResponseStatusException.class,
                            () -> roleService.editRole(editingBasicRoleId, roleRequest),
                            "It is forbidden to modify the basic roles");
            assertEquals(ExceptionConst.ROLE_EDITING_FORBIDDEN_MESSAGE,
                    exception.getReason(),
                    "Exception message does not match expected");
        }
    }

    @Test
    @DisplayName("[editRole]-(Neg) Editing a NON-existing role")
    void editNonExistRoleTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        final int editingRoleID = BASIC_ROLES_COUNT + FAKE_ROLES_COUNT + 1;

        // ====----  Execution & Assertions ----====
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.editRole(editingRoleID, roleRequest),
                "It is not possible to edit a NON-existing role: %s".formatted(roleRequest.name()));
        assertTrue(exception.getMessage().contains("не найдена"),
                exception.getReason());
    }

    @Test
    @DisplayName("[editRole]-(Neg) Editing role using NON-unique name")
    void editRoleUsingNonUniqueNameTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        RoleRequest roleRequest = new RoleRequest(
                "Фэйк_Роль_2",
                "Фэйк_Описание",
                true,
                privileges);

        final int editingRoleId = BASIC_ROLES_COUNT + 1; // id of first fake role : Фэйк_Роль_1

        // ====----  Execution & Assertions ----====
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.editRole(editingRoleId, roleRequest),
                "Must not be able to edit role using NON-unique name: %s".formatted(roleRequest.name()));
        assertEquals(ExceptionConst.ROLE_EXIST_MESSAGE,
                exception.getReason(),
                "Exception message does not match expected");
    }

    @Test
    @DisplayName("[editRole]-(Pos) Editing role but keeping previous name")
    void editRoleKeepNameTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        RoleRequest roleRequest = new RoleRequest(
                "Фэйк_Роль_1",
                "Фэйк_Описание",
                true,
                privileges);

        final int editingRoleId = BASIC_ROLES_COUNT + 1; // id of first fake role : Фэйк_Роль_1

        // ==========----  Execution  ----==========
        Role editedRole = roleService.editRole(editingRoleId, roleRequest);

        // ==========---- Assertions  ----==========
        assertNotNull(editedRole,
                "Got NULL after calling editRole()");
        assertAll(
                "Role returned by editRole() is NOT equal role-request: %s".formatted(roleRequest.name()),
                () -> assertEquals(roleRequest.name(), editedRole.getName()),
                () -> assertEquals(roleRequest.description(), editedRole.getDescription()),
                () -> assertEquals(RoleType.EVENT, editedRole.getType()),
                () -> assertEquals(privileges.size(), editedRole.getPrivileges().size())
        );
    }


    @Test
    @DisplayName("[getAll] (Pos) Getting ALL roles")
    void getAllRolesTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        final int ROLES_COUNT = BASIC_ROLES_COUNT + FAKE_ROLES_COUNT;

        // ==========----  Execution  ----==========
        List<Role> allRoles = roleService.getAll();

        // ==========---- Assertions  ----==========
        assertNotNull(allRoles,
                "Got NULL after calling getAll()");
        assertFalse(allRoles.isEmpty(),
                "Got EMPTY list after calling getAll()");
        assertEquals(ROLES_COUNT, allRoles.size(),
                "Returned roles list size [%d] does not match expected [%d]".formatted(allRoles.size(), ROLES_COUNT));
    }


    @Test
    @DisplayName("[deleteRole]-(Pos) Deleting role")
    void deleteRoleTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        final int deletingRoleID = BASIC_ROLES_COUNT + FAKE_ROLES_COUNT; // id of last fake role : Фэйк_Роль_5

        // ==========----  Execution  ----==========
        roleService.deleteRole(deletingRoleID);

        // ==========---- Assertions  ----==========
        assertEquals(BASIC_ROLES_COUNT + FAKE_ROLES_COUNT - 1,
                roleService.getAll().size(),
                "Returned role list size should be less by 1 than originally");
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> roleService.findRoleById(deletingRoleID),
                        "Deleted role should NOT be accessible");
        assertTrue(exception.getMessage().contains("не найдена"),
                exception.getReason());
    }

    @Test
    @DisplayName("[deleteRole]-(Neg) Deleting role which is BASIC & can NOT be deleted")
    void deleteBasicRoleTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        for (String basicRolesName : basicRolesNames) {
            Role basicRole = roleService.findByName(basicRolesName);
            int basicRoleId = basicRole.getId();

            // ====----  Execution & Assertions ----====
            ResponseStatusException exception =
                    assertThrows(ResponseStatusException.class,
                            () -> roleService.deleteRole(basicRoleId),
                            "It is forbidden to delete the basic roles");
            assertEquals(ExceptionConst.ROLE_DELETING_FORBIDDEN_MESSAGE,
                    exception.getReason(),
                    "Exception message does not match expected");
        }
    }

    @Test
    @DisplayName("[deleteRole]-(Neg) Deleting fake role which is ALREADY ASSIGNED to user")
    void deleteRoleAssignedTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();
        insertUsersFilling();

        final int deletingRoleID = BASIC_ROLES_COUNT + 1; // id of first fake role : Фэйк_Роль_1

        // ====----  Execution & Assertions ----====
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> roleService.deleteRole(deletingRoleID),
                        "It is forbidden to delete assigned roles");
        assertEquals(ExceptionConst.USERS_WITH_ROLE_EXIST,
                exception.getReason(),
                "Exception message does not match expected");
    }


    @Test
    @DisplayName(("[findRoleById] (Pos) Getting BASIC roles by Id"))
    void findBasicRoleByIdTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        for (int i = 0; i < BASIC_ROLES_COUNT; i++) {
            int roleId = i + 1;

            // ==========----  Execution  ----==========
            Role basicRole = roleService.findRoleById(roleId);

            // ==========---- Assertions  ----==========
            assertNotNull(basicRole,
                    "Got NULL after calling findRoleById() for an EXISTING BASIC role: %s".formatted(basicRolesNames.get(i)));
            assertEquals(basicRolesNames.get(i), basicRole.getName(),
                    "Role returned by findRoleById() is NOT equal ACTUAL BASIC role: %s".formatted(basicRolesNames.get(i)));
        }
    }

    @Test
    @DisplayName(("[findRoleById] (Pos) Getting FAKE roles by Id"))
    void findFakeRoleByIdTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        for (int i = BASIC_ROLES_COUNT; i < BASIC_ROLES_COUNT + FAKE_ROLES_COUNT; i++) {
            int roleId = i + 1;

            // ==========----  Execution  ----==========
            Role fakeRole = roleService.findRoleById(roleId);

            // ==========---- Assertions  ----==========
            assertNotNull(fakeRole,
                    "Got NULL after calling findRoleById() for an EXISTING FAKE role: %s".formatted(fakeRolesNames.get(i - BASIC_ROLES_COUNT)));
            assertEquals(fakeRolesNames.get(i - BASIC_ROLES_COUNT), fakeRole.getName(),
                    "Role returned by findRoleById() is NOT equal ACTUAL FAKE role: %s".formatted(fakeRolesNames.get(i - BASIC_ROLES_COUNT)));
        }
    }

    @Test
    @DisplayName(("[findRoleById] (Neg) Getting NON-existing roles by Id"))
    void findNonExistRoleByIdTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        final int nonExistingRoleID = BASIC_ROLES_COUNT + FAKE_ROLES_COUNT + 1;

        // ====----  Execution & Assertions ----====
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> roleService.findRoleById(nonExistingRoleID),
                        "NON-existing role can not be accessed");
        assertTrue(exception.getMessage().contains("не найдена"),
                exception.getReason());
    }


    @Test
    @DisplayName("[getOrganizational] (Pos) Getting all event roles")
    void getEventRolesTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        // ==========----  Execution  ----==========
        List<Role> allEventRoles = roleService.getOrganizational();

        // ==========---- Assertions  ----==========
        assertNotNull(allEventRoles,
                "Got NULL after calling getOrganizational()");

        assertEquals(EVENT_ROLES_COUNT, allEventRoles.size(),
                "Returned event roles list size [%d] in NOT equal expected [%d]".formatted(allEventRoles.size(), EVENT_ROLES_COUNT));
    }

    @Test
    @DisplayName("[getOrganizational] (Neg) Getting event roles")
    void getNotCreatedEventRolesTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();

        // ==========----  Execution  ----==========
        List<Role> allEventRoles = roleService.getOrganizational();

        // ==========---- Assertions  ----==========
        assertNotNull(allEventRoles,
                "Got NULL after calling getOrganizational()");
        assertTrue(allEventRoles.isEmpty(),
                "Returned event roles list is NOT EMPTY but it IS EXPECTED");
    }


    @Test
    @DisplayName("[searchByName]-(Pos) Getting BASIC roles by name")
    void searchBasicRoleByNameTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        for (String basicRoleName : basicRolesNames) {
            // ==========----  Execution  ----==========
            List<Role> basicRoles = roleService.searchByName(basicRoleName);

            // ==========---- Assertions  ----==========
            assertNotNull(basicRoles,
                    "Got NULL after calling searchByName()");
            assertFalse(basicRoles.isEmpty(),
                    "Returned roles list IS EMPTY but expected size is %d".formatted(BASIC_ROLES_COUNT));
            basicRoles.forEach(
                    basicRole -> assertEquals(basicRoleName, basicRole.getName(),
                            "Returned roles name [%s] does NOT match expected: %s".formatted(basicRole.getName(), basicRoleName))
            );
        }
    }

    @Test
    @DisplayName("[searchByName]-(Pos) Getting FAKE roles by name")
    void searchFakeRoleByNameTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();
        insertBasicFilling();
        insertFakeFilling();

        for (String fakeRoleName : fakeRolesNames) {
            // ==========----  Execution  ----==========
            List<Role> fakeRoles = roleService.searchByName(fakeRoleName);

            // ==========---- Assertions  ----==========
            assertNotNull(fakeRoles,
                    "Got NULL after calling searchByName()");
            assertFalse(fakeRoles.isEmpty(),
                    "Returned roles list IS EMPTY but expected size is %d".formatted(FAKE_ROLES_COUNT));
            fakeRoles.forEach(
                    fakeRole -> assertEquals(fakeRoleName, fakeRole.getName(),
                            "Returned roles name [%s] does NOT match expected: %s".formatted(fakeRole.getName(), fakeRoleName))
            );
        }
    }

    @Test
    @DisplayName("[searchByName]-(Neg) Getting NON-existing roles by name")
    void searchNonExistByNameTest() {
        // ====---- Setting up precondition ----====
        cleanRolesPrivileges();

        String nonExistRoleName = "Новая_Фэйк_Роль";

        // ==========---- Assertions  ----==========
        List<Role> roles = roleService.searchByName(nonExistRoleName);

        // ==========---- Assertions  ----==========
        assertNotNull(roles,
                "Got NULL after calling searchByName()");
        assertTrue(roles.isEmpty(),
                "Returned roles list IS NOT EMPTY but it IS EXPECTED");
    }


    private void cleanRolesPrivileges() {
        executeSqlScript("/sql/clean_roles_privileges.sql");
    }

    private void insertBasicFilling() {
        executeSqlScript("/db/test-migration/V0_3__fill_tables_test.sql");
    }

    private void insertFakeFilling() {
        executeSqlScript("/sql/insert_roles.sql");
        executeSqlScript("/sql/insert_privileges.sql");
    }

    private void insertUsersFilling() {
        executeSqlScript("/sql/insert_user_4.sql");
    }
}
