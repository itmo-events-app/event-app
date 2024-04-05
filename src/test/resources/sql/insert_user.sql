-- note - password is: password

insert into registration_request (email, password_hash, name, surname, status, sent_time)
values ('test_mail@test_mail.com', '$2a$10$DP4EvNpQKbgSwi68DGyZcOeK.t12oCxd1D0ZC09LVZb69wqYE8u0K', 'test', 'user', 'APPROVED', '2024-03-30T21:32:23.536819');

insert into user_notification_info (devices, enable_push_notifications, enable_email_notifications)
values ('{web, android}', false, false);

insert into user_t (role_id, notification_info_id, name, surname) values (1, 1, 'test', 'user');

insert into user_login_info (user_id, email, email_status, password_hash, reset_token, last_login_date, registration_id)
values (1, 'test_mail@test_mail.com', 'APPROVED', '$2a$10$DP4EvNpQKbgSwi68DGyZcOeK.t12oCxd1D0ZC09LVZb69wqYE8u0K', 'reset token', '2024-03-30T21:32:23.536819', 1);