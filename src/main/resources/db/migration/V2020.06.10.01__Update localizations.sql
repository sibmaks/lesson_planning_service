update less_planning.translation set translation='Добавить ученика',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null) and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.child.add');

update less_planning.translation set translation='Добавить ученика',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='rus') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.child.add');

update less_planning.translation set translation='Add student',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='eng') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.child.add');
    
update less_planning.translation set translation='Редактирование информации о ученике',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null) and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.child.edit');

update less_planning.translation set translation='Редактирование информации об ученике',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='rus') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.child.edit');

update less_planning.translation set translation='Edit student info',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='eng') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.header.child.edit');