app.controller('orderSellProductCreateCtrl', ['ProductService', 'OrderSellService', 'OrderSellProductService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'orderSell',
    function (ProductService, OrderSellService, OrderSellProductService, $scope, $rootScope, $timeout, $log, $uibModalInstance, orderSell) {

        $scope.buffer = {};

        $scope.orderSell = orderSell;

        $scope.orderSellProducts = [];

        $scope.refreshParents = function (isOpen) {
            if (isOpen) {
                ProductService.findParents().then(function (value) {
                    $scope.parents = value;
                });
            }
        };

        $scope.refreshChilds = function (isOpen, orderSellProduct) {
            if (isOpen) {
                ProductService.findChilds(orderSellProduct.parent.id).then(function (value) {
                    return orderSellProduct.parent.childs = value;
                });
            }
        };

        $scope.addOrderSellProduct = function () {
            var orderSellProduct = {};
            orderSellProduct.orderSell = $scope.orderSell;
            $scope.orderSellProducts.push(orderSellProduct);
        };

        $scope.removeOrderSellProduct = function (index) {
            $scope.orderSellProducts.splice(index, 1);
        };

        $scope.submit = function () {
            OrderSellProductService.createBatch($scope.orderSellProducts).then(function (data) {
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