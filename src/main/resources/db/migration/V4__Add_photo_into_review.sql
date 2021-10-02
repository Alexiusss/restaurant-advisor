alter table if exists reviews
    add column active bool default false not null,
    add column filename varchar (255);