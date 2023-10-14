var app = angular.module("myApp", ["ngRoute",'ngCookies']);
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
// app.run(function($http, $rootScope,$cookies){
//     $rootScope.$auth = {
//         username: null,
//         email: null,
//         photo: null,
//         fullname: null,
//         authorities: null,
//     };
//
//     $http.get(`/api/v1/user/profile`,{
//         headers: {
//             "Content-Type": "application/json",
//             "Authorization": "Bearer " + $cookies.get("access_token")
//         }
//     }).then(
//         resp => {
//             $rootScope.$auth = resp.data;
//
//     },
//         err => {
//
//             console.log(err);
//         }
//
//     );
// })

