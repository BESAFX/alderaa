var gulp = require('gulp');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var cssMin = require('gulp-css');
var gutil = require('gulp-util');


gulp.task('css', function () {

    gulp.src([
        './magnific-popup/magnific-popup.css',
        './css/creative.css',

        './css/animate.css',
        './css/animation.css',
        './css/bootstrap.css',
        './css/fonts.css',
        './css/md-icons.css',
        './css/angular-toggle-switch-bootstrap-3.css',
        './css/highlight.css',
        './bootstrap-select/css/nya-bs-select.css',
        './css/font-awesome-animation.css',
        './kdate/css/jquery.calendars.picker.css',
        './css/select.css',
        './chosen/chosen.css',
        './css/marquee.css'
    ])
        .pipe(concat('app.css'))
        .pipe(cssMin())
        .pipe(gulp.dest('./'));

});

gulp.task('scripts', function () {

    gulp.src([

        './js/material.js',
        './js/fontawesome.js',
        './js/jquery.js',

        './kdate/js/jquery.calendars.js',
        './kdate/js/jquery.calendars-ar.js',
        './kdate/js/jquery.calendars-ar-EG.js',
        './kdate/js/jquery.plugin.js',
        './kdate/js/jquery.calendars.picker.js',
        './kdate/js/jquery.calendars.picker-ar.js',
        './kdate/js/jquery.calendars.picker-ar-EG.js',
        './kdate/js/jquery.calendars.picker.lang.js',
        './kdate/js/jquery.calendars.ummalqura.js',
        './kdate/js/jquery.calendars.ummalqura-ar.js',
        './kdate/js/jquery.calendars.plus.js',

        './js/jquery-ui.js',
        './js/angular.js',
        './js/angular-sanitize.js',
        './js/angular-ui-router.js',
        './js/angular-animate.js',
        './js/angular-touch.js',
        './js/angular-filter.js',

        './angular-spinner/spin.js',
        './angular-spinner/angular-spinner.js',
        './angular-spinner/angular-loading-spinner.js',

        './js/ui-bootstrap.js',
        './js/ui-bootstrap-tpls.js',

        './js/select.js',
        './js/marquee.js',

        './sockjs/sockjs.js',
        './stomp-websocket/lib/stomp.js',
        './ng-stomp/ng-stomp.js',
        './kdate/kdate.module.js',
        './kdate/kdate.filter.js',
        './kdate/kdate.picker.js',
        './js/underscore.js',
        './js/lrDragNDrop.js',
        './js/contextMenu.js',
        './js/lrStickyHeader.js',
        './js/smart-table.js',
        './js/stStickyHeader.js',
        './js/angular-pageslide-directive.js',
        './js/elastic.js',
        './js/scrollglue.js',
        './ng-upload/angular-file-upload.js',
        './bootstrap-select/js/nya-bs-select.js',
        './js/angular-css.js',
        './js/angular-timer-all.js',
        './full-screen/angular-fullscreen.js',
        './animate-counter/angular-counter.js',
        './js/angular-focusmanager.js',
        './js/jcs-auto-validate.js',
        './js/angular-toggle-switch.js',
        './js/chosen.jquery.js',
        './chosen/angular-chosen.js',
        './js/sortable.js',
        './js/FileSaver.js',
        './js/jquery.noty.packaged.js',
        './scrollreveal/scrollreveal.js',
        './magnific-popup/jquery.magnific-popup.js',
        './js/creative.js',
        './angular-chart/Chart.js',
        './angular-chart/angular-chart.js',
        './js/datetime.js',

        //SheetJS js-xlsx library
        './js/shim.js',
        './js/xlsx.full.min.js',

        './init/config/config.js',
        './init/factory/errorHandleFactory.js',
        './init/factory/smsFactory.js',
        './init/factory/historyFactory.js',
        './init/factory/companyFactory.js',

        './init/factory/customerFactory.js',
        './init/factory/customerContactFactory.js',

        './init/factory/supplierFactory.js',
        './init/factory/supplierContactFactory.js',

        './init/factory/productFactory.js',

        './init/factory/offerFactory.js',
        './init/factory/offerAttachFactory.js',
        './init/factory/offerProductFactory.js',

        './init/factory/orderSellFactory.js',
        './init/factory/orderSellProductFactory.js',

        './init/factory/billSellFactory.js',
        './init/factory/billSellAttachFactory.js',
        './init/factory/billSellProductFactory.js',
        './init/factory/customerPaymentFactory.js',

        './init/factory/orderPurchaseFactory.js',
        './init/factory/orderPurchaseProductFactory.js',

        './init/factory/billPurchaseFactory.js',
        './init/factory/billPurchaseAttachFactory.js',
        './init/factory/billPurchaseProductFactory.js',
        './init/factory/supplierPaymentFactory.js',

        './init/factory/bankFactory.js',
        './init/factory/bankTransactionFactory.js',
        './init/factory/attachTypeFactory.js',
        './init/factory/personFactory.js',
        './init/factory/teamFactory.js',

        './init/service/service.js',
        './init/directive/directive.js',
        './init/filter/filter.js',
        './init/run/run.js',

        './partials/home/home.js',
        './partials/menu/menu.js',

        './partials/customer/customerCreate.js',
        './partials/customer/customerCreateBatch.js',
        './partials/customer/customerUpdate.js',
        './partials/customer/customerFilter.js',
        './partials/customer/customerDetails.js',
        './partials/customer/customerSendMessage.js',
        './partials/customerContact/customerContactCreateUpdate.js',
        './partials/report/customer/customersInfoData.js',
        './partials/report/customer/customersBalanceData.js',
        './partials/report/customer/customerStatement.js',

        './partials/supplier/supplierCreate.js',
        './partials/supplier/supplierCreateBatch.js',
        './partials/supplier/supplierUpdate.js',
        './partials/supplier/supplierFilter.js',
        './partials/supplier/supplierDetails.js',
        './partials/supplier/supplierSendMessage.js',
        './partials/supplierContact/supplierContactCreateUpdate.js',
        './partials/report/supplier/suppliersInfoData.js',
        './partials/report/supplier/suppliersBalanceData.js',
        './partials/report/supplier/supplierStatement.js',

        './partials/offer/offerFilter.js',
        './partials/offer/offerCreate.js',
        './partials/offer/offerUpdate.js',
        './partials/offer/offerDetails.js',
        './partials/report/offer/offerPrint.js',
        './partials/offerProduct/offerProductCreate.js',

        './partials/orderPurchase/orderPurchaseFilter.js',
        './partials/orderPurchase/orderPurchaseCreate.js',
        './partials/orderPurchase/orderPurchaseUpdate.js',
        './partials/orderPurchase/orderPurchaseDetails.js',
        './partials/report/orderPurchase/orderPurchasePrint.js',
        './partials/orderPurchaseProduct/orderPurchaseProductCreate.js',

        './partials/billPurchase/billPurchaseFilter.js',
        './partials/billPurchase/billPurchaseCreate.js',
        './partials/billPurchase/billPurchaseDetails.js',
        './partials/billPurchaseProduct/billPurchaseProductCreate.js',
        './partials/supplierPayment/supplierPaymentFilter.js',
        './partials/supplierPayment/supplierPaymentCreate.js',

        './partials/orderSell/orderSellFilter.js',
        './partials/orderSell/orderSellCreate.js',
        './partials/orderSell/orderSellUpdate.js',
        './partials/orderSell/orderSellDetails.js',
        './partials/orderSellProduct/orderSellProductCreate.js',
        './partials/report/orderSell/orderSellPrint.js',
        './partials/report/orderSell/orderSellsDetails.js',

        './partials/billSell/billSellFilter.js',
        './partials/billSell/billSellCreate.js',
        './partials/billSell/billSellDetails.js',
        './partials/billSellProduct/billSellProductCreate.js',
        './partials/customerPayment/customerPaymentFilter.js',
        './partials/customerPayment/customerPaymentCreate.js',
        './partials/report/billSell/salesByCustomer.js',
        './partials/report/billSell/salesByProduct.js',
        './partials/report/billSell/salesByPerson.js',
        './partials/report/customerPayment/incomesByCustomer.js',
        './partials/report/customerPayment/incomesByPerson.js',

        './partials/bank/bankCreateUpdate.js',
        './partials/report/bank/banksData.js',

        './partials/bankTransaction/bankTransactionFilter.js',
        './partials/bankTransaction/depositCreate.js',
        './partials/bankTransaction/withdrawCreate.js',
        './partials/bankTransaction/transferCreate.js',
        './partials/bankTransaction/expenseCreate.js',

        './partials/product/parentCreateUpdate.js',
        './partials/product/productCreateUpdate.js',
        './partials/product/productCreateBatch.js',
        './partials/product/productDetails.js',
        './partials/product/productFilter.js',

        './partials/report/product/productStocks.js',
        './partials/report/product/productPrices.js',

        './partials/person/personCreateUpdate.js',
        './partials/team/teamCreateUpdate.js',
        './partials/modal/confirmModal.js'

    ])
        .pipe(concat('app.js'))
        .pipe(uglify())
        .on('error', function (err) {
            gutil.log(gutil.colors.red('[Error]'), err.toString());
        })
        .pipe(gulp.dest('./'))

});

gulp.task('default', ['css', 'scripts']);