insert into less_planning.user_action (id, name) VALUES
    (nextval('less_planning.user_action_id_seq'), 'MODIFY_USERS'),
    (nextval('less_planning.user_action_id_seq'), 'MODIFY_ROLES'),
    (nextval('less_planning.user_action_id_seq'), 'MODIFY_COURSES');

insert into less_planning.user_role (id, name, description) VALUES
    (nextval('less_planning.user_role_id_seq'), 'ADMIN', 'Main role'),
    (nextval('less_planning.user_role_id_seq'), 'USER', 'Common users role');

insert into less_planning.user_role_allowed_actions (user_role_id, allowed_actions_id) VALUES
    ((select ur.id from less_planning.user_role ur where ur.name = 'ADMIN'), (select ua.id from less_planning.user_action ua where ua.name = 'MODIFY_USERS')),
    ((select ur.id from less_planning.user_role ur where ur.name = 'ADMIN'), (select ua.id from less_planning.user_action ua where ua.name = 'MODIFY_COURSES')),
    ((select ur.id from less_planning.user_role ur where ur.name = 'ADMIN'), (select ua.id from less_planning.user_action ua where ua.name = 'MODIFY_ROLES')),
    ((select ur.id from less_planning.user_role ur where ur.name = 'USER'), (select ua.id from less_planning.user_action ua where ua.name = 'MODIFY_COURSES'));

insert into less_planning.user (id, login, password, registration_date) VALUES
    (nextval('less_planning.user_id_seq'), 'admin', '$2a$10$cRDrpt4RtSeWloX1jyqhRuVOW73Cm5rYChneTaNYKcA.HrdpGrorO', now());

insert into less_planning.user_user_roles(user_id, user_roles_id) VALUES
    ((select u.id from less_planning.user u where u.login = 'admin'), (select ur.id from less_planning.user_role ur where ur.name = 'ADMIN'));