create table if not exists RegistrationRequest(
    id int generated always as identity primary key not null,
    password varchar(256) not null ,
    name varchar(128) not null ,
    surname varchar(128) not null,
    email varchar(128) not null
);
create table if not exists Privilege
(
    id int generated always as identity primary key not null,
    name varchar(256) not null,
    description text not null
);
create table if not exists Role
(
    id int generated always as identity primary key not null,
    name varchar(256) not null,
    description text not null
);
create table if not exists RolePrivilege(
    id int generated always as identity primary key not null,
    role_id int not null references Role(id),
    privilege_id int not null references Privilege(id)
);
create table if not exists "User"(
    id int generated always as identity primary key not null,
    email varchar(256) not null,
    name varchar(256) not null,
    surname varchar(256) not null,
    password varchar(256) not null,
    resettoken varchar(512) not null,
    last_login_date date not null,
    role_id int not null references Role(id)
);
create table if not exists Place(
    id int generated always as identity primary key not null ,
    address varchar(512) not null,
    name varchar(256) not null
);
create table if not exists Event(
    id int generated always as identity primary key not null,
    place_id integer not null references Place(id),
    start timestamp not null ,
    "end" timestamp not null,
    title varchar(256) not null ,
    short_description text not null,
    full_description text,
    approving_status varchar(64) not null ,
    public boolean not null ,
    registration_start timestamp not null ,
    registration_end timestamp not null ,
    parent_id integer references Event(id),
    participant_limit int not null,
    participant_age_lowest int not null,
    participant_age_highest int not null,
    photo bytea not null
);
create table if not exists EventRole(
    id int generated always as identity primary key not null,
    user_id integer not null references "User"(id),
    event_id integer not null references Event(id),
    role_id integer not null references role(id)
);
create table if not exists Task
(
    id int generated always as identity primary key not null,
    event_id integer not null references Event(id),
    assignee_id integer not null references "User"(id),
    assigner_id integer not null references "User"(id),
    description text not null,
    status varchar not null,
    deadline timestamp not null,
    place_id int not null
);
