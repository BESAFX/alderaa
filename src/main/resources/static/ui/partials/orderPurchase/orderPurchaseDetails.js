app.controller('orderPurchaseDetailsCtrl', ['OrderPurchaseService', 'OrderPurchaseProductService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'orderPurchase',
    function (OrderPurchaseService, OrderPurchaseProductService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, orderPurchase) {

        $scope.orderPurchase = orderPurchase;

        $scope.refreshOrderPurchase = function () {
            OrderPurchaseService.findOne($scope.orderPurchase.id).then(function (data) {
                $scope.orderPurchase = data;
            })
        };

        $scope.refreshOrderPurchaseProducts = function () {
            OrderPurchaseProductService.findByOrderPurchase($scope.orderPurchase.id).then(function (data) {
                $scope.orderPurchase.orderPurchaseProducts = data;
            })
        };

        $scope.newOrderPurchaseProduct = function () {
            ModalProvider.openOrderPurchaseProductCreateModel($scope.orderPurchase).result.then(function (data) {
                $scope.refreshOrderPurchaseProducts();
            });
        };

        $scope.deleteOrderPurchaseProduct = function (orderPurchaseProduct) {
            ModalProvider.openConfirmModel("أوامر الشراء", "delete", "هل تود حذف السلعة فعلاً؟").result.then(function (value) {
                if (value) {
                    OrderPurchaseProductService.remove(orderPurchaseProduct.id).then(function () {
                        var index = $scope.orderPurchase.orderPurchaseProducts.indexOf(orderPurchaseProduct);
                        $scope.orderPurchase.orderPurchaseProducts.splice(index, 1);
                    });
                }
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshOrderPurchase();
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);