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
// <<<<<<< HEAD
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
//         // .when("/sign-in", {
//         //     templateUrl: "/sign-in.html",
//         //     controller: "signInController"
//         // })
// =======
//         .when("/review", {
//             templateUrl: "/pages/reviewList.html",
//             controller: "reviewController"
//         })
//         .when("/voucher", {
//             templateUrl: "/pages/voucherList.html",
//             controller: "voucherController"
//         })
//         .when("/queue", {
//             templateUrl: "/pages/notifications.html",
//             controller: "queueController"
//         })
//         .when("/user", {
//             templateUrl: "/pages/userList.html",
//             controller: "userController"
//         })
//         .when("/logOut", {
//             templateUrl: "/pages/sign-in.html",
//             controller: "logOutController"
//         })
// >>>>>>> e1983af60d668323dae317a7dc493ffb6f45ec44
        .otherwise({
            redirectTo: "/"
        });
});

