app.controller('orderSellFilterCtrl', ['$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function ($scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.addSortBy = function () {
            var sortBy = {};
            $scope.pageOrderSell.sorts.push(sortBy);
        };

        $scope.submit = function () {
            $scope.pageOrderSell.page = $scope.pageOrderSell.currentPage - 1;
            $uibModalInstance.close($scope.paramOrderSell);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);