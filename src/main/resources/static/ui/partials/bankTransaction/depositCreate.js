app.controller('depositCreateCtrl', ['BankTransactionService', 'ModalProvider','$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (BankTransactionService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.bankTransaction = {};

        $scope.submit = function () {
            BankTransactionService.createDeposit($scope.bankTransaction).then(function (data) {

                ModalProvider.openConfirmModel("سندات الإيداع", "print", "هل تود طباعة سند الإيداع")
                    .result
                    .then(function (value) {
                        if (value) {
                            window.open('/report/receiptDeposit/' + data.id);
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