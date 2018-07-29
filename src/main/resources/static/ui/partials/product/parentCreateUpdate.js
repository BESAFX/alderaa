app.controller('parentCreateUpdateCtrl', ['ProductService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'parent',
    function (ProductService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, parent) {

        $scope.buffer = {};

        $scope.title = title;

        $scope.action = action;

        $scope.product = parent;

        $scope.submit = function () {
            switch ($scope.action) {
                case 'create' :
                    ProductService.create($scope.product).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    ProductService.update($scope.product).then(function (data) {
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