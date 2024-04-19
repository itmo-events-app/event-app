--change mail to env variable
--change password to env variable
insert into registration_request (email, password_hash, name, surname, status, sent_time)
values ('admin_mail@itmo.ru', '$2a$10$DP4EvNpQKbgSwi68DGyZcOeK.t12oCxd1D0ZC09LVZb69wqYE8u0K', 'mega', 'admin', 'APPROVED', (select now()));

insert into user_notification_info (devices, enable_push_notifications, enable_email_notifications)
values ('{web, android}', false, false);

insert into user_t (notification_info_id, name, surname) values (1, 'mega', 'admin');

insert into user_login_info (user_id, login, login_status, login_type, password_hash, reset_token, last_login_date, registration_id)
values (1, 'admin_mail@itmo.ru', 'APPROVED', 'EMAIL', '$2a$10$DP4EvNpQKbgSwi68DGyZcOeK.t12oCxd1D0ZC09LVZb69wqYE8u0K', 'reset token', (select now()), 1);

insert into user_role (user_id, role_id) values (1, 1);

insert into login_attempts (login, attempts, lockout_expired) values ('admin_mail@itmo.ru', 0, '2025-12-01')