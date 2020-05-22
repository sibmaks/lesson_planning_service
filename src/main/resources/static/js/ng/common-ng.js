const commonModule = angular.module('lessonPlanning', []);
commonModule.controller('CommonController', function ($scope) {
        $scope.page = window.location.pathname;
        $scope.allowedActions = allowedActions;
        $scope.translations = translations;

        $scope.doLogout = function () {
            $('button#sing_out').prop("disabled", true);
            $.ajax({
                type: "POST",
                url: '/v3/user/logout',
                data: JSON.stringify({}),
                success: $scope.handleLogoutResponse,
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            });
        }

        $scope.handleLogoutResponse = function(data) {
            $scope.$apply(function () {
                $('button#sing_out').prop("disabled", false);
                $.cookie('X-User-Session-Id', '', { expires: -1});
                window.location.replace("/");
            });
        }
    }
);

commonModule.directive( 'elemReady', function( $parse ) {
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

function isEmpty(line) {
    return line === null || line === undefined || line.trim().length === 0;
}

function isNull(obj) {
    return obj === null || obj === undefined;
}