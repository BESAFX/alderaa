app.controller('billSellUpdateCtrl', ['BillSellService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'billSell',
    function (BillSellService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, billSell) {

        $scope.billSell = billSell;

        $scope.submit = function () {
            BillSellService.update($scope.billSell).then(function (data) {
                BillSellService.findOne(data.id).then(function (value) {
                    $uibModalInstance.close(value);
                });
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);