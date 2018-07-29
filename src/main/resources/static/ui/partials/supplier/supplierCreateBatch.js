app.controller('supplierCreateBatchCtrl', ['SupplierService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (SupplierService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.buffer = {};

        $scope.buffer.excelSuppliers = [];

        $scope.suppliers = [];

        $scope.title = title;

        $scope.$watch(function ($scope) {
            return $scope.buffer.excelSuppliers;
        }, function (newVal) {
            angular.forEach(newVal, function (row) {
                var supplier = {};
                supplier.contact = {};
                supplier.contact.name = row['الاسم'];
                supplier.contact.nationality = row['الجنسية'];
                supplier.contact.identityNumber = row['السجل المدني'];
                supplier.contact.mobile = row['الجوال'];
                supplier.contact.phone = row['الهاتف'];
                supplier.registerDate = row['تاريخ التسجيل'] ? new Date(row['تاريخ التسجيل']) : new Date();
                $scope.suppliers.push(supplier);
            });
        }, true);

        $scope.clear = function () {
            $scope.form.$setPristine();
            $scope.buffer = {};
            $scope.buffer.excelSuppliers = [];
            $scope.suppliers = [];
        };

        $scope.uploadExcelFile = function () {
            $scope.clear();
            document.getElementById('excelUploader').click();
        };

        $scope.submit = function () {
            SupplierService.createBatch($scope.suppliers).then(function (data) {
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