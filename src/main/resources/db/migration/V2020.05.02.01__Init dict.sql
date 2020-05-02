insert into less_planning.user_action (id, name) VALUES
    (nextval('less_planning.user_action_id_seq'), 'MODIFY_CHILDREN');

insert into less_planning.user_role (id, name, description) VALUES
    (nextval('less_planning.user_role_id_seq'), 'DIRECTOR', 'Role that have ability to manage children and courses');

insert into less_planning.user_role_allowed_actions (user_role_id, allowed_actions_id) VALUES
    ((select ur.id from less_planning.user_role ur where ur.name = 'ADMIN'), (select ua.id from less_planning.user_action ua where ua.name = 'MODIFY_CHILDREN')),
    ((select ur.id from less_planning.user_role ur where ur.name = 'DIRECTOR'), (select ua.id from less_planning.user_action ua where ua.name = 'MODIFY_COURSES')),
    ((select ur.id from less_planning.user_role ur where ur.name = 'DIRECTOR'), (select ua.id from less_planning.user_action ua where ua.name = 'MODIFY_CHILDREN'));