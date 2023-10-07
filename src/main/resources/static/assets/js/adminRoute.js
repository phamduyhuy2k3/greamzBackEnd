var app = angular.module("myApp", ["ngRoute"]);
app.config(function ($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "/pages/dashboard.html",
            controller: "dashboardController"
        })
        .when("/game", {
            templateUrl: "/pages/gameList.html",
            controller: "gameController"
        })
        .when("/order", {
            templateUrl: "/pages/orderList.html",
            controller: "oderController"
        })
        .when("/review", {
            templateUrl: "/pages/reviewList.html",
            controller: "reviewController"
        })
        .when("/voucher", {
            templateUrl: "/pages/voucherList.html",
            controller: "voucherController"
        })
        .when("/queue", {
            templateUrl: "/pages/notifications.html",
            controller: "queueController"
        })
        .when("/user", {
            templateUrl: "/pages/userList.html",
            controller: "userController"
        })
        .when("/logOut", {
            templateUrl: "/pages/sign-in.html",
            controller: "logOutController"
        })
        .otherwise({
            redirectTo: "/"
        });
});

