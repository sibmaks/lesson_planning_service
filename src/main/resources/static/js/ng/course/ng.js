angular.module('lessonPlanningCourse', ['lessonPlanning'])
    .controller('CourseController', function ($scope) {
            $scope.courses = courses;

            $scope.showList = function() {
                $scope.pageHeader = translations['ui.header.course.list'].translation;
                $scope.pageTitle = translations['ui.title.course.list'].translation;
                $scope.constraints = {};
                $scope.mode = 'list';
            }

        $scope.showList();

        $scope.doEdit = function (id) {
            for(const item in $scope.courses) {
                if($scope.courses[item].id === id) {
                    $scope.course = $scope.courses[item];
                    $scope.pageHeader = translations['ui.header.course.edit'].translation;
                    $scope.pageTitle = translations['ui.title.course.edit'].translation;
                    $scope.mode = 'modify';
                    break;
                }
            }
        }

        $scope.doAdd = function () {
            $scope.course = {};
            $scope.pageHeader = translations['ui.header.course.add'].translation;
            $scope.pageTitle = translations['ui.title.course.add'].translation;
            $scope.mode = 'modify';
        }

            $scope.doSave = function () {
                $('button#save_course').prop("disabled", true);

                if($scope.course?.id === null || $scope.course?.id === undefined) {
                    $.ajax({
                        type: "POST",
                        url: '/v3/course/add',
                        data: JSON.stringify({name: $scope.course.name}),
                        success: $scope.handleCourseModifyResponse,
                        contentType: "application/json; charset=utf-8",
                        dataType: "json"
                    });
                } else {
                    $.ajax({
                        type: "POST",
                        url: '/v3/course/update',
                        data: JSON.stringify({id: $scope.course.id, name: $scope.course.name}),
                        success: $scope.handleCourseModifyResponse,
                        contentType: "application/json; charset=utf-8",
                        dataType: "json"
                    });
                }
            }

            $scope.handleCourseModifyResponse = function(data) {
                $scope.error = null;
                $scope.$apply(function () {
                    $('button#save_course').prop("disabled", false);
                    const responseCode = data?.responseInfo?.resultCode;
                    const responseMessage = data?.responseInfo?.message;
                    if (responseCode === "Ok") {
                        if($scope.course.id === null || $scope.course.id === undefined) {
                            $scope.courses.push(data.courseInfo);
                        }
                        $scope.showList();
                    } else if (responseCode === "ConstraintException") {
                        if(data.constrains['name'] !== null && data.constrains['name'] !== undefined) {
                            $scope.constraints = data.constrains;
                        } else {
                            document.location.reload(true);
                        }
                    } else if (responseMessage !== undefined && responseMessage !== null) {
                        $scope.error = responseMessage;
                    }
                });
            }
        }
    );