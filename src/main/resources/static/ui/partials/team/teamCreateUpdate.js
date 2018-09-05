app.controller('teamCreateUpdateCtrl', ['TeamService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'team',
    function (TeamService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, team) {

        $scope.title = title;

        $scope.action = action;

        $scope.roles = [];

        //////////////////////////Company////////////////////////////////////////
        $scope.roles.push({
            name: 'تعديل بيانات الشركة',
            value: 'ROLE_COMPANY_UPDATE',
            selected: false,
            category: 'الإدارة'
        });
        $scope.roles.push({
            name: 'إرسال الرسائل',
            value: 'ROLE_SMS_SEND',
            selected: false,
            category: 'الإدارة'
        });
        //////////////////////////Bank//////////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء حساب مالي',
            value: 'ROLE_BANK_CREATE',
            selected: false,
            category: 'المالية'
        });
        $scope.roles.push({
            name: 'تعديل حساب مالي',
            value: 'ROLE_BANK_UPDATE',
            selected: false,
            category: 'المالية'
        });
        $scope.roles.push({
            name: 'حذف حساب مالي',
            value: 'ROLE_BANK_DELETE',
            selected: false,
            category: 'المالية'
        });
        $scope.roles.push({
            name: 'إنشاء سند إيداع',
            value: 'ROLE_DEPOSIT_CREATE',
            selected: false,
            category: 'المالية'
        });
        $scope.roles.push({
            name: 'إنشاء سند سحب',
            value: 'ROLE_WITHDRAW_CREATE',
            selected: false,
            category: 'المالية'
        });
        $scope.roles.push({
            name: 'إنشاء سند تحويل',
            value: 'ROLE_TRANSFER_CREATE',
            selected: false,
            category: 'المالية'
        });
        $scope.roles.push({
            name: 'إنشاء سند صرف',
            value: 'ROLE_EXPENSE_CREATE',
            selected: false,
            category: 'المالية'
        });
        //////////////////////////Customer//////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء حسابات العملاء',
            value: 'ROLE_CUSTOMER_CREATE',
            selected: false,
            category: 'العملاء'
        });
        $scope.roles.push({
            name: 'تعديل حسابات العملاء',
            value: 'ROLE_CUSTOMER_UPDATE',
            selected: false,
            category: 'العملاء'
        });
        $scope.roles.push({
            name: 'حذف حسابات العملاء',
            value: 'ROLE_CUSTOMER_DELETE',
            selected: false,
            category: 'العملاء'
        });
        //////////////////////////CustomerContact//////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء جهات اتصال العملاء',
            value: 'ROLE_CUSTOMER_CONTACT_CREATE',
            selected: false,
            category: 'العملاء'
        });
        $scope.roles.push({
            name: 'تعديل جهات اتصال العملاء',
            value: 'ROLE_CUSTOMER_CONTACT_UPDATE',
            selected: false,
            category: 'العملاء'
        });
        $scope.roles.push({
            name: 'حذف جهات اتصال العملاء',
            value: 'ROLE_CUSTOMER_CONTACT_DELETE',
            selected: false,
            category: 'العملاء'
        });
        //////////////////////////Customer Note//////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء ملاحظات العملاء',
            value: 'ROLE_CUSTOMER_NOTE_CREATE',
            selected: false,
            category: 'العملاء'
        });
        $scope.roles.push({
            name: 'تعديل ملاحظات العملاء',
            value: 'ROLE_CUSTOMER_NOTE_UPDATE',
            selected: false,
            category: 'العملاء'
        });
        $scope.roles.push({
            name: 'حذف ملاحظات العملاء',
            value: 'ROLE_CUSTOMER_NOTE_DELETE',
            selected: false,
            category: 'العملاء'
        });
        //////////////////////////Supplier//////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء حسابات الموردين',
            value: 'ROLE_SUPPLIER_CREATE',
            selected: false,
            category: 'الموردين'
        });
        $scope.roles.push({
            name: 'تعديل حسابات الموردين',
            value: 'ROLE_SUPPLIER_UPDATE',
            selected: false,
            category: 'الموردين'
        });
        $scope.roles.push({
            name: 'حذف حسابات الموردين',
            value: 'ROLE_SUPPLIER_DELETE',
            selected: false,
            category: 'الموردين'
        });
        //////////////////////////SupplierContact//////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء جهات اتصال الموردين',
            value: 'ROLE_SUPPLIER_CONTACT_CREATE',
            selected: false,
            category: 'الموردين'
        });
        $scope.roles.push({
            name: 'تعديل جهات اتصال الموردين',
            value: 'ROLE_SUPPLIER_CONTACT_UPDATE',
            selected: false,
            category: 'الموردين'
        });
        $scope.roles.push({
            name: 'حذف جهات اتصال الموردين',
            value: 'ROLE_SUPPLIER_CONTACT_DELETE',
            selected: false,
            category: 'الموردين'
        });
        //////////////////////////Product//////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء التصنيفات',
            value: 'ROLE_PRODUCT_CREATE',
            selected: false,
            category: 'المخزون'
        });
        $scope.roles.push({
            name: 'تعديل التصنيفات',
            value: 'ROLE_PRODUCT_UPDATE',
            selected: false,
            category: 'المخزون'
        });
        $scope.roles.push({
            name: 'حذف التصنيفات',
            value: 'ROLE_PRODUCT_DELETE',
            selected: false,
            category: 'المخزون'
        });
        //////////////////////////Offer////////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء العروض',
            value: 'ROLE_OFFER_CREATE',
            selected: false,
            category: 'العروض'
        });
        $scope.roles.push({
            name: 'تعديل العروض',
            value: 'ROLE_OFFER_UPDATE',
            selected: false,
            category: 'العروض'
        });
        $scope.roles.push({
            name: 'حذف العروض',
            value: 'ROLE_OFFER_DELETE',
            selected: false,
            category: 'العروض'
        });
        $scope.roles.push({
            name: 'اضافة سلع إلى العروض',
            value: 'ROLE_OFFER_PRODUCT_CREATE',
            selected: false,
            category: 'العروض'
        });
        $scope.roles.push({
            name: 'حذف سلع من العروض',
            value: 'ROLE_OFFER_PRODUCT_DELETE',
            selected: false,
            category: 'العروض'
        });
        //////////////////////////OrderPurchase////////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء أوامر الشراء',
            value: 'ROLE_ORDER_PURCHASE_CREATE',
            selected: false,
            category: 'المشتريات'
        });
        $scope.roles.push({
            name: 'تعديل أوامر الشراء',
            value: 'ROLE_ORDER_PURCHASE_UPDATE',
            selected: false,
            category: 'المشتريات'
        });
        $scope.roles.push({
            name: 'حذف أوامر الشراء',
            value: 'ROLE_ORDER_PURCHASE_DELETE',
            selected: false,
            category: 'المشتريات'
        });
        $scope.roles.push({
            name: 'اضافة سلع إلى أوامر الشراء',
            value: 'ROLE_ORDER_PURCHASE_PRODUCT_CREATE',
            selected: false,
            category: 'المشتريات'
        });
        $scope.roles.push({
            name: 'حذف سلع من أوامر الشراء',
            value: 'ROLE_ORDER_PURCHASE_PRODUCT_DELETE',
            selected: false,
            category: 'المشتريات'
        });
        //////////////////////////BillPurchase//////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء فواتير الشراء',
            value: 'ROLE_BILL_PURCHASE_CREATE',
            selected: false,
            category: 'المشتريات'
        });
        $scope.roles.push({
            name: 'تعديل فواتير الشراء',
            value: 'ROLE_BILL_PURCHASE_UPDATE',
            selected: false,
            category: 'المشتريات'
        });
        $scope.roles.push({
            name: 'حذف فواتير الشراء',
            value: 'ROLE_BILL_PURCHASE_DELETE',
            selected: false,
            category: 'المشتريات'
        });
        $scope.roles.push({
            name: 'اضافة الدفعات المالية للشراء',
            value: 'ROLE_SUPPLIER_PAYMENT_CREATE',
            selected: false,
            category: 'المشتريات'
        });
        $scope.roles.push({
            name: 'حذف الدفعات المالية للشراء',
            value: 'ROLE_SUPPLIER_PAYMENT_DELETE',
            selected: false,
            category: 'المشتريات'
        });
        $scope.roles.push({
            name: 'اضافة سلع إلى فواتير الشراء',
            value: 'ROLE_BILL_PURCHASE_PRODUCT_CREATE',
            selected: false,
            category: 'المشتريات'
        });
        $scope.roles.push({
            name: 'حذف سلع من فواتير الشراء',
            value: 'ROLE_BILL_PURCHASE_PRODUCT_DELETE',
            selected: false,
            category: 'المشتريات'
        });
        //////////////////////////OrderSell////////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء أوامر البيع',
            value: 'ROLE_ORDER_SELL_CREATE',
            selected: false,
            category: 'المبيعات'
        });
        $scope.roles.push({
            name: 'تعديل أوامر البيع',
            value: 'ROLE_ORDER_SELL_UPDATE',
            selected: false,
            category: 'المبيعات'
        });
        $scope.roles.push({
            name: 'حذف أوامر البيع',
            value: 'ROLE_ORDER_SELL_DELETE',
            selected: false,
            category: 'المبيعات'
        });
        $scope.roles.push({
            name: 'اضافة سلع إلى أوامر البيع',
            value: 'ROLE_ORDER_SELL_PRODUCT_CREATE',
            selected: false,
            category: 'المبيعات'
        });
        $scope.roles.push({
            name: 'حذف سلع من أوامر البيع',
            value: 'ROLE_ORDER_SELL_PRODUCT_DELETE',
            selected: false,
            category: 'المبيعات'
        });
        //////////////////////////BillSell////////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء فواتير البيع',
            value: 'ROLE_BILL_SELL_CREATE',
            selected: false,
            category: 'المبيعات'
        });
        $scope.roles.push({
            name: 'تعديل فواتير البيع',
            value: 'ROLE_BILL_SELL_UPDATE',
            selected: false,
            category: 'المبيعات'
        });
        $scope.roles.push({
            name: 'حذف فواتير البيع',
            value: 'ROLE_BILL_SELL_DELETE',
            selected: false,
            category: 'المبيعات'
        });
        $scope.roles.push({
            name: 'اضافة الدفعات المالية للبيع',
            value: 'ROLE_CUSTOMER_PAYMENT_CREATE',
            selected: false,
            category: 'المبيعات'
        });
        $scope.roles.push({
            name: 'حذف الدفعات المالية للبيع',
            value: 'ROLE_CUSTOMER_PAYMENT_DELETE',
            selected: false,
            category: 'المبيعات'
        });
        $scope.roles.push({
            name: 'اضافة سلع إلى فواتير البيع',
            value: 'ROLE_BILL_SELL_PRODUCT_CREATE',
            selected: false,
            category: 'المبيعات'
        });
        $scope.roles.push({
            name: 'حذف سلع من فواتير البيع',
            value: 'ROLE_BILL_SELL_PRODUCT_DELETE',
            selected: false,
            category: 'المبيعات'
        });
        //////////////////////////Person//////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء حسابات المستخدمين',
            value: 'ROLE_PERSON_CREATE',
            selected: false,
            category: 'الإدارة'
        });
        $scope.roles.push({
            name: 'تعديل حسابات المستخدمين',
            value: 'ROLE_PERSON_UPDATE',
            selected: false,
            category: 'الإدارة'
        });
        $scope.roles.push({
            name: 'حذف حسابات المستخدمين',
            value: 'ROLE_PERSON_DELETE',
            selected: false,
            category: 'الإدارة'
        });
        //////////////////////////Team////////////////////////////////////////////
        $scope.roles.push({
            name: 'إنشاء الصلاحيات',
            value: 'ROLE_TEAM_CREATE',
            selected: false,
            category: 'الإدارة'
        });
        $scope.roles.push({
            name: 'تعديل الصلاحيات',
            value: 'ROLE_TEAM_UPDATE',
            selected: false,
            category: 'الإدارة'
        });
        $scope.roles.push({
            name: 'حذف الصلاحيات',
            value: 'ROLE_TEAM_DELETE',
            selected: false,
            category: 'الإدارة'
        });


        if (team) {
            $scope.team = team;
            if (typeof team.authorities === 'string') {
                $scope.team.authorities = team.authorities.split(',');
            }
            //
            angular.forEach($scope.team.authorities, function (auth) {
                angular.forEach($scope.roles, function (role) {
                    if (role.value === auth) {
                        return role.selected = true;
                    }
                })
            });
        } else {
            $scope.team = {};
        }

        $scope.submit = function () {
            $scope.team.authorities = [];
            angular.forEach($scope.roles, function (role) {
                if (role.selected) {
                    $scope.team.authorities.push(role.value);
                }
            });
            $scope.team.authorities = $scope.team.authorities.join();
            switch ($scope.action) {
                case 'create' :
                    TeamService.create($scope.team).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    TeamService.update($scope.team).then(function (data) {
                        $scope.team = data;
                        $scope.team.authorities = team.authorities.split(',');
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 800);

    }]);