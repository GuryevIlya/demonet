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
  <link rel="stylesheet" href="static/css/common.css">
  <link rel="stylesheet" href="static/css/font-awesome-4.7.0/font-awesome-4.7.0/css/font-awesome.min.css">
 
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
                    $scope.isUploadingFriends = false;
                   
                    $scope.loadMoreUsers = function(){
                        if($scope.isUploadingFriends == true){
                            return;
                        }
                        var requestText = "friends?count=10"  
                                            + "&offset=" + $scope.users.length 
                                            + "&access_token="  + $scope.accessToken 
                                            + "&user_id=" + $scope.userId
                                            + "&sortType=" + $scope.sortType;
                        
                        $scope.isUploadingFriends = true;
                        $http.get(requestText).success(function(data){
                            for(var i in data){
                                $scope.users.push(data[i]);
                            }    
                        });
                        $scope.isUploadingFriends = false;
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
    <div style = "width:100%;box-sizing:border-box;height:50px;background-color:#29487d;color:white;font-family:Anton;padding: 0 250px;position:fixed;z-index:10000">
        <div style = "max-width:1036px;margin:0 auto;height:50px;">
            <span style = "font-size:25px;line-height: 50px;">demonet</span>
        </div>
    </div>
    <div style = "width:1020px;margin: 0 auto;top:70px;position:relative;">
        <div style = "display: inline-block;vertical-align: top;height:2000px;width: 150px;position:fixed;">
            <div>
                <div style = "padding:2px;">
                    <i class="fa fa-heart" aria-hidden="true" style = "color:#285473;margin-right:5px;font-size: 18px;"></i>
                    <span style = "font-family:sans-serif;font-size: 12.5px;color:black;">Симпатии</span>
                    <span style = "font-family:sans-serif;font-size: 10.5px;font-weight: bold;">+5</span>
                </div>
                <div style = "padding:2px;">
                    <i class="fa fa-heart" aria-hidden="true" style = "color:#9ab3c7;margin-right:5px;font-size: 15px;"></i>
                    <span style = "font-family:sans-serif;font-size: 12.5px;color:#6a6a6a">Симпатии</span></span>
                </div>
                <div style = "padding:2px;">
                    <i class="fa fa-diamond" aria-hidden="true" style = "color:#9ab3c7;margin-right:5px;font-size: 15px;"></i>
                    <span style = "font-family:sans-serif;font-size: 12.5px;color:#6a6a6a">Симпатии</span></span>
                </div>
                <div style = "padding:2px;">
                    <i class="fa fa-heart-o" aria-hidden="true" style = "color:#285473;margin-right:5px;font-size: 15px;"></i>
                    <span style = "font-family:sans-serif;font-size: 12.5px;color:#6a6a6a">Симпатии</span></span>
                </div>
                <div style = "padding:2px;">
                    <i class="fa fa-diamond" aria-hidden="true" style = "color:#285473;margin-right:5px;font-size: 15px;"></i>
                    <span style = "font-family:sans-serif;font-size: 12.5px;color:#6a6a6a">Симпатии</span></span>
                </div>
                <div style = "padding:2px;">
                    <i class="fa fa-cubes" aria-hidden="true" style = "color:#285473;margin-right:5px;font-size: 15px;"></i>
                    <span style = "font-family:sans-serif;font-size: 12.5px;color:#6a6a6a">Расчеты</span></span>
                </div>
                <div style = "padding:2px;">
                    <i class="fa fa-address-book-o" aria-hidden="true" style = "color:#285473;margin-right:5px;font-size: 15px;"></i>
                    <span style = "font-family:sans-serif;font-size: 12.5px;color:#6a6a6a">Расчеты</span></span>
                </div>
            </div>    
        </div>
        <div style = "display: inline-block;vertical-align: top;height:3000px;width: 850px;margin-left: 150px;">
            <div style = "width:100%;background: white;margin-bottom: 20px;height: 100px;border-radius:3px;border-width: 1px;border-color:rgb(221, 223, 226);border-style:solid">
                
            </div>
            <div style = "width:100%;background: white;border-radius:3px;border-width: 1px;border-color:rgb(221, 223, 226);border-style:solid">
                <div ng-repeat = "user in users" style = "margin:0 auto;width:95%;border-bottom-width: 1px;border-bottom-color:rgb(221, 223, 226);border-bottom-style:solid;font-family: sans-serif;"> 
                    <div style = "width:100px;height: 100px;margin:5px;display: inline-block;vertical-align: top;">
                        <img src="{{user.urlPhoto}}" >
                    </div>
                    <div style = "display: inline-block;vertical-align: top;margin:5px;width:calc(95% - 100px)">
                        <div style = "font-weight: 700;font-size: 13px;">
                            <a href = "" class = "link" style = "color:#2a5885;">{{user.firstName}}&nbsp{{user.lastName}}</a>
                        </div>
                        <div style = "font-size:12.5px;font-size:12.5px">
                            <a href = "" class = "link" style = "color: #656565;">{{user.commonFriendsCount}}&nbspобщих друзей</a>
                        </div>
                    </div>
                    
                </div>
            </div>
        </div>
    </div>
    
<!--    <select ng-change ="sortTypeChange()" ng-model = "sortType">
                <option value="number">По номеру аккаунта</option>
                <option value="numberAndProximity">По количеству общих друзей и номеру аккаунта</option>
                <option value="sim">По схожести</option>
            </select>-->
    
    
    
    
</body>
</html>
