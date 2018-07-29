app.controller('salesByCustomerCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance',
    function ($scope, $rootScope, $timeout, $uibModalInstance) {

        $scope.buffer = {};

        $scope.submit = function () {
            var param= [];
            if($scope.buffer.exportType){
                param.push('exportType=');
                param.push($scope.buffer.exportType);
                param.push('&');
            }
            if($scope.buffer.dateFrom){
                param.push('dateFrom=');
                param.push($scope.buffer.dateFrom);
                param.push('&');
            }
            if($scope.buffer.dateTo){
                param.push('dateTo=');
                param.push($scope.buffer.dateTo);
                param.push('&');
            }
            window.open('/report/salesByCustomer?' + param.join(''));
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);