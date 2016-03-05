'use strict';

angular.module('timesheetApp')
    .factory('Client', function ($resource, DateUtils) {
        return $resource('api/clients/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.projectJoiningDate = DateUtils.convertLocaleDateFromServer(data.projectJoiningDate);
                    data.projectEndingDate = DateUtils.convertLocaleDateFromServer(data.projectEndingDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.projectJoiningDate = DateUtils.convertLocaleDateToServer(data.projectJoiningDate);
                    data.projectEndingDate = DateUtils.convertLocaleDateToServer(data.projectEndingDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.projectJoiningDate = DateUtils.convertLocaleDateToServer(data.projectJoiningDate);
                    data.projectEndingDate = DateUtils.convertLocaleDateToServer(data.projectEndingDate);
                    return angular.toJson(data);
                }
            }
        });
    });
