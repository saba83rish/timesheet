'use strict';

angular.module('timesheetApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


