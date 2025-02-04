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
            controller: "orderController"
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

app.run(function ($rootScope, $location, $http, $cookies, $route) {
    $rootScope.requireRoles = ["ADMIN", "MANAGER", "EMPLOYEE"];
    $rootScope.requireRolesAdmin = ["ADMIN", "MANAGER"];
    if ($cookies.get('access_token') === 'undefined') {
        window.location.href = "/sign-in";

    } else {
        $rootScope.checkRoleAdmin = function () {
            return $rootScope.requireRolesAdmin.some(role => role === $rootScope.account.role)
        }
        $rootScope.logoutForUserDontHaveRole = function () {
            const hasAnyAuthority = $rootScope.checkRoleAdmin();
            if (!hasAnyAuthority) {
                alert("You don't have permission to access this page.")
                $location.path('/');
            }
        }
        $rootScope.fetchAccount = async function () {
            let reult = await $http.get(`/api/user/currentUser`).then(resp => {
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
            $http.post("/api/v1/auth/logout",  {
                headers: {
                    "Authorization": "Dashboard"
                }
            }).then(resp => {
                window.location.href = "/sign-in"
            })

        }
        $rootScope.$on('$routeChangeStart', function () {
            $rootScope.fetchAccount().then(resp => {
                if (!resp) {
                    window.location.href = "/sign-in";
                }
                const hasAnyAuthority = $rootScope.requireRoles.some(role => role === $rootScope.account.role);
                if (!hasAnyAuthority) {
                    $rootScope.account = null;
                    window.location.href = "/sign-in?error=access_denied"
                }
                if ($route.current.templateUrl=='/pages/userList.html') {
                    $rootScope.logoutForUserDontHaveRole();
                }

            });
        });
        $rootScope.$on('$routeChangeSuccess', function () {

        });
        $rootScope.$on('$routeChangeError', function () {

        });

    }


})

