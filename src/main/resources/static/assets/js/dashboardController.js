app.controller("dashboardController", function ($scope, $http, $document, $cookies) {
    $scope.totalGames = 0;
    $scope.totalGameLastWeek = 0;
    $scope.percentageChange = 0;
    $scope.initialize = function () {
        $http.get("/api/v1/game/totalGame", {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get('token')
            }
        }).then(resp => {
            console.log(resp.data)
            $scope.totalGames = resp.data;

            $http.get(`/api/v1/game/totalGameLastWeek`, {
                headers: {
                    'Authorization': 'Bearer ' + $cookies.get('token')
                }
            }).then(r => {
                console.log(r.data)
                $scope.totalGameLastWeek = r.data;

                // Hàm tính % sự thay đổi
                $scope.calculatePercentageChange = function (currentValue, previousValue) {
                    if (previousValue === 0) {
                        return currentValue > 0 ? 100 : 0;
                    }

                    const percentageChange = ((currentValue - previousValue) / previousValue) * 100;
                    return percentageChange.toFixed(1); // Làm tròn đến 2 chữ số thập phân
                };

                // Tính % sự thay đổi và gán vào biến $scope
                $scope.percentageChange = $scope.calculatePercentageChange($scope.totalGames, $scope.totalGameLastWeek);
            });
        });
    }

    $scope.initialize();
});