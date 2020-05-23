const lPCModule = angular.module('lessonPlanningLesson', ['lessonPlanning']);

moment.locale(locale);

lPCModule.controller('LessonController', function ($scope) {
        $scope.lesson = lesson;
        $scope.childrenScheduling = childrenScheduling;

        $scope.initRequest = function() {
            $scope.requestEntity = {
                id: $scope.lesson.id,
                courseInfo: $scope.lesson.courseInfo,
                dayOfWeek: $scope.lesson.dayOfWeek,
                timeStart: $scope.lesson.timeStart,
                timeEnd: $scope.lesson.timeEnd,
                lessonStartDate: $scope.lesson.lessonStartDate,
                lessonEndDate: $scope.lesson.lessonEndDate,
                children: []
            };

            for(const key in $scope.lesson.children) {
                $scope.requestEntity.children.push($scope.lesson.children[key])
            }

            if($scope.lesson.children === null || $scope.lesson.children === undefined) {
                $scope.lesson.children = [];
            }
            for(const key in $scope.childrenScheduling) {
                $scope.lesson.children.push($scope.childrenScheduling[key].child)
            }
        }

        $scope.initRequest();

        $scope.initStartTime = function () {
            const field = $('#timeStartInput');
            const val = isEmpty(field.val()) ? moment('12:00', moment.HTML5_FMT.TIME_SECONDS) : moment(field.val(), moment.HTML5_FMT.TIME_SECONDS);
            field.datetimepicker({
                format: 'HH:mm',
                date: val,
                enabledHours: [6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
                stepping: 30
            });
        }

        $scope.initEndTime = function () {
            const startField = $('#timeStartInput');
            const endField = $('#timeEndInput');
            const startVal = moment(startField.val(), moment.HTML5_FMT.TIME_SECONDS);
            const val = isEmpty(endField.val()) ? moment('13:00', moment.HTML5_FMT.TIME_SECONDS) : moment(endField.val(), moment.HTML5_FMT.TIME_SECONDS);
            $(endField).datetimepicker({
                format: 'HH:mm',
                date: val,
                enabledHours: [6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
                stepping: 30
            });
            endField.datetimepicker('minDate', startVal.add(30, 'minutes'));
            startField.datetimepicker('maxDate', val.add(-30, 'minutes'));

            startField.on("change.datetimepicker", function (e) {
                endField.datetimepicker('minDate', e.date.add(30, 'minutes'));
            });
            endField.on("change.datetimepicker", function (e) {
                startField.datetimepicker('maxDate', e.date.add(-30, 'minutes'));
            });
        }

        $scope.initStartDate = function () {
            const field = $('#dateStartInput');
            const val = isEmpty(field.val()) ? moment() : moment(field.val(), 'DD-MM-YYYY');
            field.datetimepicker({
                format: 'DD-MM-YYYY',
                date: val,
                locale: locale
            });
        }

        $scope.initEndDate = function () {
            const startField = $('#dateStartInput');
            const endField = $('#dateEndInput');
            const startVal = moment(startField.val(), 'DD-MM-YYYY');
            const val = isEmpty(endField.val()) ? null : moment(endField.val(), 'DD-MM-YYYY');
            if(val === null) {
                $(endField).datetimepicker({
                    format: 'DD-MM-YYYY',
                    locale: locale
                });
            } else {
                $(endField).datetimepicker({
                    format: 'DD-MM-YYYY',
                    date: val,
                    locale: locale
                });
            }
            endField.datetimepicker('minDate', startVal.add(1, 'day'));
            if(val !== null) {
                startField.datetimepicker('maxDate', val.add(-1, 'day'));
            }

            startField.on("change.datetimepicker", function (e) {
                endField.datetimepicker('minDate', e.date.add(1, 'day'));
            });
            endField.on("change.datetimepicker", function (e) {
                startField.datetimepicker('maxDate', e.date.add(-1, 'day'));
            });
        }

        $scope.clearEndDate = function() {
            $('#dateEndInput').val('');
            $('#dateStartInput').datetimepicker('maxDate', false);
        }

        $scope.doAddRow = function() {
            $scope.requestEntity.children.push({id: null});
        }

        $scope.doRemove = function(child) {
            if(!isNull($scope.requestEntity.children)) {
                for (let i = 0, size = $scope.requestEntity.children.length; i < size; i++) {
                    const iInfo = $scope.requestEntity.children[i];
                    if (iInfo === child) {
                        $scope.requestEntity.children.splice(i, 1);
                        break;
                    }
                }
            }
        }

        $scope.changeChild = function() {
            if(!isNull($scope.requestEntity.children)) {
                for (let i = 0, size = $scope.requestEntity.children.length; i < size; i++) {
                    const iInfo = $scope.requestEntity.children[i];
                    if (isNull(iInfo.id)) {
                        continue;
                    }
                    let duplicate = false;
                    for (let j = 0; j < i; j++) {
                        const jInfo = $scope.requestEntity.children[j];
                        if (isNull(jInfo.id)) {
                            continue;
                        }
                        if (iInfo.id === jInfo.id) {
                            iInfo.duplicate = true;
                            duplicate = true;
                        }
                    }
                    if (!duplicate) {
                        iInfo.duplicate = false;
                    }
                }
            }
        }

        $scope.doSave = function () {
            $('button#save_lesson').prop("disabled", true);

            if (isNull($scope.lesson.id)) {
                $.ajax({
                    type: "POST",
                    url: '/v3/lesson/add',
                    data: JSON.stringify({lessonEntity: $scope.requestEntity}),
                    success: $scope.handleLessonModifyResponse,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                });
            } else {
                $.ajax({
                    type: "POST",
                    url: '/v3/lesson/update',
                    data: JSON.stringify({lessonEntity: $scope.requestEntity}),
                    success: $scope.handleLessonModifyResponse,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                });
            }
        }

        $scope.handleLessonModifyResponse = function (data) {
            $scope.error = null;
            $scope.$apply(function () {
                $('button#save_lesson').prop("disabled", false);
                const responseCode = data.responseInfo.resultCode;
                const responseMessage = data.responseInfo.message;
                if (responseCode === "Ok") {
                    if(data.redirectUrl !== null && data.redirectUrl !== undefined) {
                        window.location.replace(data.redirectUrl);
                    }
                    if (responseMessage !== undefined && responseMessage !== null) {
                        $scope.resultInfo = responseMessage;
                    }
                } else if (responseCode === "ConstraintException") {
                    if (!isNull(data.constrains['lessonEntity.id'])) {
                        document.location.reload(true);
                    } else {
                        $scope.constraints = data.constrains;
                    }
                } else if (!isNull(responseMessage)) {
                    $scope.error = responseMessage;
                }
            });
        }
    }
);