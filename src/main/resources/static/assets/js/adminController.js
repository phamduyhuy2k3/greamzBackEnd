app.controller("mainController", function ($scope, $routeParams,$cookies, $http, $rootScope,$route, $location) {
    $http.get(`/api/user/currentUser`,
        {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get('accessToken')
            }
        }).then(resp => {
        if(resp.data){
            $rootScope.account = resp.data;
            console.log($rootScope.account)
        }
    });
    $scope.logout = function () {
        $cookies.remove('accessToken');
        $cookies.remove('refreshToken');
        $rootScope.account = null;
        window.location.href = "/sign-in"
    }
//breadcrumb
    $scope.breadcrumb = function (){
        let path = $location.path();
        if (path === "/") {
            return "Dashboard";
        } else if (path === "/game") {
            return "Game Management";
        } else if (path === "/category") {
            return "Category Management";
        } else if (path === "/order") {
            return "Order Management";
        } else if (path === "/user") {
            return "User Management";
        } else if (path === "/review") {
            return "Review Management";
        } else if (path === "/queue") {
            return "Queue Management";
        } else if (path === "/voucher") {
            return "Voucher Management";
        } else if (path === "/profile") {
            return "Profile";
        } else if (path === "/change-password") {
            return "Change Password";
        } else if (path === "/sign-in") {
            return "Sign In";
        } else if (path === "/sign-up") {
            return "Sign Up";
        } else if (path === "/forgot-password") {
            return "Forgot Password";
        } else if (path === "/reset-password") {
            return "Reset Password";
        } else if (path === "/game-detail") {
            return "Game Detail";
        } else if (path === "/cart") {
            return "Cart";
        } else if (path === "/checkout") {
            return "Checkout";
        } else if (path === "/order-history") {
            return "Order History";
        } else if (path === "/order-detail") {
            return "Order Detail";
        } else if (path === "/payment") {
            return "Payment";
        } else if (path === "/payment-success") {
            return "Payment Success";
        } else if (path === "/payment-failed") {
            return "Payment Failed";
        } else if (path === "/payment-cancel") {
            return "Payment Cancel";
        } else if (path === "/search") {
            return "Search";
        } else if (path === "/search-result") {
            return "Search Result";
        } else if (path === "/review-detail") {
            return "Review Detail";
        } else if (path === "/review-list") {
            return "Review List";
        } else if (path === "/review-edit") {
            return "Review Edit";
        }
    }

});


