app.controller('orderPurchaseProductCreateCtrl', ['ProductService', 'OrderPurchaseService', 'OrderPurchaseProductService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'orderPurchase',
    function (ProductService, OrderPurchaseService, OrderPurchaseProductService, $scope, $rootScope, $timeout, $log, $uibModalInstance, orderPurchase) {

        $scope.buffer = {};

        $scope.orderPurchase = orderPurchase;

        $scope.orderPurchaseProducts = [];

        $scope.refreshParents = function (isOpen) {
            if (isOpen) {
                ProductService.findParents().then(function (value) {
                    $scope.parents = value;
                });
            }
        };

        $scope.refreshChilds = function (isOpen, orderPurchaseProduct) {
            if (isOpen) {
                ProductService.findChilds(orderPurchaseProduct.parent.id).then(function (value) {
                    return orderPurchaseProduct.parent.childs = value;
                });
            }
        };

        $scope.addOrderPurchaseProduct = function () {
            var orderPurchaseProduct = {};
            orderPurchaseProduct.orderPurchase = $scope.orderPurchase;
            $scope.orderPurchaseProducts.push(orderPurchaseProduct);
        };

        $scope.removeOrderPurchaseProduct = function (index) {
            $scope.orderPurchaseProducts.splice(index, 1);
        };

        $scope.submit = function () {
            OrderPurchaseProductService.createBatch($scope.orderPurchaseProducts).then(function (data) {
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