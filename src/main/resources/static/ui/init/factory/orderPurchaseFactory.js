app.factory("OrderPurchaseService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/orderPurchase/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByPurchase: function (customerId) {
                return $http.get("/api/orderPurchase/findByPurchase/" + customerId).then(function (response) {
                    return response.data;
                });
            },
            create: function (contract) {
                return $http.post("/api/orderPurchase/create", contract).then(function (response) {
                    return response.data;
                });
            },
            update: function (orderPurchase) {
                return $http.put("/api/orderPurchase/update", orderPurchase).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/orderPurchase/delete/" + id);
            },
            send: function (orderPurchaseId) {
                return $http.get("/api/orderPurchase/send/" + orderPurchaseId).then(function (response) {
                    return response.data;
                });
            },
            filter: function (search) {
                return $http.get("/api/orderPurchase/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);