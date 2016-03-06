'use strict';

angular.module('timesheetApp').controller('TimesheetDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'account', 'Timesheet', 'NgTableParams', 'Principal',
        function($scope, $stateParams, $uibModalInstance, entity, account, Timesheet, NgTableParams, Principal) {
    	
    	$scope.account = account;
    	Principal.identity(true).then(function(account) {
            $scope.account = account;
            console.log(account);
        });

        $scope.timesheet = entity;
        
        $scope.getCurrentDate = function(){
        	var date = new Date();
        	return new Date(date.getFullYear(), date.getMonth(), date.getDate());
        };
        
        $scope.getDate = function(date, daysToAdd){
        	var newDate = new Date();
        	newDate.setTime( date.getTime() + daysToAdd * 86400000 );
        	return newDate;
        };
        
        $scope.load = function(id) {
        	if ( typeof id === 'undefined' ){
        		var curr = $scope.getCurrentDate(); // get current date
        		var first = new Date(curr.setDate(curr.getDate() - curr.getDay()));
        		var last = $scope.getDate(first, 6);//new Date(curr.setDate(curr.getDate() - curr.getDay()+6));
        		
        		$scope.timesheet = { weekStart: first, weekEnd:last, tasks:[{task:'', timeSpent:0, timeshetDate: curr}]}
        		console.log($scope.timesheet);
        		$scope.transformTasksToUi();
        	} else {
	            Timesheet.get({id : id}, function(result) {
	                $scope.timesheet = result;
	                $scope.transformTasksToUi();
	            });
        	}
        };
        
        $scope.transformTasksToUi = function(){
        	var getMapValues = function(map){
        		var arrayValues = new Array();

        		for (var key in map) {
        			arrayValues.push(map[key]);
        		}
        		return arrayValues;
        	}
        	
        	if ( typeof $scope.timesheet.tasks !== 'undefined' ){
        		var rows = {},
        			tasks = $scope.timesheet.tasks,
        			tasksLength = tasks.length,
        			taskName,
        			rowObj,
        			taskDay;
        		for(var idx=0; idx<tasksLength; idx++){
        			taskName = tasks[idx].task;
        			rowObj = rows[taskName];
        			if ( rowObj == null ){
//        				console.log('creating rowObj for'+taskName);
        				rowObj = {task:taskName, days:[0,0,0,0,0,0,0]};
        				rows[taskName]=rowObj;
        			}
        			taskDay = new Date(Date.parse(tasks[idx].timeshetDate)).getDay();
        			rowObj.days[taskDay] = tasks[idx].timeSpent;
        		}
        		$scope.timesheet.tableParams = new NgTableParams({page: 1, count: 50}, {dataset: getMapValues(rows)});
        	} else {
        		$scope.timesheet.tableParams = new NgTableParams({}, {dataset: []});
        	}
        };
        
        $scope.newRow = function(){
        	var rowObj = {task:'',days:[0,0,0,0,0,0,0]};
        	$scope.timesheet.tableParams.data.push(rowObj);
        }
        
        $scope.deleteRow = function(index){
        	$scope.timesheet.tableParams.data.splice(index, 1);
        }
        
        $scope.transformRowsToTasks = function(){
        	if ( typeof $scope.timesheet.tableParams !== 'undefined' ){
        		var tasks = [],
        			rows = $scope.timesheet.tableParams.data,
        			rowsLength = rows.length,
        			taskObj,
        			taskDate;
        		for(var idx=0; idx<rowsLength; idx++){
        			
        			for(var dayIdx=0; dayIdx<rows[idx].days.length; dayIdx++){
        				taskDate = $scope.getDate($scope.timesheet.weekStart, dayIdx);
        				if ( rows[idx].days[dayIdx] != null && rows[idx].days[dayIdx] > 0 ){
        					taskObj = {task:  rows[idx].task, timeSpent: rows[idx].days[dayIdx], timeshetDate: taskDate};
        					console.log("taskObj:"+taskObj);
        					
        					console.log();
        					tasks.push(taskObj);
        				}
        			}
        		}
        		$scope.timesheet.tasks = tasks;
        	} else {
        		$scope.timesheet.tasks = [];
        	}
        }

        var onSaveSuccess = function (result) {
            $scope.$emit('timesheetApp:timesheetUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            $scope.transformRowsToTasks();
            if ( typeof $scope.timesheet.id !== 'undefined') {
                Timesheet.update($scope.timesheet, onSaveSuccess, onSaveError);
            } else {
                Timesheet.save($scope.timesheet, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        
        $scope.load($stateParams.id);
}]);
