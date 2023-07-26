create schema if not exists acceptance_test;
create table if not exists acceptance_test.customers
(
    id   bigint auto_increment,
    name varchar(30),
    dni  varchar(20),
    type varchar(5),
    primary key (id)
);
