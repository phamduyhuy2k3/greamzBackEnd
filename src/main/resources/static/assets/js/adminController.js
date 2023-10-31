app.controller("mainController", function ($scope, $routeParams,$cookies, $http, $rootScope, $location) {
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
});


