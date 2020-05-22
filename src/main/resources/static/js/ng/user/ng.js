const lPCModule = angular.module('lessonPlanningUser', ['lessonPlanning']);

lPCModule.controller('UserController', function ($scope) {
        $scope.users = users;
        $scope.roles = roles;

        $scope.showList = function () {
            $scope.pageHeader = translations['ui.header.user.list'].translation;
            $scope.mode = 'list';
        }

        $scope.showList();

        $scope.doAdd = function () {
            $scope.registerRequest = {userInfo: {}};
            $scope.constraints = {};
            $scope.pageHeader = translations['ui.header.user.add'].translation;
            $scope.mode = 'add';
        }

        $scope.doAddRow = function () {
            if (isNull($scope.registerRequest.roles)) {
                $scope.registerRequest.roles = [];
            }
            $scope.registerRequest.roles.push({id: null});
        }

        $scope.doRemove = function (role) {
            if (!isNull($scope.child)) {
                for (let i = 0; i < $scope.registerRequest.roles.length; i++) {
                    if ($scope.registerRequest.roles[i] === role) {
                        $scope.registerRequest.roles.splice(i, 1);
                        break;
                    }
                }
            }
        }

        $scope.doSave = function () {
            $('button#save_user').prop("disabled", true);
            $.ajax({
                type: "POST",
                url: '/v3/user/register',
                data: JSON.stringify($scope.registerRequest),
                success: $scope.handleUserRegisterResponse,
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            });
        }

        $scope.handleUserRegisterResponse = function (data) {
            $scope.error = null;
            $scope.$apply(function () {
                $('button#save_user').prop("disabled", false);
                const responseCode = data?.responseInfo?.resultCode;
                const responseMessage = data?.responseInfo?.message;
                if (responseCode === "Ok") {
                    $scope.users.push(data.userInfo);
                    $scope.showList();
                } else if (responseCode === "ConstraintException") {
                    $scope.constrains = data.constrains;
                } else if (!isNull(responseMessage)) {
                    $scope.error = responseMessage;
                }
            });
        }
    }
);