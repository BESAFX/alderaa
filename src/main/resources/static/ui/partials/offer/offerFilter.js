app.controller('offerFilterCtrl', ['$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function ($scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.addSortBy = function () {
            var sortBy = {};
            $scope.pageOffer.sorts.push(sortBy);
        };

        $scope.submit = function () {
            $scope.pageOffer.page = $scope.pageOffer.currentPage - 1;
            $uibModalInstance.close($scope.paramOffer);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);