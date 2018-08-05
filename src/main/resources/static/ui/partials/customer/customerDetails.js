app.controller('customerDetailsCtrl', ['CustomerService', 'CustomerContactService', 'BillSellService', 'CustomerPaymentService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'customer',
    function (CustomerService, CustomerContactService, BillSellService, CustomerPaymentService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, customer) {

        $scope.customer = customer;

        $scope.refreshCustomer = function () {
            CustomerService.findOne($scope.customer.id).then(function (data) {
                $scope.customer = data;
            })
        };

        $scope.refreshBillSells = function () {
            BillSellService.findByCustomer($scope.customer.id).then(function (value) {
                $scope.customer.billSells = value;
            });
        };

        $scope.refreshCustomerPayments = function () {
            CustomerPaymentService.findByCustomer($scope.customer.id).then(function (value) {
                $scope.customer.customerPayments = value;
            });
        };

        $scope.refreshCustomerContacts = function () {
            CustomerContactService.findByCustomer($scope.customer.id).then(function (value) {
                $scope.customer.customerContacts = value;
            });
        };

        $scope.newCustomerContact = function () {
            ModalProvider.openCustomerContactCreateModel($scope.customer).result.then(function (data) {
                $scope.customer.customerContacts.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };

        $scope.deleteCustomerContact = function (customerContact) {
            ModalProvider.openConfirmModel("جهات الاتصال", "delete", "هل تود حذف جهة الاتصال فعلاً؟").result.then(function (value) {
                if (value) {
                    CustomerContactService.remove(customerContact.id).then(function () {
                        var index = $scope.customer.customerContacts.indexOf(customerContact);
                        $scope.customer.customerContacts.splice(index, 1);
                    });
                }
            });
        };


        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshCustomer();
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);