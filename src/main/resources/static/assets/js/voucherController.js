app.controller("voucherController", function ($scope, $http, $document, $cookies) {
    $scope.vouchers = [];

    $scope.action = 'create'
    $scope.voucher = {
        id: null,
        name: '',
        description: '',
        dateExpired: ''
    }

    $scope.create = function () {
        console.log($scope.voucher)
        $http.post("/api/voucher/create", $scope.voucher,{
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken"),
                "Content-Type": "application/json"
            }
        }).then(
            resp => {
                $scope.initialize()
            },
            error => {
            }
        )
    }
    $scope.delete = function () {
        $http.delete("/api/voucher/delete/" + $scope.voucher.id, {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            },

        }).then(
            resp => {
                $scope.initialize()
            },
            error => {
            }
        )
    }
    $scope.update = function () {
        $http.p("/api/voucher/update", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            },
            data: $scope.voucher
        }).then(
            resp => {
                $scope.initialize()
            },
            error => {
            }
        )
    }

    $scope.edit = function (id) {
        $scope.voucher = $scope.vouchers.find(value => value.id === id)
    }
    $scope.initialize = function () {
        $http.get("/api/voucher/findALl", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(
            resp => {
                $scope.vouchers = resp.data;
            },
            error => {
            }
        )
    }
    $scope.initialize()

});