app.factory("BillSellProductService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/billSellProduct/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/billSellProduct/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBillSell: function (id) {
                return $http.get("/api/billSellProduct/findByBillSell/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByProduct: function (id) {
                return $http.get("/api/billSellProduct/findByProduct/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (billSellProduct) {
                return $http.post("/api/billSellProduct/create", billSellProduct).then(function (response) {
                    return response.data;
                });
            },
            createBatch: function (billSellProducts) {
                return $http.post("/api/billSellProduct/createBatch", billSellProducts).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/billSellProduct/delete/" + id);
            }
        };
    }]);