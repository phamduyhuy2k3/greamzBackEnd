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
        .otherwise({
            redirectTo: "/"
        });
});

