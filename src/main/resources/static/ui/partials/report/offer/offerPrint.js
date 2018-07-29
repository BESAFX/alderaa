app.controller('offerPrintCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance', 'offer',
    function ($scope, $rootScope, $timeout, $uibModalInstance, offer) {

        $scope.offer = offer;

        $scope.submit = function () {
            window.open('/report/offer?offerId=' + offer.id);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);