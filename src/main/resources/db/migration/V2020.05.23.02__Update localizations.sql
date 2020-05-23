update less_planning.translation set translation='Произошла ошибка в работе сервиса, обратитесь к администратору',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3 is null) and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.text');

update less_planning.translation set translation='Произошла ошибка в работе сервиса, обратитесь к администратору',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='rus') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.text');

update less_planning.translation set translation='There was an error in the service, contact the administrator',
    update_date_time=now() where
    locale_id=(select loc.id from less_planning.locale loc where loc.country_iso3 is null and loc.language_iso3='eng') and
    localization_id=(select loc.id from less_planning.localization loc where loc.code = 'ui.error.500.text');