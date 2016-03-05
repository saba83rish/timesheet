'use strict';

angular.module('timesheetApp')
    .controller('ClientDetailController', function ($scope, $rootScope, $stateParams, entity, Client, User) {
        $scope.client = entity;
        $scope.load = function (id) {
            Client.get({id: id}, function(result) {
                $scope.client = result;
            });
        };
        var unsubscribe = $rootScope.$on('timesheetApp:clientUpdate', function(event, result) {
            $scope.client = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
