app.controller("userController", function ($scope, $http, $document, $cookies) {
    $scope.accounts = [];
    $scope.roles = [];
    $scope.action = 'create'
    $scope.action = 'edit'
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

        $scope.$apply(function () {
            $scope.form = $scope.accounts.find(value => value.id === id)
        })

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
        return account.authorities.map(value => value.role).includes(authority)
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
    $scope.checkFormAuthority = function (authority) {
        return $scope.form.authorities.map(value => value.role).includes(authority)
    }
    $scope.authority_of = function (acc, role) {
        if ($scope.authorities) {
            return $scope.authorities.find(ur => ur.account.id == acc.id && ur.role == role);
        }
    }

    $scope.authority_changed = function (acc, role) {
        var authority = $scope.authority_of(acc, role);
        if (authority) { // đã cấp quyền => thu hồi quyền (xóa)
            $scope.revoke_authority(authority);
        } else { // chưa được cấp quyền => cấp quyền (thêm mới)
            authority = {account: acc, role: role};
            $scope.grant_authority(authority);
        }
    }

    // Thêm mới authority
    $scope.grant_authority = function (authority) {
        $http.post(`/rest/authorities`, authority).then(resp => {
            $scope.roles.push(resp.data)
            alert("Cấp quyền sử dụng thành công");
        }).catch(error => {
            alert("Cấp quyền sử dụng thất bại");
            console.log("Error", error);
        })
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
    }
    // Xóa authority
    $scope.revoke_authority = function (authority) {
        $http.delete(`/rest/authorities/${authority.id}`).then(resp => {
            var index = $scope.roles.findIndex(a => a.id == authority.id);
            $scope.roles.splice(index, 1);
            alert("Thu hồi quyền sử dụng thành công");
        }).catch(error => {
            alert("Thu hồi quyền sử dụng thất bại");
            console.log("Error", error);
        })
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
})