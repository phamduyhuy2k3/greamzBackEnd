app.controller("voucherController", function ($scope, $http, $document, $cookies) {
    $scope.vouchers = [];

    $scope.action = 'create'
    $scope.voucher = {
        id: null,
        name: '',
        description: '',
        dateAt: '',
        dateExpired: '',
        discount: '',
        orderCondition: '',
        maxPrice: ''
    }

    $scope.create = function () {
        console.log($scope.voucher)
        $http.post("/api/voucher/create", $scope.voucher, {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken"),
                "Content-Type": "application/json"
            }
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
                    text: "Error deleting voucher!",
                });
                console.log("Error", error);
            }
        )
    }

    $scope.delete = function (id) {
        Swal.fire({
            title: "Do you want to delete this category?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, delete it!"
        }).then((result) => {
            if (result.isConfirmed) {
                if (id) {
                    $http.delete(`/api/voucher/delete/${id}`, {
                        headers: {
                            "Authorization": "Bearer " + $cookies.get("accessToken")
                        }
                    }).then(resp => {
                        Swal.fire({
                            title: "Deleted!",
                            text: "Your voucher has been deleted.",
                            icon: "success"
                        });
                        $scope.initialize();
                    }).catch(error => {
                        Swal.fire({
                            icon: "error",
                            title: "Oops...",
                            text: "Error deleting voucher!",
                        });
                        console.log("Error", error);
                    });
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "Error deleting voucher!",
                    });
                    console.log("Error", error);
                }
            }
        });
    }

    $scope.reset = function () {
        $scope.voucher = {
            id: null,
            name: '',
            description: '',
            dateAt: '',
            dateExpired: '',
            discount: '',
            orderCondition: '',
            maxPrice: ''
        }
        $scope.action = 'create';
        $scope.initialize();
    }

    $scope.update = function () {
        $http.p("/api/voucher/update", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            },
            data: $scope.voucher
        }).then(
            resp => {
                $scope.initialize();
            },
            error => {
            }
        )
    }


    $scope.edit = async function (id) {
        await $http.get(`/api/voucher/${id}`, {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get('accessToken')
            }
        }).then(resp => {
                $scope.action = 'update';
                $scope.voucher = resp.data;
            }, error => {
                return error;
            }
        )
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
            $http.get(`/api/voucher/findAllPagination?page=${this.number}&size=7`, {
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

    $scope.initialize = function () {
        $http.get("/api/voucher/findAllPagination", {
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
    }
    $scope.initialize();

});