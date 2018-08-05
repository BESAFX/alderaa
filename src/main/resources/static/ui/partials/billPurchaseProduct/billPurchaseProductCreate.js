app.controller('billPurchaseProductCreateCtrl', ['ProductService', 'BillPurchaseService', 'BillPurchaseProductService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'billPurchase',
    function (ProductService, BillPurchaseService, BillPurchaseProductService, $scope, $rootScope, $timeout, $log, $uibModalInstance, billPurchase) {

        $scope.buffer = {};

        $scope.billPurchase = billPurchase;

        $scope.billPurchaseProducts = [];

        $scope.refreshParents = function (isOpen) {
            if (isOpen) {
                ProductService.findParents().then(function (value) {
                    $scope.parents = value;
                });
            }
        };

        $scope.refreshChilds = function (isOpen, billPurchaseProduct) {
            if (isOpen) {
                ProductService.findChilds(billPurchaseProduct.parent.id).then(function (value) {
                    return billPurchaseProduct.parent.childs = value;
                });
            }
        };

        $scope.addBillPurchaseProduct = function () {
            var billPurchaseProduct = {};
            billPurchaseProduct.billPurchase = $scope.billPurchase;
            $scope.billPurchaseProducts.push(billPurchaseProduct);
        };

        $scope.removeBillPurchaseProduct = function (index) {
            $scope.billPurchaseProducts.splice(index, 1);
        };

        $scope.submit = function () {
            BillPurchaseProductService.createBatch($scope.billPurchaseProducts).then(function (data) {
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