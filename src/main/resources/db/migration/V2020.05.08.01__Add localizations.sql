create sequence less_planning.locale_id_seq start 1 increment 50;
create sequence less_planning.localization_id_seq start 1 increment 50;

create table less_planning.locale (id int8 not null, country_iso3 varchar(255), language_iso3 varchar(255), primary key (id));
create table less_planning.localization (id int8 not null, code varchar(255), primary key (id));
create table less_planning.translation (translation varchar(255), update_date_time timestamp, locale_id int8 not null, localization_id int8 not null, editor_id int8, primary key (locale_id, localization_id));

alter table if exists less_planning.localization add constraint UK_localization_code_unique unique (code);
alter table if exists less_planning.translation add constraint FKkmag1c0rj5p2rqickfpgq8hvx foreign key (locale_id) references less_planning.locale;
alter table if exists less_planning.translation add constraint FKtljbk7a5dmnb4umm4ra4j2tsh foreign key (localization_id) references less_planning.localization;
alter table if exists less_planning.translation add constraint FKfs0jhcs19neqs921gfycnsvyg foreign key (editor_id) references less_planning.user;
