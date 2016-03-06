'use strict';

angular.module('timesheetApp')
    .controller('TimesheetDetailController', 
    	    ['$scope', '$rootScope', '$stateParams', 'entity', 'Timesheet', 'NgTableParams',
    		function ($scope, $rootScope, $stateParams, entity, Timesheet, NgTableParams) {
        $scope.timesheet = entity;
        
//        $scope.load = function (id) {
//            Timesheet.get({id: id}, function(result) {
//                $scope.timesheet = result;
//                $scope.transformTasksToUi();
//            });
//        };
        $scope.dateToUTC = function(dateString) {
        	var date = new Date(Date.parse(dateString));
            return new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate());
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
        				rowObj = {task:taskName,days:[0,0,0,0,0,0,0]};
        				rows[taskName]=rowObj;
        			}
        			taskDay = $scope.dateToUTC(tasks[idx].timeshetDate).getDay();
        			rowObj.days[taskDay] = tasks[idx].timeSpent;
        			console.log(taskName + " : day "+taskDay + " : hours " + rowObj.days[taskDay] +" : date "+tasks[idx].timeshetDate + " taskDay : "+taskDay);
        		}
        		console.log("rows:"+rows);
        		console.log("data:"+getMapValues(rows));
        		$scope.timesheet.tableParams = new NgTableParams({page: 1, count: 50}, {dataset: getMapValues(rows)});
        	} else {
        		$scope.timesheet.tableParams = new NgTableParams({page: 1, count: 50}, {dataset: []});
        	}
        };
        
        var unsubscribe = $rootScope.$on('timesheetApp:timesheetUpdate', function(event, result) {
            $scope.timesheet = result;
            $scope.transformTasksToUi();
        });
        $scope.$on('$destroy', unsubscribe);
        
        $scope.transformTasksToUi();

    }]);
