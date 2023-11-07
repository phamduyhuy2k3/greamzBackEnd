app.controller("categoryController", function ($scope, $http, $document, $cookies) {
    $scope.categories = [];
    $scope.categoryTypes = [];
    $scope.imageUrls = '';
    $scope.selectedCategoryType = '';
    $scope.action = 'create';
    $scope.imageCloudinary;


        $scope.form = {
            id: '',
            name: '',
            description: '',
            image: '',
            categoryTypes: '',
        }
        $scope.pager = {
            toFirst() {
                this.number = 0;
                this.fetchPage();
            },
            toLast() {
                this.number = this.totalPages - 1
                this.fetchPage();

            },
            next() {
                this.number++;
                if (this.number >= this.totalPages) {
                    this.number = 0;
                }
                this.fetchPage();
            },
            prev() {
                this.number--;
                if (this.number < 0) {
                    this.number = this.totalPages - 1;
                }
                this.fetchPage();
            },
            fetchPage() {
                $http.get(`/api/v1/category/findAllPagination?page=${this.number}&size=10`, {
                    headers: {
                        'Authorization': 'Bearer ' + $cookies.get('accessToken')
                    }
                }).then(resp => {
                    $scope.pager = {
                        ...$scope.pager,
                        ...resp.data
                    };
                })
            }

    }
    $scope.searchCategoryEvent = function () {
        if ($scope.searchCategory === '') {
            $scope.pager.fetchPage();
        } else {
            $http.get(`/api/v1/category/search?term=${$scope.searchCategory}`, {
                headers: {
                    'Authorization': 'Bearer ' + $cookies.get('accessToken')
                }
            }).then(resp => {
                $scope.pager = {
                    ...$scope.pager,
                    ...resp.data
                };
            })
        }
    }
    //Image
    $scope.imageCloudinary = cloudinary.createMediaLibrary(
        {
            cloud_name: "dtreuuola",
            api_key: "118212349948963",
            use_saml: false,
            remove_header: true,
            insert_caption: "Insert",
            inline_container: "#imageCloudinary",
            default_transformations: [[]],
            multiple: false
        },
        {
            insertHandler: function (data) {
                data.assets.forEach((asset) => {
                    let url = asset.url
                    $scope.form.image = url;
                    $scope.imageUrls = url;
                    console.log("image: " + $scope.form.image)
                });
            }
        }
    );
    //Image
    $(document).ready(function () {
        // Xử lý sự kiện khi nhấn nút "Thêm ảnh" trong modal cloudinary
        $("#btnCloseCategoryModalHeader").click(function () {
            // Thêm URL ảnh mới vào mảng imageUrls
            $scope.$apply(); // Cập nhật scope của AngularJS
            $("#headerModal").modal("hide"); // Ẩn modal cloudinary
            $("#categoryModal").modal("show"); // Hiện modal create
        }),
            //nút x modal
            $("#btnCloseCategoryModalHeader2").click(function () {
                // Thêm URL ảnh mới vào mảng imageUrls
                $scope.$apply(); // Cập nhật scope của AngularJS
                $("#headerModal").modal("hide"); // Ẩn modal cloudinary
                $("#categoryModal").modal("show"); // Hiện modal create
            })
    });


        $scope.initialize = function () {

            $http.get("/api/v1/category/findAllPagination", {
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken")
                }
            }).then(
                resp => {
                    $scope.pager = {
                        ...$scope.pager,
                        ...resp.data
                    };
                },
                error => {
                    console.log("Error", error);
                }
            );

            $http.get("/api/v1/category/types", {
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken")
                }
            }).then(
                resp => {
                    $scope.categoryTypes = resp.data;
                    console.log($scope.categoryTypes); // Kiểm tra giá trị của categoryTypes
                },
                error => {
                    console.log("Error", error);
                }
            );
        }

        $scope.deleteCategoryImage = function () {
            $scope.form.image = $scope.form.image.replace($scope.form.image, '');
            console.log($scope.form.image)
        }

        $scope.delete = function (id) {
            Swal.fire({
                title: "Are you sure?",
                text: "You won't be able to revert this!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, delete it!"
            }).then((result) => {
                if (result.isConfirmed) {
                    if (id) {
                        $http.delete(`/api/v1/category/delete/${id}`, {
                            headers: {
                                "Authorization": "Bearer " + $cookies.get("accessToken")
                            }
                        }).then(resp => {
                            Swal.fire({
                                title: "Deleted!",
                                text: "Deleted the category successfully!",
                                icon: "success"
                            });
                            $scope.initialize();
                        }).catch(error => {
                            Swal.fire({
                                icon: "error",
                                title: "Oops...",
                                text: "Error deleting category!",
                            });
                            console.log("Error", error);
                        });
                    }
                }
            });
        }


        $scope.reset = function () {
            $scope.form = {
                id: '',
                name: '',
                description: '',
                image: '',
                categoryTypes: '',
            };
            $scope.action = 'create';
            $scope.initialize();
        }

        $scope.create = function () {
            // Gán giá trị type từ selectedCategoryType vào form
            $http.post("/api/v1/category/save", $scope.form, {
                headers: {
                    "Authorization": "Bearer " + $cookies.get("accessToken"),
                    "Content-Type": "application/json"
                },
            }).then(
                resp => {
                    Swal.fire({
                        position: "center",
                        icon: "success",
                        title: "Saved the category successfully!",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    $scope.reset();
                },
                error => {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "Error saving category!",
                    });
                    console.log("Error", error);
                }
            )
        }
        $scope.edit = function (id) {
            $scope.form = $scope.categories.find(value => value.id === id);
        }

        $scope.initialize();
    }
)