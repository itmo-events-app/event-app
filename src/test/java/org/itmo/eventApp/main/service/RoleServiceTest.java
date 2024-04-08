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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


// TODO - test assignSystemRole
// TODO - test revokeSystemRole

public class RoleServiceTest extends AbstractTestContainers {

    @Autowired
    private RoleService roleService;


    private final List<String> basicRolesNames
            = Arrays.asList("Администратор", "Читатель", "Организатор", "Помощник");


    @Test
    @DisplayName("[createRole]-(Neg) Creating a new role using a non-unique name")
    void createRoleNonUniNameTest() {
        insertFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        RoleRequest roleRequest = new RoleRequest(
                "Фэйк_Роль_1",
                "Фэйк_Описание_1",
                true,
                privileges);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.createRole(roleRequest));

        assertEquals(ExceptionConst.ROLE_EXIST_MESSAGE,
                exception.getReason());
    }

    @Test
    @DisplayName("[createRole]-(Pos) Creating a new role using unique name")
    void createRoleUniNameTest() {
        insertFilling();

        List<Integer> privileges = new ArrayList<>();
//        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4); // TODO solve stackOverflow problem

        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        Role createdRole = roleService.createRole(roleRequest);

        assertNotNull(createdRole);
        assertAll(
                () -> assertEquals(roleRequest.name(), createdRole.getName()),
                () -> assertEquals(roleRequest.description(), createdRole.getDescription()),
                () -> assertEquals(RoleType.EVENT, createdRole.getType()),
                () -> assertEquals(privileges.size(), createdRole.getPrivileges().size())
        );
    }

    @Test
    @DisplayName("[findByName]-(Pos) Getting basic roles")
    void findBasicRolesTest() {
        insertBasicFilling();

        Role adminRole = roleService.getAdminRole();
        assertNotNull(adminRole);
        assertEquals(basicRolesNames.get(0), adminRole.getName());

        Role readerRole = roleService.getReaderRole();
        assertNotNull(readerRole);
        assertEquals(basicRolesNames.get(1), readerRole.getName());

        Role organizerRole = roleService.getOrganizerRole();
        assertNotNull(organizerRole);
        assertEquals(basicRolesNames.get(2), organizerRole.getName());

        Role assistantRole = roleService.getAssistantRole();
        assertNotNull(assistantRole);
        assertEquals(basicRolesNames.get(3), assistantRole.getName());
    }

    @Test
    @DisplayName("[editRole]-(Neg) Editing basic role")
    void editBasicRoleTest() {
        insertBasicFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        for (String basicRolesName : basicRolesNames) {
            Role basicRole = roleService.findByName(basicRolesName);

            int basicRoleId = basicRole.getId();

            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class,
                    () -> roleService.editRole(basicRoleId, roleRequest),
                    "It is forbidden to modify the basic roles");

            assertEquals(ExceptionConst.ROLE_EDITING_FORBIDDEN_MESSAGE,
                    exception.getReason());
        }
    }

    @Test
    @DisplayName("[editRole]-(Neg) Editing a non existent role")
    void editNonExistRoleTest() {
        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
//        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4); // TODO solve stackOverflow problem

        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.editRole(1, roleRequest),
                "It is not possible to edit a non existent role");

        assertTrue(exception.getMessage().contains("не найдена"),
                exception.getReason());
    }

    @Test
    @DisplayName("[editRole]-(Neg) Editing role using non unique name")
    void editRoleUsingNonUniqueNameTest() {
        insertBasicFilling();
        insertFilling();

        int editingRoleId = basicRolesNames.size() + 2; // {1:size} for basics, size+1 - for "Фэйк_Роль_1", size+2 - for editing

        List<Integer> privileges = new ArrayList<>();
//        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4); // TODO solve stackOverflow problem

        RoleRequest roleRequest = new RoleRequest(
                "Фэйк_Роль_1",
                "Фэйк_Описание",
                true,
                privileges);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.editRole(editingRoleId, roleRequest),
                "Must not be able to edit role using not unique name");

        assertEquals(ExceptionConst.ROLE_EXIST_MESSAGE, exception.getReason());
    }

    @Test
    @DisplayName("[editRole]-(Pos) Editing role")
    void editRoleDoNotKeepNameTest() {
        insertFilling();

        List<Integer> privileges = new ArrayList<>();
//        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4); // TODO solve stackOverflow problem

        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        Role editedRole = roleService.editRole(1, roleRequest);

        assertNotNull(editedRole);
        assertAll(
                () -> assertEquals(roleRequest.name(), editedRole.getName()),
                () -> assertEquals(roleRequest.description(), editedRole.getDescription()),
                () -> assertEquals(RoleType.EVENT, editedRole.getType()),
                () -> assertEquals(privileges.size(), editedRole.getPrivileges().size())
        );
    }

    @Test
    @DisplayName("[editRole]-(Pos) Editing role but keeping previous name")
    void editRoleKeepNameTest() {
        insertFilling();

        List<Integer> privileges = new ArrayList<>();
//        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4); // TODO solve stackOverflow problem

        RoleRequest roleRequest = new RoleRequest(
                "Фэйк_Роль_1",
                "Фэйк_Описание",
                true,
                privileges);

        Role editedRole = roleService.editRole(1, roleRequest);

        assertNotNull(editedRole);
        assertAll(
                () -> assertEquals(roleRequest.name(), editedRole.getName()),
                () -> assertEquals(roleRequest.description(), editedRole.getDescription()),
                () -> assertEquals(RoleType.EVENT, editedRole.getType()),
                () -> assertEquals(privileges.size(), editedRole.getPrivileges().size())
        );
    }

    @Test
    @DisplayName("[deleteRole]-(Neg) Deleting basic role")
    void deleteBasicRoleTest() {
        insertBasicFilling();

        for (String basicRolesName : basicRolesNames) {
            Role basicRole = roleService.findByName(basicRolesName);
            int basicRoleId = basicRole.getId();

            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class,
                    () -> roleService.deleteRole(basicRoleId),
                    "It is forbidden to delete the basic roles");

            assertEquals(ExceptionConst.ROLE_DELETING_FORBIDDEN_MESSAGE,
                    exception.getReason());
        }
    }

    @Test
    @DisplayName("[deleteRole]-(Neg) Deleting role assigned to user")
    void deleteRoleAssignedTest() {
        insertFilling();
        insertUsersFilling();

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.deleteRole(1),
                "It is forbidden to delete assigned roles");

        assertEquals(ExceptionConst.USERS_WITH_ROLE_EXIST,
                exception.getReason());
    }

    // TODO Deleting role used in event (eventRole)

    @Test
    @DisplayName("[deleteRole]-(Neg) Deleting unnecessary role")
    void deleteRole() {
        insertFilling();

        roleService.deleteRole(1);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.findRoleById(1),
                "Deleted role can not be accessed");

        assertTrue(exception.getMessage().contains("не найдена"),
                exception.getReason());
    }

    @Test
    @DisplayName("[getAll] (Pos) Getting all roles")
    void getAllRoles() {
        insertBasicFilling();   // 4
        insertFilling();        // 4
        final int ROLES_COUNT = 8;

        List<Role> allRoles = roleService.getAll();

        assertNotNull(allRoles);
        assertEquals(ROLES_COUNT, allRoles.size());
    }

    @Test
    @DisplayName(("[findRoleById] (Pos) Getting roles by Id"))
    void findRoleById() {
        insertBasicFilling();
        final int BASIC_ROLES_COUNT = 4;

        for (int i = 0; i < BASIC_ROLES_COUNT; i++) {
            int roleId = i + 1;

            Role basicRole = roleService.findRoleById(roleId);

            assertNotNull(basicRole);

            assertEquals(basicRolesNames.get(i), basicRole.getName());
        }
    }

    @Test
    @DisplayName(("[findRoleById] (Neg) Getting non existing roles by Id"))
    void findNonExistRoleById() {

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.findRoleById(1),
                "Non existing role can not be accessed");

        assertTrue(exception.getMessage().contains("не найдена"),
                exception.getReason());
    }

    @Test
    @DisplayName("[getOrganizational] (Neg) Getting not created organizational roles")
    void getOrganizationalNotCreatedRolesTest() {
        List<Role> roles = roleService.getOrganizational();

        roles.forEach(role ->
                assertNotEquals(RoleType.EVENT, role.getType()));
    }

    @Test
    @DisplayName("[getOrganizational] (Pos) Getting all organizational roles")
    void getOrganizationalRolesTest() {
        insertBasicFilling();

        List<Role> roles = roleService.getOrganizational();

        long eventRoleCount = roles.stream()
                .filter(role -> role.getType().equals(RoleType.EVENT))
                .count();

        assertNotEquals(0, eventRoleCount);
    }

    @Test
    @DisplayName("[searchByName]-(Pos) Getting basic roles by name")
    void searchByNameTest() {
        insertBasicFilling();

        for (String basicRoleName : basicRolesNames) {
            List<Role> basicRoles = roleService.searchByName(basicRoleName);

            assertNotNull(basicRoles);

            assertFalse(basicRoles.isEmpty());

            basicRoles.forEach(
                    basicRole -> assertEquals(basicRoleName, basicRole.getName())
            );
        }
    }

    @Test
    @DisplayName("[findByName]-(Pos) Getting basic roles by name")
    void findRoleByNameTest() {
        insertBasicFilling();

        for (String basicRoleName : basicRolesNames) {
            Role basicRole = roleService.findByName(basicRoleName);

            assertNotNull(basicRole);

            assertEquals(basicRoleName, basicRole.getName());
        }
    }


    private void insertFilling() {
        executeSqlScript("/sql/insert_roles.sql");
        executeSqlScript("/sql/insert_privileges.sql");
    }

    private void insertBasicFilling() {
        executeSqlScript("/db/test-migration/V0_3__fill_tables_test.sql");
    }

    private void insertUsersFilling() {
        executeSqlScript("/sql/insert_user.sql");
    }
}
