const lPCModule = angular.module('lessonPlanningChild', ['lessonPlanning']);

lPCModule.controller('ChildController', function ($scope) {
        $scope.startDatetimepickerTemplate = "startDatetimepicker";
        $scope.endDatetimepickerTemplate = "endDatetimepicker";
        $scope.children = children;
        $scope.courses = courses;
        $scope.dateTimePickerIndex = 0;

        $scope.showList = function () {
            $scope.pageHeader = translations['ui.header.children'].translation;
            $scope.constraints = {};
            $scope.mode = 'list';
            $scope.child = {};
        }

        $scope.showList();

        $scope.initAndIncDateTimePickerIndex = function (field) {
            if (isNull(field.dateTimePickerIndex)) {
                field.dateTimePickerIndex = $scope.dateTimePickerIndex;
                $scope.dateTimePickerIndex += 1;
            }
        }

        $scope.doEdit = function (id) {
            for (const item in $scope.children) {
                if ($scope.children[item].childInfo.id === id) {
                    $scope.child = $scope.children[item];
                    $scope.pageHeader = translations['ui.header.child.edit'].translation;
                    $scope.mode = 'modify';
                    break;
                }
            }
        }

        $scope.doAdd = function () {
            $scope.child = {};
            $scope.pageHeader = translations['ui.header.child.add'].translation;
            $scope.mode = 'modify';
        }

        $scope.initStartDate = function (id) {
            const field = $('#' + $scope.startDatetimepickerTemplate + id);
            const val = isEmpty(field.val()) ? moment('12:00', moment.HTML5_FMT.TIME_SECONDS) : moment(field.val(), moment.HTML5_FMT.TIME_SECONDS);
            field.datetimepicker({
                format: 'HH:mm',
                date: val,
                enabledHours: [6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19],
                stepping: 30
            });
        }

        $scope.initEndDate = function (id) {
            const startField = $('#' + $scope.startDatetimepickerTemplate + id);
            const endField = $('#' + $scope.endDatetimepickerTemplate + id);
            const startVal = moment(startField.val(), moment.HTML5_FMT.TIME_SECONDS);
            const val = isEmpty(endField.val()) ? moment('13:00', moment.HTML5_FMT.TIME_SECONDS) : moment(endField.val(), moment.HTML5_FMT.TIME_SECONDS);
            $(endField).datetimepicker({
                format: 'HH:mm',
                date: val,
                enabledHours: [6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19],
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

        $scope.doAddRow = function () {
            if (isNull($scope.child.courseSchedulingInfos)) {
                $scope.child.courseSchedulingInfos = [];
            }
            $scope.child.courseSchedulingInfos.push({
                courseInfo: Object.assign({}, $scope.courses[0]),
                dayOfWeek: 1
            });
        }

        $scope.doRemove = function (courseSchedulingInfo) {
            if (!isNull($scope.child)) {
                for (let i = 0; i < $scope.child.courseSchedulingInfos.length; i++) {
                    if ($scope.child.courseSchedulingInfos[i] === courseSchedulingInfo) {
                        $scope.child.courseSchedulingInfos.splice(i, 1);
                        break;
                    }
                }
            }
        }

        $scope.doSave = function () {
            $('button#save_child').prop("disabled", true);

            if (isNull($scope.child?.childInfo?.id)) {
                $.ajax({
                    type: "POST",
                    url: '/v3/child/add',
                    data: JSON.stringify($scope.child),
                    success: $scope.handleChildModifyResponse,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                });
            } else {
                $.ajax({
                    type: "POST",
                    url: '/v3/child/update',
                    data: JSON.stringify($scope.child),
                    success: $scope.handleChildModifyResponse,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                });
            }
        }

        $scope.handleChildModifyResponse = function (data) {
            $scope.error = null;
            $scope.$apply(function () {
                $('button#save_child').prop("disabled", false);
                const responseCode = data?.responseInfo?.resultCode;
                const responseMessage = data?.responseInfo?.message;
                if (responseCode === "Ok") {
                    if (isNull($scope.child.childInfo.id)) {
                        $scope.children.push({
                            childInfo: data.childInfo,
                            courseSchedulingInfos: data.courseSchedulingInfos
                        });
                    }
                    for(let i = 0, size = $scope.child?.courseSchedulingInfos?.length; i < size; i++) {
                        $scope.child.courseSchedulingInfos[i].duplicate = null;
                    }
                    $scope.showList();
                } else if (responseCode === "ConstraintException") {
                    if (!isNull(data.constrains['childInfo.id'])) {
                        document.location.reload(true);
                    } else {
                        $scope.constraints = data.constrains;
                    }
                } else if (responseCode === "Duplicates") {
                    if (!isNull(responseMessage)) {
                        $scope.error = responseMessage;
                    }

                    for(let i = 0, size = $scope.child?.courseSchedulingInfos?.length; i < size; i++) {
                        const iInfo = $scope.child?.courseSchedulingInfos[i];
                        for(let j = 0; j < i; j++) {
                            const jInfo = $scope.child?.courseSchedulingInfos[j];
                            if(Number(iInfo.courseInfo.id) === Number(jInfo.courseInfo.id) &&
                                Number(iInfo.dayOfWeek) === Number(jInfo.dayOfWeek) &&
                                iInfo.timeStart === jInfo.timeStart && iInfo.timeEnd === jInfo.timeEnd) {
                                iInfo.duplicate = true;
                                break;
                            }
                        }
                    }
                } else if (!isNull(responseMessage)) {
                    $scope.error = responseMessage;
                }
            });
        }
    }
);

lPCModule.directive( 'elemReady', function( $parse ) {
    return {
        restrict: 'A',
        link: function( $scope, elem, attrs ) {
            elem.ready(function(){
                $scope.$apply(function(){
                    const func = $parse(attrs.elemReady);
                    func($scope);
                })
            })
        }
    }
})