app.factory("OfferAttachService",
    ['$http', '$log', function ($http, $log) {
        return {
            upload: function (offer, files) {
                return $http.post("/api/offerAttach/upload",
                    {offerId: offer.id, files: files},
                    {
                        transformRequest: function () {
                            var formData = new FormData();
                            formData.append("offerId", offer.id);
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
            remove: function (offerAttach) {
                return $http.delete("/api/offerAttach/delete/" + offerAttach.id).then(function (response) {
                    return response.data;
                });
            },
            removeWhatever: function (offerAttach) {
                return $http.delete("/api/offerAttach/deleteWhatever/" + offerAttach.id);
            },
            findByOffer: function (offer) {
                return $http.get("/api/offerAttach/findByOffer/" + offer.id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);