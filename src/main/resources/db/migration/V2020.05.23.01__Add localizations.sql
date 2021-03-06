insert into less_planning.localization (id, code) values
    (nextval('less_planning.localization_id_seq'), 'ui.error.500.title'),
    (nextval('less_planning.localization_id_seq'), 'ui.error.500.header'),
    (nextval('less_planning.localization_id_seq'), 'ui.error.500.text');


insert into less_planning.translation (locale_id, localization_id, editor_id, update_date_time, translation) values
   ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.title'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Что-то произошло...'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'rus'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.title'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Что-то произошло...'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'eng'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.title'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Something happened'),

   ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.header'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Что-то пошло не так'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'rus'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.header'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Что-то пошло не так'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'eng'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.header'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Something went wrong'),

   ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.text'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Произошла ошибка в работе сервиса, обратитесь к администратору.'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'rus'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.text'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Произошла ошибка в работе сервиса, обратитесь к администратору.'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'eng'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.text'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'There was an error in the service, contact the administrator.');