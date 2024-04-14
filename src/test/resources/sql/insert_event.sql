-- assumed, that place already exists.
-- You can use insert_place.sql for this purpose

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
    '2030-03-30T21:32:23.536819',
    '2030-03-30T21:32:23.536819',
    'party',
    'cool party',
    'very cool party',
    'OFFLINE',
    'PUBLISHED',
    '2030-03-30T21:32:23.536819',
    '2030-03-30T21:32:23.536819',
    null,
    10,
    5,
    7,
    '2030-03-30T21:32:23.536819',
    '2030-03-30T21:32:23.536819'
);