var app = angular.module("myApp", ["ngRoute", "ngCookies"]);
app.config(function ($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "/pages/dashboard.html",
            controller: "dashboardController"
        })
        //game management
        .when("/game", {
            templateUrl: "/pages/gameList.html",
            controller: "gameController"
        })
        //category management
        .when("/category", {
            templateUrl: "/pages/categoryList.html",
            controller: "categoryController"
        })
        //platform management
        .when("/platform", {
            templateUrl: "/pages/platformList.html",
            controller: "platformController"
        })
        //order management
        .when("/order", {
            templateUrl: "/pages/orderList.html",
            controller: "oderController"
        })
        .when("/user", {
            templateUrl: "/pages/userList.html",
            controller: "userController"
        })
        //review management
        .when("/review", {
            templateUrl: "/pages/review.html",
            controller: "reviewController"
        })
        //queue management
        .when("/queue", {
            templateUrl: "/pages/notifications.html",
            controller: "queueController"
        })
        //voucher management
        .when("/voucher", {
            templateUrl: "/pages/voucherList.html",
            controller: "voucherController"
        })
        .otherwise({
            redirectTo: "/"
        });
});

app.run(function ($rootScope, $http, $cookies, $location) {
    if ($cookies.get('accessToken') === 'undefined') {
        window.location.href = "/sign-in";

    } else {
        console.log($cookies.get('accessToken'))
        $rootScope.$on('$routeChangeStart', function () {
            $rootScope.fetchAccount().then(resp => {
                if (!resp) {
                    window.location.href = "/sign-in";
                }
            });
        });
        $rootScope.$on('$routeChangeSuccess', function () {
            // alert("success")
        });
        $rootScope.$on('$routeChangeError', function () {

            alert("Lỗi");
        });

        $rootScope.fetchAccount = async function () {
            let reult = await $http.get(`/api/user/currentUser`,
                {
                    headers: {
                        'Authorization': 'Bearer ' + $cookies.get('accessToken')
                    }
                }).then(resp => {
                    if (resp.data) {
                        $rootScope.account = resp.data;
                        console.log($rootScope.account)
                    }
                    return true;
                }, error => {
                    alert("Your session has expired. Please log in again.")
                    $rootScope.account = null;
                    window.location.href = "/sign-in"
                    return false;
                }
            )
            return reult;
        }
        $rootScope.logout = function () {
            $cookies.remove('accessToken');
            $cookies.remove('refreshToken');
            $rootScope.account = null;
            window.location.href = "/sign-in"
        }
    }


})

