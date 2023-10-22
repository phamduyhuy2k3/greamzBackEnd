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
        $scope.cloudImages = cloudinary.createMediaLibrary(
            {
                cloud_name: "dtreuuola",
                api_key: "118212349948963",
                //su dung khi nhieu file
                // max_files: "1",
                remove_header: false,
                insert_caption: "Insert",
                inline_container: "#widget_images",
                default_transformations: [[]],
                button_class: "myBtn",
                button_caption: "Select Image or Video"
            },
            {
                insertHandler: function (data) {
                    data.assets.forEach((asset) => {
                        //only file
                        // $scope.form.images.push= asset.url;
                        $scope.form.images.push(asset.url);
                    });
                }
            },
            document.getElementById("images")
        );

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
            $http.get("/api/v1/category/all", {
                headers: {"Authorization": "Bearer " + $cookies.get("accessToken")}

            }).then(
                resp => {
                    $scope.categories = resp.data;
                }).then(
                () => {
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
                        $scope.form.gameCategory.push(e.params.data);
                        console.log($scope.form.gameCategory)

                    });
                    $scope.select.on('select2:unselect', function (e) {
                        const id = e.params.data.id;
                        const removedIndex = $scope.form.gameCategory.findIndex(data => data.id === id);
                        $scope.form.gameCategory.slice(removedIndex, 1)

                    });
                }
            )

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
            $http.post("/api/v1/game/create", $scope.form,
                {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken"),
                        "Content-Type": "application/json"
                    }
                }).then(resp => {
                $scope.initialize();
                // $scope.resetUppyState();
                alert('Thêm mới sản phẩm thành công!')
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