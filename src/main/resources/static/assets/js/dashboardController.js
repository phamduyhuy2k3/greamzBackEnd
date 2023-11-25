app.controller("dashboardController", function ($scope, $http, $document, $cookies) {
    $scope.totalGames = 0;
    $scope.totalGameLastWeek = 0;
    $scope.percentageChange = 0;

    $scope.gameOfTheMonth = [];
    $scope.gameOfTheYear = [];
    $scope.selectedMonth = '11';
    $scope.months = [
        {name:'Tháng 1', value:'1'},
        {name:'Tháng 2', value:'2'},
        {name:'Tháng 3', value:'3'},
        {name:'Tháng 4', value:'4'},
        {name:'Tháng 5', value:'5'},
        {name:'Tháng 6', value:'6'},
        {name:'Tháng 7', value:'7'},
        {name:'Tháng 8', value:'8'},
        {name:'Tháng 9', value:'9'},
        {name:'Tháng 10', value:'10'},
        {name:'Tháng 11', value:'11'},
        {name:'Tháng 12', value:'12'}

    ]

    // Tạo một đối tượng Date hiện tại
    const currentDate = new Date();
    $scope.selectedYear = currentDate.getFullYear();

// Lấy tháng hiện tại (chú ý rằng phương thức getMonth trả về giá trị từ 0 đến 11, nên bạn có thể cần cộng thêm 1)
    const currentMonth = currentDate.getMonth() + 1;

    $('.counter-count').each(function () {
        const totalString = $scope.totalGames.toString();
        $(this).prop('Counter', 0).animate({
            Counter: totalString
        }, {
            //chnage count up speed here
            duration: 2000,
            easing: 'swing',
            step: function (now) {
                $(this).text(Math.ceil(now));
            }
        });
    });

    $scope.initialize = function () {
        $http.get("/api/v1/game/totalGame", {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get('token')
            }
        }).then(resp => {
            console.log(resp.data)
            $scope.totalGames = resp.data;

            $http.get(`/api/v1/game/totalGameLastWeek`, {
                headers: {
                    'Authorization': 'Bearer ' + $cookies.get('token')
                }
            }).then(r => {
                console.log(r.data)
                $scope.totalGameLastWeek = r.data;

                // Hàm tính % sự thay đổi
                $scope.calculatePercentageChange = function (currentValue, previousValue) {
                    if (previousValue === 0) {
                        return currentValue > 0 ? 100 : 0;
                    }

                    const percentageChange = ((currentValue - previousValue) / previousValue) * 100;
                    return percentageChange.toFixed(1); // Làm tròn đến 2 chữ số thập phân
                };

                // Tính % sự thay đổi và gán vào biến $scope
                $scope.percentageChange = $scope.calculatePercentageChange($scope.totalGames, $scope.totalGameLastWeek);
            }).then(
                $http.get(`/api/v1/dashboard/getTopSellingProductsInMonthYear?year=${$scope.selectedYear}`, {
                    headers: {
                        "Authorization": "Bearer " + $cookies.get("accessToken")
                    }
                }).then(resp =>{
                    $scope.gameOfTheYear = resp.data;
                    $scope.gameOfTheMonth = $scope.gameOfTheYear['month11'];
                    console.log(resp.data);
                    console.log($scope.gameOfTheMonth)

                })
            )
        });


    }
    $scope.getGameBestSeller = function () {
        $http.get(`/api/v1/dashboard/getTopSellingProductsInMonthYear?year=${$scope.selectedYear}`, {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(resp => {
            $scope.gameOfTheYear = resp.data;
            console.log(resp.data);
        })
    }
    $scope.getGamesByMonth = function (){
        $scope.gameOfTheMonth = $scope.gameOfTheYear['month'+$scope.selectedMonth];
        console.log($scope.gameOfTheMonth)
    }

    $scope.initialize();
});