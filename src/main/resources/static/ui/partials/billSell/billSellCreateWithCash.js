app.controller('billSellCreateWithCashCtrl', ['ProductService', 'BillSellService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (ProductService, BillSellService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.buffer = {};

        $scope.billSell = {};

        $scope.billSell.writtenDate = new Date();

        $scope.billSell.discount = 0;

        $scope.billSellProducts = [];

        $scope.newProduct = function () {
            ModalProvider.openProductCreateModel().result.then(function (data) {
                $scope.products.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };

        $scope.newParent = function () {
            ModalProvider.openParentCreateModel().result.then(function (data) {
                $scope.parents.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };

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
            $scope.billSellProducts.push({});
        };

        $scope.removeBillSellProduct = function (index) {
            $scope.billSellProducts.splice(index, 1);
        };

        $scope.submit = function () {
            //ربط الأصناف بالفاتورة
            $scope.billSell.billSellProducts = $scope.billSellProducts;
            BillSellService.createWithCash($scope.billSell, $scope.buffer.customerName, $scope.buffer.customerMobile, $scope.buffer.toBank.id).then(function (data) {
                BillSellService.findOne(data.id).then(function (value) {
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