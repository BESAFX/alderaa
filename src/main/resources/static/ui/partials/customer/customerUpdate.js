app.controller('customerUpdateCtrl', ['CustomerService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'customer',
    function (CustomerService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, customer) {

        $scope.customer = customer;

        $scope.title = title;

        $scope.submit = function () {
            CustomerService.update($scope.customer).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);