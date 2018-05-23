(function () {
    'use strict';

    angular.module('products').controller('ProductController',
        ProductController);

    ProductController.$inject = ['ProductService', '$scope', '$http', '$mdDialog', 'UserService', 'ngCart', 'DialogService'];

    function ProductController(productService, $scope, $http, dialog, userService, ngCart, dialogService) {
        var vm = this;
        vm.getProduct = getProduct;
        vm.cart = cart;
        vm.infoProduct = infoProduct;
        vm.onCloseClick = onCloseClick;
        vm.getProductDetails = getProductDetails;
        vm.showAddProduct = showAddProduct;
        vm.increaseNumberProduct = increaseNumberProduct;
        vm.showIncreaseNumberProduct = showIncreaseNumberProduct;
        vm.backToAddButton = backToAddButton;
        vm.exportSalesByDatesInRange = exportSalesByDatesInRange;
        vm.exportSalesByProduct = exportSalesByProduct;
        $scope.products = [];
        vm.minDate = new Date(2017, 12, 1);
        vm.maxDate = new Date();
        vm.dateFrom = new Date();
        vm.dateTo = new Date();
        $scope.showSuccessAddedProduct = false;

        initUserProps();
        getProducts();

        vm.allProducts = loadAll();
        vm.querySearch = querySearch;

        function querySearch(query) {
            return query ? vm.allProducts.filter(createFilterFor(query)) : vm.allProducts;
        }

        function loadAll() {
            productService.getProducts().then(function (response) {
                $scope.products = response;
                vm.allProducts = [];
                for (var i = 0; i < $scope.products.length; i++) {
                    vm.allProducts.push($scope.products[i].name)
                }
                return vm.allProducts;
            });
        }

        function createFilterFor(query) {
            var lowercaseQuery = angular.lowercase(query);

            return function filterFn(product) {
                return (product.indexOf(lowercaseQuery) === 0);
            };
        }

        $scope.sort = function (keyname) {
            $scope.sortKey = keyname;
            $scope.reverse = !$scope.reverse;

        };

        function getProducts() {
            productService.getProducts().then(function (response) {
                $scope.products = response;
                return $scope.products;
            });
        }

        function getProduct(id) {
            return productService.getProduct(id).then(function (response) {
                vm.product = response.data;
                return vm.product;
            });
        }

        function getProductDetails(id) {
            return productService.getProductDetails(id).then(function (response) {
                vm.productDetails = response;
                return vm.productDetails;
            });
        }

        function getStockInRange(id) {
            return productService.getStockInRange(id).then(function (response) {
                vm.stockInRange = response;
                return vm.stockInRange;
            });
        }

        function cart(event) {
            initCustomerCart();
            return dialogService.open(event, document.body);
        }

        function infoProduct(event, id, number, reservedNumber) {
            return dialogService.openWithControllerAndParams(event, document.body, prepareProductDetails, 'detailsCtrl', id, number, reservedNumber);
        }

        function initCustomerCart() {
            userService.getUserProperties().then(function (response) {
                vm.userProps = response[0];
                ngCart.setTaxRate(vm.userProps.vatRate * 100);
                ngCart.setShipping(vm.userProps.deliveryCost);
                ngCart.showPositiveNotification = false;
                ngCart.showNegativeNotification = false;
            });
        }

        function onCloseClick() {
            dialogService.finish();
        }

        function prepareProductDetails(id, number, reservedNumber) {
            var vm = this;
            vm.number = number;
            vm.reservedNumber = reservedNumber;
            vm.productDetails = getProductDetails(id).then(function (response) {
                return vm.productDetails = response;
            });
            vm.stockInRange = getStockInRange(id).then(function (response) {
                return vm.stockInRange = response;
            });
        }

        function initUserProps() {
            return userService.getUserProperties().then(function (response) {
                vm.userProps = response[0];
                return vm.userProps;
            });
        }

        function showAddProduct(event) {
            $scope.showSuccessAddedProduct = false;
            return dialogService.openWithController(event, $scope);
        }

        $scope.onSaveClick = function (product) {
            productService.saveProduct(product).then(function (response) {
                if (response){
                    $scope.showSuccessAddedProduct = true;
                    $scope.product = {};
                    getProducts();
                }
                $scope.onCloseClick();
            });
        };

        $scope.onCloseClick = function() {
            dialogService.cancel();
        };

        function showIncreaseNumberProduct(event) {
            $(event.currentTarget).css("display", "none");
            $(event.composedPath()[1].children[1]).css("display", "inline-block");
            $(event.composedPath()[1].children[2]).css("display", "inline-block");
            $(event.composedPath()[1].children[3]).css("display", "inline-block");

        }
        
        function increaseNumberProduct(event, product, number) {
            product.number = product.number + number;
            productService.saveProduct(product);
            backToAddButton(event);
        }

        function backToAddButton(event) {
            $(event.composedPath()[2].children[0]).css("display", "inline-block");
            $(event.composedPath()[2].children[1]).css("display", "none");
            $(event.composedPath()[2].children[2]).css("display", "none");
            $(event.composedPath()[2].children[3]).css("display", "none");
        }

        function exportSalesByDatesInRange(dateFrom, dateTo) {
            productService.sendDataToExportSalesByDatesInRange(dateFrom, dateTo).then(function (response) {
                if (response === true) {
                    window.location = '/productsByDatesInRange';
                }
            })
        }

        function exportSalesByProduct(product) {
            productService.sendDataToExportSalesByProduct(product).then(function (response) {
                if (response === true) {
                    window.location = '/salesByProduct';
                }
            })
        }
    }


})();

