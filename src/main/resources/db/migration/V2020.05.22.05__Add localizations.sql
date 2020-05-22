insert into less_planning.localization (id, code) values
    (nextval('less_planning.localization_id_seq'), 'ui.text.password_changed');


insert into less_planning.translation (locale_id, localization_id, editor_id, update_date_time, translation) values
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.text.password_changed'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Пароль успешно изменён'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'rus'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.text.password_changed'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Пароль успешно изменён'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'eng'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.text.password_changed'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Password successfully changed');