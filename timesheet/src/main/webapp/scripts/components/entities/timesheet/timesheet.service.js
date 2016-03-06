'use strict';

angular.module('timesheetApp')
    .factory('Timesheet', function ($resource, DateUtils) {
        return $resource('api/timesheets/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.weekStart = DateUtils.convertDateTimeFromServer(data.weekStart);
                    data.weekEnd = DateUtils.convertDateTimeFromServer(data.weekEnd);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
