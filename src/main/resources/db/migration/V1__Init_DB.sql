create sequence hibernate_sequence start with 2 increment by 1;

create table restaurants (
                             id int4 not null,
                             cuisine varchar(255) not null,
                             filename varchar(255),
                             name varchar(255) not null,
                             primary key (id)
);

create table contacts (
                          restaurant_id int4 not null,
                          address varchar(255) not null,
                          email varchar(255),
                          phone_number varchar(255) not null,
                          website varchar(255),
                          primary key (restaurant_id),
                          constraint contacts_unique_idx unique (address, phone_number),
                          foreign key (restaurant_id) references restaurants on delete cascade
);

create table reviews (
                         id int4 not null,
                         comment varchar(2048),
                         date timestamp default now() not null,
                         rating int4 not null,
                         title varchar(255),
                         restaurant_id int4 not null,
                         user_id int4 not null,
                         primary key (id),
                         foreign key (restaurant_id) references restaurants (id) on delete cascade
);

create unique index reviews_unique_user_restaurant_idx on reviews (user_id, restaurant_id);

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

create unique index user_email_unique on users (email);

create table user_role (
                           user_id int4 not null,
                           role varchar(255),
                           constraint user_roles_unique unique (user_id, role),
                           foreign key (user_id) references users (id) on delete cascade
);
