/* global angular */

(function () {

    'use strict';

    var modules = [
        'angularUtils.directives.dirPagination',
        'ngMaterial',
        'ngCart',
        'users',
        'products',
        'carts',
        'dialogs',
        'orders',
        'filters',
        'growlNotifications'
    ], app = angular.module('app', modules);

    angular.module('users', []);
    angular.module('products', []);
    angular.module('carts', []);
    angular.module('dialogs', []);
    angular.module('orders', []);
    angular.module('filters', []);

    app.constant('ORDER_STATUSES', ['Ordered', 'Accepted', 'Assigned', 'Realize', 'Pause', 'Completed', 'Cancelled']);
    app.constant('USER_ROLES', ['Admin', 'Warehouseman', 'Office', 'Customer']);
})();
