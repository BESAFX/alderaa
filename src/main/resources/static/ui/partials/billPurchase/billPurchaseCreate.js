app.controller('billPurchaseCreateCtrl', ['ProductService', 'BillPurchaseService', 'SupplierService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (ProductService, BillPurchaseService, SupplierService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.buffer = {};

        $scope.billPurchase = {};

        $scope.billPurchase.writtenDate = new Date();

        $scope.billPurchase.discount = 0;

        $scope.billPurchaseProducts = [];

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

        $scope.refreshChilds = function (isOpen, billPurchaseProduct) {
            if (isOpen) {
                ProductService.findChilds(billPurchaseProduct.parent.id).then(function (value) {
                    return billPurchaseProduct.parent.childs = value;
                });
            }
        };

        $scope.addBillPurchaseProduct = function () {
            $scope.billPurchaseProducts.push({});
        };

        $scope.removeBillPurchaseProduct = function (index) {
            $scope.billPurchaseProducts.splice(index, 1);
        };

        $scope.submit = function () {
            //ربط الأصناف بالفاتورة
            $scope.billPurchase.billPurchaseProducts = $scope.billPurchaseProducts;
            BillPurchaseService.create($scope.billPurchase).then(function (data) {
                BillPurchaseService.findOne(data.id).then(function (value) {
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