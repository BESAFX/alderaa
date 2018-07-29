app.controller('orderSellPrintCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance', 'orderSell',
    function ($scope, $rootScope, $timeout, $uibModalInstance, orderSell) {

        $scope.orderSell = orderSell;

        $scope.submit = function () {
            window.open('/report/orderSell?orderSellId=' + orderSell.id);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);