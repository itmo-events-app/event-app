CREATE TYPE event_format AS ENUM (
    'ONLINE',
    'OFFLINE',
    'HYBRID'
);
CREATE TYPE place_format AS ENUM (
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

create type privilege_type as ENUM(
    'SYSTEM',
    'EVENT'
);
create cast (character varying as event_format) with inout as implicit;
create cast (character varying as place_format) with inout as implicit;
create cast (character varying as task_status) with inout as implicit;
create cast (character varying as event_status) with inout as implicit;
create cast (character varying as email_status) with inout as implicit;
create cast (character varying as registration_request_status) with inout as implicit;
create cast (character varying as role_type) with inout as implicit;
create cast (character varying as privilege_type) with inout as implicit;
