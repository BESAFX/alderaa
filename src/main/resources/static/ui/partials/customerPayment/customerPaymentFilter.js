app.controller('customerPaymentFilterCtrl', ['$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function ($scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.addSortBy = function () {
            var sortBy = {};
            $scope.pageCustomerPayment.sorts.push(sortBy);
        };

        $scope.submit = function () {
            $scope.pageCustomerPayment.page = $scope.pageCustomerPayment.currentPage - 1;
            $uibModalInstance.close($scope.paramCustomerPayment);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);