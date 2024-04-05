package org.itmo.eventApp.main.service;

import org.assertj.core.util.Lists;
import org.itmo.eventApp.main.controller.AbstractTestContainers;
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


public class RoleServiceTest extends AbstractTestContainers {

    @Autowired
    private RoleService roleService;


    private final List<String> basicRolesNames
            = Arrays.asList("Администратор", "Читатель", "Организатор", "Помощник");


    @Test
    @DisplayName("[createRole]-(Neg) Creating a new role using a non-unique name")
    public void createRoleNonUniNameTest() {
        insertFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        // TODO Check if name is actually non-unique
        RoleRequest roleRequest = new RoleRequest(
                "Фэйк_Роль_1",
                "Фэйк_Описание_1",
                true,
                privileges);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.createRole(roleRequest));
        assertEquals("Роль с таким именем уже существует",
                exception.getReason());
    }

    @Test
    @DisplayName("[createRole]-(Pos) Creating a new role using unique name")
    public void createRoleUniNameTest() {
        insertFilling();

        List<Integer> privileges = new ArrayList<>();
//        privileges.add(1);
//        privileges.add(2);
//        privileges.add(3);
//        privileges.add(4); // Todo refactor (Laze + transaction -> stackOverflow problem)
        // TODO Check if name is actually unique
        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        Role updatedRole = roleService.createRole(roleRequest);

        assertNotNull(updatedRole);
        assertAll(
                () -> assertEquals(updatedRole.getName(), roleRequest.name()),
                () -> assertEquals(updatedRole.getDescription(), roleRequest.description()),
                () -> assertEquals(updatedRole.getType(), RoleType.EVENT),
                () -> assertArrayEquals(    // TODO fix
                        updatedRole.getPrivileges().toArray(), roleRequest.privileges().toArray(),
                        "The set of privileges does not match")
        );
    }

    @Test
    @DisplayName("[findByName]-(Pos) Getting basic roles")
    public void findBasicRolesTest() {
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
    public void editBasicRoleTest() {
        insertBasicFilling();

        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        basicRolesNames.stream()
                .map(roleService::findByName)
                .forEach(basicRole -> {
                    ResponseStatusException exception = assertThrows(
                            ResponseStatusException.class,
                            () -> roleService.editRole(basicRole.getId(), roleRequest),
                            "It is forbidden to modify the basic roles");
                    assertEquals("Невозможно изменить эту роль", exception.getReason());
                });
    }

    @Test
    @DisplayName("[editRole]-(Neg) Editing a non existent role")
    public void editNonExistRoleTest() {
        List<Integer> privileges = Lists.newArrayList(1, 2, 3, 4);
//        List<Integer> privileges = new LinkedList<>();

        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.editRole(1, roleRequest),
                "It is not possible to edit a non existent role");
        assertTrue(exception.getMessage().contains("не существует"),
                exception.getReason()); // to refactor
    }

    @Test
    @DisplayName("[editRole]-(Neg) Editing role using non unique name")
    public void editRoleUsingNonUniqueNameTest() {
        insertBasicFilling();
        insertFilling();

        List<Integer> privileges = new ArrayList<>();
//        privileges.add(1);
//        privileges.add(2);
//        privileges.add(3);
//        privileges.add(4); // Todo refactor (Laze + transaction -> stackOverflow problem)
        RoleRequest roleRequest = new RoleRequest(
                "Фэйк_Роль_1",
                "Фэйк_Описание",
                true,
                privileges);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.editRole(basicRolesNames.size() + 2, roleRequest),    // to refactor
                "Must not be able to edit role using not unique name");
        assertEquals("Роль с таким именем уже существует", exception.getReason());
    }

    @Test
    @DisplayName("[editRole]-(Pos) Editing role")
    public void editRoleDoNotKeepNameTest() {
        insertFilling();

        List<Integer> privileges = new ArrayList<>();
//        privileges.add(1);
//        privileges.add(2);
//        privileges.add(3);
//        privileges.add(4); // to refactor (stackOverflow)

        RoleRequest roleRequest = new RoleRequest(
                "Новая_Фэйк_Роль",
                "Фэйк_Описание",
                true,
                privileges);

        Role editedRole = roleService.editRole(1, roleRequest);

        assertNotNull(editedRole);
        assertAll(
                () -> assertEquals(editedRole.getName(), roleRequest.name()),
                () -> assertEquals(editedRole.getDescription(), roleRequest.description()),
                () -> assertEquals(editedRole.getType(), RoleType.EVENT),
                () -> assertArrayEquals(editedRole.getPrivileges().toArray(), roleRequest.privileges().toArray())
        );
    }

    @Test
    @DisplayName("[editRole]-(Pos) Editing role but keeping previous name")
    public void editRoleKeepNameTest() {
        insertFilling();

        List<Integer> privileges = new ArrayList<>();
//        privileges.add(1);
//        privileges.add(2);
//        privileges.add(3);
//        privileges.add(4); // Todo refactor (Laze + transaction -> stackOverflow problem)

        RoleRequest roleRequest = new RoleRequest(
                "Фэйк_Роль_1",
                "Фэйк_Описание",
                true,
                privileges);

        Role editedRole = roleService.editRole(1, roleRequest);
        assertNotNull(editedRole);
        assertAll(
                () -> assertEquals(editedRole.getName(), roleRequest.name()),
                () -> assertEquals(editedRole.getDescription(), roleRequest.description()),
                () -> assertEquals(editedRole.getType(), RoleType.EVENT),
                () -> assertArrayEquals(editedRole.getPrivileges().toArray(), roleRequest.privileges().toArray())
        );
    }

    @Test
    @DisplayName("[deleteRole]-(Neg) Deleting basic role")
    public void deleteBasicRoleTest() {
        insertBasicFilling();

        basicRolesNames.stream()
                .map(roleService::findByName)
                .forEach(basicRole -> {
                    ResponseStatusException exception = assertThrows(
                            ResponseStatusException.class,
                            () -> roleService.deleteRole(basicRole.getId()),
                            "It is forbidden to delete the basic roles");
                    assertEquals("Невозможно удалить эту роль", exception.getReason());
                });
    }

    @Test
    @DisplayName("[deleteRole]-(Neg) Deleting role assigned to user")
    public void deleteRoleAssignedTest() {
        insertFilling();
        insertUsersFilling();

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.deleteRole(1),
                "It is forbidden to delete assigned roles");
        assertEquals("Невозможно удалить роль, так как существуют пользователи, которым она назначена",
                exception.getReason());
    }

    // TODO Deleting role used in event (eventRole)

    @Test
    @DisplayName("[deleteRole]-(Neg) Deleting unnecessary role")
    public void deleteRole() {
        insertFilling();

        roleService.deleteRole(1);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.findRoleById(1),
                "Deleted role can not be accessed");
        assertTrue(exception.getMessage().contains("не существует"),
                exception.getReason()); // to refactor
    }

    @Test
    @DisplayName("[getAll] (Pos) Getting all roles")
    public void getAllRoles() {
        insertBasicFilling();   // 4
        insertFilling();        // 4

        List<Role> allRoles = roleService.getAll();

        assertNotNull(allRoles);
        assertEquals(8, allRoles.size());
        // to refactor
    }

    @Test
    @DisplayName(("[findRoleById] (Pos) Getting roles by Id"))
    public void findRoleById() {
        insertBasicFilling();

        Role adminRole = roleService.findRoleById(1);
        assertNotNull(adminRole);
        assertEquals(basicRolesNames.get(0), adminRole.getName());

        Role readerRole = roleService.findRoleById(2);
        assertNotNull(readerRole);
        assertEquals(basicRolesNames.get(1), readerRole.getName());

        Role organizerRole = roleService.findRoleById(3);
        assertNotNull(organizerRole);
        assertEquals(basicRolesNames.get(2), organizerRole.getName());

        Role assistantRole = roleService.findRoleById(4);
        assertNotNull(assistantRole);
        assertEquals(basicRolesNames.get(3), assistantRole.getName());
    }

    @Test
    @DisplayName(("[findRoleById] (Neg) Getting non existing roles by Id"))
    public void findNonExistRoleById() {

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> roleService.findRoleById(1),
                "Non existing role can not be accessed");
        assertTrue(exception.getMessage().contains("не существует"),
                exception.getReason()); // to refactor
    }

    @Test
    @DisplayName("[getOrganizational] (Neg) Getting not created organizational roles")
    public void getOrganizationalNotCreatedRolesTest() {
        List<Role> roles = roleService.getOrganizational();

        roles.forEach(role ->
                assertNotEquals(role.getType(), RoleType.EVENT));
    }

    @Test
    @DisplayName("[getOrganizational] (Pos) Getting all organizational roles")
    public void getOrganizationalRolesTest() {
        insertBasicFilling();

        List<Role> roles = roleService.getOrganizational();

        long eventRoleCount = roles.stream()
                .filter(role -> role.getType().equals(RoleType.EVENT))
                .count();

        assertNotEquals(0, eventRoleCount);
    }

    @Test
    @DisplayName("[searchByName]-(Pos) Getting basic roles by name")
    public void searchByNameTest() {
        insertBasicFilling();

        List<Role> adminRoles = roleService.searchByName(basicRolesNames.get(0));
        assertNotNull(adminRoles);
        assertFalse(adminRoles.isEmpty());
        adminRoles.forEach(adminRole -> assertAll(
                () -> assertEquals(basicRolesNames.get(0), adminRole.getName()),
                () -> assertEquals("Имеет полный доступ к системе", adminRole.getDescription()),
                () -> assertEquals(RoleType.SYSTEM, adminRole.getType())
        ));

        List<Role> readerRoles = roleService.searchByName(basicRolesNames.get(1));
        assertNotNull(readerRoles);
        assertFalse(readerRoles.isEmpty());
        readerRoles.forEach(readerRole -> assertAll(
                () -> assertEquals(basicRolesNames.get(1), readerRole.getName()),
                () -> assertEquals("Базовая пользовательская система", readerRole.getDescription()),
                () -> assertEquals(RoleType.SYSTEM, readerRole.getType())
        ));

        List<Role> organizerRoles = roleService.searchByName(basicRolesNames.get(2));
        assertNotNull(organizerRoles);
        assertFalse(organizerRoles.isEmpty());
        organizerRoles.forEach(organizerRole -> assertAll(
                () -> assertEquals(basicRolesNames.get(2), organizerRole.getName()),
                () -> assertEquals("Организатор мероприятия", organizerRole.getDescription()),
                () -> assertEquals(RoleType.EVENT, organizerRole.getType())
        ));

        List<Role> assistantRoles = roleService.searchByName(basicRolesNames.get(3));
        assertNotNull(assistantRoles);
        assertFalse(assistantRoles.isEmpty());
        assistantRoles.forEach(assistantRole -> assertAll(
                () -> assertEquals(basicRolesNames.get(3), assistantRole.getName()),
                () -> assertEquals("Помощь в мероприятиях", assistantRole.getDescription()),
                () -> assertEquals(RoleType.EVENT, assistantRole.getType())
        ));
    }

    // TODO test assignSystemRole

    // TODO test revokeSystemRole

    @Test
    @DisplayName("[findByName]-(Pos) Getting basic roles by name")
    public void findRoleByNameTest() {
        insertBasicFilling();

        Role adminRole = roleService.findByName(basicRolesNames.get(0));
        assertNotNull(adminRole);
        assertAll(
                () -> assertEquals(basicRolesNames.get(0), adminRole.getName()),
                () -> assertEquals("Имеет полный доступ к системе", adminRole.getDescription()),
                () -> assertEquals(RoleType.SYSTEM, adminRole.getType())
        );

        Role readerRoles = roleService.findByName(basicRolesNames.get(1));
        assertNotNull(readerRoles);
        assertAll(
                () -> assertEquals(basicRolesNames.get(1), readerRoles.getName()),
                () -> assertEquals("Базовая пользовательская система", readerRoles.getDescription()),
                () -> assertEquals(RoleType.SYSTEM, readerRoles.getType())
        );

        Role organizerRoles = roleService.findByName(basicRolesNames.get(2));
        assertNotNull(organizerRoles);
        assertAll(
                () -> assertEquals(basicRolesNames.get(2), organizerRoles.getName()),
                () -> assertEquals("Организатор мероприятия", organizerRoles.getDescription()),
                () -> assertEquals(RoleType.EVENT, organizerRoles.getType())
        );

        Role assistantRoles = roleService.findByName(basicRolesNames.get(3));
        assertNotNull(assistantRoles);
        assertAll(
                () -> assertEquals(basicRolesNames.get(3), assistantRoles.getName()),
                () -> assertEquals("Помощь в мероприятиях", assistantRoles.getDescription()),
                () -> assertEquals(RoleType.EVENT, assistantRoles.getType())
        );
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
