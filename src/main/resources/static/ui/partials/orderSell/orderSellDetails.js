app.controller('orderSellDetailsCtrl', ['OrderSellService', 'OrderSellProductService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'orderSell',
    function (OrderSellService, OrderSellProductService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, orderSell) {

        $scope.orderSell = orderSell;

        $scope.refreshOrderSell = function () {
            OrderSellService.findOne($scope.orderSell.id).then(function (data) {
                $scope.orderSell = data;
            })
        };

        $scope.refreshOrderSellProducts = function () {
            OrderSellProductService.findByOrderSell($scope.orderSell.id).then(function (data) {
                $scope.orderSell.orderSellProducts = data;
            })
        };

        $scope.newOrderSellProduct = function () {
            ModalProvider.openOrderSellProductCreateModel($scope.orderSell).result.then(function (data) {
                $scope.refreshOrderSellProducts();
            });
        };

        $scope.deleteOrderSellProduct = function (orderSellProduct) {
            ModalProvider.openConfirmModel("أوامر البيع", "delete", "هل تود حذف السلعة فعلاً؟").result.then(function (value) {
                if (value) {
                    OrderSellProductService.remove(orderSellProduct.id).then(function () {
                        var index = $scope.orderSell.orderSellProducts.indexOf(orderSellProduct);
                        $scope.orderSell.orderSellProducts.splice(index, 1);
                    });
                }
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshOrderSell();
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);