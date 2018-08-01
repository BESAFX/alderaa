app.factory("BillSellService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/billSell/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByCustomer: function (customerId) {
                return $http.get("/api/billSell/findByCustomer/" + customerId).then(function (response) {
                    return response.data;
                });
            },
            create: function (billSell) {
                return $http.post("/api/billSell/create", billSell).then(function (response) {
                    return response.data;
                });
            },
            createWithCash: function (billSell, customerName, customerMobile, bankId) {
                return $http.post("/api/billSell/createWithCash/" + customerName + "/" + customerMobile + "/" + bankId, billSell).then(function (response) {
                    return response.data;
                });
            },
            createFromOffer: function (offer) {
                return $http.post("/api/billSell/createFromOffer", offer).then(function (response) {
                    return response.data;
                });
            },
            createFromOrder: function (orderSell) {
                return $http.post("/api/billSell/createFromOrder", orderSell).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/billSell/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/billSell/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);