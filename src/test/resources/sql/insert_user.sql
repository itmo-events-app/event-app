insert into registration_request (email, password_hash, name, surname, status, sent_time)
values ('test_mail@test_mail.com', 'strong password', 'test', 'user', 'APPROVED', '2024-03-30T21:32:23.536819');

insert into user_notification_info (devices, enable_push_notifications, enable_email_notifications)
values ('{web, android}', false, false);

insert into user_login_info (login, email, email_status, password_hash, reset_token, last_login_date, registration_id)
values ('ip-13', 'test_mail@test_mail.com', 'APPROVED', 'strong password', 'reset token', '2024-03-30T21:32:23.536819', 1);

insert into user_t (role_id, notification_info_id, login_info_id, name, surname) values (1, 1, 1, 'test', 'user');