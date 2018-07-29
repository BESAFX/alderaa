app.factory("OrderSellService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/orderSell/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByCustomer: function (customerId) {
                return $http.get("/api/orderSell/findByCustomer/" + customerId).then(function (response) {
                    return response.data;
                });
            },
            create: function (contract) {
                return $http.post("/api/orderSell/create", contract).then(function (response) {
                    return response.data;
                });
            },
            update: function (orderSell) {
                return $http.put("/api/orderSell/update", orderSell).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/orderSell/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/orderSell/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);