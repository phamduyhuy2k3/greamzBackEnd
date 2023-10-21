app.controller("mainController", function ($scope, $routeParams) {

});

app.controller("gameController", function ($scope, $http, $document, $cookies) {
    $scope.games = [];
    $scope.action = 'create'
    $scope.categories = [];
    // const selectBtn = document.querySelector(".select-btn"),
    //     items = document.querySelectorAll(".item");
    //
    // selectBtn.addEventListener("click", () => {
    //     selectBtn.classList.toggle("open");
    // });
    //
    // items.forEach(item => {
    //     item.addEventListener("click", () => {
    //         item.classList.toggle("checked");
    //
    //         let checked = document.querySelectorAll(".checked"),
    //             btnText = document.querySelector(".btn-text");
    //
    //         if(checked && checked.length > 0){
    //             btnText.innerText = `${checked.length} Selected`;
    //         }else{
    //             btnText.innerText = "Select Language";
    //         }
    //     });
    // });



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
        $scope.uppy1.cancelAll();
        $scope.uppy.cancelAll();
        $scope.action = 'create';
        $scope.initialize();
    }
    $scope.initialize = function () {
        // initialization multi select tag
        new MultiSelectTag('countries');

        // category
        $http.get("/api/category/findALl",
            {
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

        $http.get("/api/game/findALl",
            {
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken")
                }
            }).then(resp => {
            $scope.items = resp.data;
            $scope.items.forEach(item => {
                item.createDate = new Date(item.createDate)
            })
<<<<<<<<< Temporary merge branch 1
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
=========
            console.log($scope.items);
        })
            .catch(error => {
                console.log("Error", error);
            });
>>>>>>>>> Temporary merge branch 2
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
        return Promise.all(
            [
                $scope.uppy1.getFiles().some(file => !file.progress.uploadComplete || !file.progress.uploadStarted),
                $scope.uppy.getFiles().some(file => !file.progress.uploadComplete || !file.progress.uploadStarted)
            ])
            .then(([result1, result2]) => {
                if (result1 && result2) {
                    return {
                        uppy: [$scope.uppy1, $scope.uppy],
                        result: result1
                    };
                }
                if (result1) {
                    return {
                        uppy: $scope.uppy1,
                        result: result1
                    };
                } else if (result2) {
                    return {
                        uppy: $scope.uppy,
                        result: result2
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
        autoProceed: false,
        restrictions: {
            minNumberOfFiles: 1,
            maxNumberOfFiles: 1,
            maxFileSize: 100000000,
            allowedFileTypes: ['image/*']
        }
    })
        .use(Uppy.Dashboard,
            {
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
            endpoint: 'http://localhost:8080/api/files/upload',
            formData: true,
            fieldName: 'file'
        })

    $scope.uppy = Uppy.Core({
        autoProceed: false,
        restrictions: {
            maxFileSize: 100000000,
            allowedFileTypes: ['image/*']
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
        endpoint: 'http://localhost:8080/api/files/upload',
        formData: true,
        fieldName: 'file'
    });

    $scope.uppy1.on('file-removed', (file) => {
        $scope.form.image = null;

    });

    $scope.uppy1.on('complete', (result) => {
        console.log(result)
        console.log(result.successful[0].response.body.uploadUrl)

        $scope.form.image = result.successful[0].response.body.uploadUrl;
        console.log($scope.form.image);
    });

    $scope.uppy.on('complete', (result) => {
        console.log(result)
        const fileURLs = result.successful.map((file) => file.response.body.uploadUrl);
        if (!$scope.form.images) {
            $scope.form.images = [];
        }
        fileURLs.forEach((url) => {
            $scope.form.images.push(url);
        });
        console.log($scope.form.images);


    });

    $scope.uppy.on('file-removed', (file) => {
        const removedIndex = $scope.form.images.findIndex(image => image === file.name);

        if (removedIndex !== -1) {
            $scope.form.images.splice(removedIndex, 1);

        }

    });
    $scope.action = 'create';
    $scope.initialize();
})
