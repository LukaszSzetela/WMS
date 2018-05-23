(function () {
    'use strict';

    angular.module('users').service('UserService', UserService);

    UserService.$inject = ['$http'];

    function UserService($http) {
        var service = {
            getUserProperties: getUserProperties,
            getUser: getUser,
            getRoles: getRoles,
            saveUser: saveUser,
            saveCustomer: saveCustomer,
            getUsers: getUsers,
            getCustomers: getCustomers
        };

        return service;

        function getUserProperties() {
            return $http.get('/api/userId')
                .then(getComplete)
                .catch(getFailed);
        }
        
        function getUser(id) {
            return $http.get('/api/users/' + id)
                .then(getComplete)
                .catch(getFailed);
        }

        function getUsers() {
            return $http.get('/api/users')
                .then(getComplete)
                .catch(getFailed);
        }

        function getCustomers() {
            return $http.get('/api/customers')
                .then(getComplete)
                .catch(getFailed);
        }

        function saveUser(user) {
            return $http.post('api/users', user)
                .then(getComplete).catch(getFailed);
        }

        function saveCustomer(customer) {
            return $http.post('api/customers', customer)
                .then(getComplete).catch(getFailed);
        }

        function getRoles() {
            return $http.get('/api/roles')
                .then(getComplete)
                .catch(getFailed);
        }

        function getComplete(response) {
            return response.data;
        }

        function getFailed(error) {
            console.log('Error in UserService: ' + error.data);
        }
    }
})();
