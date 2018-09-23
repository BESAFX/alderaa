app.controller('expenseCreateCtrl', ['BankTransactionService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (BankTransactionService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.bankTransaction = {};

        $scope.submit = function () {
            BankTransactionService.createExpense($scope.bankTransaction).then(function (data) {

                ModalProvider.openConfirmModel("سندات الصرف", "print", "هل تود طباعة سند الصرف")
                    .result
                    .then(function (value) {
                        if (value) {
                            window.open('/report/receiptExpense/' + data.id);
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