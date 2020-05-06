alter table if exists less_planning.lesson add constraint UK_lesson_must_be_unique
 unique (day_of_week, time_end, time_start, course_id, teacher_id);