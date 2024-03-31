-- assumed, that user, place, event already exists.
-- You can use insert_user.sql, insert_place.sql, insert_event.sql for this purpose

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
