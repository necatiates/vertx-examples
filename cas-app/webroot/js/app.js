var iframeUrl = "localhost";
var casApp = angular.module('casApp', ['ngRoute']);
        casApp.factory('Auth',['$http', function($http){
            var user = { username : '-',balance : '-'};
            $http({
                method  : 'GET',
                url     : '/bet/info',  // pass in data as strings
                headers : { 'Accept': 'application/json' }  // set the headers so angular passing info as form data (not request payload)
            })
                .success(function(data) {
                    console.log(data);

                    if (!data.success) {
                        // if not successful, bind errors to error variables
                        $scope.errorName = data.errors.name;
                        $scope.errorSuperhero = data.errors.superheroAlias;
                    } else {
                        user = {username : data.username,balance : data.cash};
                        // if successful, bind success message to message
                    }
                });

            return{
                setUser : function(aUser){
                    user = aUser;
                },
                isLoggedIn : function(){
                    return(user.username != "-")? true : false;
                },
                getUser : function(){
                    return user;
                },
                addBalance : function(amount){
                    user.balance = user.balance + parseFloat(amount);
                    $('#navPanel a').each(function(index,el){
                        var jqEl = $(el);
                        if(jqEl.text().indexOf('Balance') != -1){
                            jqEl.text('Balance : ' + user.balance);
                        }
                    });
                },
                logout : function (){
                    user.balance = 0;
                    user.username = "-";
                }
              }
        }]);
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
            var forbiddenPaths = ['/deposit','/cashout','/poker','/scratchCard','/gorillaz','/assasin','/mayans','/nasa','/xcom','/amonra','/stoneage','/theft', '/movies','/100m','/gold_miner','/special_forces','/scary_house','/fruits','/spaceAdventure','/ultimateSoccer','/christmas','/arabianNight','/istanbul','/paris','/coctail','/bingo','/wallStreet','/superRich','/blackJack', '/cats'];
            $rootScope.currentUserSignedIn = false;
            $rootScope.user = Auth.getUser();
            $rootScope.$on('$routeChangeStart', function (event) {
                    if (forbiddenPaths.indexOf($location.$$path) != -1) {
                        if (!Auth.isLoggedIn()) {
                            event.preventDefault();
                            $location.path('/signin');
                        }
                    }
            });

            $rootScope.$watch(Auth.isLoggedIn, function (value, oldValue) {
                if(!value && oldValue) {
                  $rootScope.currentUserSignedIn = false;
                }
                if(value) {
                  $rootScope.user = Auth.getUser();
                  $rootScope.currentUserSignedIn = true;
                  $location.path('/games');
                }

              }, true);

            var attrMap = new Object(); // or var attrMap = {};
            attrMap['#deposit'] = true;
            attrMap['#cashout'] = true;
            attrMap['#exit'] = true;
            attrMap['undefined'] = true;
            attrMap['javascript:void(0)'] = true;
            $('body').on('DOMNodeInserted',function(e){
                if(typeof e.target.getAttribute === "function" && e.target.getAttribute('id') == 'navPanel'){
                    $('#navPanel a').each(function(index,el){
                        var jqEl = $(el);
                        if(attrMap[jqEl.attr('href')] && !Auth.isLoggedIn()){
                            jqEl.hide();
                        }else if(jqEl.attr('href') != '#games' &&
                            jqEl.attr('href') != '#helpdesk' &&
                            attrMap[jqEl.attr('href')] == undefined &&
                            Auth.isLoggedIn()){
                            jqEl.hide();
                        }else if(attrMap[jqEl.attr('href')] && Auth.isLoggedIn()){
                            jqEl.show();
                            if(jqEl.text().indexOf('Balance') != -1){
                                jqEl.text('Balance : ' + Auth.getUser().balance);
                            }else if(jqEl.text().indexOf('Username') != -1){
                                jqEl.text('Username : ' + Auth.getUser().username);
                            }
                        }
                        if(jqEl.attr('href') == 'undefined'){
                            jqEl.attr('href','javascript:void(0)');
                        }
                    });
                }
            });

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
                .when('/poker', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'pokerController'
                })
                .when('/gorillaz', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'gorillazController'
                })
                .when('/assasin', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'assasinController'
                })
                .when('/mayans', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'mayansController'
                })
                .when('/nasa', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'nasaController'
                })
                .when('/xcom', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'xcomController'
                })
                .when('/amonra', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'amonraController'
                })
                .when('/stoneage', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'stoneageController'
                })
                .when('/theft', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'theftController'
                })
                .when('/movies', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'moviesController'
                })
                .when('/100m', {
                    templateUrl : 'iframe_container.html',
                    controller  : '100mController'
                })
                .when('/gold_miner', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'goldMinerController'
                })
                .when('/special_forces', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'specialForcesController'
                })
                .when('/scary_house', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'scaryHouseController'
                })
                .when('/fruits', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'fruitsController'
                })
                .when('/spaceAdventure', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'spaceAdventureController'
                })
                .when('/ultimateSoccer', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'ultimateSoccerController'
                })
                .when('/christmas', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'christmasController'
                })
                .when('/arabianNight', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'arabianNightController'
                })
                .when('/istanbul', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'istanbulController'
                })
                .when('/paris', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'parisController'
                })
                .when('/coctail', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'coctailController'
                })
                .when('/bingo', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'bingoController'
                })
                .when('/wallStreet', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'wallStreetController'
                })
                .when('/superRich', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'superRichController'
                })
                .when('/blackJack', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'blackJackController'
                })
                .when('/cats', {
                    templateUrl : 'iframe_container.html',
                    controller  : 'catsController'
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

        casApp.controller('signupController', [ '$scope', 'Auth', '$location','$http', function ($scope, Auth, $location,$http) {
            $scope.formData = {};
            $scope.signup = function () {
                $http({
                    method  : 'POST',
                    url     : '/registerhandler',
                    data    : $.param($scope.formData),  // pass in data as strings
                    headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
                })
                    .success(function(data) {
                        console.log(data);

                        if (!data.success) {
                            // if not successful, bind errors to error variables
                            $scope.errorName = data.errors.name;
                            $scope.errorSuperhero = data.errors.superheroAlias;
                        } else {
                            Auth.setUser({username : data.username,balance : data.balance});
                            // if successful, bind success message to message
                        }
                    });

            };
        }]);

        casApp.controller('depositController', [ '$scope', 'Auth','$http',function($scope,Auth,$http) {
            $scope.formData = {};
            $scope.deposit = function () {
                $http({
                    method  : 'POST',
                    url     : '/transfer/checkin',
                    data    : $scope.formData,  // pass in data as strings
                    headers : { 'Content-Type': 'application/json' }  // set the headers so angular passing info as json data (not request payload)
                })
                    .success(function(data) {
                        console.log(data);

                        if (!data.success) {
                            // if not successful, bind errors to error variables
                            $scope.errorName = data.errors.name;
                            $scope.errorSuperhero = data.errors.superheroAlias;
                        } else {
                            Auth.addBalance($scope.formData.amount);
                            $scope.formData = {};
                            // if successful, bind success message to message
                        }
                    });

            };
        }]);
        casApp.controller('cashoutController', [ '$scope', 'Auth',function($scope,Auth) {

        }]);

        casApp.controller('exitController', [ '$scope', 'Auth',function($scope,Auth) {
            Auth.logout();
        }]);

        casApp.controller('scratchCardController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/scratchcard/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('pokerController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/poker/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('gorillazController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/gorillaz/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('assasinController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/assasins/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('mayansController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/mayan/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('nasaController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/nasa/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('xcomController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/xcom/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('amonraController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/amon_ra/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('stoneageController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/stoneage/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('theftController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/theft/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('moviesController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/movies/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('100mController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/100m/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('goldMinerController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/gold_miner/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('specialForcesController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/special_forces/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('scaryHouseController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/korku_evi/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('fruitsController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/slotmachine/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('spaceAdventureController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/spaceadventure/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('ultimateSoccerController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/ultimatesoccer/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('christmasController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/christmas/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('arabianNightController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/arabiannight/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('istanbulController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/istanbul/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('parisController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/paris/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('coctailController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/coctailstrach/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('bingoController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/bingo/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('wallStreetController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/wallstreet/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('superRichController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/rich/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('blackJackController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/blackjack/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);
        casApp.controller('catsController', [ '$scope', '$window','Auth','SizingService',function($scope,$window,Auth,SizingService) {
            $scope.iframeUrl = "https://" + iframeUrl + "/private/cats/index.html";
            $scope.iframeHeight = SizingService.getGameHeight() + "px";
        }]);

        casApp.controller('signinController', [ '$scope', 'Auth', '$location','$http', function ($scope, Auth, $location,$http) {
          $scope.formData = {};
          $scope.login = function () {
              $http({
                  method  : 'POST',
                  url     : '/loginhandler',
                  data    : $.param($scope.formData),  // pass in data as strings
                  headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
              })
                  .success(function(data) {
                      console.log(data);

                      if (!data.success) {
                          // if not successful, bind errors to error variables
                          $scope.errorName = data.errors.name;
                          $scope.errorSuperhero = data.errors.superheroAlias;
                      } else {
                          Auth.setUser({username : data.username,balance : data.balance});
                          // if successful, bind success message to message
                      }
                  });

          };
        }]);

