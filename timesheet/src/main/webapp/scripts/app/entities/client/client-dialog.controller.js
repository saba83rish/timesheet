'use strict';

angular.module('timesheetApp').controller('ClientDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Client', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Client, User) {

        $scope.client = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Client.get({id : id}, function(result) {
                $scope.client = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('timesheetApp:clientUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.client.id != null) {
                Client.update($scope.client, onSaveSuccess, onSaveError);
            } else {
                Client.save($scope.client, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForProjectJoiningDate = {};

        $scope.datePickerForProjectJoiningDate.status = {
            opened: false
        };

        $scope.datePickerForProjectJoiningDateOpen = function($event) {
            $scope.datePickerForProjectJoiningDate.status.opened = true;
        };
        $scope.datePickerForProjectEndingDate = {};

        $scope.datePickerForProjectEndingDate.status = {
            opened: false
        };

        $scope.datePickerForProjectEndingDateOpen = function($event) {
            $scope.datePickerForProjectEndingDate.status.opened = true;
        };
}]);
