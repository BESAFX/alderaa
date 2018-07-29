app.factory("CustomerContactService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/customerContact/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (customerContact) {
                return $http.post("/api/customerContact/create", customerContact).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/customerContact/delete/" + id);
            },
            update: function (customerContact) {
                return $http.put("/api/customerContact/update", customerContact).then(function (response) {
                    return response.data;
                });
            },
            findByCustomer: function (id) {
                return $http.get("/api/customerContact/findByCustomer/" + id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);