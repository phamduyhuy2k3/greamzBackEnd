app.controller("reviewController", function ($scope, $http, $document, $cookies) {
    $scope.reviews = [];

    $scope.action = 'create'

    $scope.review = {
        text: '',
        rating: 0,
        likes: '',
        dislikes: ''
    }

    $scope.create = function () {
        console.log($scope.review)
        $http.post("/api/v1/review/create", $scope.review, {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken"),
                "Content-Type": "application/json"
            }
        }).then(
            resp => {
                console.log($scope.review)
                if ($scope.action === 'create') {
                    Swal.fire({
                        position: "center",
                        icon: "success",
                        title: "Saved successfully!",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    $scope.reset();
                    $scope.initialize()
                } else {
                    Swal.fire({
                        position: "center",
                        icon: "success",
                        title: "Update successfully!",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    $scope.initialize()
                }
            },
            error => {
                alert("Error!");
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
                        $http.delete(`/api/v1/review/delete/${id}`, {
                            headers: {
                                "Authorization": "Bearer " + $cookies.get("accessToken")
                            }
                        }).then(resp => {
                                Swal.fire({
                                    position: "center",
                                    icon: "success",
                                    title: "Delete successfully!",
                                    showConfirmButton: false,
                                    timer: 1500
                                })
                                $scope.initialize();
                            },
                            error => {
                                Swal.fire({
                                    position: "center",
                                    icon: "error",
                                    title: "Delete failed!",
                                    showConfirmButton: false,
                                    timer: 1500
                                })
                            }
                        )
                    }
                }
            }
        )

    }


    $scope.edit = async function (id) {
        await $http.get(`/api/v1/review/${id}`, {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get('accessToken')
            }
        }).then(resp => {
                $scope.action = 'update';
                $scope.review = resp.data;
            }, error => {
                return error;
            }
        )
    }
    $scope.reset = function () {
        $scope.action = 'create';
        $scope.review = {
            text: '',
            rating: '',
            likes: '',
            dislikes: ''
        }
        $scope.initialize();
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
            $http.get(`/api/v1/review/findAllPagination?page=${this.number}&size=7`, {
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
        $http.get("/api/v1/review/findAllPagination", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(
            resp => {
                $scope.pager = {
                    ...$scope.pager,
                    ...resp.data
                };
                console.log($scope.pager)
            },
            error => {
                console.log("Error", error);
            }
        );
    }
    $scope.initialize()

})
;