truncate table role cascade;
truncate table privilege cascade;

alter sequence role_id_seq restart with 1;
alter sequence privilege_id_seq restart with 1;