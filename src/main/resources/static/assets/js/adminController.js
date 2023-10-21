app.controller("mainController", function ($scope, $routeParams) {

});

app.controller("gameController", function ($scope, $http, $document, $cookies) {

<<<<<<< HEAD

    $scope.create = function () {
        console.log("$scope.actionadwewewewewewewewewewe")
        if ($scope.uppy1.getFiles().length > 0 && $scope.uppy.getFiles().length > 0) {
            areSomeFileNotUploaded().then(someFileNotUploaded => {
                if (someFileNotUploaded.result) {
                    areUploadsComplete(someFileNotUploaded).then(uploadComplete => {
                        if (!uploadComplete) {
                            alert('Some files failed to upload. Please try again.');
                            return;
                        }
                        setTimeout(function () {
                            $http.post('/api/game/create', {
                                headers: {
                                    "Authorization": "Bearer " + $cookies.get("accessToken")
                                }
                            }, $scope.form).then(resp => {
                                resp.data.createDate = new Date(resp.data.createDate);
                                $scope.items.push(resp.data);
                                $scope.reset();
                                $scope.resetUppyState();
                                alert('Thêm mới sản phẩm thành công!')

                            }).catch(error => {
                                if (error.status === 400) {
                                    alert(error.data);
                                    return;
                                }
                                alert('Lỗi thêm mới sản phẩm!');
                                console.log('Error', error);
                            });
                        }, 500);
                    });
                } else {
                    $http.post('/api/game/create', {
                        headers: {
                            "Authorization": "Bearer " + $cookies.get("accessToken")
                        }
                    }, $scope.form).then(resp => {
                        resp.data.createDate = new Date(resp.data.createDate);
                        $scope.items.push(resp.data);
                        $scope.reset();
                        $scope.resetUppyState();
                        alert('Thêm mới sản phẩm thành công!')

                    }).catch(error => {
                        if (error.status === 400) {
                            alert(error.data);
                            return;
                        }
                        alert('Lỗi thêm mới sản phẩm!');
                        console.log('Error', error);
                    });
                }
            });

        } else {
            $http.post('/api/game/create', {
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken")
                }
            }, $scope.form).then(resp => {
                resp.data.createDate = new Date(resp.data.createDate);
                $scope.items.push(resp.data);
                $scope.reset();
                $scope.resetUppyState();
                alert('Thêm mới sản phẩm thành công!')
            }).catch(error => {
                if (error.status === 400) {
                    alert(error.data);
                    return;
                }
                alert('Lỗi thêm mới sản phẩm!');
                console.log('Error', error);
            });
        }
    }
    $scope.reset = function () {
=======
        $scope.games = [];
        $scope.action = 'create'
        $scope.categories = [];
        $scope.types = []
        $scope.uppyHeaderImage;
        $scope.uppyCapsuleImage;
        $scope.uppyImages;
        $scope.uppyMovies;
>>>>>>> 9408f3f4cc34e82961d1f7207741f240f3bf37c2
        $scope.form = {
            appid: '',
            name: '',
            detailed_description: '',
            about_the_game: '',
            short_description: '',
            supported_languages: '',
            header_image: '',
            website: '',
            capsule_image: '',
            images: [],
            movies: [],
            gameCategory: [],
        }
<<<<<<< HEAD
        $scope.uppy1.cancelAll();
        $scope.uppy.cancelAll();
        $scope.action = 'create';
        $scope.initialize();
    }
    $scope.initialize = function () {
        // initialization multi select tag
        new MultiSelectTag('countries');

        // category
        $http.get("/api/category/findALl", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(resp => {
            $scope.categories = resp.data;
            console.log($scope.categories);
        })
            .catch(error => {
                console.log("Error", error);
            });

        $http.get("/api/game/findALl", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(resp => {
            $scope.items = resp.data;
            $scope.items.forEach(item => {
                item.createDate = new Date(item.createDate)
            })
            console.log($scope.items);
        })
            .catch(error => {
                console.log("Error", error);
            });
    }
    $scope.delete = function (item) {
        if (confirm("Bạn muốn xóa sản phẩm này?")) {
            $http.delete(`/api/game/delete/${item.appid}`, {
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken")
                }
            }).then(resp => {
                var index = $scope.items.findIndex(p => p.appid === item.appid);
                $scope.items.splice(index, 1);
                $scope.initialize();
                // $scope.reset();
                alert("Xóa sản phẩm thành công!");
            }).catch(error => {
                alert("Lỗi xóa sản phẩm!");
                console.log("Error", error);
            })
        }
    }
    $scope.resetUppyState = () => {
        $scope.uppy1.cancelAll()
        $scope.uppy.cancelAll();
    }

    function areSomeFileNotUploaded() {
        return Promise.all([$scope.uppy1.getFiles().some(file => !file.progress.uploadComplete || !file.progress.uploadStarted), $scope.uppy.getFiles().some(file => !file.progress.uploadComplete || !file.progress.uploadStarted)])
            .then(([result1, result2]) => {
                if (result1 && result2) {
                    return {
                        uppy: [$scope.uppy1, $scope.uppy], result: result1
                    };
                }
                if (result1) {
                    return {
                        uppy: $scope.uppy1, result: result1
                    };
                } else if (result2) {
                    return {
                        uppy: $scope.uppy, result: result2
                    }
                } else {
                    return {
                        result: false
                    };
                }
            });
    }

    const targetElement1 = angular.element(document.querySelector('#drag-drop-area1'))[0];
    const targetElement2 = angular.element(document.querySelector('#drag-drop-area2'))[0];
    const targetElement3 = angular.element(document.querySelector('#drag-drop-area3'))[0];
    const targetElement4 = angular.element(document.querySelector('#drag-drop-area4'))[0];
    $scope.uppy1 = Uppy.Core({
        autoProceed: false, restrictions: {
            minNumberOfFiles: 1, maxNumberOfFiles: 1, maxFileSize: 100000000, allowedFileTypes: ['image/*']
        }
    })
        .use(Uppy.Dashboard, {
            showLinkToFileUploadResult: false,
            inline: true,
            target: targetElement1,

            proudlyDisplayPoweredByUppy: false,
            showProgressDetails: true,
            showRemoveButtonAfterComplete: true,
            height: 200,
            plugins: ['Webcam'],
            maxNumberOfFiles: 1
        })
        .use(Uppy.XHRUpload, {
            endpoint: 'http://localhost:8080/api/files/upload', formData: true, fieldName: 'file'
=======
        $scope.uppyHeaderImage =
            Uppy.Core({
                autoProceed: false,
                restrictions: {
                    minNumberOfFiles: 1,
                    maxNumberOfFiles: 1,
                    maxFileSize: 100000000,
                    allowedFileTypes: ['image/*']
                }
            }).use(Uppy.Dashboard,
                {
                    showLinkToFileUploadResult: false,
                    inline: true,
                    target: angular.element(document.querySelector('#headerImage'))[0],
                    proudlyDisplayPoweredByUppy: false,
                    showProgressDetails: true,
                    showRemoveButtonAfterComplete: true,
                    height: 200,
                    plugins: ['Webcam'],
                    maxNumberOfFiles: 1
                }).use(Uppy.XHRUpload, {
                endpoint: 'http://localhost:8080/api/v1/file/image',
                formData: true,
                fieldName: 'file',
                headers: {
                    authorization: 'Bearer ' + $cookies.get("accessToken")
                }
            }).on('file-removed', (file) => {
                $scope.form.header_image = null;
            }).on('complete', (result) => {
                $scope.form.header_image = result.successful[0].response.body.url;
                console.log($scope.form.header_image);
            })
        $scope.uppyCapsuleImage =
            Uppy.Core({
                autoProceed: false,
                restrictions: {
                    minNumberOfFiles: 1,
                    maxNumberOfFiles: 1,
                    maxFileSize: 100000000,
                    allowedFileTypes: ['image/*']
                }
            }).use(Uppy.Dashboard,
                {
                    showLinkToFileUploadResult: false,
                    inline: true,
                    target: angular.element(document.querySelector('#capsule'))[0],
                    proudlyDisplayPoweredByUppy: false,
                    showProgressDetails: true,
                    showRemoveButtonAfterComplete: true,
                    height: 200,
                    plugins: ['Webcam'],
                    maxNumberOfFiles: 1
                }).use(Uppy.XHRUpload, {
                endpoint: 'http://localhost:8080/api/v1/file/image',
                formData: true,
                fieldName: 'file',
                headers: {
                    authorization: 'Bearer ' + $cookies.get("accessToken")
                }
            }).on('file-removed', (file) => {
                $scope.form.capsule_image = null;
            }).on('complete', (result) => {
                $scope.form.capsule_image = result.successful[0].response.body.url;
            })
        $scope.uppyImages = Uppy.Core({
            autoProceed: false,
            restrictions: {
                maxFileSize: 100000000,
                allowedFileTypes: ['image/*']
            }
        }).use(Uppy.Dashboard, {
            showLinkToFileUploadResult: false,
            target: angular.element(document.querySelector('#images'))[0],
            inline: true,
            height: 200,
            showProgressDetails: true,
            showRemoveButtonAfterComplete: true,
            plugins: ['Webcam'],
            proudlyDisplayPoweredByUppy: false
        }).use(Uppy.XHRUpload, {
            endpoint: 'http://localhost:8080/api/v1/file/image',
            formData: true,
            fieldName: 'file',
            headers: {
                authorization: 'Bearer ' + $cookies.get("accessToken")
            }
        }).on('complete', (result) => {
            const fileURLs = result.successful.map((file) => file.response.body.url);
            if (!$scope.form.images) {
                $scope.form.images = [];
            }
            fileURLs.forEach((url) => {
                $scope.form.images.push(url);
            });
        }).on('file-removed', (file) => {
            const removedIndex = $scope.form.images.findIndex(image => image === file.name);
            if (removedIndex !== -1) {
                $scope.form.images.splice(removedIndex, 1);
            }
>>>>>>> 9408f3f4cc34e82961d1f7207741f240f3bf37c2
        })
        $scope.uppyMovies = Uppy.Core({
            autoProceed: false,
            restrictions: {
                maxFileSize: 100000000,
                allowedFileTypes: ['video/**']
            }
        }).use(Uppy.Dashboard, {
            showLinkToFileUploadResult: false,
            target: angular.element(document.querySelector('#movies'))[0],
            inline: true,
            height: 200,
            showProgressDetails: true,
            showRemoveButtonAfterComplete: true,
            plugins: ['Webcam'],
            proudlyDisplayPoweredByUppy: false
        }).use(Uppy.XHRUpload, {
            endpoint: 'http://localhost:8080/api/v1/file/video',
            formData: true,
            fieldName: 'file',
            headers: {
                authorization: 'Bearer ' + $cookies.get("accessToken")
            }
        }).on('file-removed', (file) => {
            const removedIndex = scope.findIndex(image => image === file.name);
            if (removedIndex !== -1) {
                $scope.form.movies.splice(removedIndex, 1);
            }
        }).on('complete', (result) => {
            const fileURLs = result.successful.map((file) => file.response.body.url);
            if (!$scope.form.movies) {
                $scope.form.movies = [];
            }
            fileURLs.forEach((url) => {
                $scope.form.movies.push(url);
            });
            console.log($scope.form.movies);
        })
        $scope.loadCategory = async function () {
            await $http.get("/api/v1/category/type/" + $scope.form.type,
                {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken")
                    }
                }).then(resp => {
                    $scope.categories = resp.data;

<<<<<<< HEAD
    $scope.uppy = Uppy.Core({
        autoProceed: false, restrictions: {
            maxFileSize: 100000000, allowedFileTypes: ['image/*']
        }
    }).use(Uppy.Dashboard, {
        showLinkToFileUploadResult: false,
        target: targetElement2,
        inline: true,
        height: 400,
        showProgressDetails: true,
        showRemoveButtonAfterComplete: true,
        plugins: ['Webcam'],
        proudlyDisplayPoweredByUppy: false
    }).use(Uppy.XHRUpload, {
        endpoint: 'http://localhost:8080/api/files/upload', formData: true, fieldName: 'file'
    });
=======
                },
                error => {
                    console.log("Error", error);
                })
                .then(r1 => {
                    if ($scope.select != null) {
                        $scope.select.empty();
                        $scope.select.select2({
                            // Use the modified 'data' object
                            data: $scope.categories,
                            placeholder: "Select Categories",
                            templateResult: function (data) {
                                if (!data.id) return data.text; // Option is not an object (e.g., the "Select a country" option)
                                let $result = $('<span>' + data.name + '</span>');
                                return $result;
                            },
                            templateSelection: function (data) {
                                if (!data.id) return data.name; // Option is not an object (e.g., the "Select a country" option)
                                let $selection = $('<span>' + data.name + '</span>');
                                return $selection;
                            }
                        })
                        return;
                    }

                    $scope.select = $('#categoriesSelect').select2({
                        // Use the modified 'data' object
                        data: $scope.categories,
                        placeholder: "Select Categories",
                        templateResult: function (data) {
                            if (!data.id) return data.text; // Option is not an object (e.g., the "Select a country" option)
                            let $result = $('<span>' + data.text + '</span>');
                            return $result;
                        },
                        templateSelection: function (data) {
                            if (!data.id) return data.text; // Option is not an object (e.g., the "Select a country" option)
                            let $selection = $('<span>' + data.text + '</span>');
                            return $selection;
                        }
                    });
                    $scope.select.on('select2:select', function (e) {
                        $scope.form.gameCategory.push(e.params.data) ;
                        console.log($scope.form.gameCategory)

                    });
                    $scope.select.on('select2:unselect', function (e) {
                        const id=e.params.data.id;
                        const removedIndex = $scope.form.gameCategory.findIndex(data => data.id === id);
                        $scope.form.gameCategory.slice(removedIndex,1)

                    });
                });
        }
        $scope.initialize = function () {
            $http.get("/api/v1/category/types", {
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken")
                }
            }).then(
                resp => {
                    $scope.types = resp.data;
                },
                error => {
                }
            )
            $http.get("/api/v1/game/findALl",
                {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken")
                    }
                }).then(resp => {
                $scope.games = resp.data;
>>>>>>> 9408f3f4cc34e82961d1f7207741f240f3bf37c2

            })
                .catch(error => {
                    console.log("Error", error);
                });

        }
        $scope.initialize();

<<<<<<< HEAD
    });
    $scope.action = 'create';
    $scope.initialize();
});

