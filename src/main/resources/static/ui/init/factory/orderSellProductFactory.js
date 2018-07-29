app.factory("OrderSellProductService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/orderSellProduct/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/orderSellProduct/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByOrderSell: function (id) {
                return $http.get("/api/orderSellProduct/findByOrderSell/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByProduct: function (id) {
                return $http.get("/api/orderSellProduct/findByProduct/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (orderSellProduct) {
                return $http.post("/api/orderSellProduct/create", orderSellProduct).then(function (response) {
                    return response.data;
                });
            },
            createBatch: function (orderSellProducts) {
                return $http.post("/api/orderSellProduct/createBatch", orderSellProducts).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/orderSellProduct/delete/" + id);
            }
        };
    }]);