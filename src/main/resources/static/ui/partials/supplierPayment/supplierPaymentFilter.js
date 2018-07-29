app.controller('supplierPaymentFilterCtrl', ['$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function ($scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.addSortBy = function () {
            var sortBy = {};
            $scope.pageSupplierPayment.sorts.push(sortBy);
        };

        $scope.submit = function () {
            $scope.pageSupplierPayment.page = $scope.pageSupplierPayment.currentPage - 1;
            $uibModalInstance.close($scope.paramSupplierPayment);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);