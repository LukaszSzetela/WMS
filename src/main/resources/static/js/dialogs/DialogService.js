(function () {
    'use strict';

    angular.module('users').service('DialogService', DialogService);

    DialogService.$inject = ['$mdDialog'];

    function DialogService($mdDialog) {
        var service = {
            cancel: cancel,
            finish: finish,
            open: open,
            openWithControllerAndParam: openWithControllerAndParam,
            openWithControllerAndParams: openWithControllerAndParams,
            openWithController: openWithController
        };

        return service;

        function cancel() {
            $mdDialog.cancel();
        }

        function finish() {
            $mdDialog.hide();
        }

        function open(event, parent, currentTarget) {
            var templateUrl = event.currentTarget !== null ? event.currentTarget.name + ".tmpl.html" :
                currentTarget.name + ".tmpl.html";
            return $mdDialog.show({
                templateUrl: templateUrl,
                parent: angular.element(parent),
                targetEvent: event,
                clickOutsideToClose: true
            });
        }

        function openWithControllerAndParam(event, parent, ctrl, ctrlAs, id) {
            var templateUrl = event.currentTarget.name + ".tmpl.html";
            return $mdDialog.show({
                locals: {id: id},
                controller: ctrl,
                controllerAs: ctrlAs,
                templateUrl: templateUrl,
                parent: angular.element(parent),
                targetEvent: event,
                clickOutsideToClose: true
            });
        }

        function openWithControllerAndParams(event, parent, ctrl, ctrlAs, id, number, reservedNumber) {
            var templateUrl = event.currentTarget.name + ".tmpl.html";
            return $mdDialog.show({
                locals: {id: id, number: number, reservedNumber: reservedNumber},
                controller: ctrl,
                controllerAs: ctrlAs,
                templateUrl: templateUrl,
                parent: angular.element(parent),
                targetEvent: event,
                clickOutsideToClose: true
            });
        }

        function openWithController(event, $scope) {
            var templateUrl = event.currentTarget.name + ".tmpl.html";
            return $mdDialog.show({
                controller: angular.noop,
                controllerAs: 'vm',
                templateUrl: templateUrl,
                targetEvent: event,
                clickOutsideToClose: true,
                bindToController: true,
                locals: {parent: $scope}
            });
        }
    }
})();
