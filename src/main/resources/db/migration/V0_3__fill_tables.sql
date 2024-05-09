INSERT INTO role (name, description, type)
VALUES ('Администратор', 'Имеет полный доступ к системе', 'SYSTEM'),
       ('Читатель', 'Базовая пользовательская система', 'SYSTEM'),
       ('Организатор', 'Организатор мероприятия', 'EVENT'),
       ('Помощник', 'Помощь в мероприятиях', 'EVENT');


INSERT INTO privilege (name, type, description)
VALUES ('APPROVE_REGISTRATION_REQUEST', 'SYSTEM', 'Привилегия одобрения запроса на регистрацию пользователя. Пользователь, обладающий данной привилегией, может одобрять заявки людей, подавших регистрационный запрос. '),
       ('REJECT_REGISTRATION_REQUEST', 'SYSTEM', 'Привилегия на отклонение запроса на регистрацию пользователя. Пользователь, обладающий данной привилегией, может отклонять заявки людей, подавших регистрационный запрос.'),
       ('MODIFY_PROFILE_DATA', 'SYSTEM', 'Привилегия на возможность редактирования профиля пользователя. Обладая данной привилегией, пользователь может редактировать данные своего профиля: изменять имя и фамилию, логин, пароль.'),
       ('VIEW_OTHER_USERS_PROFILE', 'SYSTEM', 'Привилегия просмотра профилей других пользователей. Пользователь, обладающий данной привилегией может просматривать данные профилей других пользователей, а именно имя, фамилия, email, системные и организационные роли.'),
       ('VIEW_ALL_EVENTS', 'SYSTEM', 'Привилегия просмотра всех мероприятий. Обладая данной привилегией, пользователь может просматривать список всех мероприятий в системе. Также мероприятия доступны к просмотру на карте.'),
       ('SEARCH_EVENTS_AND_ACTIVITIES', 'SYSTEM', 'Привилегия поиска мероприятий и активностей. Обладая данной привилегией, пользователь может искать конкретные мероприятия с использованием фильтрации: начало проведения, конец проведения, статус, формат. '),
       ('CREATE_EVENT', 'SYSTEM', 'Пользователь, имеющий данную привилегию, может создавать новое мероприятие в системе.'),
       ('VIEW_EVENT_PLACE', 'SYSTEM', 'Привилегия просмотра площадки мероприятия. Пользователь может просматривать подробную информацию о площадке: адрес, формат, аудитория, координаты, описание.'),
       ('VIEW_ROUTE_BETWEEN_ROOMS', 'SYSTEM', 'Привилегия просмотра маршрута между помещениями позволяет выбрать точку отправления и точку назначения в корпусе на Кронверкском 49 и затем просмотреть построенный маршрут.'),
       ('ASSIGN_ORGANIZER_ROLE', 'EVENT', 'Привилегия назначения другому пользователю роли Организатора, позволяет назначить данную роль в контексте конкретного мероприятия.'),
       ('REVOKE_ORGANIZER_ROLE', 'SYSTEM', 'Привилегия лишения другого пользователя роли Организатора позволяет лишить другого пользователя данной роли в контексте конкретного мероприятия.'),
       ('CREATE_EVENT_VENUE', 'SYSTEM', 'Привилегия создания площадки для проведения мероприятия, позволяет создать новую площадку с заполнением таких полей, как адрес, формат, аудитория, координаты, описание. Аудиторию можно выбрать на карте.'),
       ('DELETE_EVENT_VENUE', 'SYSTEM', 'Привилегия удаления площадки для проведения мероприятия позволяет удалить площадку. При удалении площадка также пропадает из информации о мероприятии, у которого она была указана.'),
       ('EDIT_EVENT_VENUE', 'SYSTEM', 'Привилегия редактирования площадки проведения мероприятия позволяет редактировать у площадки такие поля, как адрес, формат, аудитория, координаты, описание.'),
       ('CREATE_ROLE', 'SYSTEM', 'Привилегия создания роли позволяет создавать новую системную или организационную роль с различным набором привилегий.'),
       ('DELETE_ROLE', 'SYSTEM', 'Привилегия удаления роли позволяет удалить любую системную или организационную роль, кроме таких стандартных ролей, как Администратор, Читатель, Организатор, Помощник.'),
       ('EDIT_ROLE', 'SYSTEM', 'Привилегия редактирования роли позволяет отредактировать любую системную или организационную роль, кроме таких стандартных ролей, как Администратор, Читатель, Организатор, Помощник.'),
       ('ASSIGN_SYSTEM_ROLE', 'SYSTEM', 'Привилегия назначения пользователю системной роли позволяет назначить другому пользователю любую системную роль.'),
       ('REVOKE_SYSTEM_ROLE', 'SYSTEM', 'Привилегия лишения пользователя системной роли позволяет лишить другого пользователя системной роли. У пользователя нельзя забрать единственную системную роль.'),
       ('EDIT_EVENT_INFO', 'EVENT', 'Привилегия редактирования мероприятия позволяет редактировать у мероприятия такие поля, как название, краткое описание, полное описание, максимальное количество участников, максимальный возраст для участия, минимальный возраст для участия, формат, место, состояние, время начала, время окончания, время начала регистрации, время окончания регистрации, время начала подготовки, время окончания подготовки, картинка.'),
       ('ASSIGN_ASSISTANT_ROLE', 'EVENT', 'Привилегия назначения роли Помощника позволяет назначит другому пользователю данную организационную роль в контексте конкретного мероприятия.'),
       ('REVOKE_ASSISTANT_ROLE', 'EVENT', 'Привилегия лишения роли Помощника позволяет лишить другого пользователя данной организационной роли в контексте конкретного мероприятия.'),
       ('VIEW_ORGANIZER_USERS', 'EVENT', 'Привилегия просмотра списка пользователей с ролью Организатор позволяет посмотреть всех пользователей системы с данной ролью.'),
       ('VIEW_ASSISTANT_USERS', 'EVENT', 'Привилегия просмотра списка пользователей с ролью Помощник позволяет посмотреть всех пользователей системы с данной ролью.'),
       ('CREATE_EVENT_ACTIVITIES', 'EVENT', 'Привилегия создания активности у мероприятия позволяет создать новую активность с указанием  таких полей, как название, краткое описание, полное описание, максимальное количество участников, максимальный возраст для участия, минимальный возраст для участия, формат, место, состояние, время начала, время окончания, время начала регистрации, время окончания регистрации, время начала подготовки, время окончания подготовки, картинка.'),
       ('DELETE_EVENT_ACTIVITIES', 'EVENT', 'Привилегия удаления активности у мероприятия позволяет удалить конкретную активность.'),
       ('EDIT_EVENT_ACTIVITIES', 'EVENT', 'Привилегия редактирования активности у мероприятия позволяет изменять такие поля у активности, как название, краткое описание, полное описание, максимальное количество участников, максимальный возраст для участия, минимальный возраст для участия, формат, место, состояние, время начала, время окончания, время начала регистрации, время окончания регистрации, время начала подготовки, время окончания подготовки, картинка.'),
       ('VIEW_EVENT_ACTIVITIES', 'EVENT', 'Привилегия просмотра списка активностей мероприятия позволяет посмотреть все активности конкретного мероприятия.'),
       ('CREATE_TASK', 'EVENT', 'Привилегия создания задач позволяет создавать новые задачи для конкретного мероприятия с указанием таких данных: название, описание, место, пользователи, крайний срок, напоминание.'),
       ('DELETE_TASK', 'EVENT', 'Привилегия удаления задач позволяет удалить задачу конкретного мероприятия.'),
       ('EDIT_TASK', 'EVENT', 'Привилегия редактирования задач позволяет редактировать у задачи такие поля, как название, описание, место, пользователи, крайний срок, напоминание.'),
       ('CHANGE_TASK_STATUS', 'EVENT', 'Привилегия изменения статуса задач позволяет изменить статус у конкретной задачи мероприятия.'),
       ('ASSIGN_TASK_EXECUTOR', 'EVENT', 'Привилегия назначения исполнителя на задачу позволяет назначить другого пользователя в качестве исполнителя задачи.'),
       ('REPLACE_TASK_EXECUTOR', 'EVENT', 'Привилегия изменения исполнителя на задачу позволяет изменить пользователя, указанного ответственным за задачу.'),
       ('DELETE_TASK_EXECUTOR', 'EVENT', 'Привилегия удаления исполнителя у задачи позволяет удалить указанного пользователя с назначения исполнителя задачи.'),
       ('ASSIGN_ORGANIZATIONAL_ROLE', 'EVENT', 'Привилегия назначения пользователю организационной роли позволяет присвоить другому пользователю любую организационную роль.'),
       ('REVOKE_ORGANIZATIONAL_ROLE', 'EVENT', 'Привилегия лишения пользователя организационной роли позволяет лишить другого пользователя любой организационной роли.'),
       ('VIEW_ALL_EVENT_TASKS', 'EVENT', 'Привилегия просмотра всех задач мероприятия позволяет просмотреть список всех задач.'),
       ('CHANGE_ASSIGNED_TASK_STATUS', 'EVENT', 'Привилегия изменение статуса присвоенных задач позволяет изменить статус задачи, в которой пользователь является исполнителем.'),
       ('ASSIGN_SELF_AS_TASK_EXECUTOR', 'EVENT', 'Привилегия назначения себя на роль исполнителя задачи позволяет выбирать себя исполнителем задачи.'),
       ('DECLINE_TASK_EXECUTION', 'EVENT', 'Привилегия отклонения задачи на выполнение позволяет пользователю отказаться от задачи, в которой он был выбран исполнителем.'),
       ('IMPORT_PARTICIPANT_LIST_XLSX', 'EVENT', 'Привилегия импорта списка участников позволяет импортировать в систему файл в формате xlsx со списком участников.'),
       ('EXPORT_PARTICIPANT_LIST_XLSX', 'EVENT', 'Привилегия экспорта списка участников позволяет пользователю скачать файл со списком участников в формате xlsx.'),
       ('WORK_WITH_PARTICIPANT_LIST', 'EVENT', 'Привилегия изменения статуса явки у участников списка мероприятия позволяет пользователю выставлять галочку для отметки присутствия участника.')
