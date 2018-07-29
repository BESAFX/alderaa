app.controller('orderPurchasePrintCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance', 'orderPurchase',
    function ($scope, $rootScope, $timeout, $uibModalInstance, orderPurchase) {

        $scope.orderPurchase = orderPurchase;

        $scope.submit = function () {
            window.open('/report/orderPurchase?orderPurchaseId=' + orderPurchase.id);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);