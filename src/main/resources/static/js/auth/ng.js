angular.module('authApp', [])
    .controller('AuthorizationController', function ($scope) {
            $scope.codes = codes;
            $scope.language = language;
            $scope.translations = translations;

            $scope.changeLanguage = function (lang) {
                $.ajax({
                    type: "POST",
                    url: '/v3/translation/get',
                    data: JSON.stringify({languageIso3: lang, codes: $scope.codes}),
                    success: $scope.handleChangeLanguageResponse,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                });
            }

            $scope.handleChangeLanguageResponse = function (data) {
                $scope.$apply(function () {
                    if(data?.responseInfo?.resultCode === "Ok") {
                        $scope.language = data.translations[$scope.codes[0]].languageISO3;
                        $scope.translations = data.translations;
                    }
                })
            }
        }
    );