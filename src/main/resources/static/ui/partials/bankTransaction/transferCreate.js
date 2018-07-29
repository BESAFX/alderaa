app.controller('transferCreateCtrl', ['SupplierService', 'BankTransactionService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (SupplierService, BankTransactionService, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.buffer = {};

        $scope.submit = function () {
            BankTransactionService.createTransfer(
                $scope.buffer.amount,
                $scope.buffer.fromBank.id,
                $scope.buffer.toBank.id,
                $scope.buffer.note
            )
                .then(function (data) {
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