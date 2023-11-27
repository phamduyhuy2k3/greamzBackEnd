app.controller("dashboardController", function ($scope, $http, $document, $cookies) {
    $scope.totalGames = 0;
    $scope.totalGameLastWeek = 0;
    $scope.percentageChange = 0;

    $scope.gameOfTheMonth = [];
    $scope.gameOfTheYear = [];
    $scope.selectedMonth = '11';
    $scope.months = [
        {name: 'Tháng 1', value: '1'},
        {name: 'Tháng 2', value: '2'},
        {name: 'Tháng 3', value: '3'},
        {name: 'Tháng 4', value: '4'},
        {name: 'Tháng 5', value: '5'},
        {name: 'Tháng 6', value: '6'},
        {name: 'Tháng 7', value: '7'},
        {name: 'Tháng 8', value: '8'},
        {name: 'Tháng 9', value: '9'},
        {name: 'Tháng 10', value: '10'},
        {name: 'Tháng 11', value: '11'},
        {name: 'Tháng 12', value: '12'}

    ]

    $scope.isLoading = true;

    $scope.initialize = async function () {
        // Hàm để tạo mảng năm
        function generateYears() {
            const currentDate = new Date();
            const currentYear = currentDate.getFullYear();

            const years = [];
            for (let i = currentYear; i >= currentYear - 2; i--) {
                years.push({name: i.toString(), value: i.toString()});
            }

            return years;
        }

        // Hàm để chọn năm hiện tại hoặc năm tiếp theo nếu đã qua năm mới
        function getCurrentOrNextYear() {
            const currentDate = new Date();
            const currentYear = currentDate.getFullYear();

            // Nếu đã qua năm mới, chọn năm tiếp theo
            if (currentDate.getMonth() === 0) {
                return (currentYear - 1).toString();
            } else {
                return currentYear.toString();
            }
        }

        // Tạo mảng năm
        $scope.years = generateYears();

        // Chọn năm hiện tại làm giá trị mặc định cho ô chọn năm
        $scope.selectedYear = getCurrentOrNextYear();

        await $http.get("/api/v1/game/totalGame", {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get('token')
            }
        }).then(resp => {
            console.log(resp.data)
            $scope.totalGames = resp.data;


        });

        await $http.get(`/api/v1/game/totalGameLastWeek`, {
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
            console.log($scope.percentageChange)
        });

        $http.get(`/api/v1/dashboard/getTopSellingProductsInMonthYear?year=2023`, {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(resp => {
            $scope.gameOfTheYear = resp.data;
            $scope.gameOfTheMonth = $scope.gameOfTheYear['month11'];
            console.log(resp.data);
            console.log($scope.gameOfTheMonth)
            $scope.isLoading = false;
        })

    }
    $scope.getGameBestSeller = async function () {
        await $http.get(`/api/v1/dashboard/getTopSellingProductsInMonthYear?year=${$scope.selectedYear}`, {
            headers: {
                "Authorization": "Bearer " + $cookies.get("accessToken")
            }
        }).then(resp => {

            $scope.gameOfTheYear = resp.data;
            console.log(resp.data);
        })
    }
    $scope.getGamesByMonth = function () {
        $scope.gameOfTheMonth = $scope.gameOfTheYear['month' + $scope.selectedMonth];
        console.log($scope.gameOfTheMonth)
    }

    $scope.initialize();
});