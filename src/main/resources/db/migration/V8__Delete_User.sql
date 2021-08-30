alter table user_role drop constraint fkj345gk1bovqvfame88rcx7yyx;

alter table user_role
    add constraint user_role_users__fk
        foreign key (user_id) references users (id)
            on delete cascade;