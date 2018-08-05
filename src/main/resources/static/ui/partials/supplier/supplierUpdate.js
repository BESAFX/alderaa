app.controller('supplierUpdateCtrl', ['SupplierService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'supplier',
    function (SupplierService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, supplier) {

        $scope.supplier = supplier;

        $scope.title = title;

        $scope.submit = function () {
            SupplierService.update($scope.supplier).then(function (data) {
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