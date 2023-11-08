app.controller("reviewController", function ($scope, $http, $document, $cookies) {
    $scope.reviews = [];

    $scope.action = 'create'

    $scope.review = {
        id: null,
        text: '',
        rating: '',
        likes: '',
        dislikes: ''
    }

    $scope.create = function () {
        console.log($scope.review)
        $http.post("/api/review/create", $scope.review,{
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken"),
                "Content-Type": "application/json"
            }
        }).then(
            resp => {
            console.log($scope.review)
                $scope.initialize()
            },
            error => {

            }
        )
    }

    $scope.delete = function (id) {
                if (confirm("Bạn muốn xóa sản phẩm này?")) {
                    $http.delete(`/api/review/delete/${id}`, {
                        headers: {
                            "Authorization": "Bearer " + $cookies.get("accessToken")
                        }
                    }).then(resp => {
                        $scope.initialize();
                        // $scope.reset();
                        alert("Xóa sản phẩm thành công!");
                    }).catch(error => {
                        alert("Lỗi xóa sản phẩm!");

                        console.log("Error", error);
                    })
                }
            }






     $scope.edit =async function (id) {
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
            $scope.review = {
                id: null,
                text: '',
                rating: '',
                likes: '',
                dislikes: ''
        }
                $scope.action = 'create';
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
            $http.get(`/api/review/findAllPagination?page=${this.number}&size=7`, {
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
        $http.get("/api/review/findAllPagination", {
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
    $scope.initialize()

});