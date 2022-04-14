create sequence address_sequence start 1 increment 1;
create sequence flat_sequence start 1 increment 1;
create sequence real_estate_sequence start 1 increment 1;
create sequence user_sequence start 1 increment 1;


create table address
(
    address_id      int8 not null,
    city            varchar(255),
    country         varchar(255),
    registry_number varchar(255),
    street          varchar(255),
    primary key (address_id)
);

create table client
(
    client_id int8        not null,
    email     varchar(255),
    password  varchar(255),
    phone_num int8        not null,
    enabled   boolean     not null,
    role      varchar(10) not null,
    primary key (client_id)
);

create table flat
(
    flat_id     int8    not null,
    floor       int4    not null,
    is_repaired boolean not null,
    number      int4    not null,
    rooms       int4    not null,
    primary key (flat_id)
);

create table real_estate
(
    real_estate_id   int8    not null,
    add_info         varchar(2048),
    area             float8,
    deleted          boolean not null,
    is_rented        boolean not null,
    price            float8,
    real_estate_type varchar(10),
    address_id       int8,
    client_id        int8,
    flat_id          int8,
    primary key (real_estate_id)
);

alter table if exists client
    add constraint UK_mail unique (email);

alter table if exists real_estate
    add constraint FK_real_estate_address foreign key (address_id) references address;

alter table if exists real_estate
    add constraint FK_real_estate_client foreign key (client_id) references client;

alter table if exists real_estate
    add constraint FK_real_estate_flat foreign key (flat_id) references flat