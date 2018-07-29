app.controller('supplierDetailsCtrl', ['SupplierService', 'SupplierContactService', 'BillPurchaseService', 'SupplierPaymentService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'supplier',
    function (SupplierService, SupplierContactService, BillPurchaseService, SupplierPaymentService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, supplier) {

        $scope.supplier = supplier;

        $scope.refreshSupplier = function () {
            SupplierService.findOne($scope.supplier.id).then(function (data) {
                $scope.supplier = data;
            })
        };

        $scope.refreshBillPurchases = function () {
            BillPurchaseService.findBySupplier($scope.supplier.id).then(function (value) {
                $scope.supplier.billPurchases = value;
            });
        };

        $scope.refreshSupplierPayments = function () {
            SupplierPaymentService.findBySupplier($scope.supplier.id).then(function (value) {
                $scope.supplier.supplierPayments = value;
            });
        };

        $scope.refreshSupplierContacts = function () {
            SupplierContactService.findBySupplier($scope.supplier.id).then(function (value) {
                $scope.supplier.supplierContacts = value;
            });
        };

        $scope.newSupplierContact = function () {
            ModalProvider.openSupplierContactCreateModel($scope.supplier).result.then(function (data) {
                $scope.supplier.supplierContacts.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };

        $scope.deleteSupplierContact = function (supplierContact) {
            ModalProvider.openConfirmModel("جهات الاتصال", "delete", "هل تود حذف جهة الاتصال فعلاً؟").result.then(function (value) {
                if (value) {
                    SupplierContactService.remove(supplierContact.id).then(function () {
                        var index = $scope.supplier.supplierContacts.indexOf(supplierContact);
                        $scope.supplier.supplierContacts.splice(index, 1);
                    });
                }
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshSupplier();
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);