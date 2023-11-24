app.controller("userController", function ($scope, $http, $document, $cookies) {

    $scope.accounts = [];
    $scope.orders = [];
    $scope.reviews = [];
    $scope.vouchers = [];
    $scope.accountView = '';
    $scope.roles = ["USER", "ADMIN", "MANAGER", "EMPLOYEE"];
    $scope.action = 'create'
    $scope.photoCloudinary;
    $scope.photoUrls = '';
    $scope.form = {
        id: '',
        username: '',
        password: '',
        fullname: '',
        email: '',
        photo: '',
        isEnabled: false,
        role: '',
    }
    $scope.errors = {
        fullname: null,
        username: null,
        password: null,
        email: null,

    }

    //photo
    $scope.photoCloudinary = cloudinary.createMediaLibrary(
        {
            cloud_name: "dtreuuola",
            api_key: "118212349948963",
            username: "phamhuytrieu12092003@gmail.com",
            use_saml: false,
            remove_header: true,
            insert_caption: "Insert",
            inline_container: "#photoCloudinary",
            default_transformations: [[]],
            multiple: false

        },
        {
            insertHandler: function (data) {
                data.assets.forEach((asset) => {
                    let url = asset.url
                    $scope.form.photo = url;
                    // $scope.photoUrls = url;
                    console.log("photo: " + $scope.form.photo);
                });
            }
        }
    );
    //photo
    $(document).ready(function () {
        // Xử lý sự kiện khi nhấn nút "Thêm ảnh" trong modal cloudinary
        $("#btnCloseModalPhoto").click(function () {
            // Thêm URL ảnh mới vào mảng imageUrls
            $scope.$apply(); // Cập nhật scope của AngularJS
            $("#photoModal").modal("hide"); // Ẩn modal cloudinary
            $("#userModal").modal("show"); // Hiện modal create
        }),
            //nút x modal
            $("#btnCloseModal2").click(function () {
                // Thêm URL ảnh mới vào mảng imageUrls
                $scope.$apply(); // Cập nhật scope của AngularJS
                $("#photoModal").modal("hide"); // Ẩn modal cloudinary
                $("#userModal").modal("show"); // Hiện modal create
            })
    });
    $scope.deletePhoto = function () {
        $scope.form.photo = $scope.form.photo.replace($scope.form.photo, '');
        console.log($scope.form.photo)
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

    }

    $scope.delete = function (id) {
        Swal.fire({
            title: "Do you want to delete this account?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, delete it!"
        }).then((result) => {
            if (result.isConfirmed) {
                if (id) {
                    $http.delete(`/api/user/delete/${id}`, {
                        headers: {
                            "Authorization": "Bearer " + $cookies.get("accessToken")
                        }
                    }).then(resp => {
                        Swal.fire({
                            title: "Deleted!",
                            text: "Your account has been deleted.",
                            icon: "success"
                        });
                        $scope.initialize();
                    }).catch(error => {
                        Swal.fire({
                            icon: "error",
                            title: "Oops...",
                            text: "Error deleting account!",
                        });
                        console.log("Error", error);
                    });
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "Error deleting account!",
                    });
                    console.log("Error", error);
                }
            }
        });
    }

    $scope.reset = function () {
        $scope.form = {
            id: '',
            username: '',
            password: '',
            fullname: '',
            email: '',
            photo: '',
            isEnabled: false,
            role: '',
        }
        $scope.action = 'create';
    }

    $scope.create = function () {
        console.log($scope.form)
        $http.post("/api/user/save", $scope.form, {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken"),
                "Content-Type": "application/json"
            },
        }).then(
            resp => {
                Swal.fire({
                    position: "center",
                    icon: "success",
                    title: "Saved successfully!",
                    showConfirmButton: false,
                    timer: 1500
                });
                $scope.reset();
                $scope.initialize();
            },
            error => {
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Error creating account!",
                });
                console.log("Error", error);
            }
        )
    }

    $scope.update = function () {
        $http.put("/api/user/update", $scope.form, {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken"),
                "Content-Type": "application/json"
            },
        }).then(
            resp => {
                Swal.fire({
                    position: "center",
                    icon: "success",
                    title: "Saved successfully!",
                    showConfirmButton: false,
                    timer: 1500
                });
                $scope.reset();
                $scope.initialize();
            },
            error => {
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Error creating account!",
                });
                console.log(error)
            }
        )
    }

    $scope.edit = function (id) {
        $scope.form = $scope.accounts.find(value => value.id === id)
        $scope.form.password = '';
        $scope.action = 'update';

    }
    $scope.view = function (id) {
        $scope.form = $scope.accounts.find(value => value.id === id)
        $scope.form.password = '';
        $scope.action = 'view';
        $http.get(`/api/user/findOrdersByAccountId/${id}`,{
            headers:{
                'Authorization': 'Bearer ' + $cookies.get('accessToken')
            }

        }).then(resp =>{
            $scope.orders = resp.data;
            console.log($scope.orders)
        })
        $http.get(`/api/user/findReviewsByAccountId/${id}`,{
                    headers:{
                        'Authorization': 'Bearer ' + $cookies.get('accessToken')
                    }

                }).then(resp =>{
                    $scope.reviews = resp.data;
                    console.log($scope.reviews)

                })
    }
    $scope.initialize();

})