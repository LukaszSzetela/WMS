(function () {
    'use strict';

    angular.module('users').controller('UserController',
        UserController);

    UserController.$inject = ['UserService', '$mdDialog', 'DialogService', 'USER_ROLES', '$scope', 'OrderService'];

    function UserController(userService, dialog, dialogService, USER_ROLES, $scope, orderService) {
        var vm = this;
        vm.getUserProperties = getUserProperties;
        vm.getUser = getUser;
        vm.addUserForm = addUserForm;
        vm.addCustomerForm = addCustomerForm;
        vm.editUserForm = editUserForm;
        vm.editCustomerForm = editCustomerForm;
        vm.getRoles = getRoles;
        vm.onCancelClick = onCancelClick;
        vm.onCloseClick = onCloseClick;
        vm.onSaveClick = onSaveClick;
        vm.exportOrdersByUser = exportOrdersByUser;
        vm.onChangeUser = onChangeUser;
        vm.onChangeCustomer = onChangeCustomer;
        vm.onChangeUserForCustomer = onChangeUserForCustomer;
        vm.saveCustomer = saveCustomer;
        vm.user = {};
        vm.customer = {};
        vm.users = [];
        vm.customers = [];
        vm.editUser = {};
        vm.editCustomer = {};
        vm.showSuccessAddedUser = false;
        vm.showSuccessAddedCustomer = false;
        vm.showSuccessEditedUser = false;
        vm.showSuccessEditedCustomer = false;
        vm.processingUserId = 0;
        vm.processingCustomerId = 0;

        getUserProperties();
        getRoles();


        vm.allUsers = loadAllUsers();
        vm.allCustomerUser = loadAllCustomerUser();
        vm.allCustomers = loadAllCustomers();
        vm.querySearch = querySearch;
        vm.querySearchForCustomerUser = querySearchForCustomerUser;
        vm.customersQuerySearch = customersQuerySearch;


        function querySearch(query) {
            return query ? vm.allUsers.filter(createFilterFor(query)) : vm.allUsers;
        }

        function querySearchForCustomerUser(query) {
            return query ? vm.allCustomerUser.filter(createFilterFor(query)) : vm.allCustomerUser;
        }

        function customersQuerySearch(query) {
            return query ? vm.allCustomers.filter(createFilterFor(query)) : vm.allCustomers;
        }


        function loadAllUsers() {
            userService.getUsers().then(function (response) {
                vm.users = response;
                vm.allUsers = [];
                for (var i = 0; i < vm.users.length; i++) {
                    vm.allUsers.push(vm.users[i].firstName.concat(" ", vm.users[i].lastName))
                }
                return vm.allUsers;
            });
        }

        function loadAllCustomerUser() {
            userService.getUsers().then(function (response) {
                vm.users = response;
                vm.allCustomerUser = [];
                for (var i = 0; i < vm.users.length; i++) {
                    if (vm.users[i].role === "Customer" && vm.users[i].customerId === null) {
                        vm.allCustomerUser.push(vm.users[i].firstName.concat(" ", vm.users[i].lastName))
                    }
                }
                return vm.allCustomerUser;
            });
        }

        function loadAllCustomers() {
            userService.getCustomers().then(function (response) {
                vm.customers = response;
                vm.allCustomers = [];
                for (var i = 0; i < vm.customers.length; i++) {
                    vm.allCustomers.push(vm.customers[i].name);
                }
                return vm.allCustomers;
            });
        }

        function createFilterFor(query) {
            var lowercaseQuery = angular.lowercase(query);

            return function filterFn(value) {
                value = angular.lowercase(value);
                return (value.indexOf(lowercaseQuery) === 0);
            };
        }

        function getUserProperties() {
            return userService.getUserProperties().then(function (response) {
                vm.userProps = response[0];
                return vm.userProps;
            });
        }

        function getUser(id) {
            return userService.getUser(id).then(function (response) {
                vm.user = response.data;
                return vm.user;
            });
        }

        function getRoles() {
            vm.roles = USER_ROLES;
            return vm.roles;
        }

        function onCancelClick() {
            dialogService.cancel();
        }

        function onCloseClick() {
            dialogService.finish();
        }

        function onSaveClick(user) {
            userService.saveUser(user).then(function (response) {
                if (response) {
                    if (angular.isDefined(user.userId)) {
                        vm.showSuccessEditedUser = true;
                    } else {
                        vm.showSuccessAddedUser = true;
                    }
                    vm.user = {};
                }
                onCloseClick();
            });
        }

        function saveCustomer(customer) {
            userService.saveCustomer(customer).then(function (response) {
                if (response) {
                    if (angular.isDefined(customer.customerId)) {
                        vm.showSuccessEditedCustomer = true;
                    } else {
                        vm.showSuccessAddedCustomer = true;
                    }
                    vm.customer = {};
                }
                onCloseClick();
            })
        }

        function addUserForm(event) {
            vm.showSuccessAddedUser = false;
            vm.user.active = false;
            vm.user.userId = null;
            return dialogService.open(event, document.body);
        }

        function addCustomerForm(event) {
            vm.showSuccessAddedCustomer = false;
            vm.user.active = false;
            vm.user.userId = null;
            return dialogService.open(event, document.body);
        }

        function editUserForm(event) {
            vm.showSuccessEditedUser = false;
            return dialogService.open(event, document.body);
        }

        function editCustomerForm(event) {
            vm.showSuccessEditedCustomer = false;
            return dialogService.open(event, document.body);
        }

        function exportOrdersByUser(user) {
            orderService.sendDataToExportOrdersByUser(user).then(function (response) {
                if (response === true) {
                    window.location = '/ordersByUser';
                }
            })
        }

        function onChangeUser(selectedUser) {
            if (selectedUser !== null) {
                var users = vm.users;
                for (var i = 0; i < users.length; i++) {
                    if (users[i].firstName.concat(" ", users[i].lastName) === selectedUser) {
                        vm.editUser = users[i];
                        vm.user.userId = vm.editUser.userId;
                        vm.user.firstName = vm.editUser.firstName;
                        vm.user.lastName = vm.editUser.lastName;
                        vm.user.username = vm.editUser.username;
                        vm.user.email = vm.editUser.email;
                        vm.user.role = vm.editUser.role;
                        vm.user.active = vm.editUser.active;
                        vm.processingUserId = vm.editUser.userId;
                        break;
                    }
                }
            } else {
                vm.editUser = {};
                vm.user.active = false;
                vm.user.userId = undefined;
                vm.user.role = undefined;
                vm.user.userId = undefined;
                vm.user.firstName = undefined;
                vm.user.lastName = undefined;
                vm.user.username = undefined;
                vm.user.email = undefined;
                vm.processingUserId = 0;
            }
        }

        function onChangeUserForCustomer(selectedUser) {
            if (selectedUser !== null) {
                var users = vm.users;
                for (var i = 0; i < users.length; i++) {
                    if (users[i].firstName.concat(" ", users[i].lastName) === selectedUser) {
                        vm.editUser = users[i];
                        vm.customer.userId = vm.editUser.userId;
                        break;
                    }
                }
            } else {
                vm.customer.userId = undefined;
            }
        }

        function onChangeCustomer(selectedCustomer) {
            if (selectedCustomer !== null) {
                var customers = vm.customers;
                for (var i = 0; i < customers.length; i++) {
                    if (customers[i].name === selectedCustomer) {
                        vm.editCustomer = customers[i];
                        vm.customer.customerId = vm.editCustomer.customerId;
                        vm.customer.name = vm.editCustomer.name;
                        vm.customer.nip = vm.editCustomer.nip;
                        vm.customer.regon = vm.editCustomer.regon;
                        vm.customer.deliveryCost = vm.editCustomer.deliveryCost;
                        vm.customer.email = vm.editCustomer.email;
                        vm.customer.vatRate = vm.editCustomer.vatRate;
                        vm.processingCustomerId = vm.editCustomer.customerId;
                        break;
                    }
                }
            } else {
                vm.editCustomer = {};
                vm.customer.customerId = undefined;
                vm.customer.name = undefined;
                vm.customer.nip = undefined;
                vm.customer.regon = undefined;
                vm.customer.deliveryCost = undefined;
                vm.customer.email = undefined;
                vm.customer.vatRate = undefined;
                vm.processingCustomerId = 0;
            }
        }
    }
})();

