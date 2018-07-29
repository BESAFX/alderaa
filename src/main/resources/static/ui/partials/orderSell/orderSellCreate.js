app.controller('orderSellCreateCtrl', ['ProductService', 'OrderSellService', 'CustomerService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (ProductService, OrderSellService, CustomerService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.buffer = {};

        $scope.orderSell = {};

        $scope.orderSell.discount = 0;

        $scope.orderSell.transferFees = 0;

        $scope.orderSellProducts = [];

        $scope.customers = [];

        $scope.newCustomer = function () {
            ModalProvider.openCustomerCreateModel().result.then(function (data) {
                $scope.customers.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };

        $scope.searchCustomers = function ($select, $event) {

            // no event means first load!
            if (!$event) {
                $scope.pageCustomer = 0;
                $scope.customers = [];
            } else {
                $event.stopPropagation();
                $event.preventDefault();
                $scope.pageCustomer++;
            }

            var search = [];

            search.push('size=');
            search.push(10);
            search.push('&');

            search.push('page=');
            search.push($scope.pageCustomer);
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

            return CustomerService.filter(search.join("")).then(function (data) {
                $scope.buffer.lastCustomer = data.last;
                return $scope.customers = $scope.customers.concat(data.content);
            });

        };

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
            $scope.orderSellProducts.push({});
        };

        $scope.removeOrderSellProduct = function (index) {
            $scope.orderSellProducts.splice(index, 1);
        };

        $scope.submit = function () {
            //ربط الأصناف بأمر بيع
            $scope.orderSell.orderSellProducts = $scope.orderSellProducts;
            OrderSellService.create($scope.orderSell).then(function (data) {
                OrderSellService.findOne(data.id).then(function (value) {
                    $uibModalInstance.close(value);
                });
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);