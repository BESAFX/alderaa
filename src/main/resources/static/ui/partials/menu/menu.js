app.controller("menuCtrl", [
    'CompanyService',
    'CustomerService',
    'SupplierService',
    'ProductService',
    'OfferService',
    'OrderPurchaseService',
    'BillPurchaseService',
    'OrderSellService',
    'BillSellService',
    'SupplierPaymentService',
    'CustomerPaymentService',
    'BankService',
    'BankTransactionService',
    'AttachTypeService',
    'PersonService',
    'SmsService',
    'HistoryService',
    'TeamService',
    'ModalProvider',
    '$scope',
    '$rootScope',
    '$state',
    '$uibModal',
    '$timeout',
    function (CompanyService,
              CustomerService,
              SupplierService,
              ProductService,
              OfferService,
              OrderPurchaseService,
              BillPurchaseService,
              OrderSellService,
              BillSellService,
              SupplierPaymentService,
              CustomerPaymentService,
              BankService,
              BankTransactionService,
              AttachTypeService,
              PersonService,
              SmsService,
              HistoryService,
              TeamService,
              ModalProvider,
              $scope,
              $rootScope,
              $state,
              $uibModal,
              $timeout) {

        $scope.$watch('toggleState', function (newValue, oldValue) {
            switch (newValue) {
                case 'menu': {
                    $scope.pageTitle = 'القائمة';
                    break;
                }
                case 'company': {
                    $scope.pageTitle = 'المؤسسة';
                    break;
                }
                case 'customer': {
                    $scope.pageTitle = 'العملاء';
                    break;
                }
                case 'supplier': {
                    $scope.pageTitle = 'الموردين';
                    break;
                }
                case 'product': {
                    $scope.pageTitle = 'الأصناف';
                    break;
                }
                case 'offer': {
                    $scope.pageTitle = 'العروض';
                    break;
                }
                case 'orderPurchase': {
                    $scope.pageTitle = 'أوامر الشراء';
                    break;
                }
                case 'billPurchase': {
                    $scope.pageTitle = 'فواتير الشراء';
                    break;
                }
                case 'supplierPayment': {
                    $scope.pageTitle = 'دفعات الشراء';
                    break;
                }
                case 'orderSell': {
                    $scope.pageTitle = 'أوامر البيع';
                    break;
                }
                case 'billSell': {
                    $scope.pageTitle = 'فواتير البيع';
                    break;
                }
                case 'customerPayment': {
                    $scope.pageTitle = 'دفعات البيع';
                    break;
                }
                case 'bankTransaction': {
                    $scope.pageTitle = 'المعاملات المالية';
                    break;
                }
                case 'team': {
                    $scope.pageTitle = 'الصلاحيات';
                    break;
                }
                case 'person': {
                    $scope.pageTitle = 'المستخدمين';
                    break;
                }
                case 'profile': {
                    $scope.pageTitle = 'الملف الشخصي';
                    break;
                }
                case 'help': {
                    $scope.pageTitle = 'المساعدة';
                    break;
                }
                case 'about': {
                    $scope.pageTitle = 'عن البرنامج';
                    break;
                }
                case 'report': {
                    $scope.pageTitle = 'التقارير';
                    break;
                }
            }
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        }, true);
        $scope.toggleState = 'menu';
        $scope.openStateMenu = function () {
            $scope.toggleState = 'menu';
            $rootScope.refreshGUI();
        };
        $scope.openStateCompany = function () {
            $scope.toggleState = 'company';
            $rootScope.refreshGUI();
        };
        $scope.openStateCustomer = function () {
            $scope.toggleState = 'customer';
            $scope.searchCustomers({});
            $rootScope.refreshGUI();
        };
        $scope.openStateSupplier = function () {
            $scope.toggleState = 'supplier';
            $scope.searchSuppliers({});
            $rootScope.refreshGUI();
        };
        $scope.openStateProduct = function () {
            $scope.toggleState = 'product';
            $scope.searchProducts({});
            $rootScope.refreshGUI();
        };
        $scope.openStateOffer = function () {
            $scope.toggleState = 'offer';
            $scope.searchOffers({});
            $rootScope.refreshGUI();
        };
        $scope.openStateOrderPurchase = function () {
            $scope.toggleState = 'orderPurchase';
            $scope.searchOrderPurchases({});
            $rootScope.refreshGUI();
        };
        $scope.openStateBillPurchase = function () {
            $scope.toggleState = 'billPurchase';
            $scope.searchBillPurchases({});
            $rootScope.refreshGUI();
        };
        $scope.openStateSupplierPayment = function () {
            $scope.toggleState = 'supplierPayment';
            $scope.searchSupplierPayments({});
            $rootScope.refreshGUI();
        };
        $scope.openStateOrderSell = function () {
            $scope.toggleState = 'orderSell';
            $scope.searchOrderSells({});
            $rootScope.refreshGUI();
        };
        $scope.openStateBillSell = function () {
            $scope.toggleState = 'billSell';
            $scope.searchBillSells({});
            $rootScope.refreshGUI();
        };
        $scope.openStateCustomerPayment = function () {
            $scope.toggleState = 'customerPayment';
            $scope.searchCustomerPayments({});
            $rootScope.refreshGUI();
        };
        $scope.openStateBankTransaction = function () {
            $scope.toggleState = 'bankTransaction';
            $scope.searchBankTransactions({});
            $rootScope.refreshGUI();
        };
        $scope.openStateTeam = function () {
            $scope.toggleState = 'team';
            $scope.fetchTeamTableData();
            $rootScope.refreshGUI();
        };
        $scope.openStatePerson = function () {
            $scope.toggleState = 'person';
            $scope.fetchPersonTableData();
            $rootScope.refreshGUI();
        };
        $scope.openStateProfile = function () {
            $scope.toggleState = 'profile';
            $rootScope.refreshGUI();
        };
        $scope.openStateHelp = function () {
            $scope.toggleState = 'help';
            $rootScope.refreshGUI();
        };
        $scope.openStateAbout = function () {
            $scope.toggleState = 'about';
            $rootScope.refreshGUI();
        };
        $scope.openStateReport = function () {
            $scope.toggleState = 'report';
            $scope.toggleReport = 'mainReportFrame';
            $rootScope.refreshGUI();
        };

        $rootScope.toggleMenu = function () {
            $rootScope.activeDrawer = 'menu';
            $rootScope.drawer = document.querySelector('.mdl-layout');
            $rootScope.drawer.MaterialLayout.toggleDrawer();
        };

        $rootScope.toggleOfferHelp = function () {
            $rootScope.activeDrawer = 'offerHelp';
            $rootScope.drawer = document.querySelector('.mdl-layout');
            $rootScope.drawer.MaterialLayout.toggleDrawer();
        };

        $rootScope.toggleOrderPurchaseHelp = function () {
            $rootScope.activeDrawer = 'orderPurchaseHelp';
            $rootScope.drawer = document.querySelector('.mdl-layout');
            $rootScope.drawer.MaterialLayout.toggleDrawer();
        };

        $rootScope.toggleOrderSellHelp = function () {
            $rootScope.activeDrawer = 'orderSellHelp';
            $rootScope.drawer = document.querySelector('.mdl-layout');
            $rootScope.drawer.MaterialLayout.toggleDrawer();
        };

        $rootScope.toggleBillSellHelp = function () {
            $rootScope.activeDrawer = 'billSellHelp';
            $rootScope.drawer = document.querySelector('.mdl-layout');
            $rootScope.drawer.MaterialLayout.toggleDrawer();
        };

        /**************************************************************************************************************
         *                                                                                                            *
         * Company                                                                                                    *
         *                                                                                                            *
         **************************************************************************************************************/
        $rootScope.selectedCompany = {};
        $rootScope.selectedCompany.options = {};
        $rootScope.sms = {};
        $scope.submitCompany = function () {
            $rootScope.selectedCompany.options = JSON.stringify($rootScope.selectedCompany.options);
            CompanyService.update($rootScope.selectedCompany).then(function (data) {
                $rootScope.selectedCompany = data;
                $rootScope.selectedCompany.options = JSON.parse(data.options);
            });
        };
        $scope.updateCompanyOptions = function () {

            var param = [];

            param.push("yamamahUserName=");
            param.push($rootScope.selectedCompany.options.yamamahUserName);
            param.push("&");

            param.push("yamamahPassword=");
            param.push($rootScope.selectedCompany.options.yamamahPassword);
            param.push("&");

            param.push("vatFactor=");
            param.push($rootScope.selectedCompany.options.vatFactor);
            param.push("&");

            param.push("logo=");
            param.push($rootScope.selectedCompany.options.logo);
            param.push("&");

            param.push("background=");
            param.push($rootScope.selectedCompany.options.background);
            param.push("&");

            param.push("reportTitle=");
            param.push($rootScope.selectedCompany.options.reportTitle);
            param.push("&");

            param.push("reportSubTitle=");
            param.push($rootScope.selectedCompany.options.reportSubTitle);
            param.push("&");

            param.push("reportFooter=");
            param.push($rootScope.selectedCompany.options.reportFooter);
            param.push("&");

            param.push("tokenLengthInHours=");
            param.push($rootScope.selectedCompany.options.tokenLengthInHours);
            param.push("&");

            CompanyService.updateOptions(param.join("")).then(function (value) {

            });
        };
        $scope.browseCompanyLogo = function () {
            document.getElementById('uploader-company').click();
        };
        $scope.uploadCompanyLogo = function (files) {
            CompanyService.uploadCompanyLogo(files[0]).then(function (data) {
                $rootScope.selectedCompany.logo = data;
                CompanyService.update($rootScope.selectedCompany).then(function (data) {
                    $rootScope.selectedCompany = data;
                });
            });
        };
        $rootScope.findSmsCredit = function () {
            SmsService.getCredit().then(function (value) {
                $rootScope.sms.credit = value.GetCreditPostResult.Credit;
                $rootScope.sms.description = value.GetCreditPostResult.Description;
                return $rootScope.sms;
            });
        };

        /**************************************************************************************************************
         *                                                                                                            *
         * Customer                                                                                                   *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.customers = [];
        $scope.paramCustomer = {};
        $scope.customers.checkAll = false;

        $scope.pageCustomer = {};
        $scope.pageCustomer.sorts = [];
        $scope.pageCustomer.page = 0;
        $scope.pageCustomer.totalPages = 0;
        $scope.pageCustomer.currentPage = $scope.pageCustomer.page + 1;
        $scope.pageCustomer.currentPageString = ($scope.pageCustomer.page + 1) + ' / ' + $scope.pageCustomer.totalPages;
        $scope.pageCustomer.size = 25;
        $scope.pageCustomer.first = true;
        $scope.pageCustomer.last = true;

        $scope.openCustomersFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/customer/customerFilter.html',
                controller: 'customerFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramCustomer) {
                $scope.searchCustomers(paramCustomer);
            }, function () {
            });
        };
        $scope.searchCustomers = function (paramCustomer) {
            var search = [];
            search.push('size=');
            search.push($scope.pageCustomer.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageCustomer.page);
            search.push('&');
            angular.forEach($scope.pageCustomer.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if (paramCustomer.codeFrom) {
                search.push('codeFrom=');
                search.push(paramCustomer.codeFrom);
                search.push('&');
            }
            if (paramCustomer.codeTo) {
                search.push('codeTo=');
                search.push(paramCustomer.codeTo);
                search.push('&');
            }
            if (paramCustomer.registerDateTo) {
                search.push('registerDateTo=');
                search.push(paramCustomer.registerDateTo.getTime());
                search.push('&');
            }
            if (paramCustomer.registerDateFrom) {
                search.push('registerDateFrom=');
                search.push(paramCustomer.registerDateFrom.getTime());
                search.push('&');
            }
            if (paramCustomer.name) {
                search.push('name=');
                search.push(paramCustomer.name);
                search.push('&');
            }
            if (paramCustomer.mobile) {
                search.push('mobile=');
                search.push(paramCustomer.mobile);
                search.push('&');
            }
            if (paramCustomer.phone) {
                search.push('phone=');
                search.push(paramCustomer.phone);
                search.push('&');
            }
            if (paramCustomer.identityNumber) {
                search.push('identityNumber=');
                search.push(paramCustomer.identityNumber);
                search.push('&');
            }
            if (paramCustomer.qualification) {
                search.push('qualification=');
                search.push(paramCustomer.qualification);
                search.push('&');
            }
            if (paramCustomer.enabled) {
                search.push('enabled=');
                search.push(paramCustomer.enabled);
                search.push('&');
            }

            CustomerService.filter(search.join("")).then(function (data) {
                $scope.customers = data.content;

                $scope.pageCustomer.currentPage = $scope.pageCustomer.page + 1;
                $scope.pageCustomer.first = data.first;
                $scope.pageCustomer.last = data.last;
                $scope.pageCustomer.number = data.number;
                $scope.pageCustomer.numberOfElements = data.numberOfElements;
                $scope.pageCustomer.size = data.size;
                $scope.pageCustomer.totalElements = data.totalElements;
                $scope.pageCustomer.totalPages = data.totalPages;
                $scope.pageCustomer.currentPageString = ($scope.pageCustomer.page + 1) + ' / ' + $scope.pageCustomer.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextCustomersPage = function () {
            $scope.pageCustomer.page++;
            $scope.searchCustomers($scope.paramCustomer);
        };
        $scope.selectPrevCustomersPage = function () {
            $scope.pageCustomer.page--;
            $scope.searchCustomers($scope.paramCustomer);
        };
        $scope.checkAllCustomers = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('#checkAllCustomers input').is(":checked")) {
                    element.MaterialCheckbox.check();
                }
                else {
                    element.MaterialCheckbox.uncheck();
                }
            }
            angular.forEach($scope.customers, function (customer) {
                customer.isSelected = $scope.customers.checkAll;
            });
        };
        $scope.checkCustomer = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('.check input:checked').length == $('.check input').length) {
                    document.querySelector('#checkAllCustomers').MaterialCheckbox.check();
                } else {
                    document.querySelector('#checkAllCustomers').MaterialCheckbox.uncheck();
                }
            }
        };
        $scope.newCustomer = function () {
            ModalProvider.openCustomerCreateModel().result.then(function (data) {
                $scope.customers.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.newCustomerBatch = function () {
            ModalProvider.openCustomerCreateBatchModel().result.then(function (data) {
                Array.prototype.push.apply($scope.customers, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deleteCustomer = function (customer) {
            ModalProvider.openConfirmModel("العملاء", "delete", "هل تود حذف العميل فعلاً؟").result.then(function (value) {
                if (value) {
                    CustomerService.remove(customer.id).then(function () {
                        var index = $scope.customers.indexOf(customer);
                        $scope.customers.splice(index, 1);
                    });
                }
            });
        };
        $scope.printCustomerStatement = function (customer) {
            window.open('/report/customerStatement/' + customer.id + '?exportType=PDF');
        };
        $scope.rowMenuCustomer = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newCustomer();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openCustomerUpdateModel($itemScope.customer);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteCustomer($itemScope.customer);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/about.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>التفاصيل...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openCustomerDetailsModel($itemScope.customer);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/print.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>كشف حساب...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.printCustomerStatement($itemScope.customer);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/send.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>إرسال رسالة...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SMS_SEND']);
                },
                click: function ($itemScope, $event, value) {
                    var customers = [];
                    angular.forEach($scope.customers, function (customer) {
                        if (customer.isSelected) {
                            customers.push(customer);
                        }
                    });
                    if (customers.length === 0) {
                        ModalProvider.openConfirmModel('إرسال الرسائل', 'send', 'فضلا قم باختيار عميل واحد على الأقل');
                        return;
                    }
                    ModalProvider.openCustomerSendMessageModel(customers);
                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * Supplier                                                                                                     *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.suppliers = [];
        $scope.paramSupplier = {};
        $scope.suppliers.checkAll = false;

        $scope.pageSupplier = {};
        $scope.pageSupplier.sorts = [];
        $scope.pageSupplier.page = 0;
        $scope.pageSupplier.totalPages = 0;
        $scope.pageSupplier.currentPage = $scope.pageSupplier.page + 1;
        $scope.pageSupplier.currentPageString = ($scope.pageSupplier.page + 1) + ' / ' + $scope.pageSupplier.totalPages;
        $scope.pageSupplier.size = 25;
        $scope.pageSupplier.first = true;
        $scope.pageSupplier.last = true;

        $scope.openSuppliersFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/supplier/supplierFilter.html',
                controller: 'supplierFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramSupplier) {
                $scope.searchSuppliers(paramSupplier);
            }, function () {
            });
        };
        $scope.searchSuppliers = function (paramSupplier) {
            var search = [];
            search.push('size=');
            search.push($scope.pageSupplier.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageSupplier.page);
            search.push('&');
            angular.forEach($scope.pageSupplier.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if (paramSupplier.codeFrom) {
                search.push('codeFrom=');
                search.push(paramSupplier.codeFrom);
                search.push('&');
            }
            if (paramSupplier.codeTo) {
                search.push('codeTo=');
                search.push(paramSupplier.codeTo);
                search.push('&');
            }
            if (paramSupplier.registerDateTo) {
                search.push('registerDateTo=');
                search.push(paramSupplier.registerDateTo.getTime());
                search.push('&');
            }
            if (paramSupplier.registerDateFrom) {
                search.push('registerDateFrom=');
                search.push(paramSupplier.registerDateFrom.getTime());
                search.push('&');
            }
            if (paramSupplier.name) {
                search.push('name=');
                search.push(paramSupplier.name);
                search.push('&');
            }
            if (paramSupplier.mobile) {
                search.push('mobile=');
                search.push(paramSupplier.mobile);
                search.push('&');
            }
            if (paramSupplier.phone) {
                search.push('phone=');
                search.push(paramSupplier.phone);
                search.push('&');
            }
            if (paramSupplier.identityNumber) {
                search.push('identityNumber=');
                search.push(paramSupplier.identityNumber);
                search.push('&');
            }
            if (paramSupplier.qualification) {
                search.push('qualification=');
                search.push(paramSupplier.qualification);
                search.push('&');
            }
            if (paramSupplier.enabled) {
                search.push('enabled=');
                search.push(paramSupplier.enabled);
                search.push('&');
            }

            SupplierService.filter(search.join("")).then(function (data) {
                $scope.suppliers = data.content;

                $scope.pageSupplier.currentPage = $scope.pageSupplier.page + 1;
                $scope.pageSupplier.first = data.first;
                $scope.pageSupplier.last = data.last;
                $scope.pageSupplier.number = data.number;
                $scope.pageSupplier.numberOfElements = data.numberOfElements;
                $scope.pageSupplier.size = data.size;
                $scope.pageSupplier.totalElements = data.totalElements;
                $scope.pageSupplier.totalPages = data.totalPages;
                $scope.pageSupplier.currentPageString = ($scope.pageSupplier.page + 1) + ' / ' + $scope.pageSupplier.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextSuppliersPage = function () {
            $scope.pageSupplier.page++;
            $scope.searchSuppliers($scope.paramSupplier);
        };
        $scope.selectPrevSuppliersPage = function () {
            $scope.pageSupplier.page--;
            $scope.searchSuppliers($scope.paramSupplier);
        };
        $scope.checkAllSuppliers = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('#checkAllSuppliers input').is(":checked")) {
                    element.MaterialCheckbox.check();
                }
                else {
                    element.MaterialCheckbox.uncheck();
                }
            }
            angular.forEach($scope.suppliers, function (supplier) {
                supplier.isSelected = $scope.suppliers.checkAll;
            });
        };
        $scope.checkSupplier = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('.check input:checked').length == $('.check input').length) {
                    document.querySelector('#checkAllSuppliers').MaterialCheckbox.check();
                } else {
                    document.querySelector('#checkAllSuppliers').MaterialCheckbox.uncheck();
                }
            }
        };
        $scope.newSupplier = function () {
            ModalProvider.openSupplierCreateModel().result.then(function (data) {
                $scope.suppliers.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.newSupplierBatch = function () {
            ModalProvider.openSupplierCreateBatchModel().result.then(function (data) {
                Array.prototype.push.apply($scope.suppliers, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deleteSupplier = function (supplier) {
            ModalProvider.openConfirmModel("الموردين", "delete", "هل تود حذف المورد فعلاً؟").result.then(function (value) {
                if (value) {
                    SupplierService.remove(supplier.id).then(function () {
                        var index = $scope.suppliers.indexOf(supplier);
                        $scope.suppliers.splice(index, 1);
                    });
                }
            });
        };
        $scope.printSupplierStatement = function (supplier) {
            window.open('/report/supplierStatement/' + supplier.id + '?exportType=PDF');
        };
        $scope.rowMenuSupplier = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newSupplier();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openSupplierUpdateModel($itemScope.supplier);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteSupplier($itemScope.supplier);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/supplierPayment.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>دفعة مالية...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_PAYMENT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newSupplierPayment($itemScope.supplier);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/about.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>التفاصيل...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openSupplierDetailsModel($itemScope.supplier);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/print.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>كشف حساب...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.printSupplierStatement($itemScope.supplier);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/send.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>إرسال رسالة...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SMS_SEND']);
                },
                click: function ($itemScope, $event, value) {
                    var suppliers = [];
                    angular.forEach($scope.suppliers, function (supplier) {
                        if (supplier.isSelected) {
                            suppliers.push(supplier);
                        }
                    });
                    if (suppliers.length === 0) {
                        ModalProvider.openConfirmModel('إرسال الرسائل', 'send', 'فضلا قم باختيار مورد واحد على الأقل');
                        return;
                    }
                    ModalProvider.openCustomerSendMessageModel(suppliers);
                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * Product                                                                                                    *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.products = [];
        $scope.parents = [];
        $scope.paramProduct = {};
        $scope.products.checkAll = false;

        $scope.pageProduct = {};
        $scope.pageProduct.sorts = [];
        $scope.pageProduct.page = 0;
        $scope.pageProduct.totalPages = 0;
        $scope.pageProduct.currentPage = $scope.pageProduct.page + 1;
        $scope.pageProduct.currentPageString = ($scope.pageProduct.page + 1) + ' / ' + $scope.pageProduct.totalPages;
        $scope.pageProduct.size = 25;
        $scope.pageProduct.first = true;
        $scope.pageProduct.last = true;

        $scope.openProductsFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/product/productFilter.html',
                controller: 'productFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramProduct) {
                $scope.paramProduct = paramProduct;
                $scope.searchProducts(paramProduct);
            }, function () {
            });
        };
        $scope.searchProducts = function (paramProduct) {
            var search = [];
            search.push('size=');
            search.push($scope.pageProduct.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageProduct.page);
            search.push('&');
            angular.forEach($scope.pageProduct.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if ($scope.pageProduct.sorts.length === 0) {
                search.push('sort=registerDate,desc&');
            }
            //Product Filters
            if (paramProduct.codeFrom) {
                search.push('codeFrom=');
                search.push(paramProduct.codeFrom);
                search.push('&');
            }
            if (paramProduct.codeTo) {
                search.push('codeTo=');
                search.push(paramProduct.codeTo);
                search.push('&');
            }
            if (paramProduct.registerDateFromTo) {
                search.push('registerDateFromTo=');
                search.push(paramProduct.registerDateFromTo.getTime());
                search.push('&');
            }
            if (paramProduct.registerDateFromFrom) {
                search.push('registerDateFromFrom=');
                search.push(paramProduct.registerDateFromFrom.getTime());
                search.push('&');
            }
            if (paramProduct.name) {
                search.push('name=');
                search.push(paramProduct.name);
                search.push('&');
            }
            if (paramProduct.parent) {
                search.push('parentId=');
                search.push(paramProduct.parent.id);
                search.push('&');
            }

            ProductService.filter(search.join("")).then(function (data) {
                $scope.products = data.content;

                $scope.pageProduct.currentPage = $scope.pageProduct.page + 1;
                $scope.pageProduct.first = data.first;
                $scope.pageProduct.last = data.last;
                $scope.pageProduct.number = data.number;
                $scope.pageProduct.numberOfElements = data.numberOfElements;
                $scope.pageProduct.size = data.size;
                $scope.pageProduct.totalElements = data.totalElements;
                $scope.pageProduct.totalPages = data.totalPages;
                $scope.pageProduct.currentPageString = ($scope.pageProduct.page + 1) + ' / ' + $scope.pageProduct.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextProductsPage = function () {
            $scope.pageProduct.page++;
            $scope.searchProducts($scope.paramProduct);
        };
        $scope.selectPrevProductsPage = function () {
            $scope.pageProduct.page--;
            $scope.searchProducts($scope.paramProduct);
        };
        $scope.fetchParents = function () {
            ProductService.findParents().then(function (value) {
                $scope.parents = value;
            });
        };
        $scope.newProduct = function () {
            ModalProvider.openProductCreateModel().result.then(function (data) {
                $scope.products.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.newParent = function () {
            ModalProvider.openParentCreateModel().result.then(function (data) {
                $scope.parents.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.newProductBatch = function () {
            ModalProvider.openProductCreateBatchModel().result.then(function (data) {
                Array.prototype.push.apply($scope.products, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.rowMenuProduct = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تصنيف رئيسي جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newParent();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تصنيف فرعي جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newProduct();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل التصنيف الرئيسي...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openParentUpdateModel($itemScope.product.parent);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل التصنيف الفرعي...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openProductUpdateModel($itemScope.product);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف التصنيف الرئيسي...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_DELETE']);
                },
                click: function ($itemScope, $event, value) {

                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف التصنيف الفرعي...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_DELETE']);
                },
                click: function ($itemScope, $event, value) {

                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/about.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>التفاصيل...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openProductDetailsModel($itemScope.product);
                }
            }
        ];
        $scope.rowMenuParent = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newParent();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openParentUpdateModel($itemScope.parent);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_DELETE']);
                },
                click: function ($itemScope, $event, value) {

                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * Offer                                                                                                      *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.offers = [];
        $scope.paramOffer = {};
        $scope.offers.checkAll = false;

        $scope.pageOffer = {};
        $scope.pageOffer.sorts = [];
        $scope.pageOffer.page = 0;
        $scope.pageOffer.totalPages = 0;
        $scope.pageOffer.currentPage = $scope.pageOffer.page + 1;
        $scope.pageOffer.currentPageString = ($scope.pageOffer.page + 1) + ' / ' + $scope.pageOffer.totalPages;
        $scope.pageOffer.size = 25;
        $scope.pageOffer.first = true;
        $scope.pageOffer.last = true;

        $scope.openOffersFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/offer/offerFilter.html',
                controller: 'offerFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramOffer) {
                $scope.searchOffers(paramOffer);
            }, function () {
            });
        };
        $scope.searchOffers = function (paramOffer) {
            var search = [];
            search.push('size=');
            search.push($scope.pageOffer.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageOffer.page);
            search.push('&');
            angular.forEach($scope.pageOffer.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if ($scope.pageOffer.sorts.length === 0) {
                search.push('sort=writtenDate,desc&');
            }
            //Offer Filters
            if (paramOffer.codeFrom) {
                search.push('codeFrom=');
                search.push(paramOffer.codeFrom);
                search.push('&');
            }
            if (paramOffer.codeTo) {
                search.push('codeTo=');
                search.push(paramOffer.codeTo);
                search.push('&');
            }
            if (paramOffer.writtenDateTo) {
                search.push('writtenDateTo=');
                search.push(paramOffer.writtenDateTo.getTime());
                search.push('&');
            }
            if (paramOffer.writtenDateFrom) {
                search.push('writtenDateFrom=');
                search.push(paramOffer.writtenDateFrom.getTime());
                search.push('&');
            }
            //Customer Filters
            if (paramOffer.customerCodeFrom) {
                search.push('customerCodeFrom=');
                search.push(paramOffer.customerCodeFrom);
                search.push('&');
            }
            if (paramOffer.customerCodeTo) {
                search.push('customerCodeTo=');
                search.push(paramOffer.customerCodeTo);
                search.push('&');
            }
            if (paramOffer.customerRegisterDateTo) {
                search.push('customerRegisterDateTo=');
                search.push(paramOffer.customerRegisterDateTo.getTime());
                search.push('&');
            }
            if (paramOffer.customerRegisterDateFrom) {
                search.push('customerRegisterDateFrom=');
                search.push(paramOffer.customerRegisterDateFrom.getTime());
                search.push('&');
            }
            if (paramOffer.customerName) {
                search.push('customerName=');
                search.push(paramOffer.customerName.getTime());
                search.push('&');
            }
            if (paramOffer.customerMobile) {
                search.push('customerMobile=');
                search.push(paramOffer.customerMobile.getTime());
                search.push('&');
            }
            OfferService.filter(search.join("")).then(function (data) {
                $scope.offers = data.content;

                $scope.pageOffer.currentPage = $scope.pageOffer.page + 1;
                $scope.pageOffer.first = data.first;
                $scope.pageOffer.last = data.last;
                $scope.pageOffer.number = data.number;
                $scope.pageOffer.numberOfElements = data.numberOfElements;
                $scope.pageOffer.size = data.size;
                $scope.pageOffer.totalElements = data.totalElements;
                $scope.pageOffer.totalPages = data.totalPages;
                $scope.pageOffer.currentPageString = ($scope.pageOffer.page + 1) + ' / ' + $scope.pageOffer.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextOffersPage = function () {
            $scope.pageOffer.page++;
            $scope.searchOffers($scope.paramOffer);
        };
        $scope.selectPrevOffersPage = function () {
            $scope.pageOffer.page--;
            $scope.searchOffers($scope.paramOffer);
        };
        $scope.checkAllOffers = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('#checkAllOffers input').is(":checked")) {
                    element.MaterialCheckbox.check();
                }
                else {
                    element.MaterialCheckbox.uncheck();
                }
            }
            angular.forEach($scope.offers, function (offer) {
                offer.isSelected = $scope.offers.checkAll;
            });
        };
        $scope.checkOffer = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('.check input:checked').length == $('.check input').length) {
                    document.querySelector('#checkAllOffers').MaterialCheckbox.check();
                } else {
                    document.querySelector('#checkAllOffers').MaterialCheckbox.uncheck();
                }
            }
        };
        $scope.newOffer = function () {
            ModalProvider.openOfferCreateModel().result.then(function (data) {
                $scope.offers.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deleteOffer = function (offer) {
            ModalProvider.openConfirmModel("العروض", "delete", "هل تود حذف العرض فعلاً؟").result.then(function (value) {
                if (value) {
                    OfferService.remove(offer.id).then(function () {
                        var index = $scope.offers.indexOf(offer);
                        $scope.offers.splice(index, 1);
                    });
                }
            });
        };
        $scope.sendOffer = function (offer) {
            ModalProvider.openConfirmModel("العروض", "send", "هل تود إرسال العرض إلى العميل؟").result.then(function (value) {
                if (value) {
                    OfferService.send(offer.id);
                }
            });
        };
        $scope.convertOfferToBill = function (offer) {
            ModalProvider.openConfirmModel("العروض", "send", "هل تود الموافقة على العرض وتحويله إلى فاتورة بيع للعميل؟").result.then(function (value) {
                if (value) {
                    BillSellService.createFromOffer(offer).then(function (value) {
                        return $scope.billSells.splice(0, 0, value);
                    });
                }
            });
        };
        $scope.newOfferProduct = function (offer) {
            ModalProvider.openOfferProductCreateModel(offer).result.then(function (data) {
                if (!offer.offerProducts) {
                    offer.offerProducts = [];
                }
                return offer.offerProducts.splice(0, 0, data);
            });
        };
        $scope.printOffer = function (offer) {
            window.open('/report/offer?offerId=' + offer.id);
        };
        $scope.findDailyOffers = function () {
            OfferService.findDaily().then(function (data) {
                $scope.widgetOffers = data;
            });
        };
        $scope.rowMenuOffer = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_OFFER_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newOffer();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_OFFER_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openOfferUpdateModel($itemScope.offer);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_OFFER_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteOffer($itemScope.offer);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/product.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>اضافة سلعة...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_OFFER_PRODUCT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newOfferProduct($itemScope.offer);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/about.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>التفاصيل...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openOfferDetailsModel($itemScope.offer);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/print.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>طباعة...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.printOffer($itemScope.offer);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/send.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>إرسال...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.sendOffer($itemScope.offer);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/billSell.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تحويل لفاتورة بيع...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.convertOfferToBill($itemScope.offer);
                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * Bank                                                                                                       *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.fetchAllBanks = function () {
            BankService.findAll().then(function (value) {
                $rootScope.banks = value;
            });
        };
        $scope.newBank = function () {
            ModalProvider.openBankCreateModel().result.then(function (data) {
                $rootScope.banks.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.rowMenuBank = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BANK_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newBank();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BANK_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openBankUpdateModel($itemScope.bank);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BANK_DELETE']);
                },
                click: function ($itemScope, $event, value) {

                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * OrderPurchase                                                                                                  *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.orderPurchases = [];
        $scope.paramOrderPurchase = {};
        $scope.orderPurchases.checkAll = false;

        $scope.pageOrderPurchase = {};
        $scope.pageOrderPurchase.sorts = [];
        $scope.pageOrderPurchase.page = 0;
        $scope.pageOrderPurchase.totalPages = 0;
        $scope.pageOrderPurchase.currentPage = $scope.pageOrderPurchase.page + 1;
        $scope.pageOrderPurchase.currentPageString = ($scope.pageOrderPurchase.page + 1) + ' / ' + $scope.pageOrderPurchase.totalPages;
        $scope.pageOrderPurchase.size = 25;
        $scope.pageOrderPurchase.first = true;
        $scope.pageOrderPurchase.last = true;

        $scope.openOrderPurchasesFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/orderPurchase/orderPurchaseFilter.html',
                controller: 'orderPurchaseFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramOrderPurchase) {
                $scope.searchOrderPurchases(paramOrderPurchase);
            }, function () {
            });
        };
        $scope.searchOrderPurchases = function (paramOrderPurchase) {
            var search = [];
            search.push('size=');
            search.push($scope.pageOrderPurchase.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageOrderPurchase.page);
            search.push('&');
            angular.forEach($scope.pageOrderPurchase.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if ($scope.pageOrderPurchase.sorts.length === 0) {
                search.push('sort=writtenDate,desc&');
            }
            //OrderPurchase Filters
            if (paramOrderPurchase.codeFrom) {
                search.push('codeFrom=');
                search.push(paramOrderPurchase.codeFrom);
                search.push('&');
            }
            if (paramOrderPurchase.codeTo) {
                search.push('codeTo=');
                search.push(paramOrderPurchase.codeTo);
                search.push('&');
            }
            if (paramOrderPurchase.writtenDateTo) {
                search.push('writtenDateTo=');
                search.push(paramOrderPurchase.writtenDateTo.getTime());
                search.push('&');
            }
            if (paramOrderPurchase.writtenDateFrom) {
                search.push('writtenDateFrom=');
                search.push(paramOrderPurchase.writtenDateFrom.getTime());
                search.push('&');
            }
            //Supplier Filters
            if (paramOrderPurchase.supplierCodeFrom) {
                search.push('supplierCodeFrom=');
                search.push(paramOrderPurchase.supplierCodeFrom);
                search.push('&');
            }
            if (paramOrderPurchase.supplierCodeTo) {
                search.push('supplierCodeTo=');
                search.push(paramOrderPurchase.supplierCodeTo);
                search.push('&');
            }
            if (paramOrderPurchase.supplierRegisterDateTo) {
                search.push('supplierRegisterDateTo=');
                search.push(paramOrderPurchase.supplierRegisterDateTo.getTime());
                search.push('&');
            }
            if (paramOrderPurchase.supplierRegisterDateFrom) {
                search.push('supplierRegisterDateFrom=');
                search.push(paramOrderPurchase.supplierRegisterDateFrom.getTime());
                search.push('&');
            }
            if (paramOrderPurchase.supplierName) {
                search.push('supplierName=');
                search.push(paramOrderPurchase.supplierName.getTime());
                search.push('&');
            }
            if (paramOrderPurchase.supplierMobile) {
                search.push('supplierMobile=');
                search.push(paramOrderPurchase.supplierMobile.getTime());
                search.push('&');
            }
            OrderPurchaseService.filter(search.join("")).then(function (data) {
                $scope.orderPurchases = data.content;

                $scope.pageOrderPurchase.currentPage = $scope.pageOrderPurchase.page + 1;
                $scope.pageOrderPurchase.first = data.first;
                $scope.pageOrderPurchase.last = data.last;
                $scope.pageOrderPurchase.number = data.number;
                $scope.pageOrderPurchase.numberOfElements = data.numberOfElements;
                $scope.pageOrderPurchase.size = data.size;
                $scope.pageOrderPurchase.totalElements = data.totalElements;
                $scope.pageOrderPurchase.totalPages = data.totalPages;
                $scope.pageOrderPurchase.currentPageString = ($scope.pageOrderPurchase.page + 1) + ' / ' + $scope.pageOrderPurchase.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextOrderPurchasesPage = function () {
            $scope.pageOrderPurchase.page++;
            $scope.searchOrderPurchases($scope.paramOrderPurchase);
        };
        $scope.selectPrevOrderPurchasesPage = function () {
            $scope.pageOrderPurchase.page--;
            $scope.searchOrderPurchases($scope.paramOrderPurchase);
        };
        $scope.checkAllOrderPurchases = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('#checkAllOrderPurchases input').is(":checked")) {
                    element.MaterialCheckbox.check();
                }
                else {
                    element.MaterialCheckbox.uncheck();
                }
            }
            angular.forEach($scope.orderPurchases, function (orderPurchase) {
                orderPurchase.isSelected = $scope.orderPurchases.checkAll;
            });
        };
        $scope.checkOrderPurchase = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('.check input:checked').length == $('.check input').length) {
                    document.querySelector('#checkAllOrderPurchases').MaterialCheckbox.check();
                } else {
                    document.querySelector('#checkAllOrderPurchases').MaterialCheckbox.uncheck();
                }
            }
        };
        $scope.newOrderPurchase = function () {
            ModalProvider.openOrderPurchaseCreateModel().result.then(function (data) {
                $scope.orderPurchases.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deleteOrderPurchase = function (orderPurchase) {
            ModalProvider.openConfirmModel("أوامر الشراء", "delete", "هل تود حذف أمر الشراء فعلاً؟").result.then(function (value) {
                if (value) {
                    OrderPurchaseService.remove(orderPurchase.id).then(function () {
                        var index = $scope.orderPurchases.indexOf(orderPurchase);
                        $scope.orderPurchases.splice(index, 1);
                    });
                }
            });
        };
        $scope.newOrderPurchaseProduct = function (orderPurchase) {
            ModalProvider.openOrderPurchaseProductCreateModel(orderPurchase).result.then(function (data) {
                if (!orderPurchase.orderPurchaseProducts) {
                    orderPurchase.orderPurchaseProducts = [];
                }
                return orderPurchase.orderPurchaseProducts.splice(0, 0, data);
            });
        };
        $scope.printOrderPurchase = function (orderPurchase) {
            window.open('/report/orderPurchase?orderPurchaseId=' + orderPurchase.id);
        };
        $scope.sendOrderPurchase = function (orderPurchase) {
            ModalProvider.openConfirmModel("أوامر الشراء", "send", "هل تود إرسال أمر الشراء إلى المورد؟").result.then(function (value) {
                if (value) {
                    OrderPurchaseService.send(orderPurchase.id);
                }
            });
        };
        $scope.convertOrderPurchaseToBillPurchase = function (orderPurchase) {
            ModalProvider.openConfirmModel("أوامر الشراء", "send", "هل تود تحويل أمر الشراء إلى فاتورة شراء؟").result.then(function (value) {
                if (value) {
                    BillPurchaseService.createFromOrder(orderPurchase).then(function (value) {
                        return $scope.billPurchases.splice(0, 0, value);
                    });
                }
            });
        };
        $scope.rowMenuOrderPurchase = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_PURCHASE_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newOrderPurchase();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_PURCHASE_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openOrderPurchaseUpdateModel($itemScope.orderPurchase);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_PURCHASE_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteOrderPurchase($itemScope.orderPurchase);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/product.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>اضافة سلعة...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_PURCHASE_PRODUCT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newOrderPurchaseProduct($itemScope.orderPurchase);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/about.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>التفاصيل...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openOrderPurchaseDetailsModel($itemScope.orderPurchase);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/print.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>طباعة...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.printOrderPurchase($itemScope.orderPurchase);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/send.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>إرسال...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.sendOrderPurchase($itemScope.orderPurchase);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/billPurchase.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تحويل لفاتورة شراء...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_PURCHASE_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.convertOrderPurchaseToBillPurchase($itemScope.orderPurchase);
                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * BillPurchase                                                                                               *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.billPurchases = [];
        $scope.paramBillPurchase = {};
        $scope.billPurchases.checkAll = false;

        $scope.pageBillPurchase = {};
        $scope.pageBillPurchase.sorts = [];
        $scope.pageBillPurchase.page = 0;
        $scope.pageBillPurchase.totalPages = 0;
        $scope.pageBillPurchase.currentPage = $scope.pageBillPurchase.page + 1;
        $scope.pageBillPurchase.currentPageString = ($scope.pageBillPurchase.page + 1) + ' / ' + $scope.pageBillPurchase.totalPages;
        $scope.pageBillPurchase.size = 25;
        $scope.pageBillPurchase.first = true;
        $scope.pageBillPurchase.last = true;

        $scope.openBillPurchasesFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/billPurchase/billPurchaseFilter.html',
                controller: 'billPurchaseFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramBillPurchase) {
                $scope.searchBillPurchases(paramBillPurchase);
            }, function () {
            });
        };
        $scope.searchBillPurchases = function (paramBillPurchase) {
            var search = [];
            search.push('size=');
            search.push($scope.pageBillPurchase.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageBillPurchase.page);
            search.push('&');
            angular.forEach($scope.pageBillPurchase.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if ($scope.pageBillPurchase.sorts.length === 0) {
                search.push('sort=writtenDate,desc&');
            }
            //BillPurchase Filters
            if (paramBillPurchase.codeFrom) {
                search.push('codeFrom=');
                search.push(paramBillPurchase.codeFrom);
                search.push('&');
            }
            if (paramBillPurchase.codeTo) {
                search.push('codeTo=');
                search.push(paramBillPurchase.codeTo);
                search.push('&');
            }
            if (paramBillPurchase.writtenDateTo) {
                search.push('writtenDateTo=');
                search.push(paramBillPurchase.writtenDateTo.getTime());
                search.push('&');
            }
            if (paramBillPurchase.writtenDateFrom) {
                search.push('writtenDateFrom=');
                search.push(paramBillPurchase.writtenDateFrom.getTime());
                search.push('&');
            }
            //Supplier Filters
            if (paramBillPurchase.supplierCodeFrom) {
                search.push('supplierCodeFrom=');
                search.push(paramBillPurchase.supplierCodeFrom);
                search.push('&');
            }
            if (paramBillPurchase.supplierCodeTo) {
                search.push('supplierCodeTo=');
                search.push(paramBillPurchase.supplierCodeTo);
                search.push('&');
            }
            if (paramBillPurchase.supplierRegisterDateTo) {
                search.push('supplierRegisterDateTo=');
                search.push(paramBillPurchase.supplierRegisterDateTo.getTime());
                search.push('&');
            }
            if (paramBillPurchase.supplierRegisterDateFrom) {
                search.push('supplierRegisterDateFrom=');
                search.push(paramBillPurchase.supplierRegisterDateFrom.getTime());
                search.push('&');
            }
            if (paramBillPurchase.supplierName) {
                search.push('supplierName=');
                search.push(paramBillPurchase.supplierName.getTime());
                search.push('&');
            }
            if (paramBillPurchase.supplierMobile) {
                search.push('supplierMobile=');
                search.push(paramBillPurchase.supplierMobile.getTime());
                search.push('&');
            }
            BillPurchaseService.filter(search.join("")).then(function (data) {
                $scope.billPurchases = data.content;

                $scope.pageBillPurchase.currentPage = $scope.pageBillPurchase.page + 1;
                $scope.pageBillPurchase.first = data.first;
                $scope.pageBillPurchase.last = data.last;
                $scope.pageBillPurchase.number = data.number;
                $scope.pageBillPurchase.numberOfElements = data.numberOfElements;
                $scope.pageBillPurchase.size = data.size;
                $scope.pageBillPurchase.totalElements = data.totalElements;
                $scope.pageBillPurchase.totalPages = data.totalPages;
                $scope.pageBillPurchase.currentPageString = ($scope.pageBillPurchase.page + 1) + ' / ' + $scope.pageBillPurchase.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextBillPurchasesPage = function () {
            $scope.pageBillPurchase.page++;
            $scope.searchBillPurchases($scope.paramBillPurchase);
        };
        $scope.selectPrevBillPurchasesPage = function () {
            $scope.pageBillPurchase.page--;
            $scope.searchBillPurchases($scope.paramBillPurchase);
        };
        $scope.checkAllBillPurchases = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('#checkAllBillPurchases input').is(":checked")) {
                    element.MaterialCheckbox.check();
                }
                else {
                    element.MaterialCheckbox.uncheck();
                }
            }
            angular.forEach($scope.billPurchases, function (billPurchase) {
                billPurchase.isSelected = $scope.billPurchases.checkAll;
            });
        };
        $scope.checkBillPurchase = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('.check input:checked').length == $('.check input').length) {
                    document.querySelector('#checkAllBillPurchases').MaterialCheckbox.check();
                } else {
                    document.querySelector('#checkAllBillPurchases').MaterialCheckbox.uncheck();
                }
            }
        };
        $scope.newBillPurchase = function () {
            ModalProvider.openBillPurchaseCreateModel().result.then(function (data) {
                $scope.billPurchases.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deleteBillPurchase = function (billPurchase) {
            ModalProvider.openConfirmModel("فواتير الشراء", "delete", "هل تود حذف الفاتورة فعلاً؟").result.then(function (value) {
                if (value) {
                    BillPurchaseService.remove(billPurchase.id).then(function () {
                        var index = $scope.billPurchases.indexOf(billPurchase);
                        $scope.billPurchases.splice(index, 1);
                    });
                }
            });
        };
        $scope.newBillPurchaseProduct = function (billPurchase) {
            ModalProvider.openBillPurchaseProductCreateModel(billPurchase).result.then(function (data) {
                if (!billPurchase.billPurchaseProducts) {
                    billPurchase.billPurchaseProducts = [];
                }
                return billPurchase.billPurchaseProducts.splice(0, 0, data);
            });
        };
        $scope.rowMenuBillPurchase = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_PURCHASE_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newBillPurchase();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_PURCHASE_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteBillPurchase($itemScope.billPurchase);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/product.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>اضافة سلعة...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_PURCHASE_PRODUCT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newBillPurchaseProduct($itemScope.billPurchase);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/bankTransaction.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تسديد دفعة مالية...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_PAYMENT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    SupplierService.findOne($itemScope.billPurchase.supplier.id).then(function (value) {
                        $scope.newSupplierPayment(value);
                    });
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/about.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>التفاصيل...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openBillPurchaseDetailsModel($itemScope.billPurchase);
                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * SupplierPayment                                                                                            *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.supplierPayments = [];
        $scope.paramSupplierPayment = {};
        $scope.supplierPayments.checkAll = false;

        $scope.pageSupplierPayment = {};
        $scope.pageSupplierPayment.sorts = [];
        $scope.pageSupplierPayment.page = 0;
        $scope.pageSupplierPayment.totalPages = 0;
        $scope.pageSupplierPayment.currentPage = $scope.pageSupplierPayment.page + 1;
        $scope.pageSupplierPayment.currentPageString = ($scope.pageSupplierPayment.page + 1) + ' / ' + $scope.pageSupplierPayment.totalPages;
        $scope.pageSupplierPayment.size = 25;
        $scope.pageSupplierPayment.first = true;
        $scope.pageSupplierPayment.last = true;

        $scope.openSupplierPaymentsFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/supplierPayment/supplierPaymentFilter.html',
                controller: 'supplierPaymentFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramSupplierPayment) {
                $scope.searchSupplierPayments(paramSupplierPayment);
            }, function () {
            });
        };
        $scope.searchSupplierPayments = function (paramSupplierPayment) {
            var search = [];
            search.push('size=');
            search.push($scope.pageSupplierPayment.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageSupplierPayment.page);
            search.push('&');
            angular.forEach($scope.pageSupplierPayment.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if ($scope.pageSupplierPayment.sorts.length === 0) {
                search.push('sort=date,desc&');
            }
            if (paramSupplierPayment.dateFrom) {
                search.push('dateFrom=');
                search.push(paramSupplierPayment.dateFrom.getTime());
                search.push('&');
            }
            if (paramSupplierPayment.dateTo) {
                search.push('dateTo=');
                search.push(paramSupplierPayment.dateTo.getTime());
                search.push('&');
            }
            if (paramSupplierPayment.supplierName) {
                search.push('supplierName=');
                search.push(paramSupplierPayment.supplierName);
                search.push('&');
            }
            if (paramSupplierPayment.supplierMobile) {
                search.push('supplierMobile=');
                search.push(paramSupplierPayment.supplierMobile);
                search.push('&');
            }

            search.push('filterCompareType=or');

            SupplierPaymentService.filter(search.join("")).then(function (data) {
                $scope.supplierPayments = data.content;

                $scope.pageSupplierPayment.currentPage = $scope.pageSupplierPayment.page + 1;
                $scope.pageSupplierPayment.first = data.first;
                $scope.pageSupplierPayment.last = data.last;
                $scope.pageSupplierPayment.number = data.number;
                $scope.pageSupplierPayment.numberOfElements = data.numberOfElements;
                $scope.pageSupplierPayment.size = data.size;
                $scope.pageSupplierPayment.totalElements = data.totalElements;
                $scope.pageSupplierPayment.totalPages = data.totalPages;
                $scope.pageSupplierPayment.currentPageString = ($scope.pageSupplierPayment.page + 1) + ' / ' + $scope.pageSupplierPayment.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextSupplierPaymentsPage = function () {
            $scope.pageSupplierPayment.page++;
            $scope.searchSupplierPayments($scope.paramSupplierPayment);
        };
        $scope.selectPrevSupplierPaymentsPage = function () {
            $scope.pageSupplierPayment.page--;
            $scope.searchSupplierPayments($scope.paramSupplierPayment);
        };
        $scope.newSupplierPayment = function (supplier) {
            ModalProvider.openSupplierPaymentCreateModel(supplier).result.then(function (data) {
                $scope.supplierPayments.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deleteSupplierPayment = function (supplierPayment) {
            ModalProvider.openConfirmModel("سندات الصرف", "delete", "هل تود حذف سند الصرف فعلاً؟").result.then(function (value) {
                if (value) {
                    SupplierPaymentService.remove(supplierPayment.id).then(function () {
                        var index = $scope.supplierPayments.indexOf(supplierPayment);
                        $scope.supplierPayments.splice(index, 1);
                    });
                }
            });
        };

        /**************************************************************************************************************
         *                                                                                                            *
         * OrderSell                                                                                                  *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.orderSells = [];
        $scope.paramOrderSell = {};
        $scope.orderSells.checkAll = false;

        $scope.pageOrderSell = {};
        $scope.pageOrderSell.sorts = [];
        $scope.pageOrderSell.page = 0;
        $scope.pageOrderSell.totalPages = 0;
        $scope.pageOrderSell.currentPage = $scope.pageOrderSell.page + 1;
        $scope.pageOrderSell.currentPageString = ($scope.pageOrderSell.page + 1) + ' / ' + $scope.pageOrderSell.totalPages;
        $scope.pageOrderSell.size = 25;
        $scope.pageOrderSell.first = true;
        $scope.pageOrderSell.last = true;

        $scope.openOrderSellsFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/orderSell/orderSellFilter.html',
                controller: 'orderSellFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramOrderSell) {
                $scope.searchOrderSells(paramOrderSell);
            }, function () {
            });
        };
        $scope.searchOrderSells = function (paramOrderSell) {
            var search = [];
            search.push('size=');
            search.push($scope.pageOrderSell.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageOrderSell.page);
            search.push('&');
            angular.forEach($scope.pageOrderSell.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if ($scope.pageOrderSell.sorts.length === 0) {
                search.push('sort=writtenDate,desc&');
            }
            //OrderSell Filters
            if (paramOrderSell.codeFrom) {
                search.push('codeFrom=');
                search.push(paramOrderSell.codeFrom);
                search.push('&');
            }
            if (paramOrderSell.codeTo) {
                search.push('codeTo=');
                search.push(paramOrderSell.codeTo);
                search.push('&');
            }
            if (paramOrderSell.writtenDateTo) {
                search.push('writtenDateTo=');
                search.push(paramOrderSell.writtenDateTo.getTime());
                search.push('&');
            }
            if (paramOrderSell.writtenDateFrom) {
                search.push('writtenDateFrom=');
                search.push(paramOrderSell.writtenDateFrom.getTime());
                search.push('&');
            }
            //Customer Filters
            if (paramOrderSell.customerCodeFrom) {
                search.push('customerCodeFrom=');
                search.push(paramOrderSell.customerCodeFrom);
                search.push('&');
            }
            if (paramOrderSell.customerCodeTo) {
                search.push('customerCodeTo=');
                search.push(paramOrderSell.customerCodeTo);
                search.push('&');
            }
            if (paramOrderSell.customerRegisterDateTo) {
                search.push('customerRegisterDateTo=');
                search.push(paramOrderSell.customerRegisterDateTo.getTime());
                search.push('&');
            }
            if (paramOrderSell.customerRegisterDateFrom) {
                search.push('customerRegisterDateFrom=');
                search.push(paramOrderSell.customerRegisterDateFrom.getTime());
                search.push('&');
            }
            if (paramOrderSell.customerName) {
                search.push('customerName=');
                search.push(paramOrderSell.customerName.getTime());
                search.push('&');
            }
            if (paramOrderSell.customerMobile) {
                search.push('customerMobile=');
                search.push(paramOrderSell.customerMobile.getTime());
                search.push('&');
            }
            OrderSellService.filter(search.join("")).then(function (data) {
                $scope.orderSells = data.content;

                $scope.pageOrderSell.currentPage = $scope.pageOrderSell.page + 1;
                $scope.pageOrderSell.first = data.first;
                $scope.pageOrderSell.last = data.last;
                $scope.pageOrderSell.number = data.number;
                $scope.pageOrderSell.numberOfElements = data.numberOfElements;
                $scope.pageOrderSell.size = data.size;
                $scope.pageOrderSell.totalElements = data.totalElements;
                $scope.pageOrderSell.totalPages = data.totalPages;
                $scope.pageOrderSell.currentPageString = ($scope.pageOrderSell.page + 1) + ' / ' + $scope.pageOrderSell.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextOrderSellsPage = function () {
            $scope.pageOrderSell.page++;
            $scope.searchOrderSells($scope.paramOrderSell);
        };
        $scope.selectPrevOrderSellsPage = function () {
            $scope.pageOrderSell.page--;
            $scope.searchOrderSells($scope.paramOrderSell);
        };
        $scope.checkAllOrderSells = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('#checkAllOrderSells input').is(":checked")) {
                    element.MaterialCheckbox.check();
                }
                else {
                    element.MaterialCheckbox.uncheck();
                }
            }
            angular.forEach($scope.orderSells, function (orderSell) {
                orderSell.isSelected = $scope.orderSells.checkAll;
            });
        };
        $scope.checkOrderSell = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('.check input:checked').length == $('.check input').length) {
                    document.querySelector('#checkAllOrderSells').MaterialCheckbox.check();
                } else {
                    document.querySelector('#checkAllOrderSells').MaterialCheckbox.uncheck();
                }
            }
        };
        $scope.newOrderSell = function () {
            ModalProvider.openOrderSellCreateModel().result.then(function (data) {
                $scope.orderSells.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deleteOrderSell = function (orderSell) {
            ModalProvider.openConfirmModel("أوامر البيع", "delete", "هل تود حذف أمر البيع فعلاً؟").result.then(function (value) {
                if (value) {
                    OrderSellService.remove(orderSell.id).then(function () {
                        var index = $scope.orderSells.indexOf(orderSell);
                        $scope.orderSells.splice(index, 1);
                    });
                }
            });
        };
        $scope.newOrderSellProduct = function (orderSell) {
            ModalProvider.openOrderSellProductCreateModel(orderSell).result.then(function (data) {
                if (!orderSell.orderSellProducts) {
                    orderSell.orderSellProducts = [];
                }
                return orderSell.orderSellProducts.splice(0, 0, data);
            });
        };
        $scope.convertOrderSellToBillSell = function (orderSell) {
            ModalProvider.openConfirmModel("أوامر البيع", "send", "هل تود تحويل أمر البيع إلى فاتورة بيع؟").result.then(function (value) {
                if (value) {
                    BillSellService.createFromOrder(orderSell).then(function (value) {
                        return $scope.billSells.splice(0, 0, value);
                    });
                }
            });
        };
        $scope.rowMenuOrderSell = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_SELL_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newOrderSell();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_SELL_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openOrderSellUpdateModel($itemScope.orderSell);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_SELL_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteOrderSell($itemScope.orderSell);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/product.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>اضافة سلعة...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_SELL_PRODUCT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newOrderSellProduct($itemScope.orderSell);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/about.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>التفاصيل...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openOrderSellDetailsModel($itemScope.orderSell);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/print.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>طباعة...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openOrderSellPrintModel($itemScope.orderSell);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/billSell.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تحويل لفاتورة بيع...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.convertOrderSellToBillSell($itemScope.orderSell);
                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * BillSell                                                                                               *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.billSells = [];
        $scope.paramBillSell = {};
        $scope.billSells.checkAll = false;

        $scope.pageBillSell = {};
        $scope.pageBillSell.sorts = [];
        $scope.pageBillSell.page = 0;
        $scope.pageBillSell.totalPages = 0;
        $scope.pageBillSell.currentPage = $scope.pageBillSell.page + 1;
        $scope.pageBillSell.currentPageString = ($scope.pageBillSell.page + 1) + ' / ' + $scope.pageBillSell.totalPages;
        $scope.pageBillSell.size = 25;
        $scope.pageBillSell.first = true;
        $scope.pageBillSell.last = true;

        $scope.openBillSellsFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/billSell/billSellFilter.html',
                controller: 'billSellFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramBillSell) {
                $scope.searchBillSells(paramBillSell);
            }, function () {
            });
        };
        $scope.searchBillSells = function (paramBillSell) {
            var search = [];
            search.push('size=');
            search.push($scope.pageBillSell.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageBillSell.page);
            search.push('&');
            angular.forEach($scope.pageBillSell.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if ($scope.pageBillSell.sorts.length === 0) {
                search.push('sort=writtenDate,desc&');
            }
            //BillSell Filters
            if (paramBillSell.codeFrom) {
                search.push('codeFrom=');
                search.push(paramBillSell.codeFrom);
                search.push('&');
            }
            if (paramBillSell.codeTo) {
                search.push('codeTo=');
                search.push(paramBillSell.codeTo);
                search.push('&');
            }
            if (paramBillSell.writtenDateTo) {
                search.push('writtenDateTo=');
                search.push(paramBillSell.writtenDateTo.getTime());
                search.push('&');
            }
            if (paramBillSell.writtenDateFrom) {
                search.push('writtenDateFrom=');
                search.push(paramBillSell.writtenDateFrom.getTime());
                search.push('&');
            }
            //Customer Filters
            if (paramBillSell.customerCodeFrom) {
                search.push('customerCodeFrom=');
                search.push(paramBillSell.customerCodeFrom);
                search.push('&');
            }
            if (paramBillSell.customerCodeTo) {
                search.push('customerCodeTo=');
                search.push(paramBillSell.customerCodeTo);
                search.push('&');
            }
            if (paramBillSell.customerRegisterDateTo) {
                search.push('customerRegisterDateTo=');
                search.push(paramBillSell.customerRegisterDateTo.getTime());
                search.push('&');
            }
            if (paramBillSell.customerRegisterDateFrom) {
                search.push('customerRegisterDateFrom=');
                search.push(paramBillSell.customerRegisterDateFrom.getTime());
                search.push('&');
            }
            if (paramBillSell.customerName) {
                search.push('customerName=');
                search.push(paramBillSell.customerName.getTime());
                search.push('&');
            }
            if (paramBillSell.customerMobile) {
                search.push('customerMobile=');
                search.push(paramBillSell.customerMobile.getTime());
                search.push('&');
            }
            BillSellService.filter(search.join("")).then(function (data) {
                $scope.billSells = data.content;

                $scope.pageBillSell.currentPage = $scope.pageBillSell.page + 1;
                $scope.pageBillSell.first = data.first;
                $scope.pageBillSell.last = data.last;
                $scope.pageBillSell.number = data.number;
                $scope.pageBillSell.numberOfElements = data.numberOfElements;
                $scope.pageBillSell.size = data.size;
                $scope.pageBillSell.totalElements = data.totalElements;
                $scope.pageBillSell.totalPages = data.totalPages;
                $scope.pageBillSell.currentPageString = ($scope.pageBillSell.page + 1) + ' / ' + $scope.pageBillSell.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextBillSellsPage = function () {
            $scope.pageBillSell.page++;
            $scope.searchBillSells($scope.paramBillSell);
        };
        $scope.selectPrevBillSellsPage = function () {
            $scope.pageBillSell.page--;
            $scope.searchBillSells($scope.paramBillSell);
        };
        $scope.checkAllBillSells = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('#checkAllBillSells input').is(":checked")) {
                    element.MaterialCheckbox.check();
                }
                else {
                    element.MaterialCheckbox.uncheck();
                }
            }
            angular.forEach($scope.billSells, function (billSell) {
                billSell.isSelected = $scope.billSells.checkAll;
            });
        };
        $scope.checkBillSell = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('.check input:checked').length == $('.check input').length) {
                    document.querySelector('#checkAllBillSells').MaterialCheckbox.check();
                } else {
                    document.querySelector('#checkAllBillSells').MaterialCheckbox.uncheck();
                }
            }
        };
        $scope.newBillSell = function () {
            ModalProvider.openBillSellCreateModel().result.then(function (data) {
                $scope.billSells.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.newBillSellWithCash = function () {
            ModalProvider.openBillSellCreateWithCashModel().result.then(function (data) {
                $scope.billSells.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deleteBillSell = function (billSell) {
            ModalProvider.openConfirmModel("فواتير البيع", "delete", "هل تود حذف الفاتورة فعلاً؟").result.then(function (value) {
                if (value) {
                    BillSellService.remove(billSell.id).then(function () {
                        var index = $scope.billSells.indexOf(billSell);
                        $scope.billSells.splice(index, 1);
                    });
                }
            });
        };
        $scope.newBillSellProduct = function (billSell) {
            ModalProvider.openBillSellProductCreateModel(billSell).result.then(function (data) {
                if (!billSell.billSellProducts) {
                    billSell.billSellProducts = [];
                }
                return billSell.billSellProducts.splice(0, 0, data);
            });
        };
        $scope.rowMenuBillSell = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_PURCHASE_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newBillSell();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openBillSellUpdateModel($itemScope.billSell);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_PURCHASE_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteBillSell($itemScope.billSell);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/product.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>اضافة سلعة...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_PURCHASE_PRODUCT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newBillSellProduct($itemScope.billSell);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/bankTransaction.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تسديد دفعة مالية...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_PAYMENT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    CustomerService.findOne($itemScope.billSell.customer.id).then(function (value) {
                        $scope.newCustomerPayment(value);
                    });
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/about.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>التفاصيل...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openBillSellDetailsModel($itemScope.billSell);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/print.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>طباعة...</span>' +
                '</div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    window.open('/report/billSell?billSellId=' + $itemScope.billSell.id);
                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * CustomerPayment                                                                                            *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.customerPayments = [];
        $scope.paramCustomerPayment = {};
        $scope.customerPayments.checkAll = false;

        $scope.pageCustomerPayment = {};
        $scope.pageCustomerPayment.sorts = [];
        $scope.pageCustomerPayment.page = 0;
        $scope.pageCustomerPayment.totalPages = 0;
        $scope.pageCustomerPayment.currentPage = $scope.pageCustomerPayment.page + 1;
        $scope.pageCustomerPayment.currentPageString = ($scope.pageCustomerPayment.page + 1) + ' / ' + $scope.pageCustomerPayment.totalPages;
        $scope.pageCustomerPayment.size = 25;
        $scope.pageCustomerPayment.first = true;
        $scope.pageCustomerPayment.last = true;

        $scope.openCustomerPaymentsFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/customerPayment/customerPaymentFilter.html',
                controller: 'customerPaymentFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramCustomerPayment) {
                $scope.searchCustomerPayments(paramCustomerPayment);
            }, function () {
            });
        };
        $scope.searchCustomerPayments = function (paramCustomerPayment) {
            var search = [];
            search.push('size=');
            search.push($scope.pageCustomerPayment.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageCustomerPayment.page);
            search.push('&');
            angular.forEach($scope.pageCustomerPayment.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if ($scope.pageCustomerPayment.sorts.length === 0) {
                search.push('sort=date,desc&');
            }
            if (paramCustomerPayment.dateFrom) {
                search.push('dateFrom=');
                search.push(paramCustomerPayment.dateFrom.getTime());
                search.push('&');
            }
            if (paramCustomerPayment.dateTo) {
                search.push('dateTo=');
                search.push(paramCustomerPayment.dateTo.getTime());
                search.push('&');
            }
            if (paramCustomerPayment.customerName) {
                search.push('customerName=');
                search.push(paramCustomerPayment.customerName);
                search.push('&');
            }
            if (paramCustomerPayment.customerMobile) {
                search.push('customerMobile=');
                search.push(paramCustomerPayment.customerMobile);
                search.push('&');
            }

            search.push('filterCompareType=or');

            CustomerPaymentService.filter(search.join("")).then(function (data) {
                $scope.customerPayments = data.content;

                $scope.pageCustomerPayment.currentPage = $scope.pageCustomerPayment.page + 1;
                $scope.pageCustomerPayment.first = data.first;
                $scope.pageCustomerPayment.last = data.last;
                $scope.pageCustomerPayment.number = data.number;
                $scope.pageCustomerPayment.numberOfElements = data.numberOfElements;
                $scope.pageCustomerPayment.size = data.size;
                $scope.pageCustomerPayment.totalElements = data.totalElements;
                $scope.pageCustomerPayment.totalPages = data.totalPages;
                $scope.pageCustomerPayment.currentPageString = ($scope.pageCustomerPayment.page + 1) + ' / ' + $scope.pageCustomerPayment.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextCustomerPaymentsPage = function () {
            $scope.pageCustomerPayment.page++;
            $scope.searchCustomerPayments($scope.paramCustomerPayment);
        };
        $scope.selectPrevCustomerPaymentsPage = function () {
            $scope.pageCustomerPayment.page--;
            $scope.searchCustomerPayments($scope.paramCustomerPayment);
        };
        $scope.newCustomerPayment = function (customer) {
            ModalProvider.openCustomerPaymentCreateModel(customer).result.then(function (data) {
                $scope.customerPayments.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deleteCustomerPayment = function (customerPayment) {
            ModalProvider.openConfirmModel("سندات القبض", "delete", "هل تود حذف سند القبض فعلاً؟").result.then(function (value) {
                if (value) {
                    CustomerPaymentService.remove(customerPayment.id).then(function () {
                        var index = $scope.customerPayments.indexOf(customerPayment);
                        $scope.customerPayments.splice(index, 1);
                    });
                }
            });
        };

        /**************************************************************************************************************
         *                                                                                                            *
         * BankTransaction                                                                                            *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.bankTransactions = [];
        $scope.paramBankTransaction = {};
        $scope.paramBankTransaction.transactionTypeCodes = [];
        $scope.bankTransactions.checkAll = false;

        $scope.pageBankTransaction = {};
        $scope.pageBankTransaction.sorts = [];
        $scope.pageBankTransaction.page = 0;
        $scope.pageBankTransaction.totalPages = 0;
        $scope.pageBankTransaction.currentPage = $scope.pageBankTransaction.page + 1;
        $scope.pageBankTransaction.currentPageString = ($scope.pageBankTransaction.page + 1) + ' / ' + $scope.pageBankTransaction.totalPages;
        $scope.pageBankTransaction.size = 25;
        $scope.pageBankTransaction.first = true;
        $scope.pageBankTransaction.last = true;

        $scope.openBankTransactionsFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/bankTransaction/bankTransactionFilter.html',
                controller: 'bankTransactionFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (paramBankTransaction) {
                $scope.searchBankTransactions(paramBankTransaction);
            }, function () {
            });
        };
        $scope.searchBankTransactions = function (paramBankTransaction) {
            var search = [];
            search.push('size=');
            search.push($scope.pageBankTransaction.size);
            search.push('&');
            search.push('page=');
            search.push($scope.pageBankTransaction.page);
            search.push('&');
            angular.forEach($scope.pageBankTransaction.sorts, function (sortBy) {
                search.push('sort=');
                search.push(sortBy.name + ',' + sortBy.direction);
                search.push('&');
            });
            if ($scope.pageBankTransaction.sorts.length === 0) {
                search.push('sort=date,desc&');
            }
            if (paramBankTransaction.codeFrom) {
                search.push('codeFrom=');
                search.push(paramBankTransaction.codeFrom);
                search.push('&');
            }
            if (paramBankTransaction.codeTo) {
                search.push('codeTo=');
                search.push(paramBankTransaction.codeTo);
                search.push('&');
            }
            if (paramBankTransaction.dateTo) {
                search.push('dateTo=');
                search.push(paramBankTransaction.dateTo.getTime());
                search.push('&');
            }
            if (paramBankTransaction.dateFrom) {
                search.push('dateFrom=');
                search.push(paramBankTransaction.dateFrom.getTime());
                search.push('&');
            }
            if (paramBankTransaction.supplierName) {
                search.push('supplierName=');
                search.push(paramBankTransaction.supplierName);
                search.push('&');
            }
            if (paramBankTransaction.supplierMobile) {
                search.push('supplierMobile=');
                search.push(paramBankTransaction.supplierMobile);
                search.push('&');
            }
            if (paramBankTransaction.supplierIdentityNumber) {
                search.push('supplierIdentityNumber=');
                search.push(paramBankTransaction.supplierIdentityNumber);
                search.push('&');
            }
            if (paramBankTransaction.transactionTypeCode) {
                search.push('transactionTypeCodes=');
                search.push([paramBankTransaction.transactionTypeCode]);
                search.push('&');
            }

            BankTransactionService.filter(search.join("")).then(function (data) {
                $scope.bankTransactions = data.content;

                $scope.pageBankTransaction.currentPage = $scope.pageBankTransaction.page + 1;
                $scope.pageBankTransaction.first = data.first;
                $scope.pageBankTransaction.last = data.last;
                $scope.pageBankTransaction.number = data.number;
                $scope.pageBankTransaction.numberOfElements = data.numberOfElements;
                $scope.pageBankTransaction.size = data.size;
                $scope.pageBankTransaction.totalElements = data.totalElements;
                $scope.pageBankTransaction.totalPages = data.totalPages;
                $scope.pageBankTransaction.currentPageString = ($scope.pageBankTransaction.page + 1) + ' / ' + $scope.pageBankTransaction.totalPages;

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.selectNextBankTransactionsPage = function () {
            $scope.pageBankTransaction.page++;
            $scope.searchBankTransactions($scope.paramBankTransaction);
        };
        $scope.selectPrevBankTransactionsPage = function () {
            $scope.pageBankTransaction.page--;
            $scope.searchBankTransactions($scope.paramBankTransaction);
        };
        $scope.newDeposit = function () {
            ModalProvider.openDepositCreateModel().result.then(function (data) {
                $scope.bankTransactions.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.newWithdraw = function () {
            ModalProvider.openWithdrawCreateModel().result.then(function (data) {
                $scope.bankTransactions.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.newTransfer = function () {
            ModalProvider.openTransferCreateModel().result.then(function () {
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.newExpense = function () {
            ModalProvider.openExpenseCreateModel().result.then(function (data) {
                $scope.bankTransactions.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };

        /**************************************************************************************************************
         *                                                                                                            *
         * Person                                                                                                     *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.persons = [];
        $scope.fetchPersonTableData = function () {
            PersonService.findAll().then(function (data) {
                $scope.persons = data;
            });
        };
        $scope.newPerson = function () {
            ModalProvider.openPersonCreateModel().result.then(function (data) {
                $scope.persons.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deletePerson = function (person) {
            ModalProvider.openConfirmModel("المستخدمين", "delete", "هل تود حذف المستخدم فعلاً؟").result.then(function (value) {
                if (value) {
                    PersonService.remove(person.id).then(function () {
                        var index = $scope.persons.indexOf(person);
                        $scope.persons.splice(index, 1);
                    });
                }
            });
        };
        $scope.rowMenuPerson = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PERSON_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newPerson();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PERSON_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openPersonUpdateModel($itemScope.person);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PERSON_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deletePerson($itemScope.person);
                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * Team                                                                                                       *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.fetchTeamTableData = function () {
            TeamService.findAll().then(function (data) {
                $scope.teams = data;
            });
        };
        $scope.newTeam = function () {
            ModalProvider.openTeamCreateModel().result.then(function (data) {
                $scope.teams.splice(0, 0, data);
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.deleteTeam = function (team) {
            ModalProvider.openConfirmModel("الصلاحيات", "delete", "هل تود حذف المجموعة فعلاً؟").result.then(function (value) {
                if (value) {
                    TeamService.remove(team.id).then(function () {
                        var index = $scope.teams.indexOf(team);
                        $scope.teams.splice(index, 1);
                    });
                }
            });
        };
        $scope.rowMenuTeam = [
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/add.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>جديد...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_TEAM_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newTeam();
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/edit.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>تعديل...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_TEAM_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openTeamUpdateModel($itemScope.team);
                }
            },
            {
                html: '<div class="drop-menu">' +
                '<img src="/ui/img/' + $rootScope.iconSet + '/delete.' + $rootScope.iconSetType + '" width="24" height="24">' +
                '<span>حذف...</span>' +
                '</div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_TEAM_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteTeam($itemScope.team);
                }
            }
        ];

        /**************************************************************************************************************
         *                                                                                                            *
         * Profile                                                                                                    *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.submitProfile = function () {
            PersonService.updateProfile($rootScope.me).then(function (data) {
                $rootScope.me = data;
            });
        };
        $scope.browseProfilePhoto = function () {
            document.getElementById('uploader-profile').click();
        };
        $scope.uploadProfilePhoto = function (files) {
            PersonService.uploadContactPhoto(files[0]).then(function (data) {
                $rootScope.me.logo = data;
                PersonService.update($rootScope.me).then(function (data) {
                    $rootScope.me = data;
                });
            });
        };

        /**************************************************************************************************************
         *                                                                                                            *
         * Print                                                                                                      *
         *                                                                                                            *
         **************************************************************************************************************/
        $rootScope.printToCart = function (printSectionId, title) {
            var innerContents = document.getElementById(printSectionId).innerHTML;
            var mywindow = window.open(title, '_blank', 'height=400,width=600');
            mywindow.document.write('<html><head><title></title>');
            mywindow.document.write('<link rel="stylesheet" href="/ui/app.css" type="text/css" />');
            mywindow.document.write('<link rel="stylesheet" href="/ui/css/style.css" type="text/css" />');
            mywindow.document.write('</head><body >');
            mywindow.document.write(innerContents);
            mywindow.document.write('</body></html>');
            mywindow.document.close();
            mywindow.focus();
            $timeout(function () {
                mywindow.print();
                mywindow.close();
            }, 1500);
            return true;
        };

        /**************************************************************************************************************
         *                                                                                                            *
         * Report                                                                                                     *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.toggleReport = 'mainReportFrame';
        //السيولة النقدية
        $scope.openReportCashBalance = function () {
            $scope.toggleReport = 'cashBalance';
            $rootScope.refreshGUI();
        };
        //تقرير المصروفات
        $scope.openReportExpenses = function () {
            $scope.toggleReport = 'expenses';
            $rootScope.refreshGUI();
        };
        //أرصدة الموردين
        $scope.openReportSupplierBalance = function () {
            $scope.toggleReport = 'supplierBalance';
            $rootScope.refreshGUI();
        };
        //تقرير العقود
        $scope.openReportBillPurchases = function () {
            $scope.toggleReport = 'billPurchases';
            $rootScope.refreshGUI();
        };
        //حركة العمليات اليومية
        $scope.openReportBankTransactions = function () {
            $scope.toggleReport = 'bankTransactions';
            $rootScope.refreshGUI();
        };
        //كشف حساب مورد
        $scope.openReportSupplierStatement = function () {
            $scope.toggleReport = 'supplierStatement';
            $rootScope.refreshGUI();
        };
        //كشف حساب عميل
        $scope.openReportCustomerStatement = function () {
            $scope.toggleReport = 'customerStatement';
            $rootScope.refreshGUI();
        };
        //تقرير التحصيل والسداد
        $scope.openReportBillPurchasePremiums = function () {
            $scope.toggleReport = 'billPurchasePremiums';
            $rootScope.refreshGUI();
        };
        //تقرير أرباح التحصيل
        $scope.openReportSupplierPaymentsProfit = function () {
            $scope.toggleReport = 'supplierPaymentsProfit';
            $rootScope.refreshGUI();
        };

        $scope.paramExpense = {};
        $scope.supplierStatement = {};
        $scope.customerStatement = {};
        $scope.supplierPaymentProfit = {};
        $scope.fetchExpenses = function () {
            var search = [];
            if ($scope.paramExpense.dateTo) {
                search.push('dateTo=');
                search.push($scope.paramExpense.dateTo.getTime());
                search.push('&');
            }
            if ($scope.paramExpense.dateFrom) {
                search.push('dateFrom=');
                search.push($scope.paramExpense.dateFrom.getTime());
                search.push('&');
            }

            search.push('transactionTypeCodes=');
            search.push(['Expense']);

            BankTransactionService.findByDateBetweenOrTransactionTypeCodeIn(search.join("")).then(function (data) {
                $scope.expenses = data;
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 300);
            });
        };
        $scope.fetchAllSupplierCombo = function () {
            SupplierService.findAllCombo().then(function (value) {
                $scope.suppliersCombo = value;
            });
        };
        $scope.fetchAllCustomerCombo = function () {
            CustomerService.findAllCombo().then(function (value) {
                $scope.customersCombo = value;
            });
        };
        $scope.fetchSupplierStatementBillPurchases = function () {
            BillPurchaseService.findBySupplier($scope.supplierStatement.supplier.id).then(function (value) {
                $scope.supplierStatement.billPurchases = value;
            });
        };
        $scope.fetchSupplierStatementBanks = function () {
            BankService.findBySupplier($scope.supplierStatement.supplier.id).then(function (value) {
                $scope.supplierStatement.banks = value;
            });
        };
        $scope.fetchSupplierPaymentsProfit = function () {
            SupplierPaymentService.findByDateBetween(
                $scope.supplierPaymentProfit.dateFrom.getTime(),
                $scope.supplierPaymentProfit.dateTo.getTime()
            )
                .then(function (value) {
                    $scope.supplierPaymentProfit.supplierPayments = value;
                });
        };
        $scope.findCustomerDetails = function () {
            CustomerService.findOne($scope.customerStatement.customer.id).then(function (value) {
                return $scope.customerStatement.customer = value;
            });
        };

        /**************************************************************************************************************
         *                                                                                                            *
         * Widget: Daily History                                                                                      *
         *                                                                                                            *
         **************************************************************************************************************/
        $scope.findDailyHistory = function () {
            HistoryService.findDaily().then(function (value) {
                $scope.dailyHistories = value;
            });
        };

        $timeout(function () {
            CompanyService.get().then(function (data) {
                $rootScope.selectedCompany = data;
                $rootScope.selectedCompany.options = JSON.parse(data.options);
            });
            PersonService.findAllCombo().then(function (data) {
                $scope.personsCombo = data;
            });
            AttachTypeService.findAll().then(function (data) {
                $scope.attachTypes = data;
            });
            $scope.fetchParents();
            $scope.fetchAllBanks();
            $scope.findDailyHistory();
            $scope.findDailyOffers();
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);