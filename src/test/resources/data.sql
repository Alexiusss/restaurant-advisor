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

INSERT INTO restaurants(id, cuisine, name)
VALUES (1, 'seafood', 'restaurant1'),
       (2, 'vegan', 'restaurant2'),
       (3, 'italian', 'cafe'),
       (4, 'italian', 'bar');

ALTER sequence hibernate_sequence RESTART WITH 33;