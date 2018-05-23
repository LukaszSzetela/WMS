(function () {
    'use strict';

    angular.module('products').service('OrderService', OrderService);

    OrderService.$inject = ['$http'];

    function OrderService($http) {
        var service = {
            getOrders: getOrders,
            getOrder: getOrder,
            changeOrderStatus: changeOrderStatus,
            getCurrentOrderForUser: getCurrentOrderForUser,
            sendCriteriaToExportOrders: sendCriteriaToExportOrders,
            updateOrder: updateOrder,
            sendDataToExportOrdersByDatesInRange: sendDataToExportOrdersByDatesInRange,
            sendDataToExportOrdersByUser: sendDataToExportOrdersByUser
        };

        return service;

        function getOrders() {
            return $http.get('/api/orders')
                .then(getComplete)
                .catch(getFailed);
        }

        function getOrder(id) {
            return $http.get('/api/orders/' + id)
                .then(getComplete)
                .catch(getFailed);
        }

        function getCurrentOrderForUser() {
            return $http.get('/currentorder')
                .then(getComplete)
                .catch(getFailed);
        }

        function changeOrderStatus(order, action) {
            return $http.get('/changestatus', {
                params: {
                    id: order.id, status: order.status, action: action
                }
            }).then(getComplete)
                .catch(getFailed);
        }

        function sendCriteriaToExportOrders(statusesToPrint) {
            return $http.get('/catchStatuses', {
                params: {
                    statuses: statusesToPrint.toString()
                }
            }).then(getComplete)
                .catch(getFailed);
        }

        function sendDataToExportOrdersByDatesInRange(dateFrom, dateTo) {
            return $http.get('/catchDatesToReport', {
                params: {
                    dateFrom: dateFrom.toString(), dateTo: dateTo.toString()
                }
            }).then(getComplete)
                .catch(getFailed);
        }

        function sendDataToExportOrdersByUser(user) {
            return $http.get('/catchUserToReport', {
                params: {
                    user: user
                }
            }).then(getComplete)
                .catch(getFailed);
        }

        function getComplete(response) {
            return response.data;
        }

        function getFailed(error) {
            console.log('Error in ProductService: ' + error.data);
        }

        function updateOrder(orderId, isOrderComplete, productId, status) {
            return $http.get('/updateOrder', {
                params: {
                    id: orderId, productId: productId, status: status, isComplete: isOrderComplete
                }
            });
        }
    }
})();