;

INSERT INTO role_privilege(role_id, privilege_id) VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,23),(1,24),(1,28);
INSERT INTO role_privilege(role_id, privilege_id) VALUES (2,3),(2,5),(2,6),(2,8),(2,9),(2,28);
INSERT INTO role_privilege(role_id, privilege_id) VALUES (3,10),(3,20),(3,21),(3,22),(3,23),(3,24),(3,25),(3,26),(3,27),(3,29),(3,30),(3,31),(3,32),(3,33),(3,34),(3,35),(3,36),(3,37),(3,38),(3,39),(3,40),(3,41),(3,42),(3,43),(3,44);
INSERT INTO role_privilege(role_id, privilege_id) VALUES (4,23),(4,24),(4,38),(4,39),(4,40),(4,41),(4,42),(4,43),(4,44);

INSERT INTO place (address, name, format, description, room, latitude, longitude) VALUES ('Кронверкский проспект, 49', 'Кронверкский проспект, 49', 'OFFLINE', 'Главный корпус', '', 30.30831, 59.95717);
INSERT INTO place (address, name, format, description, room, latitude, longitude) VALUES ('улица Ломоносова, 9', 'Ломоносова, 9', 'OFFLINE', 'Корпус на Ломоносова', '', 30.33853, 59.92665);
INSERT INTO place (address, name, format, description, room, latitude, longitude) VALUES ('улица Гастелло, 12', 'улица Гастелло, 12', 'OFFLINE', 'Корпус на Гастелло', '', 30.32411, 59.85962);
INSERT INTO place (address, name, format, description, room, latitude, longitude) VALUES ('Онлайн', 'Онлайн', 'ONLINE', 'онлайн-площадка', '', 0.0, 0.0);
