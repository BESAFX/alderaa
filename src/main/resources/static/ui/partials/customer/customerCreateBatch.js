app.controller('customerCreateBatchCtrl', ['CustomerService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (CustomerService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.buffer = {};

        $scope.buffer.excelCustomers = [];

        $scope.customers = [];

        $scope.title = title;

        $scope.$watch(function ($scope) {
            return $scope.buffer.excelCustomers;
        }, function (newVal) {
            angular.forEach(newVal, function (row) {
                var customer = {};
                customer.contact = {};
                customer.contact.name = row['الاسم'];
                customer.contact.nationality = row['الجنسية'];
                customer.contact.identityNumber = row['السجل المدني'];
                customer.contact.mobile = row['الجوال'];
                customer.contact.phone = row['الهاتف'];
                customer.registerDate = row['تاريخ التسجيل'] ? new Date(row['تاريخ التسجيل']) : new Date();
                $scope.customers.push(customer);
            });
        }, true);

        $scope.clear = function () {
            $scope.form.$setPristine();
            $scope.buffer = {};
            $scope.buffer.excelCustomers = [];
            $scope.customers = [];
        };

        $scope.uploadExcelFile = function () {
            $scope.clear();
            document.getElementById('excelUploader').click();
        };

        $scope.submit = function () {
            CustomerService.createBatch($scope.customers).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);