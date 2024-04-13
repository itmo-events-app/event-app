-- assumed, that user, place, event already exists.
-- You can use insert_user.sql, insert_place.sql, insert_event.sql for this purpose

insert into task (
    event_id,
    assignee_id,
    assigner_id,
    description,
    status,
    title,
    creation_time,
    deadline,
    place_id,
    reminder
) values (
    1,
    1,
    1,
    'write sql script for tests',
    'NEW',
    'VERY DIFFICULT TASK',
    '2025-03-10T21:32:23.536819',
    '2025-03-30T21:32:23.536819',
    1,
    '2025-03-30T21:32:23.536819'
);
