-- assumed, that place event exists.
-- You can use insert_event.sql for this purpose

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
    'partys activity',
    'cool partys activity',
    'very cool partys activity',
    'OFFLINE',
    'PUBLISHED',
    '2024-03-30T21:32:23.536819',
    '2024-03-30T21:32:23.536819',
    1,
    1,
    2,
    5,
    '2024-03-30T21:32:23.536819',
    '2024-03-30T21:32:23.536819'
);