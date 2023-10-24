app.controller("userController", function ($scope, $http, $document, $cookies) {
    $scope.accounts = [];
    $scope.authorities = [];
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

        $http.get("/api/user/authorities", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(
            resp => {
                $scope.authorities = resp.data;


            },
            error => {
                console.log("Error", error);
            }
        )
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

    $scope.toggleSelection = function(authority) {
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
        $http.post("/api/user/save", $scope.form,{
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
        // $http.get(`/api/user/findById/${id}`,
        //     {
        //     headers: {
        //         "Authorization": "Bearer " + $cookies.get("accessToken")
        //     },
        // }).then(
        //     resp => {
        //         $scope.form = resp.data;
        //         $scope.action = 'edit';
        //         console.log($scope.form)
        //     },
        //     error => {
        //         console.log(error)
        //     }
        // )
        $scope.form = $scope.accounts.find( value => value.id === id)
    }
    $scope.setAuthority= function (account,value,authority){
        if(value===true){
            $http.post(`/api/v1/authority/save`, {
                userId : account.id,
                role: authority
            },{
                headers: {
                    'Authorization': 'Bearer ' + $cookies.get('accessToken'),
                    "Content-Type": "application/json"

                }
            }).then( resp => {
                $scope.initialize();
            })

        }else{
            $http.delete(`/api/v1/authority/delete`,{
                headers: {
                    'Authorization': 'Bearer ' + $cookies.get('accessToken'),

                },
                params:{
                    userId : account.id,
                    role: authority
                }
            }).then( resp => {
                $scope.initialize();
            })
        }
    }
    $scope.checkAuthority= function (account,authority){
        return account.authorities.map( value => value.role).includes(authority)
    }
    $scope.setFormAuthority= function (value,authority){
        if(value===true){
            $scope.form.authorities.push({
                role: authority,
                authority: authority
            })

        }else{
            $scope.form.authorities = $scope.form.authorities.filter( value => value.role !== authority)
        }
    }
    $scope.checkFormAuthority= function (authority){
        return $scope.form.authorities.map( value => value.role).includes(authority)
    }

})