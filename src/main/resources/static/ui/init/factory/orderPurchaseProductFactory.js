app.factory("OrderPurchaseProductService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/orderPurchaseProduct/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/orderPurchaseProduct/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByOrderPurchase: function (id) {
                return $http.get("/api/orderPurchaseProduct/findByOrderPurchase/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByProduct: function (id) {
                return $http.get("/api/orderPurchaseProduct/findByProduct/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (orderPurchaseProduct) {
                return $http.post("/api/orderPurchaseProduct/create", orderPurchaseProduct).then(function (response) {
                    return response.data;
                });
            },
            createBatch: function (orderPurchaseProducts) {
                return $http.post("/api/orderPurchaseProduct/createBatch", orderPurchaseProducts).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/orderPurchaseProduct/delete/" + id);
            }
        };
    }]);