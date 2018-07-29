app.controller('billSellDetailsCtrl', ['BillSellService', 'BillSellAttachService', 'BillSellProductService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'billSell',
    function (BillSellService, BillSellAttachService, BillSellProductService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, billSell) {

        $scope.billSell = {};

        $scope.billSell.billSellAttaches = [];

        $scope.billSell = billSell;

        $scope.files = [];

        $scope.refreshBillSell = function () {
            BillSellService.findOne($scope.billSell.id).then(function (data) {
                $scope.billSell = data;
            })
        };

        $scope.refreshBillSellProducts = function () {
            BillSellProductService.findByBillSell($scope.billSell.id).then(function (data) {
                $scope.billSell.billSellProducts = data;
            })
        };

        $scope.newBillSellProduct = function () {
            ModalProvider.openBillSellProductCreateModel($scope.billSell).result.then(function (data) {
                $scope.refreshBillSellProducts();
            });
        };

        $scope.deleteBillSellProduct = function (billSellProduct) {
            ModalProvider.openConfirmModel("العقود", "delete", "هل تود حذف السلعة فعلاً؟").result.then(function (value) {
                if (value) {
                    BillSellProductService.remove(billSellProduct.id).then(function () {
                        var index = $scope.billSell.billSellProducts.indexOf(billSellProduct);
                        $scope.billSell.billSellProducts.splice(index, 1);
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
                BillSellAttachService.upload($scope.billSell, $scope.files).then(function (value) {
                    Array.prototype.push.apply($scope.billSell.billSellAttaches, value);
                });
            }else{
                ModalProvider.openConfirmModel('العقود', 'attach_file', 'فضلاً اختر على الأقل ملف واحد للتحميل');
            }
        };

        $scope.deleteBillSellAttach = function (billSellAttach) {
            ModalProvider.openConfirmModel("العقود", "delete", "هل تود حذف المستند فعلاً؟").result.then(function (value) {
                if (value) {
                    BillSellAttachService.remove(billSellAttach).then(function (data) {
                        if(data){
                            var index = $scope.billSell.billSellAttaches.indexOf(billSellAttach);
                            $scope.billSell.billSellAttaches.splice(index, 1);
                        }else{
                            ModalProvider.openConfirmModel("العقود", "delete", "يبدو أن الملف لم يعد موجوداً بوحدات التخزين، هل تود حذفه نهائياً؟").result.then(function (value) {
                                if(value){
                                    BillSellAttachService.removeWhatever(billSellAttach).then(function () {
                                        var index = $scope.billSell.billSellAttaches.indexOf(billSellAttach);
                                        $scope.billSell.billSellAttaches.splice(index, 1);
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
            $scope.refreshBillSell();
            window.componentHandler.upgradeAllRegistered();
        }, 700);

    }]);