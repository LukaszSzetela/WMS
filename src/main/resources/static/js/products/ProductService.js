(function () {
    'use strict';

    angular.module('products').service('ProductService', ProductService);

    ProductService.$inject = ['$http'];

    function ProductService($http) {
        var service = {
            getProducts: getProducts,
            getProduct: getProduct,
            getProductDetails: getProductDetails,
            getStockInRange: getStockInRange,
            saveProduct: saveProduct,
            sendDataToExportSalesByDatesInRange: sendDataToExportSalesByDatesInRange,
            sendDataToExportSalesByProduct: sendDataToExportSalesByProduct
        };

        return service;

        function getProducts() {
            return $http.get('/api/products')
                .then(getComplete)
                .catch(getFailed);
        }

        function getProduct(id) {
            return $http.get('/api/products/' + id)
                .then(getComplete)
                .catch(getFailed);
        }

        function getProductDetails(id) {
            return $http.get('/api/productsLog/' + id)
                .then(getComplete)
                .catch(getFailed);
        }

        function getStockInRange(id) {
            return $http.get('/api/productsDate/' + id)
                .then(getComplete)
                .catch(getFailed);
        }

        function saveProduct(product) {
            return $http.post('api/products', product)
                .then(getComplete).catch(getFailed);
        }

        function sendDataToExportSalesByDatesInRange(dateFrom, dateTo) {
            return $http.get('/catchDatesToSalesReport', {
                params: {
                    dateFrom: dateFrom.toString(), dateTo: dateTo.toString()
                }
            }).then(getComplete)
                .catch(getFailed);
        }

        function sendDataToExportSalesByProduct(product) {
            return $http.get('/catchProductToReport', {
                params: {
                    product: product
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
    }
})();
