DELETE
FROM user_role;
DELETE
FROM users;
DELETE
FROM restaurants;

INSERT INTO users (id, active, email, first_name, last_name, password)
VALUES (1, true, 'admin@gmail.com', 'Admin', 'UserLast',
        '{bcrypt}$2a$08$fcvC7plZPHirDqsgEplSgODGaVwXlWEJSCZFXbiNsYJqbpPHzz3n2'),
       (2, true, 'user@yandex.ru', 'User', 'AdminLast',
        '{bcrypt}$2a$08$fcvC7plZPHirDqsgEplSgODGaVwXlWEJSCZFXbiNsYJqbpPHzz3n2');

INSERT INTO user_role (role, user_id)
VALUES ('ADMIN', 1),
       ('USER', 1),
       ('USER', 2);

INSERT INTO restaurants(id, cuisine, name, menu)
VALUES (3, 'seafood', 'restaurant1', 'www.menu1.com'),
       (4, 'vegan', 'restaurant2', 'www.menu2.com'),
       (5, 'italian', 'cafe', 'www.menu3.com'),
       (6, 'italian', 'bar', 'www.menu4.com');

ALTER sequence hibernate_sequence RESTART WITH 33;