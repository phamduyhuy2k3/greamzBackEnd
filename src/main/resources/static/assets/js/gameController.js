app.controller("gameController", function ($scope, $http, $document, $cookies) {
        $scope.imageUrls = [];
        $scope.headerImageUrls = [];
        $scope.capsuleImageUrls = [];
        $scope.currentImageType = '';
        $scope.games = [];
        $scope.action = 'create';
        $scope.categories = [];
        $scope.types = []
        $scope.countries = [];
        $scope.uppyHeaderImage;
        $scope.uppyCapsuleImage;
        $scope.uppyImages;
        $scope.headerImage;
        $scope.capsule_image;
        $scope.quillDetailedDescription;
        $scope.quillAbout;
        $scope.quillShortDescription;
        $scope.uppyMovies;
        $scope.test = '';
        $scope.form = {
            appid: '',
            name: '',
            detailed_description: '',
            about_the_game: '',
            short_description: '',
            supported_languages: [],
            header_image: [],
            website: '',
            capsule_image: [],
            images: [],
            movies: [],
            gameCategory: [],
        }


        $scope.uppyImages = cloudinary.createMediaLibrary(
            {
                cloud_name: "dtreuuola",
                api_key: "118212349948963",
                use_saml: false,
                remove_header: true,
                insert_caption: "Insert",
                inline_container: "#widget_images",
                default_transformations: [[]],
                button_class: "myBtn2 btn btn-primary",
                button_caption: "Select Image or Video",
                multiple: true,
                max_files: 15
            },
            {
                insertHandler: function (data) {
                    data.assets.forEach((asset) => {
                        let url=asset.url
                        $scope.form.images.findIndex(data => data === url)
                        === -1 ?
                            $scope.form.images.push(url) : console.log("duplicate");


                    });
                }
            },
            document.getElementById("images")
        );
        //headerImage
        $scope.uppyHeaderImage = cloudinary.createMediaLibrary(
            {
                cloud_name: "dtreuuola",
                api_key: "118212349948963",
                use_saml: false,
                remove_header: true,
                insert_caption: "Insert",
                inline_container: "#headerImageCloudinary",
                default_transformations: [[]],
                button_class: "myBtn2 btn btn-primary",
                button_caption: "Select Image or Video",
                multiple: false

            },
            {
                insertHandler: function (data) {
                    data.assets.forEach((asset) => {
                        console.log("Header: " + asset.url)
                        $scope.form.header_image= asset.url;
                        console.log("Header: " + $scope.form.header_image);

                    });
                }
            },
            document.getElementById("header")
        );
        //capsuleImage
        $scope.uppyCapsuleImage = cloudinary.createMediaLibrary(
            {
                cloud_name: "dtreuuola",
                api_key: "118212349948963",
                use_saml: false,
                remove_header: true,
                insert_caption: "Insert",
                inline_container: "#capsuleImageCloudinary",
                default_transformations: [[]],
                button_class: "myBtn2 btn btn-primary",
                button_caption: "Select Image or Video",
                multiple: false
            },
            {
                insertHandler: function (data) {
                    data.assets.forEach((asset) => {
                        console.log("Capsule: " + asset.url)
                        $scope.form.capsule_image=asset.url;
                        console.log("Capsule: " + $scope.form.capsule_image);

                    });
                }
            },
            document.getElementById("capsule")
        );

        //xử lý sự kiện khi nhấn nút "Thêm ảnh" từ modal cloudinary vào trong modal create
        $(document).ready(function () {
            // Xử lý sự kiện khi nhấn nút "Thêm ảnh" trong modal cloudinary
            $("#btnCloseModal").click(function () {
                // Thêm URL ảnh mới vào mảng imageUrls
                $scope.imageUrls.push($scope.form.images); // Điền URL của ảnh mới ở đây
                $scope.$apply(); // Cập nhật scope của AngularJS
                $("#imageModal").modal("hide"); // Ẩn modal cloudinary
                $("#exampleModal").modal("show"); // Hiện modal create
            }),
                //nút x modal
                $("#btnCloseModal2").click(function () {
                    // Thêm URL ảnh mới vào mảng imageUrls
                    $scope.imageUrls.push($scope.form.images); // Điền URL của ảnh mới ở đây
                    $scope.$apply(); // Cập nhật scope của AngularJS
                    $("#imageModal").modal("hide"); // Ẩn modal cloudinary
                    $("#exampleModal").modal("show"); // Hiện modal create
                })
        });

        //headerImage
        $(document).ready(function () {
            // Xử lý sự kiện khi nhấn nút "Thêm ảnh" trong modal cloudinary
            $("#btnCloseModalHeader").click(function () {
                // Thêm URL ảnh mới vào mảng imageUrls
                $scope.imageUrls.push($scope.form.header_image); // Điền URL của ảnh mới ở đây
                $scope.$apply(); // Cập nhật scope của AngularJS
                $("#headerModal").modal("hide"); // Ẩn modal cloudinary
                $("#exampleModal").modal("show"); // Hiện modal create
            }),
                //nút x modal
                $("#btnModalHeader").click(function () {
                    // Thêm URL ảnh mới vào mảng imageUrls
                    $scope.imageUrls.push($scope.form.header_image); // Điền URL của ảnh mới ở đây
                    $scope.$apply(); // Cập nhật scope của AngularJS
                    $("#headerModal").modal("hide"); // Ẩn modal cloudinary
                    $("#exampleModal").modal("show"); // Hiện modal create
                })
        });
        //capsuleImage
        $(document).ready(function () {
            // Xử lý sự kiện khi nhấn nút "Thêm ảnh" trong modal cloudinary
            $("#btnCloseModalCapsule").click(function () {
                // Thêm URL ảnh mới vào mảng imageUrls
                $scope.imageUrls.push($scope.form.capsule_image); // Điền URL của ảnh mới ở đây
                $scope.$apply(); // Cập nhật scope của AngularJS
                $("#capsuleModal").modal("hide"); // Ẩn modal cloudinary
                $("#exampleModal").modal("show"); // Hiện modal create
            }),
                //nút X modal 2
                $("#btnModalCapsule").click(function () {
                    // Thêm URL ảnh mới vào mảng imageUrls
                    $scope.imageUrls.push($scope.form.capsule_image); // Điền URL của ảnh mới ở đây
                    $scope.$apply(); // Cập nhật scope của AngularJS
                    $("#capsuleModal").modal("hide"); // Ẩn modal cloudinary
                    $("#exampleModal").modal("show"); // Hiện modal create
                })
        });

        $scope.initialize = function () {
            <!-- Initialize Quill editor -->
            $scope.quillDetailedDescription = new Quill('#detail_description', {
                    theme: 'snow'
                }
            );
            $scope.quillAbout = new Quill('#about', {
                    theme: 'snow'
                }
            );
            $scope.quillShortDescription = new Quill('#short_description', {
                    theme: 'snow'
                }
            );


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

            $http.get("/api/v1/country/all", {
                headers: {"Authorization": "Bearer " + $cookies.get("accessToken")}
            })
                .then(resp => {
                    $scope.countries = resp.data;
                    console.log($scope.countries)
                })
                .then(
                    () => {
                        $scope.select = $('#languagesSelect').select2({
                            // Use the modified 'data' object
                            data: $scope.countries,
                            placeholder: "Select Countries",
                            templateResult: function (data) {
                                if (!data.id) return data.name; // Option is not an object (e.g., the "Select a country" option)
                                let $result = $('<span>' + data.name + '</span>');
                                return $result;
                            },
                            templateSelection: function (data) {
                                if (!data.id) return data.name; // Option is not an object (e.g., the "Select a country" option)
                                let $selection = $('<span>' + data.name + '</span>');
                                return $selection;
                            }
                        });
                        $scope.select.on('select2:select', function (e) {
                            $scope.form.supported_languages.push(e.params.data);
                            console.log($scope.form.supported_languages)

                        });
                        $scope.select.on('select2:unselect', function (e) {
                            const id = e.params.data.id;
                            console.log(id)
                            const removedIndex = $scope.form.supported_languages.findIndex(data => data.id === id);
                            console.log(removedIndex)
                            $scope.$apply(function () {
                                $scope.form.supported_languages.splice(removedIndex, 1);
                            })
                            console.log($scope.form.supported_languages)

                        });
                    }
                )
                .catch(error => {
                    console.log("Error", error);
                })

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
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken")
                }
            }).then(
                resp => {
                    $scope.categories = resp.data;
                },
            ).then(
                () => {
                    $scope.select = $('#categoriesSelect').select2({
                        // Use the modified 'data' object
                        data: $scope.categories,
                        placeholder: "Select Categories",
                        templateResult: function (data) {
                            if (!data.id) return data.name; // Option is not an object (e.g., the "Select a country" option)
                            let $result = $('<span>' + data.name + '</span>');
                            return $result;
                        },
                        templateSelection: function (data) {
                            if (!data.id) return data.name; // Option is not an object (e.g., the "Select a country" option)
                            let $selection = $('<span>' + data.name + '</span>');
                            return $selection;
                        }
                    });
                    $scope.select.on('select2:select', function (e) {
                        $scope.form.gameCategory.push(e.params.data);
                        console.log($scope.form.gameCategory)

                    });
                    $scope.select.on('select2:unselect', function (e) {
                        const id = e.params.data.id;
                        console.log(id)
                        const removedIndex = $scope.form.gameCategory.findIndex(data => data.id === id);
                        console.log(removedIndex)
                        $scope.$apply(function () {
                            $scope.form.gameCategory.splice(removedIndex, 1);
                        })
                        console.log($scope.form.gameCategory)

                    });
                }
            )


        }
        $scope.initialize();
        // $('#textQuill').click(function () {
        //     $scope.test = $scope.quillAbout.getContents();
        //     console.log($scope.test)
        // });
        $scope.deleteImg = function (scope,value) {
            const index = scope.findIndex(data => data === value);
            scope.splice(index, 1);

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
        $scope.reset = function () {
            $scope.form = {
                appid: '',
                name: '',
                type: '',
                detailed_description: '',
                about_the_game: '',
                short_description: '',
                supported_languages: '',
                header_image: [],
                website: '',
                capsule_image: [],
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