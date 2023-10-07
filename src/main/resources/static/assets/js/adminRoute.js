var app = angular.module("myApp", ["ngRoute"]);
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
        //order management
        .when("/order", {
            templateUrl: "/pages/orderList.html",
            controller: "oderController"
        })
        //user management
        .when("/user", {
            templateUrl: "/pages/profile.html",
            controller: "profileController"
        })
        //review management
        .when("/review", {
            templateUrl: "/pages/profile.html",
            controller: "reviewController"
        })
        //queue management
        .when("/queue", {
            templateUrl: "/pages/profile.html",
            controller: "queueController"
        })
        //voucher management
        .when("/voucher", {
            templateUrl: "/pages/profile.html",
            controller: "voucherController"
        })
        // .when("/sign-in", {
        //     templateUrl: "/sign-in.html",
        //     controller: "signInController"
        // })
        .otherwise({
            redirectTo: "/"
        });
});

