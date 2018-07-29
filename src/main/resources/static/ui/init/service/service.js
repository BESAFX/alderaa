app.service('ModalProvider', ['$uibModal', '$log', function ($uibModal, $log) {

    /**************************************************************
     *                                                            *
     * Customer Model                                             *
     *                                                            *
     *************************************************************/
    this.openCustomerCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customer/customerCreate.html',
            controller: 'customerCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return 'عميل جديد';
                }
            }
        });
    };

    this.openCustomerCreateBatchModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customer/customerCreateBatch.html',
            controller: 'customerCreateBatchCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return 'عملاء جديد';
                }
            }
        });
    };

    this.openCustomerUpdateModel = function (customer) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customer/customerUpdate.html',
            controller: 'customerUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تعديل بيانات عميل';
                },
                customer: ['CustomerService', function (CustomerService) {
                    return CustomerService.findOne(customer.id).then(function (data) {
                        return customer = data;
                    });
                }]
            }
        });
    };

    this.openCustomerSendMessageModel = function (customers) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customer/customerSendMessage.html',
            controller: "customerSendMessageCtrl",
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                customers: function () {
                    return customers;
                }
            }
        });
    };

    this.openCustomerDetailsModel = function (customer) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customer/customerDetails.html',
            controller: 'customerDetailsCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                customer: ['CustomerService', function (CustomerService) {
                    return CustomerService.findOne(customer.id).then(function (data) {
                        return customer = data;
                    });
                }]
            }
        });
    };

    this.openCustomersInfoDataReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/customer/customersInfoData.html',
            controller: 'customersInfoDataCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openCustomersBalanceDataReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/customer/customersBalanceData.html',
            controller: 'customersBalanceDataCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openCustomerStatementReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/customer/customerStatement.html',
            controller: 'customerStatementCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * CustomerContact Model                                      *
     *                                                            *
     *************************************************************/
    this.openCustomerContactCreateModel = function (customer) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customerContact/customerContactCreateUpdate.html',
            controller: 'customerContactCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'جهة اتصال جديدة';
                },
                action: function () {
                    return 'create';
                },
                customerContact: function () {
                    var customerContact = {};
                    customerContact.customer = customer;
                    return customerContact;
                }
            }
        });
    };

    this.openCustomerContactUpdateModel = function (customerContact) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customerContact/customerContactCreateUpdate.html',
            controller: 'customerContactCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تعديل بيانات جهة اتصال';
                },
                action: function () {
                    return 'update';
                },
                customerContact: function () {
                    return customerContact;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Supplier Model                                             *
     *                                                            *
     *************************************************************/
    this.openSupplierCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplier/supplierCreate.html',
            controller: 'supplierCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return 'مورد جديد';
                }
            }
        });
    };

    this.openSupplierCreateBatchModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplier/supplierCreateBatch.html',
            controller: 'supplierCreateBatchCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return 'مودرين جديد';
                }
            }
        });
    };

    this.openSupplierUpdateModel = function (supplier) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplier/supplierUpdate.html',
            controller: 'supplierUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تعديل بيانات مورد';
                },
                supplier: ['SupplierService', function (SupplierService) {
                    return SupplierService.findOne(supplier.id).then(function (data) {
                        return supplier = data;
                    });
                }]
            }
        });
    };

    this.openSupplierSendMessageModel = function (suppliers) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplier/supplierSendMessage.html',
            controller: "supplierSendMessageCtrl",
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                suppliers: function () {
                    return suppliers;
                }
            }
        });
    };

    this.openSupplierDetailsModel = function (supplier) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplier/supplierDetails.html',
            controller: 'supplierDetailsCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                supplier: ['SupplierService', function (SupplierService) {
                    return SupplierService.findOne(supplier.id).then(function (data) {
                        return supplier = data;
                    });
                }]
            }
        });
    };

    this.openSuppliersInfoDataReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/supplier/suppliersInfoData.html',
            controller: 'suppliersInfoDataCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openSuppliersBalanceDataReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/supplier/suppliersBalanceData.html',
            controller: 'suppliersBalanceDataCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openSupplierStatementReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/supplier/supplierStatement.html',
            controller: 'supplierStatementCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * SupplierContact Model                                      *
     *                                                            *
     *************************************************************/
    this.openSupplierContactCreateModel = function (supplier) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplierContact/supplierContactCreateUpdate.html',
            controller: 'supplierContactCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'جهة اتصال جديدة';
                },
                action: function () {
                    return 'create';
                },
                supplierContact: function () {
                    var supplierContact = {};
                    supplierContact.supplier = supplier;
                    return supplierContact;
                }
            }
        });
    };

    this.openSupplierContactUpdateModel = function (supplierContact) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplierContact/supplierContactCreateUpdate.html',
            controller: 'supplierContactCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تعديل بيانات جهة اتصال';
                },
                action: function () {
                    return 'update';
                },
                supplierContact: function () {
                    return supplierContact;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Product Model                                              *
     *                                                            *
     *************************************************************/
    this.openParentCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/product/parentCreateUpdate.html',
            controller: 'parentCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تصنيف رئيسي جديد';
                },
                action: function () {
                    return 'create';
                },
                parent: function () {
                    return {};
                }
            }
        });
    };

    this.openParentUpdateModel = function (parent) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/product/parentCreateUpdate.html',
            controller: 'parentCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تعديل بيانات التصنيف الرئيسي';
                },
                action: function () {
                    return 'update';
                },
                parent: function () {
                    return parent;
                }
            }
        });
    };

    this.openProductCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/product/productCreateUpdate.html',
            controller: 'productCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تصنيف فرعي جديد';
                },
                action: function () {
                    return 'create';
                },
                product: function () {
                    return {};
                }
            }
        });
    };

    this.openProductUpdateModel = function (product) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/product/productCreateUpdate.html',
            controller: 'productCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تعديل بيانات التصنيف الفرعي';
                },
                action: function () {
                    return 'update';
                },
                product: function () {
                    return product;
                }
            }
        });
    };

    this.openProductCreateBatchModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/product/productCreateBatch.html',
            controller: 'productCreateBatchCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return 'سلع جديدة';
                }
            }
        });
    };

    this.openProductDetailsModel = function (product) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/product/productDetails.html',
            controller: 'productDetailsCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                product: function () {
                    return product;
                }
            }
        });
    };

    this.openProductStocksReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/product/productStocks.html',
            controller: 'productStocksCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openProductPricesReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/product/productPrices.html',
            controller: 'productPricesCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * Offer Model                                                *
     *                                                            *
     *************************************************************/
    this.openOfferCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/offer/offerCreate.html',
            controller: 'offerCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            windowClass: 'xlg'
        });
    };

    this.openOfferUpdateModel = function (offer) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/offer/offerUpdate.html',
            controller: 'offerUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                offer: ['OfferService', function (OfferService) {
                    return OfferService.findOne(offer.id).then(function (data) {
                        return offer = data;
                    });
                }]
            }
        });
    };

    this.openOfferDetailsModel = function (offer) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/offer/offerDetails.html',
            controller: 'offerDetailsCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                offer: ['OfferService', function (OfferService) {
                    return OfferService.findOne(offer.id).then(function (data) {
                        return offer = data;
                    });
                }]
            }
        });
    };

    this.openOfferPrintModel = function (offer) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/offer/offerPrint.html',
            controller: 'offerPrintCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                offer: function () {
                    return offer;
                }
            }
        });
    };

    this.openOfferProductCreateModel = function (offer) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/offerProduct/offerProductCreate.html',
            controller: 'offerProductCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                offer: function () {
                    return offer;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Bank Model                                                 *
     *                                                            *
     *************************************************************/
    this.openBankCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bank/bankCreateUpdate.html',
            controller: 'bankCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'فتح حساب جديد';
                },
                action: function () {
                    return 'create';
                },
                bank: function () {
                    return {};
                }
            }
        });
    };

    this.openBankUpdateModel = function (bank) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bank/bankCreateUpdate.html',
            controller: 'bankCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تعديل بيانات الحساب';
                },
                action: function () {
                    return 'update';
                },
                bank: function () {
                    return bank;
                }
            }
        });
    };

    this.openBanksDataReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/bank/banksData.html',
            controller: 'banksDataCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * OrderPurchase Model                                        *
     *                                                            *
     *************************************************************/
    this.openOrderPurchaseCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/orderPurchase/orderPurchaseCreate.html',
            controller: 'orderPurchaseCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            windowClass: 'xlg'
        });
    };

    this.openOrderPurchaseUpdateModel = function (orderPurchase) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/orderPurchase/orderPurchaseUpdate.html',
            controller: 'orderPurchaseUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                orderPurchase: function () {
                    return orderPurchase;
                }
            }
        });
    };

    this.openOrderPurchaseDetailsModel = function (orderPurchase) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/orderPurchase/orderPurchaseDetails.html',
            controller: 'orderPurchaseDetailsCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                orderPurchase: function () {
                    return orderPurchase;
                }
            }
        });
    };

    this.openOrderPurchaseProductCreateModel = function (orderPurchase) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/orderPurchaseProduct/orderPurchaseProductCreate.html',
            controller: 'orderPurchaseProductCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                orderPurchase: function () {
                    return orderPurchase;
                }
            }
        });
    };

    this.openOrderPurchasePrintModel = function (orderPurchase) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/orderPurchase/orderPurchasePrint.html',
            controller: 'orderPurchasePrintCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                orderPurchase: function () {
                    return orderPurchase;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * BillPurchase Model                                         *
     *                                                            *
     *************************************************************/
    this.openBillPurchaseCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billPurchase/billPurchaseCreate.html',
            controller: 'billPurchaseCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            windowClass: 'xlg'
        });
    };

    this.openBillPurchaseDetailsModel = function (billPurchase) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billPurchase/billPurchaseDetails.html',
            controller: 'billPurchaseDetailsCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                billPurchase: ['BillPurchaseService', function (BillPurchaseService) {
                    return BillPurchaseService.findOne(billPurchase.id).then(function (data) {
                        return billPurchase = data;
                    });
                }]
            }
        });
    };

    this.openBillPurchaseProductCreateModel = function (billPurchase) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billPurchaseProduct/billPurchaseProductCreate.html',
            controller: 'billPurchaseProductCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                billPurchase: function () {
                    return billPurchase;
                }
            }
        });
    };

    this.openSupplierPaymentCreateModel = function (supplier) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplierPayment/supplierPaymentCreate.html',
            controller: 'supplierPaymentCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                supplier: function () {
                    return supplier;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * OrderSell Model                                            *
     *                                                            *
     *************************************************************/
    this.openOrderSellCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/orderSell/orderSellCreate.html',
            controller: 'orderSellCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            windowClass: 'xlg'
        });
    };

    this.openOrderSellUpdateModel = function (orderSell) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/orderSell/orderSellUpdate.html',
            controller: 'orderSellUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                orderSell: ['OrderSellService', function (OrderSellService) {
                    return OrderSellService.findOne(orderSell.id).then(function (data) {
                        return orderSell = data;
                    });
                }]
            }
        });
    };

    this.openOrderSellDetailsModel = function (orderSell) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/orderSell/orderSellDetails.html',
            controller: 'orderSellDetailsCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                orderSell: ['OrderSellService', function (OrderSellService) {
                    return OrderSellService.findOne(orderSell.id).then(function (data) {
                        return orderSell = data;
                    });
                }]
            }
        });
    };

    this.openOrderSellProductCreateModel = function (orderSell) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/orderSellProduct/orderSellProductCreate.html',
            controller: 'orderSellProductCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                orderSell: function () {
                    return orderSell;
                }
            }
        });
    };

    this.openOrderSellPrintModel = function (orderSell) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/orderSell/orderSellPrint.html',
            controller: 'orderSellPrintCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                orderSell: function () {
                    return orderSell;
                }
            }
        });
    };

    this.openOrderSellsDetailsReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/orderSell/orderSellsDetails.html',
            controller: 'orderSellsDetailsCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * BillSell Model                                             *
     *                                                            *
     *************************************************************/
    this.openBillSellCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billSell/billSellCreate.html',
            controller: 'billSellCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            windowClass: 'xlg'
        });
    };

    this.openBillSellDetailsModel = function (billSell) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billSell/billSellDetails.html',
            controller: 'billSellDetailsCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                billSell: ['BillSellService', function (BillSellService) {
                    return BillSellService.findOne(billSell.id).then(function (data) {
                        return billSell = data;
                    });
                }]
            }
        });
    };

    this.openBillSellProductCreateModel = function (billSell) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billSellProduct/billSellProductCreate.html',
            controller: 'billSellProductCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                billSell: function () {
                    return billSell;
                }
            }
        });
    };

    this.openCustomerPaymentCreateModel = function (customer) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customerPayment/customerPaymentCreate.html',
            controller: 'customerPaymentCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                customer: function () {
                    return customer;
                }
            }
        });
    };

    this.openSalesByCustomerReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/billSell/salesByCustomer.html',
            controller: 'salesByCustomerCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openSalesByProductReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/billSell/salesByProduct.html',
            controller: 'salesByProductCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openSalesByPersonReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/billSell/salesByPerson.html',
            controller: 'salesByPersonCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openIncomesByCustomerReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/customerPayment/incomesByCustomer.html',
            controller: 'incomesByCustomerCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openIncomesByPersonReportModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/customerPayment/incomesByPerson.html',
            controller: 'incomesByPersonCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * DepositCreate Model                                        *
     *                                                            *
     *************************************************************/
    this.openDepositCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bankTransaction/depositCreate.html',
            controller: 'depositCreateCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * WithdrawCreate Model                                       *
     *                                                            *
     *************************************************************/
    this.openWithdrawCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bankTransaction/withdrawCreate.html',
            controller: 'withdrawCreateCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * TransferCreate Model                                       *
     *                                                            *
     *************************************************************/
    this.openTransferCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bankTransaction/transferCreate.html',
            controller: 'transferCreateCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * ExpenseCreate Model                                        *
     *                                                            *
     *************************************************************/
    this.openExpenseCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bankTransaction/expenseCreate.html',
            controller: 'expenseCreateCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * Team Model                                                 *
     *                                                            *
     *************************************************************/
    this.openTeamCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/team/teamCreateUpdate.html',
            controller: 'teamCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'مجموعة جديدة';
                },
                action: function () {
                    return 'create';
                },
                team: function () {
                    return undefined;
                }
            }
        });
    };

    this.openTeamUpdateModel = function (team) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/team/teamCreateUpdate.html',
            controller: 'teamCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تعديل بيانات مجموعة';
                },
                action: function () {
                    return 'update';
                },
                team: function () {
                    return team;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Person Model                                               *
     *                                                            *
     *************************************************************/
    this.openPersonCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/person/personCreateUpdate.html',
            controller: 'personCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'مستخدم جديد';
                },
                action: function () {
                    return 'create';
                },
                person: function () {
                    return {};
                }
            }
        });
    };

    this.openPersonUpdateModel = function (person) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/person/personCreateUpdate.html',
            controller: 'personCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return 'تعديل بيانات مستخدم';
                },
                action: function () {
                    return 'update';
                },
                person: ['PersonService', function (PersonService) {
                    return PersonService.findOne(person.id).then(function (data) {
                        return person = data;
                    });
                }]
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Confirm Model                                              *
     *                                                            *
     *************************************************************/
    this.openConfirmModel = function (title, icon, message) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/modal/confirmModal.html',
            controller: 'confirmModalCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return title;
                },
                icon: function () {
                    return icon;
                },
                message: function () {
                    return message;
                }
            }
        });
    };

}]);

app.service('NotificationProvider', ['$http', function ($http) {

    this.notifyOne = function (code, title, message, type, receiver) {
        $http.post("/notifyOne?"
            + 'code=' + code
            + '&'
            + 'title=' + title
            + '&'
            + 'message=' + message
            + '&'
            + 'type=' + type
            + '&'
            + 'receiver=' + receiver);
    };
    this.notifyAll = function (code, title, message, type) {
        $http.post("/notifyAll?"
            + 'code=' + code
            + '&'
            + 'title=' + title
            + '&'
            + 'message=' + message
            + '&'
            + 'type=' + type
        );
    };
    this.notifyOthers = function (code, title, message, type) {
        $http.post("/notifyOthers?"
            + 'code=' + code
            + '&'
            + 'title=' + title
            + '&'
            + 'message=' + message
            + '&'
            + 'type=' + type
        );
    };

}]);

