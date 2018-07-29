app.controller('bankCreateUpdateCtrl', ['BankService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'bank',
    function (BankService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, bank) {

        $scope.title = title;

        $scope.action = action;

        $scope.bank = bank;

        $scope.submit = function () {
            switch ($scope.action) {
                case 'create' :
                    BankService.create($scope.bank).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    BankService.update($scope.bank).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);