app.controller('offerUpdateCtrl', ['OfferService', 'CustomerService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'offer',
    function (OfferService, CustomerService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, offer) {

        $scope.buffer = {};

        $scope.offer = offer;

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

        $scope.submit = function () {
            OfferService.update($scope.offer).then(function (data) {
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