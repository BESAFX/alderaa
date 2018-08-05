app.controller('billSellProductCreateCtrl', ['ProductService', 'BillSellService', 'BillSellProductService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'billSell',
    function (ProductService, BillSellService, BillSellProductService, $scope, $rootScope, $timeout, $log, $uibModalInstance, billSell) {

        $scope.buffer = {};

        $scope.billSell = billSell;

        $scope.billSellProducts = [];

        $scope.refreshParents = function (isOpen) {
            if (isOpen) {
                ProductService.findParents().then(function (value) {
                    $scope.parents = value;
                });
            }
        };

        $scope.refreshChilds = function (isOpen, billSellProduct) {
            if (isOpen) {
                ProductService.findChilds(billSellProduct.parent.id).then(function (value) {
                    return billSellProduct.parent.childs = value;
                });
            }
        };

        $scope.addBillSellProduct = function () {
            var billSellProduct = {};
            billSellProduct.billSell = $scope.billSell;
            $scope.billSellProducts.push(billSellProduct);
        };

        $scope.removeBillSellProduct = function (index) {
            $scope.billSellProducts.splice(index, 1);
        };

        $scope.submit = function () {
            BillSellProductService.createBatch($scope.billSellProducts).then(function (data) {
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