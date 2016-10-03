embedKontomatik({
    client: window.clientName,
    divId: 'kontomatik',
    locale: 'en',
    showFavicons: true,
    ownerExternalId: id,
    onSuccess: function (target, sessionId, sessionIdSignature) {
        // Pass session params to the backend
        $.ajax({
            type: 'POST',
            url: window.location.origin + '/session/data',
            data:
            {
                sessionId: sessionId,
                sessionIdSignature: sessionIdSignature,
                target: target,
                ownerId: id
            },
            dataType: 'text',
            success: function (data, textStatus, jqXHR) {
                redirect('/commands.xhtml');

            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    },
    onError: function (exception) {
        alert('Error: ' + exception);
    }
});



