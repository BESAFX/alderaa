app.factory("OfferService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/offer/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByCustomer: function (customerId) {
                return $http.get("/api/offer/findByCustomer/" + customerId).then(function (response) {
                    return response.data;
                });
            },
            create: function (contract) {
                return $http.post("/api/offer/create", contract).then(function (response) {
                    return response.data;
                });
            },
            update: function (offer) {
                return $http.put("/api/offer/update", offer).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/offer/delete/" + id);
            },
            findDaily: function () {
                return $http.get("/api/offer/findDaily").then(function (response) {
                    return response.data;
                });
            },
            send: function (offerId) {
                return $http.get("/api/offer/send/" + offerId).then(function (response) {
                    return response.data;
                });
            },
            filter: function (search) {
                return $http.get("/api/offer/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);