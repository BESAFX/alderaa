app.factory("OfferProductService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/offerProduct/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/offerProduct/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByOffer: function (id) {
                return $http.get("/api/offerProduct/findByOffer/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByProduct: function (id) {
                return $http.get("/api/offerProduct/findByProduct/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (offerProduct) {
                return $http.post("/api/offerProduct/create", offerProduct).then(function (response) {
                    return response.data;
                });
            },
            createBatch: function (offerProducts) {
                return $http.post("/api/offerProduct/createBatch", offerProducts).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/offerProduct/delete/" + id);
            }
        };
    }]);