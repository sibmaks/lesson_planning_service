insert into less_planning.localization (id, code) values
    (nextval('less_planning.localization_id_seq'), 'ui.text.error.user_blocked'),
    (nextval('less_planning.localization_id_seq'), 'ui.text.blocking');


insert into less_planning.translation (locale_id, localization_id, editor_id, update_date_time, translation) values
   ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.text.error.user_blocked'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Пользователь заблокирован'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'rus'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.text.error.user_blocked'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Пользователь заблокирован'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'eng'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.text.error.user_blocked'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'User blocked'),

   ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.text.blocking'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Блокировка'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'rus'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.text.blocking'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Блокировка'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'eng'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.text.blocking'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Blocking');