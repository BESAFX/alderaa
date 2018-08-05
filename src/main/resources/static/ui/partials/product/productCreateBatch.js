app.controller('productCreateBatchCtrl', ['ProductService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (ProductService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.buffer = {};

        $scope.buffer.excelProducts = [];

        $scope.products = [];

        $scope.title = title;

        $scope.$watch(function ($scope) {
            return $scope.buffer.excelProducts;
        }, function (newVal) {
            angular.forEach(newVal, function (row) {
                var product = {};
                product.name = row['الاسم'];
                if(row['التصنيف']){
                    product.parent = {};
                    product.parent.name = row['التصنيف'];
                }
                product.registerDate = row['تاريخ التسجيل'] ? new Date(row['تاريخ التسجيل']) : new Date();
                $scope.products.push(product);
            });
        }, true);

        $scope.clear = function () {
            $scope.form.$setPristine();
            $scope.buffer = {};
            $scope.buffer.excelProducts = [];
            $scope.products = [];
        };

        $scope.uploadExcelFile = function () {
            $scope.clear();
            document.getElementById('excelUploader').click();
        };

        $scope.submit = function () {
            ProductService.createBatch($scope.products).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);