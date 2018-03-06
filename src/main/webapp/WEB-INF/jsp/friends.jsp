<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="app">
    <head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="description" content="">
  <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no" />
  <title>Друзья и друзья друзей</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">	
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
 
  <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.2/angular.min.js"></script>

  <script>
        var scope;
        (function() {

            'use strict';

            angular
                .module('app', [])
                .config( [
                    '$compileProvider',
                    function( $compileProvider )
                    {   
                        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension|consultantplus):/);
                        // Angular before v1.2 uses $compileProvider.urlSanitizationWhitelist(...)
                    }
                ])
                .controller('AppController', ['$scope', '$http', '$element', function($scope, $http, $element) {
                    scope = $scope;
                    $scope.users = ${users};
                    $scope.sortType = "numberAndProximity";
                   
                    $scope.loadMoreUsers = function(){
                        var requestText = "friends?count=10"  
                                            + "&offset=" + $scope.users.length 
                                            + "&access_token="  + $scope.accessToken 
                                            + "&user_id=" + $scope.userId
                                            + "&sortType=" + $scope.sortType;
                        
                        $http.get(requestText).success(function(data){
                            for(var i in data){
                                $scope.users.push(data[i]);
                            }    
                        });
                    };
                    
                    $scope.sortTypeChange = function(){
                        $scope.users = [];
                        $scope.loadMoreUsers();
                    }
                    
                }]);

        })();
       
        $(document).ready(function(){
            
            $(document).scroll(function(){
                if($(window).scrollTop() + $(window).height() >= $(document).height()){
                    $("#more-button").click();
                }
            });
            
            $(window).resize(function (){
                $("body").css("font-size", $(window).width()/150);
                var tableTop =  $("#filters").height()  + 96;
                $("#table").css("margin-top", tableTop);
            });
            $(window).resize()
        });
  </script>   

  <style>
      #table td{
          padding-top:5px;
          padding-bottom:5px;
      }
      
      .angular-select-checked{
          padding: 0.75em !important;
      }
  </style>
</head>
<body ng-controller="AppController">
    <button ng-click="loadMoreUsers()" id="more-button" type="button" style="display: none"></button>
    <div style = "width:100%;height:50px;">
        <div>demonet</div>
    </div>
    <select ng-change ="sortTypeChange()" ng-model = "sortType">
        <option value="number">По номеру аккаунта</option>
        <option value="numberAndProximity">По количеству общих друзей и номеру аккаунта</option>
        <option value="sim">По схожести</option>
    </select>
    <div ng-repeat="user in users"> 
        <div>
            {{user.first_name}}&nbsp{{user.last_name}}
        </div>
        <div>
            <img src="{{user.photo_100}}" >
        </div>
    </div>
</body>
</html>
