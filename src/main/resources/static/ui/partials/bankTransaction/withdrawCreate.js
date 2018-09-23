app.controller('withdrawCreateCtrl', ['BankTransactionService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (BankTransactionService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.bankTransaction = {};

        $scope.submit = function () {
            BankTransactionService.createWithdraw($scope.bankTransaction).then(function (data) {

                ModalProvider.openConfirmModel("سندات السحب", "print", "هل تود طباعة سند السحب")
                    .result
                    .then(function (value) {
                        if (value) {
                            window.open('/report/receiptWithdraw/' + data.id);
                        }
                    });

                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);