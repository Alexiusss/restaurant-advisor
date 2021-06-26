insert into users (id, email, first_name, last_name, password, active)
values (1, 'admin@gmail.com', 'Admin', 'First', '123', true);

insert into user_role (user_id, role)
values (1, 'USER'),
       (1, 'ADMIN');