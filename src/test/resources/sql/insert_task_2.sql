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
    notification_deadline
) values (
             2,
             1,
             1,
             'write sql script for tests - 2',
             'EXPIRED',
             'VERY DIFFICULT TASK',
             '2024-03-10T21:32:23.536819',
             '2024-03-30T21:32:23.536819',
             1,
             '2024-03-30T21:32:23.536819'
         );
