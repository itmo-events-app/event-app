insert into registration_request (email, password_hash, name, surname, status, sent_time)
values ('test_mail@test_mail.com', 'strong password', 'test', 'user', 'APPROVED', '2024-03-30T21:32:23.536819');

insert into user_notification_info (devices, enable_push_notifications, enable_email_notifications)
values ('{web, android}', false, false);

insert into user_login_info (login, email, email_status, password_hash, reset_token, last_login_date, registration_id)
values ('ip-13', 'test_mail@test_mail.com', 'APPROVED', 'strong password', 'reset token', '2024-03-30T21:32:23.536819', 1);

insert into user_t (role_id, notification_info_id, login_info_id, name, surname) values (1, 1, 1, 'test', 'user');

insert into place(
    address,
    name,
    format,
    description,
    room,
    latitude,
    longitude,
    render_info
) values (
    'itmo university',
    'itmo place',
    'OFFLINE',
    'this is itmo place, you can do whatever you want',
    '13',
    11.11,
    22.22,
    'render_info'
);

insert into event (
    place_id,
    start_date,
    end_date,
    title,
    short_description,
    full_description,
    format,
    status,
    registration_start,
    registration_end,
    parent_id,
    participant_limit,
    participant_age_lowest,
    participant_age_highest,
    preparing_start,
    preparing_end
) values (
    1,
    '2024-03-30T21:32:23.536819',
    '2024-03-30T21:32:23.536819',
    'party',
    'cool party',
    'very cool party',
    'OFFLINE',
    'PUBLISHED',
    '2024-03-30T21:32:23.536819',
    '2024-03-30T21:32:23.536819',
    null,
    10,
    5,
    7,
    '2024-03-30T21:32:23.536819',
    '2024-03-30T21:32:23.536819'
);

insert into task (
    event_id,
    assignee_id,
    assigner_id,
    description,
    status,
    title,
    deadline,
    place_id,
    notification_deadline
) values (
    1,
    1,
    1,
    'write sql script for tests',
    'NEW',
    'VERY DIFFICULT TASK',
    '2024-03-30T21:32:23.536819',
    1,
    '2024-03-30T21:32:23.536819'
);
