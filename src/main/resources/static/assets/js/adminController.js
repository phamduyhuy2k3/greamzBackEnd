app.controller("mainController", function ($scope, $routeParams) {
});

app.controller("gameController", function ($scope, $http, $document) {
    $scope.games = [];
    $scope.initialize = function () {
        $http.get("/api/game/findALl").then(resp => {
            $scope.games = resp.data;
            console.log($scope.games)
        })
            .catch(error => {
                console.log("Error", error);
            });
    }
})