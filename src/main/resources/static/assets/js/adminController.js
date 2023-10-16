app.controller("mainController", function ($scope, $routeParams) {

});

app.controller("gameController", function ($scope, $http, $document,$cookies) {
    $scope.games = [];
    $scope.initialize = function () {
        $http.get("/api/game/findALl",
            {
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken")
                }
            }).then(resp => {console.log(resp.data);
            $scope.games = resp.data;
            console.log($scope.games)
        })
            .catch(error => {
                console.log("Error", error);
            });
    }
    $scope.initialize();
})