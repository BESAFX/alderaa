app.controller('withdrawCreateCtrl', ['BankTransactionService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (BankTransactionService, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.bankTransaction = {};

        $scope.submit = function () {
            BankTransactionService.createWithdraw($scope.bankTransaction).then(function (data) {
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