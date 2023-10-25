app.controller("userController", function ($scope, $http, $document, $cookies) {
    $scope.accounts = [];
    $scope.roles = [];
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

        $http.get("/api/user/roles", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(
            resp => {
                $scope.roles = resp.data;
                console.log($scope.roles)

            },
            error => {
                console.log("Error", error);
            }
        )
        $http.get("/api/v1/authority/findAll", {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get('accessToken'),
            }
        }).then(resp => {
            $scope.authorities = resp.data;
            console.log($scope.authorities)
        })

    }
    $scope.initialize();

    $scope.delete = function (id) {
        if (confirm("Do you want to delete this account?")) {
            if (id) {
                $http.delete(`/api/user/delete/${id}`, {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken")
                    }
                }).then(resp => {
                    alert("Deleted successfully!");
                    $scope.initialize();
                }).catch(error => {
                    alert("Error deleting account!");
                    console.log("Error", error);
                });
            } else {
                alert("Error deleting account!");
            }
        }
    }

    $scope.toggleSelection = function (authority) {
        console.log(authority)
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
        $scope.form.authorities = $scope.form.authorities.map(value => {
            return {
                role: value,
                authority: value
            }
        })
        console.log($scope.form.authorities)
        $http.post("/api/user/save", $scope.form, {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken"),
                "Content-Type": "application/json"
            },
        }).then(
            resp => {
                $scope.reset();
            },
            error => {
                console.log(error)
            }
        )
    }
    $scope.edit = function (id) {
        $scope.form = $scope.accounts.find(value => value.id === id)
        $scope.form.password = '';
        $scope.action = 'update';

    }
    $scope.setAuthority = function (account, value, authority) {
        if (value === true) {
            $http.post(`/api/v1/authority/save`, {
                userId: account.id,
                role: authority
            }, {
                headers: {
                    'Authorization': 'Bearer ' + $cookies.get('accessToken'),
                    "Content-Type": "application/json"

                }
            }).then(resp => {
                $scope.reset();
            })

        } else {
            $http.delete(`/api/v1/authority/delete`, {
                headers: {
                    'Authorization': 'Bearer ' + $cookies.get('accessToken'),

                },
                params: {
                    userId: account.id,
                    role: authority
                }
            }).then(resp => {
                $scope.reset();
            })
        }
    }
    $scope.checkAuthority = function (account, authority) {
        if (account.authorities) {
            return account.authorities.findIndex(value => value.role === authority) !== -1;
        }
    }
    $scope.setFormAuthority = function (value, authority) {
        if (value === true) {
            $scope.form.authorities.push({
                role: authority,
                authority: authority
            })

        } else {
            $scope.form.authorities = $scope.form.authorities.filter(value => value.role !== authority)
        }
    }
    $scope.authority_of = function (acc, role) {

        if (acc.authorities) {
            return acc.authorities.find(ur =>  ur.role == role);
        }
    }

    $scope.authority_changed = function (acc, role,value = null) {
        if(value === null){
            let authority = $scope.checkAuthority(acc, role);
            if (authority) {
                $scope.revoke_authority(role,acc);
            } else { // chưa được cấp quyền => cấp quyền (thêm mới)
                $scope.grant_authority(role,acc);
            }
        }else {
            if (value === true) {
                $scope.revoke_authority(role,acc);
            } else {
                $scope.grant_authority(role,acc);
            }
        }

    }
    //
    // // Thêm mới authority
    $scope.grant_authority = function (authority,account) {
        $http.post(`/api/v1/authority/save`, {
            userId: account.id,
            role: authority
        }, {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get('accessToken'),
                "Content-Type": "application/json"

            }
        }).then(resp => {

        })
    }
    // // Xóa authority
    $scope.revoke_authority = function (authority,account) {
        console.log(authority)
        console.log(account)
        $http.delete(`/api/v1/authority/delete`, {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get('accessToken'),
            },
            params: {
                userId: account.id,
                role: authority
            }
        }).then(resp => {

        })
    }
    $scope.toggleSelection = function (authority) {
        var idx = $scope.form.authorities.indexOf(authority);

        // Is currently selected
        if (idx > -1) {
            $scope.form.authorities.splice(idx, 1);
        }

        // Is newly selected
        else {
            $scope.form.authorities.push(authority);
        }
        console.log($scope.form.authorities)
    };
})