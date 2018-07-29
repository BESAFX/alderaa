app.factory("CustomerPaymentService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/customerPayment/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (customerPayment) {
                return $http.post("/api/customerPayment/create", customerPayment).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/customerPayment/delete/" + id);
            },
            findByCustomer: function (id) {
                return $http.get("/api/customerPayment/findByCustomer/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByDateBetween: function (startDate, endDate) {
                return $http.get("/api/customerPayment/findByDateBetween/" + startDate + "/" + endDate)
                    .then(function (response) {
                    return response.data;
                });
            },
            filter: function (search) {
                return $http.get("/api/customerPayment/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);