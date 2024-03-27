INSERT INTO role (name, description, type)
VALUES ('Administrator', 'Has full system access', 'SYSTEM'),
       ('Reader', 'Basic user system', 'SYSTEM'),
       ('Organizer', 'Can manage events', 'EVENT'),
       ('Participant', 'Can participate in events', 'EVENT');


INSERT INTO privilege (name, type)
VALUES
    ('APPROVE_REGISTRATION_REQUEST', 'SYSTEM'),
    ('REJECT_REGISTRATION_REQUEST', 'SYSTEM'),
    ('MODIFY_PROFILE_DATA', 'SYSTEM'),
    ('VIEW_OTHER_USERS_PROFILE', 'SYSTEM'),
    ('VIEW_ALL_EVENTS_AND_ACTIVITIES', 'SYSTEM'),
    ('SEARCH_EVENTS_AND_ACTIVITIES', 'SYSTEM'),
    ('CREATE_EVENT', 'EVENT'),
    ('VIEW_EVENT_PLACE', 'EVENT'),
    ('VIEW_ROUTE_BETWEEN_ROOMS', 'EVENT'),
    ('ASSIGN_ORGANIZER_ROLE', 'EVENT'),
    ('REVOKE_ORGANIZER_ROLE', 'EVENT'),
    ('CREATE_EVENT_VENUE', 'EVENT'),
    ('DELETE_EVENT_VENUE', 'EVENT'),
    ('EDIT_EVENT_VENUE', 'EVENT'),
    ('CREATE_ROLE', 'EVENT'),
    ('DELETE_ROLE', 'EVENT'),
    ('EDIT_ROLE', 'EVENT'),
    ('ASSIGN_SYSTEM_ROLE', 'EVENT'),
    ('REVOKE_SYSTEM_ROLE', 'EVENT'),
    ('EDIT_EVENT_INFO', 'EVENT'),
    ('ASSIGN_ASSISTANT_ROLE', 'EVENT'),
    ('REVOKE_ASSISTANT_ROLE', 'EVENT'),
    ('VIEW_ORGANIZER_USERS', 'EVENT'),
    ('VIEW_ASSISTANT_USERS', 'EVENT'),
    ('CREATE_EVENT_ACTIVITIES', 'EVENT'),
    ('DELETE_EVENT_ACTIVITIES', 'EVENT'),
    ('EDIT_EVENT_ACTIVITIES', 'EVENT'),
    ('VIEW_EVENT_ACTIVITIES', 'EVENT'),
    ('CREATE_TASK', 'EVENT'),
    ('DELETE_TASK', 'EVENT'),
    ('EDIT_TASK', 'EVENT'),
    ('CHANGE_TASK_STATUS', 'EVENT'),
    ('ASSIGN_TASK_EXECUTOR', 'EVENT'),
    ('REPLACE_TASK_EXECUTOR', 'EVENT'),
    ('DELETE_TASK_EXECUTOR', 'EVENT'),
    ('ASSIGN_ORGANIZATIONAL_ROLE', 'EVENT'),
    ('REVOKE_ORGANIZATIONAL_ROLE', 'EVENT'),
    ('VIEW_ALL_EVENT_TASKS', 'EVENT'),
    ('CHANGE_ASSIGNED_TASK_STATUS', 'EVENT'),
    ('ASSIGN_SELF_AS_TASK_EXECUTOR', 'EVENT'),
    ('DECLINE_TASK_EXECUTION', 'EVENT'),
    ('IMPORT_PARTICIPANT_LIST_XLSX', 'EVENT'),
    ('EXPORT_PARTICIPANT_LIST_XLSX', 'EVENT'),
    ('WORK_WITH_PARTICIPANT_LIST', 'EVENT');

CREATE OR REPLACE FUNCTION assign_all_privileges_to_administrator()
    RETURNS VOID AS $$
DECLARE
    admin_role_id INT;
BEGIN
    SELECT id INTO admin_role_id FROM role WHERE name = 'Administrator';

    INSERT INTO role_privilege (role_id, privilege_id)
    SELECT admin_role_id, id FROM privilege;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION assign_participant_privileges_to_participant()
    RETURNS VOID AS $$
DECLARE
    participant_role_id INT;
BEGIN
    SELECT id INTO participant_role_id FROM role WHERE name = 'Participant';

    INSERT INTO role_privilege (role_id, privilege_id)
    SELECT participant_role_id, id FROM privilege WHERE name = 'Participate in Events';
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION assign_event_privileges_to_organizer()
    RETURNS VOID AS $$
DECLARE
    organizer_role_id INT;
BEGIN
    SELECT id INTO organizer_role_id FROM role WHERE name = 'Organizer';

    INSERT INTO role_privilege (role_id, privilege_id)
    SELECT organizer_role_id, id FROM privilege WHERE type = 'EVENT';
END;
$$ LANGUAGE plpgsql;
Insert into role_privilege(role_id, privilege_id) VALUES (2,3),(2,5),(2,6),(2,8),(2,28);
SELECT assign_all_privileges_to_administrator();
SELECT assign_event_privileges_to_organizer();
SELECT assign_participant_privileges_to_participant();