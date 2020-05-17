alter table if exists less_planning.locale add column locale_code varchar(16);

update less_planning.locale set locale_code = 'ru' where country_iso3 is null and language_iso3 = 'rus';
update less_planning.locale set locale_code = 'en' where country_iso3 is null and language_iso3 = 'eng';
update less_planning.locale set locale_code = 'en' where country_iso3 is null and language_iso3 is null;