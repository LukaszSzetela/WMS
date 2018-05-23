(function () {
    'use strict';

    angular.module('carts').controller('CustomerCartController', CustomerCartController);

    CustomerCartController.$inject = ['DialogService', 'ngCart'];

    function CustomerCartController(dialogService, ngCart) {
        var vm = this;
        vm.onCancelClick = onCancelClick;
        vm.onCloseClick = onCloseClick;

        function onCancelClick() {
            dialogService.cancel();
            ngCart.empty();
        }

        function onCloseClick() {
            dialogService.finish();
        }
    }
})();

