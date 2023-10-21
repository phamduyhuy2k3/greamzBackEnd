app.controller("mainController", function ($scope, $routeParams) {

});

app.controller("gameController", function ($scope, $http, $document, $cookies) {

        $scope.games = [];
        $scope.action = 'create'
        $scope.categories = [];
        $scope.types = []
        $scope.uppyHeaderImage;
        $scope.uppyCapsuleImage;
        $scope.uppyImages;
        $scope.uppyMovies;
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

            })
                .catch(error => {
                    console.log("Error", error);
                });

        }
        $scope.initialize();


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
