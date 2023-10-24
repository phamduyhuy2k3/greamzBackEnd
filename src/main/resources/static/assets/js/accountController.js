app.controller("userController", function ($scope, $http, $document, $cookies) {
    $scope.accounts = [];
    $scope.authorities = [];
    $scope.action = 'create'
    $scope.action = 'edit'
    // $scope.uppyImages;
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
    // $scope.uppyImages =
    //     Uppy.Core({
    //         autoProceed: false,
    //         restrictions: {
    //             minNumberOfFiles: 1,
    //             maxNumberOfFiles: 1,
    //             maxFileSize: 100000000,
    //             allowedFileTypes: ['image/*']
    //         }
    //     }).use(Uppy.Dashboard,
    //         {
    //             showLinkToFileUploadResult: false,
    //             inline: true,
    //             target: angular.element(document.querySelector('#headerImage'))[0],
    //             proudlyDisplayPoweredByUppy: false,
    //             showProgressDetails: true,
    //             showRemoveButtonAfterComplete: true,
    //             height: 200,
    //             plugins: ['Webcam'],
    //             maxNumberOfFiles: 1
    //         }).use(Uppy.XHRUpload, {
    //         endpoint: 'http://localhost:8080/api/v1/file/image',
    //         formData: true,
    //         fieldName: 'file',
    //         headers: {
    //             authorization: 'Bearer ' + $cookies.get("accessToken")
    //         }
    //     }).on('file-removed', (file) => {
    //         $scope.form.photo = null;
    //     }).on('complete', (result) => {
    //         $scope.form.photo = result.successful[0].response.body.url;
    //         console.log($scope.form.photo);
    //     })

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
        $http.post("/api/user/save", $scope.form,{
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken"),
                "Content-Type": "application/json"
            },
        }).then(
            resp => {
                alert("Saved successfully!")
                $scope.initialize();
                $scope.reset();
            },
            error => {
                console.log(error)
            }
        )
    }


    $scope.edit = function (id) {
        $http.get(`/api/user/findById/${id}`,
            {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            },
        }).then(
            resp => {
                $scope.form = resp.data;
                $scope.action = 'edit';
                console.log($scope.form)
            },
            error => {
                console.log(error)
            }
        )
    }

})