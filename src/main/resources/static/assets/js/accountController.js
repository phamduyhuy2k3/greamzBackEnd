app.controller("userController", function ($scope, $http, $document, $cookies) {
    $scope.accounts = [];
    $scope.authorities = [];
    $scope.action = 'create'
    $scope.form = {
        id: '',
        username: '',
        password: '',
        fullname: '',
        email: '',
        photo: '',
        isEnabled: false,
        vouchers: [],
        oders: [],
        reviews: [],
        discusios: [],
        authorities: [],
    }
    $scope.initialize = function () {
        $http.get("/api/user/findAll", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(
            resp => {
                $scope.accounts = resp.data;
                console.log($scope.accounts)
            },
            error => {
                console.log("Error", error);
            }
        )

        $http.get("/api/user/authorities", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(
            resp => {
                $scope.authorities = resp.data;
                console.log($scope.authorities)
            },
            error => {
                console.log("Error", error);
            }
        )
    }
    $scope.initialize();

    $scope.delete = function (item) {
        if (confirm("Do you want to delete this account?")) {
            $http.delete(`/api/user/delete/${item.id}`, {
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken")
                }
            }).then(resp => {
                var index = $scope.items.findIndex(p => p.id === item.id);
                $scope.items.splice(index, 1);
                alert("The account was deleted successfully!");
            }).catch(error => {
                alert("Error deleting account!");
                console.log("Error", error);
            })
        }
    }

    $scope.toggleSelection = function toggleSelection(authority) {
        var idx = $scope.authorities.indexOf(authority);

        // Is currently selected
        if (idx > -1) {
            $scope.authorities.splice(idx, 1);
        }

        // Is newly selected
        else {
            $scope.authorities.push(authority);
        }
    };
    $scope.reset = function () {
        $scope.form = {
            id: '',
            username: '',
            password: '',
            fullname: '',
            email: '',
            photo: '',
            isEnabled: false,
            vouchers: [],
            oders: [],
            reviews: [],
            discusios: [],
            authorities: [],
        }
        $scope.action = 'create';
        $scope.initialize();
    }

    $scope.create = function () {
        $http.post("/api/user/create", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            },
            data: $scope.form
        }).then(
            resp => {
                alert("")
                $scope.initialize();
            },
            error =>{
                console.log(error)
            }
        )
    }

})