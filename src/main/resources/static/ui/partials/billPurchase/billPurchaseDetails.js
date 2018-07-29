app.controller('billPurchaseDetailsCtrl', ['BillPurchaseService', 'BillPurchaseAttachService', 'BillPurchaseProductService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'billPurchase',
    function (BillPurchaseService, BillPurchaseAttachService, BillPurchaseProductService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, billPurchase) {

        $scope.billPurchase = {};

        $scope.billPurchase.billPurchaseAttaches = [];

        $scope.billPurchase = billPurchase;

        $scope.files = [];

        $scope.refreshBillPurchase = function () {
            BillPurchaseService.findOne($scope.billPurchase.id).then(function (data) {
                $scope.billPurchase = data;
            })
        };

        $scope.refreshBillPurchaseProducts = function () {
            BillPurchaseProductService.findByBillPurchase($scope.billPurchase.id).then(function (data) {
                $scope.billPurchase.billPurchaseProducts = data;
            })
        };

        $scope.newBillPurchaseProduct = function () {
            ModalProvider.openBillPurchaseProductCreateModel($scope.billPurchase).result.then(function (data) {
                $scope.refreshBillPurchaseProducts();
            });
        };

        $scope.deleteBillPurchaseProduct = function (billPurchaseProduct) {
            ModalProvider.openConfirmModel("العقود", "delete", "هل تود حذف السلعة فعلاً؟").result.then(function (value) {
                if (value) {
                    BillPurchaseProductService.remove(billPurchaseProduct.id).then(function () {
                        var index = $scope.billPurchase.billPurchaseProducts.indexOf(billPurchaseProduct);
                        $scope.billPurchase.billPurchaseProducts.splice(index, 1);
                    });
                }
            });
        };

        //////////////////////////File Manager///////////////////////////////////
        $scope.uploadFiles = function () {
            document.getElementById('uploader').click();
        };

        $scope.uploadAll = function () {
            if($scope.files.length > 0){
                BillPurchaseAttachService.upload($scope.billPurchase, $scope.files).then(function (value) {
                    Array.prototype.push.apply($scope.billPurchase.billPurchaseAttaches, value);
                });
            }else{
                ModalProvider.openConfirmModel('العقود', 'attach_file', 'فضلاً اختر على الأقل ملف واحد للتحميل');
            }
        };

        $scope.deleteBillPurchaseAttach = function (billPurchaseAttach) {
            ModalProvider.openConfirmModel("العقود", "delete", "هل تود حذف المستند فعلاً؟").result.then(function (value) {
                if (value) {
                    BillPurchaseAttachService.remove(billPurchaseAttach).then(function (data) {
                        if(data){
                            var index = $scope.billPurchase.billPurchaseAttaches.indexOf(billPurchaseAttach);
                            $scope.billPurchase.billPurchaseAttaches.splice(index, 1);
                        }else{
                            ModalProvider.openConfirmModel("العقود", "delete", "يبدو أن الملف لم يعد موجوداً بوحدات التخزين، هل تود حذفه نهائياً؟").result.then(function (value) {
                                if(value){
                                    BillPurchaseAttachService.removeWhatever(billPurchaseAttach).then(function () {
                                        var index = $scope.billPurchase.billPurchaseAttaches.indexOf(billPurchaseAttach);
                                        $scope.billPurchase.billPurchaseAttaches.splice(index, 1);
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
            $scope.refreshBillPurchase();
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);