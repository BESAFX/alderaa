app.factory("BillPurchaseProductService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/billPurchaseProduct/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/billPurchaseProduct/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBillPurchase: function (id) {
                return $http.get("/api/billPurchaseProduct/findByBillPurchase/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByProduct: function (id) {
                return $http.get("/api/billPurchaseProduct/findByProduct/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (billPurchaseProduct) {
                return $http.post("/api/billPurchaseProduct/create", billPurchaseProduct).then(function (response) {
                    return response.data;
                });
            },
            createBatch: function (billPurchaseProducts) {
                return $http.post("/api/billPurchaseProduct/createBatch", billPurchaseProducts).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/billPurchaseProduct/delete/" + id);
            }
        };
    }]);