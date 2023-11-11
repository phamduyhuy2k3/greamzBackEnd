app.controller("platformController", function ($scope, $http, $document, $cookies) {
    $scope.platforms = [];
    $scope.devices = [];
    $scope.action = 'create'
    $scope.form = {
        id: '',
        name: '',
        description: '',
        devices: [],
    }
    $scope.reset= function(){
        $scope.form = {
            id: '',
            name: '',
            description: '',
            devices: [],
        }
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
            $http.get(`/api/v1/platform/findAllPagination?page=${this.number}&size=10`, {
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
        $scope.reset();
        $http.get("/api/v1/platform/findAllPagination", {
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

        $http.get("/api/v1/platform/devices", {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(
            resp => {
                $scope.devices = resp.data;
                $scope.devices.forEach(device => {
                    device.text = device;
                    device.id = device;
                })
            },
            error => {
                console.log("Error", error);
            }
        ).then(()=>{
            if (!$scope.selectDevice) {
                $scope.selectDevice = $('#deviceSelect').select2({
                    // Use the modified 'data' object
                    data: $scope.devices,
                    placeholder: "Select devices",
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
                $scope.selectDevice.on('select2:select', function (e) {
                    let data = e.params.data;
                    $scope.$apply(function () {
                        $scope.form.devices.push(data.id);
                    });
                });
                $scope.selectDevice.on('select2:unselect', function (e) {
                    const id = e.params.data.id;
                    const removedIndex = $scope.form.devices.findIndex(data => data === id);
                    console.log(removedIndex)
                    $scope.$apply(() => {
                        $scope.form.devices.splice(removedIndex, 1);
                    })

                });
            }
        });
    }

    $scope.create = function () {
        // Gán giá trị type từ selectedCategoryType vào form
        $http.post("/api/v1/platform/save", $scope.form, {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken"),
                "Content-Type": "application/json"
            },
        }).then(
            resp => {
                alert("Saved successfully!");
                console.log($scope.form)
                $scope.initialize();
            },
            error => {
                console.log("Error", error);
            }
        )
    }

    $scope.delete = function (id) {
        if (confirm("Do you want to delete this category?")) {
            if (id) {
                $http.delete(`/api/v1/platform/delete/${id}`, {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken")
                    }
                }).then(resp => {
                    alert("Deleted successfully!");
                    $scope.initialize();
                }).catch(error => {
                    alert("Error deleting platform!");
                    console.log("Error", error);
                });
            } else {
                alert("Error deleting platform!");
            }
        }
    }

    $scope.edit = function (id) {
        $http.get("/api/v1/platform/"+id, {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken"),
                        "Content-Type": "application/json"
                    },
                }).then(
                    resp => {
                        $scope.form= resp.data
                        $scope.initialize();
                    },
                    error => {
                        console.log("Error", error);
                    }
                )
    }

    $scope.initialize();
});