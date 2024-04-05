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

    private final List<String> basicRolesNames = Arrays.asList("Администратор", "Читатель", "Организатор", "Помощник");


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
//        privileges.add(4); // to refactor (stackOverflow)
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
        assertEquals(adminRole.getName(), basicRolesNames.get(0));

        Role readerRole = roleService.getReaderRole();
        assertNotNull(readerRole);
        assertEquals(readerRole.getName(), basicRolesNames.get(1));

        Role organizerRole = roleService.getOrganizerRole();
        assertNotNull(organizerRole);
        assertEquals(organizerRole.getName(), basicRolesNames.get(2));

        Role assistantRole = roleService.getAssistantRole();
        assertNotNull(assistantRole);
        assertEquals(assistantRole.getName(), basicRolesNames.get(3));
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
//        privileges.add(4); // to refactor (stackOverflow)
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
    public void editRoleDoNotKeepName() {
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
    public void editRoleKeepName() {
        insertFilling();

        List<Integer> privileges = new ArrayList<>();
//        privileges.add(1);
//        privileges.add(2);
//        privileges.add(3);
//        privileges.add(4); // to refactor (stackOverflow)

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


    private void insertFilling() {
        executeSqlScript("/sql/insert_roles.sql");
        executeSqlScript("/sql/insert_privileges.sql");
    }

    private void insertBasicFilling() {
        executeSqlScript("/db/test-migration/V0_3__fill_tables_test.sql");
    }
}
