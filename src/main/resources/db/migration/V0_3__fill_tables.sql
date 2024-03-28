INSERT INTO role (name, description, type)
VALUES ('Администратор ', 'Имеет полный доступ к системе', 'SYSTEM'),
       ('Читатель', 'Базовая пользовательская система', 'SYSTEM'),
       ('Организатор', 'Организатор мероприятия', 'EVENT'),
       ('Помощник', 'Помощь в мероприятиях', 'EVENT');


INSERT INTO privilege (name, type,description)
VALUES
    ('APPROVE_REGISTRATION_REQUEST', 'SYSTEM','Утверждение заявки на регистрацию в системе'),
    ('REJECT_REGISTRATION_REQUEST', 'SYSTEM','Отклонение заявки на регистрацию в системе'),
    ('MODIFY_PROFILE_DATA', 'SYSTEM','Изменение данных в личном кабинете'),
    ('VIEW_OTHER_USERS_PROFILE', 'SYSTEM','Просмотр личного кабинета других пользователей'),
    ('VIEW_ALL_EVENTS_AND_ACTIVITIES', 'SYSTEM','Просмотр списка всех мероприятий и активностей'),
    ('SEARCH_EVENTS_AND_ACTIVITIES', 'SYSTEM','Поиск мероприятий и активностей'),
    ('CREATE_EVENT', 'EVENT','Создание мероприятия'),
    ('VIEW_EVENT_PLACE', 'SYSTEM','Просмотр списка площадок проведения мероприятия'),
    ('VIEW_ROUTE_BETWEEN_ROOMS', 'SYSTEM','Просмотр маршрута между помещениями'),
    ('ASSIGN_ORGANIZER_ROLE', 'EVENT','Назначение пользователю роли Организатор'),
    ('REVOKE_ORGANIZER_ROLE', 'SYSTEM','Лишение пользователя роли Организатор'),
    ('CREATE_EVENT_VENUE', 'EVENT','создание площадки проведения мероприятия'),
    ('DELETE_EVENT_VENUE', 'EVENT','Удаление площадки проведения мероприятия'),
    ('EDIT_EVENT_VENUE', 'EVENT','Редактирование площадки проведения мероприятия'),
    ('CREATE_ROLE', 'SYSTEM','Создание роли'),
    ('DELETE_ROLE', 'SYSTEM','Удаление роли'),
    ('EDIT_ROLE', 'SYSTEM','Редактирование роли'),
    ('ASSIGN_SYSTEM_ROLE', 'SYSTEM','Назначение пользователю системной роли'),
    ('REVOKE_SYSTEM_ROLE', 'SYSTEM','Лишение пользователя системной роли'),
    ('EDIT_EVENT_INFO', 'EVENT','Редактирование информации о мероприятии'),
    ('ASSIGN_ASSISTANT_ROLE', 'EVENT','Назначение пользователю роли Помощник'),
    ('REVOKE_ASSISTANT_ROLE', 'EVENT','Лишение пользователя роли Помощник'),
    ('VIEW_ORGANIZER_USERS', 'EVENT','Просмотр списка пользователей с ролью Организатор'),
    ('VIEW_ASSISTANT_USERS', 'EVENT','Просмотр списка пользователей с ролью Помощник'),
    ('CREATE_EVENT_ACTIVITIES', 'EVENT','Создание активностей мероприятия'),
    ('DELETE_EVENT_ACTIVITIES', 'EVENT','Удаление активностей мероприятия'),
    ('EDIT_EVENT_ACTIVITIES', 'EVENT','Редактирование активностей мероприятия'),
    ('VIEW_EVENT_ACTIVITIES', 'EVENT','Просмотр списка активностей мероприятия'),
    ('CREATE_TASK', 'EVENT','Создание задач'),
    ('DELETE_TASK', 'EVENT','Удаление задач'),
    ('EDIT_TASK', 'EVENT','Редактирование задач'),
    ('CHANGE_TASK_STATUS', 'EVENT','Изменение статуса задач'),
    ('ASSIGN_TASK_EXECUTOR', 'EVENT','Назначение исполнителя задачи мероприятия'),
    ('REPLACE_TASK_EXECUTOR', 'EVENT','Замена исполнителя задачи'),
    ('DELETE_TASK_EXECUTOR', 'EVENT','Удаление исполнителя задачи'),
    ('ASSIGN_ORGANIZATIONAL_ROLE', 'EVENT','Назначение пользователю организационной роли'),
    ('REVOKE_ORGANIZATIONAL_ROLE', 'EVENT','Лишение пользователя организационной роли'),
    ('VIEW_ALL_EVENT_TASKS', 'EVENT','Просмотр всех задач мероприятия'),
    ('CHANGE_ASSIGNED_TASK_STATUS', 'EVENT','Изменение статуса присвоенных задач'),
    ('ASSIGN_SELF_AS_TASK_EXECUTOR', 'EVENT','Назначение себя исполнителем задачи'),
    ('DECLINE_TASK_EXECUTION', 'EVENT','Отказ от исполнения задачи'),
    ('IMPORT_PARTICIPANT_LIST_XLSX', 'EVENT','Импорт списка участников в формате xlsx'),
    ('EXPORT_PARTICIPANT_LIST_XLSX', 'EVENT','Экспорт списка участников в формате xlsx'),
    ('WORK_WITH_PARTICIPANT_LIST', 'EVENT','Работа со списком участников');

INSERT INTO role_privilege(role_id, privilege_id) values (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,23),(1,24),(1,28);
Insert into role_privilege(role_id, privilege_id) VALUES (2,3),(2,5),(2,6),(2,8),(2,9),(2,28);
Insert into role_privilege(role_id, privilege_id) VALUES (3,10),(3,20),(3,21),(3,22),(3,23),(3,24),(3,25),(3,26),(3,27),(3,29),(3,30),(3,31),(3,32),(3,33),(3,34),(3,35),(3,36),(3,37),(3,38),(3,39),(3,40),(3,41),(3,42),(3,43),(3,44);
Insert into role_privilege(role_id, privilege_id) VALUES (4,23),(4,24),(4,38),(4,39),(4,40),(4,41),(4,42),(4,43),(4,44);
