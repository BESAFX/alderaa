app.controller('supplierCreateCtrl', ['SupplierService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (SupplierService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.supplier = {};

        $scope.supplierContacts = [];

        $scope.title = title;

        $scope.addSupplierContact = function () {
            $scope.supplierContacts.push({});
        };

        $scope.removeSupplierContact = function (index) {
            $scope.supplierContacts.splice(index, 1);
        };

        $scope.submit = function () {
            //ربط جهات الاتصال بالمورد
            $scope.supplier.supplierContacts = $scope.supplierContacts;
            SupplierService.create($scope.supplier).then(function (data) {
                SupplierService.findOne(data.id).then(function (value) {
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