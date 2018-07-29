app.controller('offerProductCreateCtrl', ['ProductService', 'OfferService', 'OfferProductService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'offer',
    function (ProductService, OfferService, OfferProductService, $scope, $rootScope, $timeout, $log, $uibModalInstance, offer) {

        $scope.buffer = {};

        $scope.offer = offer;

        $scope.offerProducts = [];

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
            var offerProduct = {};
            offerProduct.offer = $scope.offer;
            $scope.offerProducts.push(offerProduct);
        };

        $scope.removeOfferProduct = function (index) {
            $scope.offerProducts.splice(index, 1);
        };

        $scope.submit = function () {
            OfferProductService.createBatch($scope.offerProducts).then(function (data) {
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