truncate table task cascade;
truncate table event_role cascade;
truncate table participant cascade;
truncate table event cascade;
truncate table place cascade;
truncate table notification cascade;
truncate table user_login_info cascade;
truncate table user_t cascade;
truncate table user_notification_info cascade;
truncate table registration_request cascade;

truncate table role cascade; -- todo
truncate table privilege cascade; -- todo
truncate table role_privilege cascade; -- todo


-- reset id sequences
select setval('task_id_seq', 1, false);
select setval('event_role_id_seq', 1, false);
select setval('participant_id_seq', 1, false);
select setval('event_id_seq', 1, false);
select setval('place_id_seq', 1, false);
alter sequence notification_id_seq restart with 1;
select setval('user_login_info_id_seq', 1, false);
select setval('user_t_id_seq', 1, false);
-- select setval('user_notification_info_id_seq', 1, false); на этом варианте валилось
alter sequence user_notification_info_id_seq restart with 1; -- Для остальных мб тоже лучше так
select setval('registration_request_id_seq', 1, false);

-- select setval('role_id_seq', 1, false); -- todo
alter sequence role_id_seq restart with 1;
-- select setval('privilege_id_seq', 1, false); -- todo
alter sequence privilege_id_seq restart with 1;

alter sequence role_privilege_id_seq restart with 1;
