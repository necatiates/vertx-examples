var casApp = angular.module('casApp', ['ngRoute']);
        casApp.factory('Auth', function(){
            var user = { username : '-',balance : '-'};


            return{
                setUser : function(aUser){
                    user = aUser;
                },
                isLoggedIn : function(){
                    return(user.username != "-")? true : false;
                },
                getUser : function(){
                    return user;
                }
              }
        });
        casApp.factory('SizingService', ['$window',function($window) {
            return {
                getGameHeight: function() {
                    var div = document.getElementById('emCalcDiv');
                    div.style.height = "1em";
                    em = div.offsetHeight;
                    return  $window.innerHeight - em * 4.25;
                }
            };
        }]);

        casApp.run(['$rootScope', '$location', 'Auth',function ($rootScope, $location,Auth) {
            var forbiddenPaths = ['/deposit','/cashout'];
            $rootScope.currentUserSignedIn = false;
            $rootScope.user = Auth.getUser();
            $rootScope.$on('$routeChangeStart', function (event) {
                    if (forbiddenPaths.indexOf($location.$$path) != -1) {
                        if (!Auth.isLoggedIn()) {
                            console.log('DENY');
                            event.preventDefault();
                            $location.path('/signin');
                        }
                    }
            });
            $rootScope.$watch(Auth.isLoggedIn, function (value, oldValue) {
                if(!value && oldValue) {
                  console.log("Disconnect");
                  $location.path('/login');
                }
                if(value) {
                  $rootScope.user = Auth.getUser();
                  $rootScope.currentUserSignedIn = true;
                  $location.path('/games');
                }

              }, true);

            $rootScope.$watch(document.getElementById('navPanel'), function (value, oldValue) {
                console.log(value);
                console.log(oldValue);

            }, true);

        }]);

        // configure our routes
        casApp.config(function($routeProvider) {
            $routeProvider

                // route for the home page
                .when('/', {
                    templateUrl : 'games.html',
                    controller  : 'gamesController'
                })
                .when('/games', {
                    templateUrl : 'games.html',
                    controller  : 'gamesController'
                })
                // route for the about page
                .when('/signin', {
                    templateUrl : 'signin.html',
                    controller  : 'signinController'
                })
                // route for the contact page
                .when('/signup', {
                    templateUrl : 'signup.html',
                    controller  : 'signupController'
                })
                .when('/deposit', {
                    templateUrl : 'deposit.html',
                    controller  : 'depositController'
                })
                .when('/cashout', {
                    templateUrl : 'cashout.html',
                    controller  : 'cashoutController'
                })
                .when('/helpdesk', {
                    templateUrl : 'helpdesk.html',
                    controller  : 'helpdeskController'
                })
                .when('/scratchCard', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'scratchCardController'
                })
                .when('/exit', {
                    templateUrl : 'exit.html',
                    controller  : 'exitController'
                });
        });

        // create the controller and inject Angular's $scope

        casApp.controller('gamesController', ['$scope', 'Auth', '$location', function ($scope, Auth, $location) {
         

        }]);


        casApp.controller('helpdeskController', function($scope) {
            
        });

        casApp.controller('signupController', function($scope) {
            
        });

        casApp.controller('depositController', function($scope) {
           
        });

        casApp.controller('cashoutController', [ '$scope', 'Auth',function($scope,Auth) {

        }]);

        casApp.controller('exitController', [ '$scope', 'Auth',function($scope,Auth) {
            
        }]);

        casApp.controller('scratchCardController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://localhost/private/scratchcard/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);

        casApp.controller('signinController', [ '$scope', 'Auth', '$location', function ($scope, Auth, $location) {
          $scope.login = function () {
            // Ask to the server, do your job and THEN set the user
            Auth.setUser({username : $scope.username,balance : 0}); //Update the state of the user in the app
          };
        }])