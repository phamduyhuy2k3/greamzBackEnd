app.controller("voucherController", function ($scope, $http, $document, $cookies) {
    $scope.vouchers = [];

    $scope.action = 'create'
    $scope.voucher = {
        id: null,
        name: '',
        description: '',
        dateAt: '',
        dateExpired: '',
        discount: '',
        orderCondition: '',
        maxPrice: ''
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

    $scope.delete = function (item) {
                if (confirm("Bạn muốn xóa sản phẩm này?")) {
                    $http.delete(`/api/voucher/delete/${item.id}`, {
                        headers: {
                            "Authorization": "Bearer " + $cookies.get("accessToken")
                        }
                    }).then(resp => {
                        $scope.initialize();
                        // $scope.reset();
                        alert("Xóa sản phẩm thành công!");
                    }).catch(error => {
                        alert("Lỗi xóa sản phẩm!");

                        console.log("Error", error);
                    })
                }
            }


    $scope.update = function () {
        $http.p("/api/voucher/update", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            },
            data: $scope.voucher
        }).then(
            resp => {
                $scope.initialize();
            },
            error => {
            }
        )
    }




    $scope.edit = function (id) {
        $scope.voucher = id
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