app.controller("userController", function ($scope, $http, $document, $cookies) {
    $scope.accounts = [];
    $scope.action = 'create'

    $scope.initialize = function () {
        $http.get("/api/user/findAll", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(resp => {
            $scope.accounts = resp.data;
            console.log($scope.accounts);
        })
            .catch(error => {
                console.log("Error", error);
            });
    }


    $scope.create = function () {

    }

    $scope.delete = function (item) {
        if (confirm("Do you want to delete this account?")) {
            $http.delete(`/api/user/delete/${item.id}`, {
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken")
                }
            }).then(resp => {
                var index = $scope.items.findIndex(p => p.id === item.id);
                $scope.items.splice(index, 1);
                $scope.initialize();
                // $scope.reset();
                alert("The account was deleted successfully!");
            }).catch(error => {
                alert("Error deleting account!");
                console.log("Error", error);
            })
        }
    }

    $scope.initialize();
})
=======

        $scope.delete = function (item) {
            if (confirm("Bạn muốn xóa sản phẩm này?")) {
                $http.delete(`/api/game/delete/${item.appid}`, {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken")
                    }
                }).then(resp => {
                    var index = $scope.items.findIndex(p => p.appid === item.appid);
                    $scope.items.splice(index, 1);
                    $scope.initialize();
                    // $scope.reset();
                    alert("Xóa sản phẩm thành công!");
                }).catch(error => {
                    alert("Lỗi xóa sản phẩm!");
                    console.log("Error", error);
                })
            }
        }
        $scope.reset = function () {
            $scope.form = {
                appid: '',
                name: '',
                type: '',
                detailed_description: '',
                about_the_game: '',
                short_description: '',
                supported_languages: '',
                header_image: '',
                website: '',
                capsule_image: '',
                images: [],
                movies: [],
                screenshots: [],
            }
            $scope.action = 'create';
            $scope.initialize();
        }
        $scope.create = function () {
            $http.get("/api/v1/game/create",
                {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken")
                    },
                    data: $scope.form
                }).then(resp => {


            })
                .catch(error => {
                    console.log("Error", error);
                });
        }

        $scope.edit = function () {

        }
        $scope.action = 'create';
    }
)
>>>>>>> 9408f3f4cc34e82961d1f7207741f240f3bf37c2
