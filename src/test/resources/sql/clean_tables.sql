truncate table login_attempts cascade;
delete from role_privilege where id > 61;   -- Number of roles-privilege relations = 61
truncate table user_role cascade;           -- Admin (21) ; Reader (6) ; Organizer (25) ; Assistant (9) ;
delete from role where id > 4;              -- Number of basic roles = 4
delete from privilege where id > 44;        -- Number of basic privilege = 44
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

-- reset id sequences
alter sequence task_id_seq restart with 1;
alter sequence task_deadline_trigger_id_seq restart with 1;
alter sequence task_reminder_trigger_id_seq restart with 1;
alter sequence event_role_id_seq restart with 1;
alter sequence participant_id_seq restart with 1;
alter sequence event_id_seq restart with 1;
alter sequence place_id_seq restart with 1;
alter sequence notification_id_seq restart with 1;
alter sequence user_login_info_id_seq restart with 1;
alter sequence user_t_id_seq restart with 1;
alter sequence user_notification_info_id_seq restart with 1;
alter sequence registration_request_id_seq restart with 1;
alter sequence user_role_id_seq restart with 1;
after sequence login_attempts restart with 1;
alter sequence role_privilege_id_seq restart with 63;   -- Number of roles-privilege relations = 61
                                                        -- Admin (21) ; Reader (6) ; Organizer (25) ; Assistant (9) ;
alter sequence role_id_seq restart with 5;              -- Number of basic roles = 4
alter sequence privilege_id_seq restart with 45;        -- Number of basic privilege = 44