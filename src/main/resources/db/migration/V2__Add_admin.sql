insert into users (id, email, first_name, last_name, password, active)
values (1, 'admin@gmail.com', 'Admin', 'First', '{bcrypt}$2a$10$TKSn8m5eMsMq7nNDGD6Sseb//vCE4LqHRo757wZbPG7iBmdMJak.2', true);

insert into user_role (user_id, role)
values (1, 'USER'),
       (1, 'ADMIN');