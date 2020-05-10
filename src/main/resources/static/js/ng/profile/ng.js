angular.module('lessonPlanningProfile', ['lessonPlanning'])
    .controller('ProfileController', function ($scope) {
            $scope.translations = translations;
            $scope.userInfo = userInfo;

            $scope.doSave = function () {
                $('button#save_profile').prop("disabled", true);
                $.ajax({
                    type: "POST",
                    url: '/v3/profile/changeProfile',
                    data: JSON.stringify({userInfo: $scope.userInfo}),
                    success: $scope.handleChangeProfileResponse,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                });
            }

            $scope.handleChangeProfileResponse = function(data) {
                $scope.$apply(function () {
                    $('button#save_profile').prop("disabled", false);
                    const responseCode = data?.responseInfo?.resultCode;
                    const responseMessage = data?.responseInfo?.message;
                    if (responseCode === "Ok") {
                        if (responseMessage !== undefined && responseMessage !== null) {
                            $scope.resultInfo = responseMessage;
                        }
                    } else if (responseMessage !== undefined && responseMessage !== null) {
                        $scope.error = responseMessage;
                    }
                });
            }
        }
    );