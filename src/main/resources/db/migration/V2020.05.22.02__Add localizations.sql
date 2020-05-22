insert into less_planning.localization (id, code) values
    (nextval('less_planning.localization_id_seq'), 'ui.title.lesson.add'),
    (nextval('less_planning.localization_id_seq'), 'ui.header.lesson.add');


insert into less_planning.translation (locale_id, localization_id, editor_id, update_date_time, translation) values
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.title.lesson.add'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'ПУ: Добавление занятия'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'rus'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.title.lesson.add'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'ПУ: Добавление занятия'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'eng'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.title.lesson.add'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'LP: Lesson add'),

    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.header.lesson.add'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Добавление занятия'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'rus'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.header.lesson.add'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Добавление занятия'),
    ((select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 = 'eng'),
    (select loc.id from less_planning.localization loc where loc.code = 'ui.header.lesson.add'),
    (select u.id from less_planning.user u where u.login = 'admin'),
    now(),
    'Lesson add');