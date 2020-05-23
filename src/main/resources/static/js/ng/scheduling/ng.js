const lPCModule = angular.module('lessonPlanningScheduling', ['lessonPlanning']);

moment.locale(locale);

lPCModule.controller('SchedulingController', function ($scope) {
        $scope.courses = courses;
        $scope.translations = translations;
        $scope.loadRequest = {courseId: (isNull($scope.courses) || $scope.courses.length === 0 ? null : $scope.courses[0].id)};


        $scope.changeMonth = function(monthVal) {
            const month = Number(monthVal);
            $scope.fromDate  = moment.utc().set('month', month).startOf('month');
            $scope.toDate = moment.utc().set('month', month).endOf('month');
            $scope.loadRequest.fromDate = $scope.fromDate.format('DD-MM-YYYY');
            $scope.loadRequest.toDate = $scope.toDate.format('DD-MM-YYYY');
        }

        $scope.init = function() {
            const months = moment.months();
            const currentMonth = moment.utc().month();
            $scope.months = [];
            for(let i = 0; i < months.length; i++) {
                $scope.months.push({id: i, val: months[i]});
                if(i === currentMonth) {
                    $scope.month = $scope.months[i].id;
                    $scope.changeMonth($scope.months[i].id);
                }
            }

            const startTime = moment.utc('06:00', 'HH:mm');
            const endTime = moment.utc('20:00', 'HH:mm');

            const timeRanges = [];

            for(let time = moment.utc(startTime); time < endTime; time.add('hour', 1)) {
                const endRangeTime = moment.utc(time).add('hour', 1);
                if(endRangeTime > endTime) {
                    continue;
                }
                timeRanges.push({from: moment.utc(time), to: endRangeTime});
            }

            $scope.timeRanges = timeRanges;
        }

        $scope.init();

        $scope.stringToDate = function(txt) {
            return txt === null || txt === undefined || txt === '' ? null : moment.utc(txt, 'DD-MM-YYYY');
        }

        $scope.filterTimeRange = function(timeRange, list) {
            const result = [];

            if(!isNull(list)) {
                for (let i = 0, size = list.length; i < size; i++) {
                    const item = list[i];
                    const timeStart = moment.utc(item.timeStart, 'HH:mm');
                    const timeEnd = moment.utc(item.timeEnd, 'HH:mm');
                    if (timeEnd > timeRange.from && timeStart < timeRange.to) {
                        result.push(item);
                    }
                }
            }

            return result;
        }

        $scope.filterDuplicateChildren = function(list) {
            const result = [];

            if(!isNull(list)) {
                for (let i = 0, size = list.length; i < size; i++) {
                    const iItem = list[i];
                    let exist = false;
                    for (let j = 0, sizeResult = result.length; j < sizeResult; j++) {
                        if (result[j].child.id === iItem.child.id) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        result.push(iItem);
                    }
                }
            }

            return result;
        }

        $scope.doLoad = function () {
            if(!isNull($scope.loadRequest.courseId) && $scope.loadRequest.courseId >= 1) {
                $('button#load_scheduling').prop("disabled", true);
                $('select#month_select').prop("disabled", true);
                $scope.lessonGetSuccess = false;
                $scope.schedulingGetSuccess = false;
                $.ajax({
                    type: "POST",
                    url: '/v3/scheduling/get',
                    data: JSON.stringify($scope.loadRequest),
                    success: $scope.handleLoadSchedulingResponse,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                });

                $.ajax({
                    type: "POST",
                    url: '/v3/lesson/get',
                    data: JSON.stringify($scope.loadRequest),
                    success: $scope.handleLoadLessonsResponse,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                });
            }
        }

        $scope.handleLoadSchedulingResponse = function (data) {
            $scope.error = null;
            $scope.$apply(function () {
                const responseCode = data.responseInfo.resultCode;
                const responseMessage = data.responseInfo.message;
                if (responseCode === "Ok") {
                    $scope._dateRange = [];
                    $scope._dayOfWeekScheduling = data.dayOfWeekScheduling;
                    for(let dayDate  = moment.utc($scope.fromDate); dayDate < $scope.toDate; dayDate.add('day', 1)) {
                        $scope._dateRange.push(moment.utc(dayDate));
                    }
                    $scope.schedulingGetSuccess = true;
                    $scope.handleAsyncResult();
                } else if (!isNull(responseMessage)) {
                    $scope.error = responseMessage;
                }
            });
        }

        $scope.handleLoadLessonsResponse = function (data) {
            $scope.error = null;
            $scope.$apply(function () {
                const responseCode = data.responseInfo.resultCode;
                const responseMessage = data.responseInfo.message;
                if (responseCode === "Ok") {
                    $scope._lessons = data.lessons;
                    $scope.lessonGetSuccess = true;
                    $scope.handleAsyncResult();
                } else if (!isNull(responseMessage)) {
                    $scope.error = responseMessage;
                }
            });
        }

        $scope.handleAsyncResult = function () {
            if($scope.lessonGetSuccess && $scope.schedulingGetSuccess) {
                $('button#load_scheduling').prop("disabled", false);
                $('select#month_select').prop("disabled", false);

                $scope.dateRange = $scope._dateRange;
                $scope.dayOfWeekScheduling = $scope._dayOfWeekScheduling;

                $scope.lessons = $scope._lessons;
            }
        }

        $scope.initPopover = function (popoverId, day, timeLesson) {
            let body = "";
            for(const key in timeLesson.children) {
                const child = timeLesson.children[key];
                body += `<p>${child.name}, ${child.parentName} - ${child.phone}</p>\n`
            }
            if(body.length !== 0) {
                body += `
                <form class="text-right" method="get" action="/lesson/">
                    <input type="hidden" name="mode" value="update"/>
                    <input type="hidden" name="lesson" value="${timeLesson.id}"/>
                    <input type="hidden" name="lessonDate" value="${day.format('DD-MM-YYYY')}"/>
                    <button type="submit" class="btn btn-outline-primary">
                    <i class="far fa-edit"></i>
                    </button>
                </form>`
            }
            $(popoverId).popover({
                content: body,
                html: true
            });
        }

        $scope.initSchedulingPopover = function (popoverId, day, timeRange, schedulingChildren) {
            let body = "";
            for(const key in schedulingChildren) {
                const schedulingChild = schedulingChildren[key];
                body += `<p>${schedulingChild.child.name}, ${schedulingChild.child.parentName} - ${schedulingChild.child.phone}
                                            (${schedulingChild.timeStart}-${schedulingChild.timeEnd})</p>\n`
            }
            if(body.length !== 0) {
                body += `
<form class="text-right" method="get" action="/lesson/">
    <input type="hidden" name="mode" value="add"/>
    <input type="hidden" name="courseId" value="${$scope.loadRequest.courseId}"/>
    <input type="hidden" name="dayOfWeek" value="${day.format('E')}"/>
    <input type="hidden" name="timeStart" value="${timeRange.from.format('HH:mm')}"/>
    <input type="hidden" name="timeEnd" value="${timeRange.to.format('HH:mm')}"/>
    <input type="hidden" name="lessonDate" value="${day.format('DD-MM-YYYY')}"/>
    <button type="submit" class="btn btn-outline-primary">
        <i class="fas fa-plus"></i>
    </button>
</form>`
            }
            $(popoverId).popover({
                content: body,
                html: true
            });
        }
    }
);