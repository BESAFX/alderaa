app.factory("BillSellAttachService",
    ['$http', '$log', function ($http, $log) {
        return {
            upload: function (billSell, files) {
                return $http.post("/api/billSellAttach/upload",
                    {billSellId: billSell.id, files: files},
                    {
                        transformRequest: function () {
                            var formData = new FormData();
                            formData.append("billSellId", billSell.id);
                            angular.forEach(files, function (file) {
                                formData.append('files', file);
                            });
                            return formData;
                        },
                        headers: {
                            'Content-Type': undefined
                        }
                    }
                ).then(function (response) {
                    return response.data;
                });

            },
            remove: function (billSellAttach) {
                return $http.delete("/api/billSellAttach/delete/" + billSellAttach.id).then(function (response) {
                    return response.data;
                });
            },
            removeWhatever: function (billSellAttach) {
                return $http.delete("/api/billSellAttach/deleteWhatever/" + billSellAttach.id);
            },
            findByBillSell: function (billSell) {
                return $http.get("/api/billSellAttach/findByBillSell/" + billSell.id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);