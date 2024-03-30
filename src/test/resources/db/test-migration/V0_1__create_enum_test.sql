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
CREATE TYPE privilege_name AS ENUM (
    'APPROVE_REGISTRATION_REQUEST',
    'REJECT_REGISTRATION_REQUEST',
    'MODIFY_PROFILE_DATA',
    'VIEW_OTHER_USERS_PROFILE',
    'VIEW_ALL_EVENTS_AND_ACTIVITIES',
    'SEARCH_EVENTS_AND_ACTIVITIES',
    'CREATE_EVENT',
    'VIEW_EVENT_PLACE',
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
create cast (character varying as privilege_name) with inout as implicit;
create cast (character varying as privilege_type) with inout as implicit;
