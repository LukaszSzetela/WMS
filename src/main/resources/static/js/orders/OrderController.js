(function () {
    'use strict';

    angular.module('orders').controller('OrderController',
        OrderController);

    OrderController.$inject = ['$scope', 'DialogService', 'OrderService', 'UserService', 'ORDER_STATUSES', '$timeout'];

    function OrderController($scope, dialogService, orderService, userService, ORDER_STATUSES, $timeout) {
        var vm = this;
        vm.getOrders = getOrders;
        vm.showOrderList = showOrderList;
        vm.onCloseClick = onCloseClick;
        vm.verifyOrder = verifyOrder;
        vm.processOrder = processOrder;
        vm.showRealizeOrderList = showRealizeOrderList;
        vm.printOrders = printOrders;
        vm.printOrdersByDatesInRange = printOrdersByDatesInRange;
        vm.exportOrdersByDatesInRange = exportOrdersByDatesInRange;
        vm.print = print;
        vm.showCurrentOrder = showCurrentOrder;
        vm.showYourOrders = showYourOrders;
        vm.backToMobileButtons = backToMobileButtons;
        vm.flagCurrent = false;
        vm.flagYourOrders = false;
        vm.currentOrder = [];
        vm.onCompleteClick = onCompleteClick;
        vm.isOrderComplete = false;
        vm.checkIfOrderCompleted = checkIfOrderCompleted;
        vm.orderStatusList = [];
        vm.showCompletedOrdersList = showCompletedOrdersList;
        vm.minDate = new Date(2017, 12, 1);
        vm.maxDate = new Date();
        vm.dateFrom = new Date();
        vm.dateTo = new Date();
        vm.printOrdersByUsers = printOrdersByUsers;
        vm.printProductsByDatesInRange = printProductsByDatesInRange;
        vm.printSalesByProduct = printSalesByProduct;
        vm.showSuccessAcceptOrder = false;
        vm.showSuccessCancelOrder = false;
        vm.showSuccessDelayOrder = false;
        vm.showSuccessAssignOrder = false;
        vm.showSuccessCompletedOrder = false;
        vm.numberProcessingOrder = 0;


        initUserProps();
        vm.$onInit = function () {
            getOrders();
            getCurrentOrderForUser();
        };

        $scope.items = ORDER_STATUSES;
        $scope.selected = [];
        $scope.toggleStatus = function (item, list) {
            var idx = list.indexOf(item);
            if (idx > -1) {
                list.splice(idx, 1);
            }
            else {
                list.push(item);
            }
        };

        $scope.exists = function (item, list) {
            return list.indexOf(item) > -1;
        };

        $scope.isIndeterminate = function () {
            return ($scope.selected.length !== 0 &&
                $scope.selected.length !== $scope.items.length);
        };

        $scope.isChecked = function () {
            return $scope.selected.length === $scope.items.length;
        };

        $scope.toggleAllStatuses = function () {
            if ($scope.selected.length === $scope.items.length) {
                $scope.selected = [];
            } else if ($scope.selected.length === 0 || $scope.selected.length > 0) {
                $scope.selected = $scope.items.slice(0);
            }
        };

        $scope.showProductsSearch = function () {
            $scope.myVar = !$scope.myVar;
        };

        function getOrders() {
            vm.orders = [];
            orderService.getOrders().then(function (response) {
                vm.orders = response;
            });
        }

        function showOrderList(event) {
            return dialogService.open(event, document.body);
        }

        function showRealizeOrderList(event) {
            vm.showSuccessCompletedOrder = false;
            vm.currentTarget = event.currentTarget;
            vm.currentOrder = orderService.getCurrentOrderForUser().then(function (response) {
                vm.currentOrder = response;
                updateOrderStatusList(vm.currentOrder.products);
                dialogService.open(event, document.body, vm.currentTarget);
            });
        }

        function onCloseClick() {
            dialogService.cancel();
        }

        function initUserProps() {
            return userService.getUserProperties().then(function (response) {
                vm.userProps = response[0];
                return vm.userProps;
            });
        }

        function verifyOrder(event, id) {
            vm.showSuccessAcceptOrder = false;
            vm.showSuccessCancelOrder = false;
            vm.showSuccessDelayOrder = false;
            dialogService.cancel();
            return dialogService.openWithControllerAndParam(event, document.body, getOrder, 'pOCtrl', id);
        }

        function getOrder(id) {
            var vm = this;
            vm.order = orderService.getOrder(id).then(function (response) {
                return vm.order = response;
            });
        }

        function getCurrentOrderForUser() {
            vm.currentOrder = orderService.getCurrentOrderForUser().then(function (response) {
                vm.currentOrder = response;
                updateOrderStatusList(vm.currentOrder.products);
                vm.isOrderComplete = checkStatusNewOrder(vm.currentOrder.products);
            });
        }

        function printOrders(event) {
            return dialogService.open(event, document.body);
        }

        function printOrdersByDatesInRange(event) {
            return dialogService.open(event, document.body);
        }

        function printOrdersByUsers(event) {
            return dialogService.open(event, document.body);
        }

        function printProductsByDatesInRange(event) {
            return dialogService.open(event, document.body);
        }

        function printSalesByProduct(event) {
            return dialogService.open(event, document.body);
        }

        function print() {
            var selectedStatusToPrint = $scope.selected;
            orderService.sendCriteriaToExportOrders(selectedStatusToPrint).then(function (response) {
                if (response === true) {
                    window.location = '/ordersByStatuses';
                }
            })
        }

        function exportOrdersByDatesInRange(dateFrom, dateTo) {
            orderService.sendDataToExportOrdersByDatesInRange(dateFrom, dateTo).then(function (response) {
                if (response === true) {
                    window.location = '/ordersByDatesInRange';
                }
            })
        }

        function processOrder(event, order, shouldClose, currentOrder) {
            event.stopPropagation();
            vm.numberProcessingOrder = order.id;
            var action = prepareOrderAction(event, shouldClose);
            vm.orderStatusList = [];
            if (action !== "finishOrder") {
                vm.isOrderComplete = false;
            }
            if (action === "finishOrder") {
                vm.showSuccessCompletedOrder = true;
            }
            if (action !== "delayOrder") {
                orderService.changeOrderStatus(order, action).then(function (response) {
                    if (action !== "acceptOrder" && action !== "assignOrder" && action !== "cancelOrder") {
                        if (response) {
                            for (var i = 0; i < vm.orders.length; i++) {
                                if (vm.orders[i].status === "Realize" && !vm.isOrderComplete) {
                                    vm.orders[i].status = "Pause";
                                } else if (vm.orders[i].status === "Realize" && vm.isOrderComplete) {
                                    vm.orders[i].status = "Completed";
                                }
                            }
                            order.status = response.order.status;
                            if (currentOrder && response.currentOrder === null) {
                                currentOrder.products = [];
                                currentOrder.id = null;
                                currentOrder.executorId = null;
                                currentOrder.executor = null;
                                currentOrder.orderDate = null;
                                currentOrder.orderer = null;
                                currentOrder.ordererId = null;
                                currentOrder.shippingCost = null;
                                currentOrder.status = null;
                                currentOrder.subTotal = null;
                                currentOrder.tax = null;
                                currentOrder.taxRate = null;
                                currentOrder.totalCost = null;
                                currentOrder.verification = null;
                                vm.isOrderComplete = false;
                            } else {
                                if (currentOrder.products.length === response.currentOrder.products.length) {
                                    for (var i = 0; i < currentOrder.products.length; i++) {
                                        currentOrder.products[i].ordinal = response.currentOrder.products[i].ordinal;
                                        currentOrder.products[i].productId = response.currentOrder.products[i].productId;
                                        currentOrder.products[i].name = response.currentOrder.products[i].name;
                                        currentOrder.products[i].isComplete = response.currentOrder.products[i].isComplete;
                                        vm.orderStatusList[response.currentOrder.products[i].ordinal - 1] = response.currentOrder.products[i].isComplete;
                                    }
                                } else if (currentOrder.products.length > response.currentOrder.products.length) {
                                    currentOrder.products = [];
                                    for (var i = 0; i < response.currentOrder.products.length; i++) {
                                        currentOrder.products.push({
                                            ordinal: response.currentOrder.products[i].ordinal,
                                            productId: response.currentOrder.products[i].productId,
                                            name: response.currentOrder.products[i].name,
                                            isComplete: response.currentOrder.products[i].isComplete
                                        });
                                        vm.orderStatusList[response.currentOrder.products[i].ordinal - 1] = response.currentOrder.products[i].isComplete;
                                    }
                                } else {
                                    for (var i = 0; i < currentOrder.products.length; i++) {
                                        currentOrder.products[i].ordinal = response.currentOrder.products[i].ordinal;
                                        currentOrder.products[i].productId = response.currentOrder.products[i].productId;
                                        currentOrder.products[i].name = response.currentOrder.products[i].name;
                                        currentOrder.products[i].isComplete = response.currentOrder.products[i].isComplete;
                                        vm.orderStatusList[response.currentOrder.products[i].ordinal - 1] = response.currentOrder.products[i].isComplete;
                                    }
                                    for (var i = currentOrder.products.length; i < response.currentOrder.products.length; i++) {
                                        currentOrder.products.push({
                                            ordinal: response.currentOrder.products[i].ordinal,
                                            productId: response.currentOrder.products[i].productId,
                                            name: response.currentOrder.products[i].name,
                                            isComplete: response.currentOrder.products[i].isComplete
                                        });
                                        vm.orderStatusList[response.currentOrder.products[i].ordinal - 1] = response.currentOrder.products[i].isComplete;
                                    }
                                }
                                currentOrder.id = response.currentOrder.id;
                                currentOrder.executorId = response.currentOrder.executorId;
                                currentOrder.executor = response.currentOrder.executor;
                                currentOrder.orderDate = response.currentOrder.orderDate;
                                currentOrder.orderer = response.currentOrder.orderer;
                                currentOrder.ordererId = response.currentOrder.ordererId;
                                currentOrder.shippingCost = response.currentOrder.shippingCost;
                                currentOrder.status = response.currentOrder.status;
                                currentOrder.subTotal = response.currentOrder.subTotal;
                                currentOrder.tax = response.currentOrder.tax;
                                currentOrder.taxRate = response.currentOrder.taxRate;
                                currentOrder.totalCost = response.currentOrder.totalCost;
                                currentOrder.verification = response.currentOrder.verification;
                                vm.isOrderComplete = checkStatusNewOrder(response.currentOrder.products);
                            }
                        }
                    } else if (action === "finishOrder" || action === "assignOrder") {
                        if (action === "assignOrder") {
                            vm.showSuccessAssignOrder = true;
                        }
                        getOrders();
                    } else if (action === "acceptOrder") {
                        vm.showSuccessAcceptOrder = true;
                    } else if (action === "cancelOrder") {
                        vm.showSuccessCancelOrder = true;
                    }
                });
            } else {
                vm.showSuccessDelayOrder = true;
            }
        }

        function prepareOrderAction(event, shouldClose) {
            if (shouldClose) {
                dialogService.cancel();
            }
            return event.currentTarget.name;
        }

        function showCurrentOrder(event) {
            event.stopPropagation();
            $timeout(function () {
                vm.flagYourOrders = false;
            }, 500);
            vm.currentOrder = orderService.getCurrentOrderForUser().then(function (response) {
                vm.flagCurrent = true;
                vm.currentOrder = response;
                vm.isOrderComplete = checkStatusNewOrder(vm.currentOrder.products);
            });
        }

        function showYourOrders(event) {
            event.stopPropagation();
            $timeout(function () {
                vm.flagCurrent = false;
                vm.flagYourOrders = true;
            }, 500);
        }

        function backToMobileButtons(event) {
            event.stopPropagation();
            $timeout(function () {
                vm.flagCurrent = false;
                vm.flagYourOrders = false;
            }, 500);
        }

        function onCompleteClick(ordinal, productId, isComplete, orderId) {
            var status = !isComplete;
            vm.orderStatusList[ordinal - 1] = status;
            var resultStatus = checkIfAllProductsIsCompleted();
            if (resultStatus === true) {
                vm.isOrderComplete = true;
            } else {
                vm.isOrderComplete = false;
            }
            orderService.updateOrder(orderId, vm.isOrderComplete, productId, status);
        }

        function checkIfOrderCompleted(orderId, isOrderComplete) {
            var status = !isOrderComplete;
            if (status === true) {
                for (var i = 0; i < vm.currentOrder.products.length; i++) {
                    vm.currentOrder.products[i].isComplete = true;
                }
            } else {
                for (var i = 0; i < vm.currentOrder.products.length; i++) {
                    vm.currentOrder.products[i].isComplete = false;
                }
                vm.orderStatusList = [];
            }
            orderService.updateOrder(orderId, status);
        }

        function checkIfAllProductsIsCompleted() {
            if (vm.currentOrder.products.length === vm.orderStatusList.length) {
                var amountCompleted = 0;
                for (var i = 0; i < vm.orderStatusList.length; i++) {
                    if (vm.orderStatusList[i] === true) {
                        amountCompleted++;
                    }
                }
            }
            return amountCompleted === vm.currentOrder.products.length;
        }

        function checkStatusNewOrder(products) {
            for (var i = 0; i < products.length; i++) {
                if (products[i].isComplete === false) {
                    return false;
                }
            }
            return products.length > 0;
        }

        function showCompletedOrdersList(event) {
            return dialogService.open(event, document.body);
        }

        function updateOrderStatusList(products) {
            if (angular.isDefined(products)) {
                for (var i = 0; i < products.length; i++) {
                    vm.orderStatusList[products[i].ordinal - 1] = products[i].isComplete;
                }
            }
        }
    }
})();

