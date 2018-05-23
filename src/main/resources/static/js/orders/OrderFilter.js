(function () {
    'use strict';

    angular.module('orders')
        .filter('ordererId', function () {
            return function (input, userId) {
                var out = [];
                for (var i = 0; i < input.length; i++) {
                    if (input[i].ordererId === userId) {
                        out.push(input[i]);
                    }
                }
                return out;
            }
        })

        .filter('status', function () {
            return function (input, stat, role) {
                var out = [];
                var output = [];
                if (role === 'Warehouseman') {
                    for (var i = 0; i < input.length; i++) {
                        if (input[i].status === stat) {
                            out.push(input[i]);
                        }
                    }
                    return out;
                } else {
                    for (var i = 0; i < input.length; i++) {
                        if (input[i].status === 'Accepted' || input[i].status === 'Ordered') {
                            output.push(input[i]);
                        }
                    }
                    return output;
                }
            }
        })

        .filter('statusWithoutRole', function () {
            return function (input, stat) {
                var out = [];
                for (var i = 0; i < input.length; i++) {
                    if (input[i].status === stat) {
                        out.push(input[i]);
                    }
                }
                return out;
            }
        })

        .filter('executorId', function () {
            return function (input, userId) {
                var out = [];
                for (var i = 0; i < input.length; i++) {
                    if (input[i].executorId === userId) {
                        out.push(input[i]);
                    }
                }
                return out;
            }
        })

        .filter('executorAndStatuses', function () {
            return function (input, stat1, stat2, stat3, userId) {
                var out = [];
                for (var i = 0; i < input.length; i++) {
                    if (input[i].executorId === userId && (input[i].status === stat1 || input[i].status === stat2 || input[i].status === stat3)) {
                        out.push(input[i]);
                    }
                }
                return out;
            }
        });
})();

