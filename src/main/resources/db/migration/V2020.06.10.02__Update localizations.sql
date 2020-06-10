update less_planning.translation set translation='Ученики',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null) and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.children');

update less_planning.translation set translation='Ученики',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='rus') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.children');

update less_planning.translation set translation='Students',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='eng') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.children');
    
update less_planning.translation set translation='Ученики',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null) and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.text.children');

update less_planning.translation set translation='Ученики',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='rus') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.text.children');

update less_planning.translation set translation='Students',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='eng') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.text.children');

update less_planning.translation set translation='Редактирование информации об ученике',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null) and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.child.edit');

update less_planning.translation set translation='ПУ: Студенты',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null) and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.title.child.list');

update less_planning.translation set translation='ПУ: Студенты',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='rus') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.title.child.list');

update less_planning.translation set translation='LP: Students',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='eng') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.title.child.list');

update less_planning.translation set translation='Учеников записано:',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null) and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.text.children_scheduled');

update less_planning.translation set translation='Учеников записано:',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='rus') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.text.children_scheduled');

update less_planning.translation set translation='Students scheduled:',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='eng') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.text.children_scheduled');

update less_planning.translation set translation='Имя ученика',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null) and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.child.name');

update less_planning.translation set translation='Имя ученика',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='rus') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.child.name');

update less_planning.translation set translation='Student name',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='eng') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.child.name');