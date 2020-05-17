const lPCModule = angular.module('lessonPlanningScheduling', ['lessonPlanning']);

moment.locale(locale);

lPCModule.controller('SchedulingController', function ($scope) {
        $scope.courses = courses;
        $scope.translations = translations;
        $scope.loadRequest = {courseId: $scope.courses[0].id};


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
            const endTime = moment.utc('19:00', 'HH:mm');

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

            for(let i = 0, size = list?.length; i < size; i++) {
                const item = list[i];
                const timeStart = moment.utc(item.timeStart, 'HH:mm:ss');
                const timeEnd = moment.utc(item.timeEnd, 'HH:mm:ss');
                if(timeEnd > timeRange.from && timeStart < timeRange.to) {
                    result.push(item);
                }
            }

            return result;
        }

        $scope.filterDuplicateChildren = function(list) {
            const result = [];

            for(let i = 0, size = list?.length; i < size; i++) {
                const iItem = list[i];
                let exist = false;
                for(let j = 0, sizeResult = result.length; j < sizeResult; j++) {
                    if(result[j].child.id === iItem.child.id) {
                        exist = true;
                        break;
                    }
                }
                if(!exist) {
                    result.push(iItem);
                }
            }

            return result;
        }

        $scope.doLoad = function () {
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

        $scope.handleLoadSchedulingResponse = function (data) {
            $scope.error = null;
            $scope.$apply(function () {
                const responseCode = data?.responseInfo?.resultCode;
                const responseMessage = data?.responseInfo?.message;
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
                const responseCode = data?.responseInfo?.resultCode;
                const responseMessage = data?.responseInfo?.message;
                if (responseCode === "Ok") {
                    $scope._lessons = data.lessons;
                    $scope.lessonGetSuccess = true;
                    $scope.handleAsyncResult();
                } else if (!isNull(responseMessage)) {
                    $scope.error = responseMessage;
                }
            });
        }

        $scope.sprintf = function(string, args) {
            return sprintf(string, args);
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
    }
);