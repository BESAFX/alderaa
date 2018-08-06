app.controller('offerCreateCtrl', ['ProductService', 'OfferService', 'CustomerService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'customer',
    function (ProductService, OfferService, CustomerService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, customer) {

        $scope.buffer = {};

        $scope.offer = {};

        $scope.offer.customer = customer;

        $scope.offer.writtenDate = new Date();

        $scope.offer.endDate = new Date();

        $scope.offer.endDate.setDate($scope.offer.writtenDate.getDate() + 30);

        $scope.offer.discount = 0;

        $scope.offer.transferFees = 0;

        $scope.offerProducts = [];

        $scope.customers = [];

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

        $scope.refreshParents = function (isOpen) {
            if (isOpen) {
                ProductService.findParents().then(function (value) {
                    $scope.parents = value;
                });
            }
        };

        $scope.refreshChilds = function (isOpen, offerProduct) {
            if (isOpen) {
                ProductService.findChilds(offerProduct.parent.id).then(function (value) {
                    return offerProduct.parent.childs = value;
                });
            }
        };

        $scope.addOfferProduct = function () {
            $scope.offerProducts.push({});
        };

        $scope.removeOfferProduct = function (index) {
            $scope.offerProducts.splice(index, 1);
        };

        $scope.submit = function () {
            //ربط الأصناف بالعرض
            $scope.offer.offerProducts = $scope.offerProducts;
            OfferService.create($scope.offer).then(function (data) {
                OfferService.findOne(data.id).then(function (value) {
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