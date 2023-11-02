app.controller("gameController", function ($scope, $http, $document, $cookies) {
        $scope.games = [];
        $scope.action = 'create';
        $scope.categories = [];
        $scope.types = [];
        $scope.countries = [];
        $scope.uppyHeaderImage;
        $scope.uppyCapsuleImage;
        $scope.uppyImages;
        $scope.uppyMovies;
        $scope.headerImage;
        $scope.capsule_image;
        $scope.quillDetailedDescription = [];
        $scope.quillAbout = [];
        $scope.quillShortDescription = [];
        $scope.uppyMovies;
        $scope.form = {
            appid: '',
            name: '',
            detailed_description: '',
            about_the_game: '',
            short_description: '',
            supported_languages: [],
            header_image: '',
            website: '',
            capsule_image: '',
            images: [],
            movies: [],
            categories: [],
        }
        $scope.imageUrls = [];
        $scope.movies = [];

        $scope.setURL = function (url,scope) {
            scope.push(url)
            url = "";
        }
        $scope.uppyImages = cloudinary.createMediaLibrary(
            {
                cloud_name: "dtreuuola",
                api_key: "118212349948963",
                use_saml: false,
                remove_header: true,
                insert_caption: "Insert",
                inline_container: "#widget_images",
                default_transformations: [[{
                    width: 80,
                    height: 80,
                    crop: "fill",
                    gravity: "auto",
                    radius: "max"
                }, {fetch_format: "auto", quality: "auto"}]],
                multiple: true,
                max_files: 15
            },
            {
                insertHandler: function (data) {
                    data.assets.forEach((asset) => {
                        let url = asset.url
                        $scope.form.images.findIndex(data => data === url)
                        === -1 ?
                            $scope.form.images.push(url) : console.log("duplicate");
                        console.log("images: " + $scope.form.images)

                    });
                }
            }
        );
        //movies
        $scope.uppyMovies = cloudinary.createMediaLibrary(
            {
                cloud_name: "dtreuuola",
                api_key: "118212349948963",
                use_saml: false,
                remove_header: true,
                insert_caption: "Insert",
                inline_container: "#moviesCloudinary",
                default_transformations: [[{
                    width: 80,
                    height: 80,
                    crop: "fill",
                    gravity: "auto",
                    radius: "max"
                }, {fetch_format: "auto", quality: "auto"}]],
                multiple: true,
                max_files: 15
            },
            {
                insertHandler: function (data) {
                    data.assets.forEach((asset) => {
                        let url = asset.url
                        $scope.form.movies.findIndex(data => data === url)
                        === -1 ?
                            $scope.form.movies.push(url) : console.log("duplicate");
                        console.log("Movies: " + $scope.form.movies)

                    });
                }
            }
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
                multiple: false

            },
            {
                insertHandler: function (data) {
                    data.assets.forEach((asset) => {
                        let url = asset.url
                        $scope.form.header_image = url
                        $scope.headerImageUrls = url;
                        console.log("header: " + $scope.form.header_image)
                    });
                }
            }
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
                multiple: false
            },
            {
                insertHandler: function (data) {
                    data.assets.forEach((asset) => {
                        let url = asset.url
                        $scope.form.capsule_image = url;
                        console.log("capsule: " + $scope.form.capsule_image)
                    });
                }
            }
        );

        //xử lý sự kiện khi nhấn nút "Thêm ảnh" từ modal cloudinary vào trong modal create
        $(document).ready(function () {
            // Xử lý sự kiện khi nhấn nút "Thêm ảnh" trong modal cloudinary
            $("#btnCloseModal").click(function () {
                // Thêm URL ảnh mới vào mảng imageUrls
                $scope.$apply(); // Cập nhật scope của AngularJS
                $("#imageModal").modal("hide"); // Ẩn modal cloudinary
                $("#exampleModal").modal("show"); // Hiện modal create
            }),
                //nút x modal
                $("#btnCloseModal2").click(function () {
                    // Thêm URL ảnh mới vào mảng imageUrls
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
                $scope.$apply(); // Cập nhật scope của AngularJS
                $("#headerModal").modal("hide"); // Ẩn modal cloudinary
                $("#exampleModal").modal("show"); // Hiện modal create
            }),
                //nút x modal
                $("#btnModalHeader").click(function () {
                    // Thêm URL ảnh mới vào mảng imageUrls
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
                $scope.$apply(); // Cập nhật scope của AngularJS
                $("#capsuleModal").modal("hide"); // Ẩn modal cloudinary
                $("#exampleModal").modal("show"); // Hiện modal create
            }),
                //nút X modal 2
                $("#btnModalCapsule").click(function () {
                    // Thêm URL ảnh mới vào mảng imageUrls
                    $scope.$apply(); // Cập nhật scope của AngularJS
                    $("#capsuleModal").modal("hide"); // Ẩn modal cloudinary
                    $("#exampleModal").modal("show"); // Hiện modal create
                })
        });
        //movies
        $(document).ready(function () {
            // Xử lý sự kiện khi nhấn nút "Thêm ảnh" trong modal cloudinary
            $("#btnCloseMovie").click(function () {
                // Thêm URL ảnh mới vào mảng imageUrls
                $scope.$apply(); // Cập nhật scope của AngularJS
                $("#movieModal").modal("hide"); // Ẩn modal cloudinary
                $("#exampleModal").modal("show"); // Hiện modal create
            }),
                //nút x modal
                $("#btnMovie").click(function () {
                    // Thêm URL ảnh mới vào mảng imageUrls
                    $scope.$apply(); // Cập nhật scope của AngularJS
                    $("#movieModal").modal("hide"); // Ẩn modal cloudinary
                    $("#exampleModal").modal("show"); // Hiện modal create
                })
        });
        let toolbarOptions = [
            ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
            ['blockquote', 'code-block'],

            [{'header': 1}, {'header': 2}],               // custom button values
            [{'list': 'ordered'}, {'list': 'bullet'}],
            [{'script': 'sub'}, {'script': 'super'}],      // superscript/subscript
            [{'indent': '-1'}, {'indent': '+1'}],          // outdent/indent
            [{'direction': 'rtl'}],                         // text direction

            [{'size': ['small', false, 'large', 'huge']}],  // custom dropdown
            [{'header': [1, 2, 3, 4, 5, 6, false]}],

            [{'color': []}, {'background': []}],          // dropdown with defaults from theme
            [{'font': []}],
            [{'align': []}],

            ['clean']                                         // remove formatting button
        ];

        <!-- Initialize Quill editor -->
        $scope.quillDetailedDescription = new Quill('#detail_description', {
                modules: {
                    toolbar: toolbarOptions
                },
                theme: 'snow'
            }
        );
        $scope.quillAbout = new Quill('#about', {
                modules: {
                    toolbar: toolbarOptions
                },
                theme: 'snow'
            }
        );
        $scope.quillShortDescription = new Quill('#short_description', {
                modules: {
                    toolbar: toolbarOptions
                },
                theme: 'snow'
            }
        );


        $scope.initialize = function () {

            $scope.countries = [];
            $http.get("https://countriesnow.space/api/v0.1/countries/flag/images")
                .then(resp => {

                    $scope.countries = resp.data.data;
                    $scope.countries[0].flag = "data:image/png;base64,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"
                    console.log($scope.countries)
                    // Add flag images to the 'data' object for each option
                    $scope.countries.forEach(function (country) {
                        country.id = country.name;
                        country.text = country.name;
                    });
                }).then(r => {
                $scope.selectCountry = $('#country').select2({
                    // Use the modified 'data' object
                    data: $scope.countries,
                    placeholder: "Select a country",
                    templateResult: function (data) {
                        if (!data.id) return data.text; // Option is not an object (e.g., the "Select a country" option)
                        let $result = $('<span><img src="' + data.flag + '" class="img-flag" style="width: 25px; height: 25px; border: #0a0c0d solid thin " /> ' + data.text + '</span>');
                        return $result;
                    },
                    templateSelection: function (data) {
                        if (!data.id) return data.text; // Option is not an object (e.g., the "Select a country" option)
                        let $selection = $('<span><img src="' + data.flag + '" class="img-flag" style="width: 25px; height: 25px; border: #0a0c0d solid thin" /> ' + data.text + '</span>');
                        return $selection;
                    }
                });
                $scope.selectCountry.on('select2:select', function (e) {
                    let data = e.params.data;
                    console.log(data);
                    $scope.$apply(function () {
                        $scope.form.supported_languages.push(data.name);
                        console.log($scope.form.supported_languages);
                    });
                });
                $scope.selectCountry.on('select2:unselect', function (e) {
                    const name = e.params.data.name;
                    console.log(name)
                    const removedIndex = $scope.form.supported_languages.findIndex(data => data === name);
                    console.log(removedIndex)
                    $scope.form.supported_languages.splice(removedIndex, 1);
                    console.log($scope.form.supported_languages)

                });
            });

            $http.get("/api/v1/game/findALl",
                {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken")
                    }
                }).then(resp => {
                $scope.games = resp.data;
                console.log($scope.games)
            })
                .catch(error => {
                    console.log("Error", error);
                });

            $http.get("/api/v1/category/findAll", {
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
                            if (!data.id) return data.text; // Option is not an object (e.g., the "Select a country" option)
                            let $result = $('<span>' + data.name + '</span>');
                            return $result;
                        },
                        templateSelection: function (data) {
                            if (!data.id) return data.text; // Option is not an object (e.g., the "Select a country" option)
                            let $selection = $('<span>' + data.name + '</span>');
                            return $selection;
                        }
                    });
                    $scope.select.on('select2:select', function (e) {
                        $scope.form.categories.push({id: parseInt(e.params.data.id)});

                    });
                    $scope.select.on('select2:unselect', function (e) {
                        const id = e.params.data.id;
                        console.log(id)
                        const removedIndex = $scope.form.categories.findIndex(data => data.id === parseInt(id));
                        console.log(removedIndex)
                        $scope.$apply(() => {
                            $scope.form.categories.splice(removedIndex, 1);
                        })
                    });
                }
            )
        }

        $scope.deleteImg = function (scope, value) {
            const index = scope.findIndex(data => data === value);
            scope.splice(index, 1);
        }
        $scope.deleteImg2 = function () {
            $scope.form.header_image = $scope.form.header_image.replace($scope.form.header_image, '');
            console.log($scope.form.header_image)
        }
        $scope.deleteImg3 = function () {
            $scope.form.capsule_image = $scope.form.capsule_image.replace($scope.form.capsule_image, '');
            console.log($scope.form.capsule_image)
        }
        $scope.delete = function (item) {
            if (confirm("Do you want to delete this game?")) {
                $http.delete(`/api/v1/game/delete/${item.appid}`, {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken")
                    }
                }).then(resp => {
                    $scope.reset();
                    $scope.initialize();
                    alert("Successfully deleted the game!");
                }).catch(error => {
                    alert("Game deletion error!");
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
                categories: [],
            }
            $scope.quillAbout.setContents([{insert: '\n'}]);
            $scope.quillShortDescription.setContents([{insert: '\n'}]);
            $scope.quillDetailedDescription.setContents([{insert: '\n'}]);
            $scope.action = 'create';
            $scope.selectCountry.val(null);
            $scope.selectCountry.trigger('change');
            $scope.select.val(null);
            $scope.select.trigger('change');
            // $scope.initialize();

        }
        $scope.create = function () {
            $scope.form.about_the_game = JSON.stringify($scope.quillAbout.getContents());
            $scope.form.short_description = JSON.stringify($scope.quillShortDescription.getContents());
            $scope.form.detailed_description = JSON.stringify($scope.quillDetailedDescription.getContents());

            $http.post("/api/v1/game/create", $scope.form,
                {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken"),
                        "Content-Type": "application/json"
                    }
                }).then(resp => {
                $scope.reset();
                $scope.initialize();
                if ($scope.action === 'create') {
                    alert("Save the game successfully!");
                } else {
                    alert("The game updated successfully!");
                }
            })
                .catch(error => {
                    console.log("Error", error);
                });
        }
        $scope.edit = async function (appid) {
            await $http.get(`/api/v1/game/findById/${appid}`, {
                headers: {
                    'Authorization': 'Bearer ' + $cookies.get('accessToken')
                }
            }).then(resp => {
                    return resp.data;
                }, error => {
                    return error;
                }
            ).then(r => {
                $scope.form = r;
                if ($scope.form.about_the_game != null || $scope.form.about_the_game !== '' || $scope.form.about_the_game !== undefined) {
                    $scope.quillAbout.setContents(JSON.parse($scope.form.about_the_game));
                }
                if ($scope.form.short_description != null || $scope.form.short_description !== '' || $scope.form.short_description !== undefined) {
                    $scope.quillShortDescription.setContents(JSON.parse($scope.form.short_description));
                }
                if ($scope.form.detailed_description != null || $scope.form.detailed_description !== '' || $scope.form.detailed_description !== undefined) {
                    $scope.quillDetailedDescription.setContents(JSON.parse($scope.form.detailed_description));
                }

                $scope.selectCountry.val($scope.form.supported_languages);
                $scope.selectCountry.trigger('change');
                let arr = $scope.form.categories.map(data => {
                    return data.id
                });
                console.log(arr)
                $scope.select.val(arr);
                $scope.select.trigger('change');
            }, error => {
                console.log(error);
            })
            // await $http.get(`/api/category/game/${appid}`, {}).then(resp => {
            //     return resp.data;
            // }, error => {
            //     console.log(error);
            // }).then(r => {
            //     $scope.form.categories = r;
            //     let arr = $scope.form.categories.map(data => {
            //         return data.id
            //     });
            //     console.log(arr)
            //     $scope.select.val(arr);
            //     $scope.select.trigger('change');
            // })
            $scope.action = 'update';
        }
        $scope.initialize()
    }
)