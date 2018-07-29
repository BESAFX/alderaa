app.factory("SupplierPaymentService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/supplierPayment/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (supplierPayment) {
                return $http.post("/api/supplierPayment/create", supplierPayment).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/supplierPayment/delete/" + id);
            },
            findBySupplier: function (id) {
                return $http.get("/api/supplierPayment/findBySupplier/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByDateBetween: function (startDate, endDate) {
                return $http.get("/api/supplierPayment/findByDateBetween/" + startDate + "/" + endDate)
                    .then(function (response) {
                    return response.data;
                });
            },
            filter: function (search) {
                return $http.get("/api/supplierPayment/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);