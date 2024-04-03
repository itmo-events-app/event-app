insert into registration_request (email, password_hash, name, surname, status, sent_time)
values ('test_mail3@itmo.ru', 'strong password', 'test3', 'user3', 'APPROVED', '2024-03-30T21:32:23.536819');

insert into user_notification_info (devices, enable_push_notifications, enable_email_notifications)
values ('{web, android}', false, false);

insert into user_t (role_id, notification_info_id, name, surname) values (1, 3, 'test3', 'user3');

insert into user_login_info (user_id, email, email_status, password_hash, reset_token, last_login_date, registration_id)
values (3, 'test_mail3@itmo.ru', 'APPROVED', 'strong password', 'reset token', '2024-03-30T21:32:23.536819', 3);