insert into less_planning.user_action (id, name) VALUES
    (nextval('less_planning.user_action_id_seq'), 'CRUD_LESSONS'),
    (nextval('less_planning.user_action_id_seq'), 'ADMIN_LESSONS');

insert into less_planning.user_role_allowed_actions (user_role_id, allowed_actions_id) VALUES
    ((select ur.id from less_planning.user_role ur where ur.name = 'ADMIN'), (select ua.id from less_planning.user_action ua where ua.name = 'ADMIN_LESSONS')),
    ((select ur.id from less_planning.user_role ur where ur.name = 'ADMIN'), (select ua.id from less_planning.user_action ua where ua.name = 'CRUD_LESSONS')),
    ((select ur.id from less_planning.user_role ur where ur.name = 'DIRECTOR'), (select ua.id from less_planning.user_action ua where ua.name = 'ADMIN_LESSONS')),
    ((select ur.id from less_planning.user_role ur where ur.name = 'DIRECTOR'), (select ua.id from less_planning.user_action ua where ua.name = 'CRUD_LESSONS')),
    ((select ur.id from less_planning.user_role ur where ur.name = 'TEACHER'), (select ua.id from less_planning.user_action ua where ua.name = 'CRUD_LESSONS'));