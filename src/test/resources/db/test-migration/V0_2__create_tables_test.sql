create table if not exists registration_request
(
    id int generated always as identity primary key not null,
    email varchar(128) not null unique,
    password_hash varchar(256) not null,
    name varchar(128) not null ,
    surname varchar(128) not null,
    status registration_request_status not null,
    sent_time timestamp not null default current_timestamp
);
create table if not exists privilege
(
    id int generated always as identity primary key not null,
    name privilege_name not null,
    description text not null,
    type privilege_type not null
);
create table if not exists role
(
    id int generated always as identity primary key not null,
    name varchar(256) not null,
    description text not null,
    type role_type not null
);
create table if not exists role_privilege
(
    id int generated always as identity primary key not null,
    role_id int not null references role(id),
    privilege_id int not null references privilege(id)
);
create table if not exists user_notification_info(
    id int generated always as identity primary key not null,
    devices varchar(64)[] not null,
    enable_push_notifications boolean not null default true,
    enable_email_notifications boolean not null default true
);
create table if not exists user_t
(
    id int generated always as identity primary key not null,
    role_id int not null references role(id),
    notification_info_id int not null references user_notification_info(id),
    name varchar(256) not null,
    surname varchar(256)
);
create table if not exists user_login_info(
    id int generated always as identity primary key not null,
    user_id int not null references user_t(id),
    email varchar(256) not null unique,
    email_status email_status not null,
    password_hash varchar(512) not null,
    reset_token varchar(512),
    last_login_date date,
    registration_id int not null references registration_request(id)
);
create table if not exists notification(
    id int generated always as identity primary key not null,
    user_id int not null references user_t(id),
    title varchar(256) not null,
    description text not null ,
    seen boolean not null default FALSE,
    read_time timestamp default null
);
create table if not exists place
(
    id int generated always as identity primary key not null,
    address varchar(512) not null,
    name varchar(256) not null,
    format place_format not null,
    description text not null,
    room varchar(128),
    latitude float not null ,
    longitude float not null,
    render_info text
);
create table if not exists event
(
    id int generated always as identity primary key not null,
    place_id integer references place(id),
    start_date timestamp,
    end_date timestamp,
    title varchar(256) not null,
    short_description text,
    full_description text,
    format event_format,
    status varchar(64),
    registration_start timestamp,
    registration_end timestamp,
    parent_id integer references event(id),
    participant_limit int,
    participant_age_lowest int,
    participant_age_highest int,
    preparing_start timestamp,
    preparing_end timestamp
);
create table if not exists participant(
    id int generated always as identity primary key not null,
    name varchar(256) not null ,
    email varchar(256) not null ,
    additional_info text,
    visited boolean not null ,
    event_id int references event(id) not null
);
create table if not exists event_role(
    id int generated always as identity primary key not null,
    user_id integer not null references user_t(id),
    event_id integer not null references event(id),
    role_id integer not null references role(id)
);
create table if not exists task
(
    id int generated always as identity primary key not null,
    event_id integer not null references event(id),
    assignee_id integer not null references user_t(id),
    assigner_id integer not null references user_t(id),
    description text not null,
    status task_status not null,
    title varchar(128) not null,
    deadline timestamp not null,
    place_id int references place(id),
    notification_deadline timestamp not null
);