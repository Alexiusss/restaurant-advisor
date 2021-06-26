create sequence hibernate_sequence start 1 increment 1;

create table contacts (
                          restaurant_id int4 not null,
                          adress varchar(255) not null,
                          email varchar(255),
                          phone_number varchar(255) not null,
                          website varchar(255),
                          primary key (restaurant_id)
);


create table restaurants (
                             id int4 not null,
                             cuisine varchar(255) not null,
                             filename varchar(255),
                             name varchar(255) not null,
                             primary key (id)
);

create table reviews (
                         id int4 not null,
                         comment varchar(2048),
                         date timestamp default now() not null,
                         rating int4 not null,
                         title varchar(255),
                         restaurant_id int4 not null,
                         user_id int4 not null,
                         primary key (id)
);

create table user_role (
                           user_id int4 not null,
                           role varchar(255)
);

create table users (
                       id int4 not null,
                       activation_code varchar(255),
                       active bool default false not null,
                       email varchar(255) not null,
                       first_name varchar(255),
                       last_name varchar(255),
                       password varchar(255) not null,
                       primary key (id)
);

alter table if exists contacts
    add constraint contacts_unique_idx unique (adress, phone_number);

alter table if exists reviews
    add constraint reviews_unique_user_date_idx unique (user_id, date);

alter table if exists user_role
    add constraint user_roles_unique unique (user_id, role);

alter table if exists users
    add constraint user_email_unique unique (email);

-- alter table if exists contacts
--     add constraint FKnde9mdcjsx5apyb1ba2971svs
--     foreign key (restaurant_id)
--     references restaurants;
--
-- alter table if exists reviews
--     add constraint FKsu8ow2q842enesfbqphjrfi5g
--     foreign key (restaurant_id)
--     references restaurants;
--
-- alter table if exists reviews
--     add constraint FKcgy7qjc1r99dp117y9en6lxye
--     foreign key (user_id)
--     references users
--     on delete cascade;
--
-- alter table if exists user_role
--     add constraint FKj345gk1bovqvfame88rcx7yyx
--     foreign key (user_id)
--     references users;