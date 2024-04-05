INSERT INTO privilege (name, type,description)
VALUES
    ('CREATE_ROLE', 'SYSTEM','Фэйк_Описание_1'),
    ('DELETE_ROLE', 'SYSTEM','Фэйк_Описание_2'),
    ('EDIT_ROLE', 'EVENT','Фэйк_Описание_3'),
    ('VIEW_OTHER_USERS_PROFILE', 'EVENT','Фэйк_Описание_4');


INSERT INTO role_privilege (role_id, privilege_id)
VALUES
    (1,1),
    (2,2),
    (3,3),
    (4,4);
