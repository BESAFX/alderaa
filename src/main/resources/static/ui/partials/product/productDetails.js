app.controller('productDetailsCtrl', ['ProductService', 'BillPurchaseProductService', 'BillSellProductService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'product',
    function (ProductService, BillPurchaseProductService, BillSellProductService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, product) {

        $scope.product = product;

        $scope.refreshProduct = function () {
            ProductService.findOne($scope.product.id).then(function (data) {
                $scope.product = data;
            })
        };

        $scope.refreshBillPurchaseProducts = function () {
            BillPurchaseProductService.findByProduct($scope.product.id).then(function (value) {
                $scope.product.billPurchaseProducts = value;
            })
        };

        $scope.refreshBillSellProducts = function () {
            BillSellProductService.findByProduct($scope.product.id).then(function (value) {
                $scope.product.billSellProducts = value;
            })
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshProduct();
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);