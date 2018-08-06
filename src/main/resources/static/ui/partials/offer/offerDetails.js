app.controller('offerDetailsCtrl', ['OfferService', 'OfferAttachService', 'OfferProductService', 'BillSellService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'offer',
    function (OfferService, OfferAttachService, OfferProductService, BillSellService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, offer) {

        $scope.offer = {};

        $scope.offer.offerAttaches = [];

        $scope.offer = offer;

        $scope.files = [];

        $scope.refreshOffer = function () {
            OfferService.findOne($scope.offer.id).then(function (data) {
                $scope.offer = data;
            })
        };

        $scope.refreshOfferProducts = function () {
            OfferProductService.findByOffer($scope.offer.id).then(function (data) {
                $scope.offer.offerProducts = data;
            })
        };

        $scope.newOfferProduct = function () {
            ModalProvider.openOfferProductCreateModel($scope.offer).result.then(function (data) {
                $scope.offer.offerProducts.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };

        $scope.deleteOfferProduct = function (offerProduct) {
            ModalProvider.openConfirmModel("العروض", "delete", "هل تود حذف السلعة فعلاً؟").result.then(function (value) {
                if (value) {
                    OfferProductService.remove(offerProduct.id).then(function () {
                        var index = $scope.offer.offerProducts.indexOf(offerProduct);
                        $scope.offer.offerProducts.splice(index, 1);
                    });
                }
            });
        };

        $scope.convertOfferToBill = function () {
            ModalProvider.openConfirmModel("العروض", "send", "هل تود الموافقة على العرض وتحويله إلى فاتورة بيع للعميل؟").result.then(function (value) {
                if (value) {
                    BillSellService.createFromOffer($scope.offer).then(function (value) {
                        return $scope.offer.billSells.splice(0, 0, value);
                    });
                }
            });
        };

        $scope.printOffer = function () {
            window.open('/report/offer?offerId=' + $scope.offer.id);
        };

        $scope.sendOffer = function () {
            ModalProvider.openConfirmModel("العروض", "send", "هل تود إرسال العرض إلى العميل؟").result.then(function (value) {
                if (value) {
                    OfferService.send($scope.offer.id);
                }
            });
        };

        //////////////////////////File Manager///////////////////////////////////
        $scope.uploadFiles = function () {
            document.getElementById('uploader').click();
        };

        $scope.uploadAll = function () {
            if($scope.files.length > 0){
                OfferAttachService.upload($scope.offer, $scope.files).then(function (value) {
                    Array.prototype.push.apply($scope.offer.offerAttaches, value);
                });
            }else{
                ModalProvider.openConfirmModel('العروض', 'attach_file', 'فضلاً اختر على الأقل ملف واحد للتحميل');
            }
        };

        $scope.deleteOfferAttach = function (offerAttach) {
            ModalProvider.openConfirmModel("العروض", "delete", "هل تود حذف المستند فعلاً؟").result.then(function (value) {
                if (value) {
                    OfferAttachService.remove(offerAttach).then(function (data) {
                        if(data){
                            var index = $scope.offer.offerAttaches.indexOf(offerAttach);
                            $scope.offer.offerAttaches.splice(index, 1);
                        }else{
                            ModalProvider.openConfirmModel("العروض", "delete", "يبدو أن الملف لم يعد موجوداً بوحدات التخزين، هل تود حذفه نهائياً؟").result.then(function (value) {
                                if(value){
                                    OfferAttachService.removeWhatever(offerAttach).then(function () {
                                        var index = $scope.offer.offerAttaches.indexOf(offerAttach);
                                        $scope.offer.offerAttaches.splice(index, 1);
                                    })
                                }
                            });
                        }
                    });
                }
            });
        };
        //////////////////////////File Manager///////////////////////////////////

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshOffer();
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);