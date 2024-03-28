INSERT INTO role (name, description, type)
VALUES ('Administrator', 'Имеет полный доступ к системе', 'SYSTEM'),
       ('Reader', 'Basic user system', 'SYSTEM'),
       ('Organizer', 'Базовая пользовательская система', 'EVENT'),
       ('Helper', 'Помощь в мероприятиях', 'EVENT');


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
    ('CREATE_ROLE', 'SYSTEM'),
    ('DELETE_ROLE', 'SYSTEM'),
    ('EDIT_ROLE', 'SYSTEM'),
    ('ASSIGN_SYSTEM_ROLE', 'SYSTEM'),
    ('REVOKE_SYSTEM_ROLE', 'SYSTEM'),
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

INSERT INTO role_privilege(role_id, privilege_id) values (1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,23),(1,24),(1,28);
Insert into role_privilege(role_id, privilege_id) VALUES (2,3),(2,5),(2,6),(2,8),(2,9),(2,28);
Insert into role_privilege(role_id, privilege_id) VALUES (3,10),(3,20),(3,21),(3,22),(3,23),(3,24),(3,25),(3,26),(3,27),(3,29),(3,30),(3,31),(3,32),(3,33),(3,34),(3,35),(3,36),(3,37),(3,38),(3,39),(3,40),(3,41),(3,42),(3,43),(3,44);
Insert into role_privilege(role_id, privilege_id) VALUES (4,23),(4,24),(4,38),(4,39),(4,40),(4,41),(4,42),(4,43),(4,44);
