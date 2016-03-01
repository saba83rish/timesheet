 'use strict';

angular.module('timesheetApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-timesheetApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-timesheetApp-params')});
                }
                return response;
            }
        };
    });
