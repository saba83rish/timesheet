'use strict';

angular.module('timesheetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('timesheet', {
                parent: 'entity',
                url: '/timesheets',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Timesheets'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/timesheet/timesheets.html',
                        controller: 'TimesheetController'
                    }
                },
                resolve: {
                }
            })
            .state('timesheet.detail', {
                parent: 'entity',
                url: '/timesheet/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Timesheet'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/timesheet/timesheet-detail.html',
                        controller: 'TimesheetDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Timesheet', function($stateParams, Timesheet) {
                        return Timesheet.get({id : $stateParams.id});
                    }]
                }
            })
            .state('timesheet.new', {
                parent: 'timesheet',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/timesheet/timesheet-dialog.html',
                        controller: 'TimesheetDialogController',
                        size: 'lg',
                        resolve: {
                        	account: function(){
                        		return {};
                        	},
                            entity: function () {
                                return {
                                    weekStart: null,
                                    weekEnd: null,
                                    userName: null,
                                    managerName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('timesheet', null, { reload: true });
                    }, function() {
                        $state.go('timesheet');
                    })
                }]
            })
            .state('timesheet.edit', {
                parent: 'timesheet',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/timesheet/timesheet-dialog.html',
                        controller: 'TimesheetDialogController',
                        size: 'lg',
                        resolve: {
                        	account: function(){
                        		return {};
                        	},
                            entity: ['Timesheet', function(Timesheet) {
                                return Timesheet.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('timesheet', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('timesheet.delete', {
                parent: 'timesheet',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/timesheet/timesheet-delete-dialog.html',
                        controller: 'TimesheetDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Timesheet', function(Timesheet) {
                                return Timesheet.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('timesheet', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
