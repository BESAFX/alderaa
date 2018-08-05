app.controller('billSellFilterCtrl', ['$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function ($scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.addSortBy = function () {
            var sortBy = {};
            $scope.pageBillSell.sorts.push(sortBy);
        };

        $scope.submit = function () {
            $scope.pageBillSell.page = $scope.pageBillSell.currentPage - 1;
            $uibModalInstance.close($scope.paramBillSell);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);