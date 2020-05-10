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

            $scope.doLogin = function () {
                $('button#sing_in').prop("disabled", true);
                $.ajax({
                    type: "POST",
                    url: '/v3/user/login',
                    data: JSON.stringify({login: $scope.login, password: $scope.password,
                        languageIso3: $scope.language}),
                    success: $scope.handleLoginResponse,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                });
            }

            $scope.handleLoginResponse = function(data, textStatus, request) {
                $scope.$apply(function () {
                    $('button#sing_in').prop("disabled", false);
                    const responseCode = data?.responseInfo?.resultCode;
                    const responseMessage = data?.responseInfo?.message;
                    if (responseCode === "Ok") {
                        $.cookie('X-User-Session-Id', request.getResponseHeader('X-User-Session-Id'),
                            {expires: 7});
                        window.location.replace(data.startPageUrl);
                    } else if (responseMessage !== undefined && responseMessage !== null) {
                        $scope.error = responseMessage;
                    }
                });
            }

            $scope.handleChangeLanguageResponse = function (data) {
                $scope.$apply(function () {
                    const responseCode = data?.responseInfo?.resultCode;
                    if (responseCode === "Ok") {
                        $scope.language = data.translations[$scope.codes[0]].languageIso3;
                        $scope.translations = data.translations;
                    }
                })
            }
        }
    );