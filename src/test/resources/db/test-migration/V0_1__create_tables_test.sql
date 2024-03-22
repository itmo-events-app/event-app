CREATE TYPE event_format AS ENUM (
    'ONLINE',
    'OFFLINE',
    'HYBRID'
    );

CREATE TYPE task_status AS ENUM (
    'NEW',
    'IN_PROGRESS',
    'DONE'
    );

CREATE TYPE event_status AS ENUM (
    'DRAFT',
    'PUBLISHED',
    'COMPLETED',
    'CANCELED'
    );

CREATE TYPE email_status AS ENUM (
    'UNAPPROVED',
    'APPROVED'
    );

CREATE TYPE registration_request_status AS ENUM (
    'NEW',
    'APPROVED',
    'DECLINED'
    );

CREATE TYPE role_type AS ENUM (
    'SYSTEM',
    'EVENT'
    );

CREATE TYPE privilege_type AS ENUM (
    'APPROVE_REGISTRATION_REQUEST',
    'REJECT_REGISTRATION_REQUEST',
    'MODIFY_PROFILE_DATA',
    'VIEW_OTHER_USERS_PROFILE',
    'VIEW_ALL_EVENTS_AND_ACTIVITIES',
    'SEARCH_EVENTS_AND_ACTIVITIES',
    'CREATE_EVENT',
    'VIEW_EVENT_VENUES',
    'VIEW_ROUTE_BETWEEN_ROOMS',
    'ASSIGN_ORGANIZER_ROLE',
    'REVOKE_ORGANIZER_ROLE',
    'CREATE_EVENT_VENUE',
    'DELETE_EVENT_VENUE',
    'EDIT_EVENT_VENUE',
    'CREATE_ROLE',
    'DELETE_ROLE',
    'EDIT_ROLE',
    'ASSIGN_SYSTEM_ROLE',
    'REVOKE_SYSTEM_ROLE',
    'EDIT_EVENT_INFO',
    'ASSIGN_ASSISTANT_ROLE',
    'REVOKE_ASSISTANT_ROLE',
    'VIEW_ORGANIZER_USERS',
    'VIEW_ASSISTANT_USERS',
    'CREATE_EVENT_ACTIVITIES',
    'DELETE_EVENT_ACTIVITIES',
    'EDIT_EVENT_ACTIVITIES',
    'VIEW_EVENT_ACTIVITIES',
    'CREATE_TASK',
    'DELETE_TASK',
    'EDIT_TASK',
    'CHANGE_TASK_STATUS',
    'ASSIGN_TASK_EXECUTOR',
    'REPLACE_TASK_EXECUTOR',
    'DELETE_TASK_EXECUTOR',
    'ASSIGN_ORGANIZATIONAL_ROLE',
    'REVOKE_ORGANIZATIONAL_ROLE',
    'VIEW_ALL_EVENT_TASKS',
    'CHANGE_ASSIGNED_TASK_STATUS',
    'ASSIGN_SELF_AS_TASK_EXECUTOR',
    'DECLINE_TASK_EXECUTION',
    'IMPORT_PARTICIPANT_LIST_XLSX',
    'EXPORT_PARTICIPANT_LIST_XLSX',
    'WORK_WITH_PARTICIPANT_LIST'
    );
create table if not exists registration_request
(
    id int generated always as identity primary key not null,
    email varchar(128) not null unique,
    encrypted_password varchar(256) not null,
    name varchar(128) not null ,
    surname varchar(128),
    status registration_request_status not null,
    sent_time timestamp not null default current_timestamp
);
create table if not exists privilege
(
    id int generated always as identity primary key not null,
    name varchar(256) not null,
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
    role_id int not null references Role(id),
    privilege_id int not null references Privilege(id)
);
create table if not exists user_login_info(
    id int generated always as identity primary key not null,
    login varchar(256) not null ,
    email varchar(256) not null unique,
    email_status email_status not null,
    name varchar(256) not null,
    surname varchar(256),
    password_hash varchar(512) not null,
    ressettoken varchar(512),
    last_login_date date,
    registration_id int not null references registration_request(id)
);
create table if not exists user_notifications_info(
    id int generated always as identity primary key not null,
    devices varchar(128)[] not null,
    enable_push_notifications boolean not null default true,
    enable_email_notifications boolean not null default true
);
create table if not exists Notifications(
    user_id int not null references "user"(id),
    title varchar(256) not null,
    description text not null ,
    sent boolean not null default FALSE,
    read_time timestamp
);
create table if not exists "user"
(
    id int generated always as identity primary key not null,
    role_id int not null references Role(id),
    notifications_info_id int not null references user_notifications_info(id),
    login_info_id int not null references user_login_info(id)
);
create table if not exists place
(
    id int generated always as identity primary key not null ,
    address varchar(512) not null,
    name varchar(256) not null,
    room varchar(128),
    latitude float not null ,
    longitude float not null,
    render_info text
);
create table if not exists participants(
    id int generated always as identity primary key not null,
    name varchar(256) not null ,
    email varchar(256) not null ,
    additional_info text,
    visited boolean not null ,
    event_id int references event(id)
);
create table if not exists event
(
    id int generated always as identity primary key not null,
    place_id integer not null references Place(id),
    start timestamp not null,
    "end" timestamp not null,
    title varchar(256) not null ,
    short_description text not null,
    full_description text not null,
    format event_format not null,
    status varchar(64) not null ,
    public boolean not null ,
    registration_start timestamp not null ,
    registration_end timestamp not null ,
    parent_id integer references Event(id),
    participant_limit int,
    participant_age_lowest int,
    participant_age_highest int,
    preparing_start timestamp not null ,
    preparing_end timestamp not null
);
create table if not exists event_role(
    id int generated always as identity primary key not null,
    user_id integer not null references "user"(id),
    event_id integer not null references Event(id),
    role_id integer not null references role(id)
);
create table if not exists task
(
    id int generated always as identity primary key not null,
    name varchar(256) not null ,
    event_id integer not null references Event(id),
    assignee_id integer not null references "user"(id),
    assigner_id integer not null references "user"(id),
    description text not null,
    status task_status not null,
    deadline timestamp not null,
    place_id int references place(id),
    notification_deadline timestamp not null
);

