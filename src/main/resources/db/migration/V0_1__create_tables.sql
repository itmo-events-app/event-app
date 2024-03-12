create table if not exists t (
    id int generated always as identity(start with 100 increment by 100) primary key,
    name varchar(64) not null
);