app.controller('customerContactCreateUpdateCtrl', ['CustomerContactService', 'CustomerService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'customerContact',
    function (CustomerContactService, CustomerService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, customerContact) {

        $scope.buffer = {};

        $scope.customerContact = customerContact;

        $scope.title = title;

        $scope.action = action;

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
            switch ($scope.action) {
                case 'create' :
                    CustomerContactService.create($scope.customerContact).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    CustomerContactService.update($scope.customerContact).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);