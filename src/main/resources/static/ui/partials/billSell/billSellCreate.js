app.controller('billSellCreateCtrl', ['ProductService', 'BillSellService', 'CustomerService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'customer',
    function (ProductService, BillSellService, CustomerService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, customer) {

        $scope.buffer = {};

        $scope.billSell = {};

        $scope.billSell.customer = customer;

        $scope.billSell.writtenDate = new Date();

        $scope.billSell.discount = 0;

        $scope.billSellProducts = [];

        $scope.customers = [];

        $scope.products = [];

        $scope.newCustomer = function () {
            ModalProvider.openCustomerCreateModel().result.then(function (data) {
                $scope.customers.splice(0, 0, data);
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

        $scope.searchProducts = function ($select, $event) {

            // no event means first load!
            if (!$event) {
                $scope.pageProduct = 0;
                $scope.products = [];
            } else {
                $event.stopPropagation();
                $event.preventDefault();
                $scope.pageProduct++;
            }

            var search = [];

            search.push('size=');
            search.push(10);
            search.push('&');

            search.push('page=');
            search.push($scope.pageProduct);
            search.push('&');

            search.push('name=');
            search.push($select.search);
            search.push('&');

            search.push('parentName=');
            search.push($select.search);
            search.push('&');

            search.push('filterCompareType=or');

            return ProductService.filter(search.join("")).then(function (data) {
                $scope.buffer.lastProduct = data.last;
                return $scope.products = $scope.products.concat(data.content);
            });

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
            BillSellService.create($scope.billSell).then(function (data) {
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