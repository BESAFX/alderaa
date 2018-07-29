app.controller('customerCreateCtrl', ['CustomerService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (CustomerService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.customer = {};

        $scope.customerContacts = [];

        $scope.title = title;

        $scope.addCustomerContact = function () {
            $scope.customerContacts.push({});
        };

        $scope.removeCustomerContact = function (index) {
            $scope.customerContacts.splice(index, 1);
        };

        $scope.submit = function () {
            //ربط جهات الاتصال بالعميل
            $scope.customer.customerContacts = $scope.customerContacts;
            CustomerService.create($scope.customer).then(function (data) {
                CustomerService.findOne(data.id).then(function (value) {
                    $uibModalInstance.close(value);
                });
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);