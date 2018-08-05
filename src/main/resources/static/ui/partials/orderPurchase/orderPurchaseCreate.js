app.controller('orderPurchaseCreateCtrl', ['ProductService', 'OrderPurchaseService', 'SupplierService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (ProductService, OrderPurchaseService, SupplierService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.buffer = {};

        $scope.orderPurchase = {};

        $scope.orderPurchase.writtenDate = new Date();

        $scope.orderPurchase.discount = 0;

        $scope.orderPurchase.transferFees = 0;

        $scope.orderPurchaseProducts = [];

        $scope.suppliers = [];

        $scope.newSupplier = function () {
            ModalProvider.openSupplierCreateModel().result.then(function (data) {
                $scope.suppliers.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };

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

        $scope.searchSuppliers = function ($select, $event) {

            // no event means first load!
            if (!$event) {
                $scope.pageSupplier = 0;
                $scope.suppliers = [];
            } else {
                $event.stopPropagation();
                $event.preventDefault();
                $scope.pageSupplier++;
            }

            var search = [];

            search.push('size=');
            search.push(10);
            search.push('&');

            search.push('page=');
            search.push($scope.pageSupplier);
            search.push('&');

            search.push('name=');
            search.push($select.search);
            search.push('&');

            search.push('identityNumber=');
            search.push($select.search);
            search.push('&');

            search.push('mobile=');
            search.push($select.search);
            search.push('&');

            search.push('filterCompareType=or');

            return SupplierService.filter(search.join("")).then(function (data) {
                $scope.buffer.lastSupplier = data.last;
                return $scope.suppliers = $scope.suppliers.concat(data.content);
            });

        };

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
            $scope.orderPurchaseProducts.push({});
        };

        $scope.removeOrderPurchaseProduct = function (index) {
            $scope.orderPurchaseProducts.splice(index, 1);
        };

        $scope.submit = function () {
            //ربط الأصناف بأمر شراء
            $scope.orderPurchase.orderPurchaseProducts = $scope.orderPurchaseProducts;
            OrderPurchaseService.create($scope.orderPurchase).then(function (data) {
                OrderPurchaseService.findOne(data.id).then(function (value) {
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