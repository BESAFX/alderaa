app.controller('orderPurchaseFilterCtrl', ['$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function ($scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.addSortBy = function () {
            var sortBy = {};
            $scope.pageOrderPurchase.sorts.push(sortBy);
        };

        $scope.submit = function () {
            $scope.pageOrderPurchase.page = $scope.pageOrderPurchase.currentPage - 1;
            $uibModalInstance.close($scope.paramOrderPurchase);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);