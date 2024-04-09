-- note - password is: password

insert into registration_request (email, password_hash, name, surname, status, sent_time)
values ('test_mail4@itmo.ru', '$2a$10$DP4EvNpQKbgSwi68DGyZcOeK.t12oCxd1D0ZC09LVZb69wqYE8u0K', 'test4', 'user4', 'APPROVED', '2024-03-30T21:32:23.536819');

insert into user_notification_info (devices, enable_push_notifications, enable_email_notifications)
values ('{web, android}', false, false);

insert into user_t (role_id, notification_info_id, name, surname) values (5, 1, 'test4', 'user4');

insert into user_login_info (user_id, login, login_status, login_type, password_hash, reset_token, last_login_date, registration_id)
values (1, 'test_mail4@itmo.ru', 'APPROVED', 'EMAIL', '$2a$10$DP4EvNpQKbgSwi68DGyZcOeK.t12oCxd1D0ZC09LVZb69wqYE8u0K', 'reset token', '2024-03-30T21:32:23.536819', 1);