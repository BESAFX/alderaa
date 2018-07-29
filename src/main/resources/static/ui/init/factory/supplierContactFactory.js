app.factory("SupplierContactService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/supplierContact/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (supplierContact) {
                return $http.post("/api/supplierContact/create", supplierContact).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/supplierContact/delete/" + id);
            },
            update: function (supplierContact) {
                return $http.put("/api/supplierContact/update", supplierContact).then(function (response) {
                    return response.data;
                });
            },
            findBySupplier: function (id) {
                return $http.get("/api/supplierContact/findBySupplier/" + id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);