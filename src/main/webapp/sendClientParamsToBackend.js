/**
 * Created by eduarddedu on 21/06/16.
 */
function sendParams() {
    var query = "?clientName=" + $("#clientname").val() + "&apiKey=" + $("#apikey").val();
    $.ajax({
        type: 'GET',
        url: window.location.origin + '/kontomatik-app/client-params' + query,

        success: function (response) {
            console.log('Got response: ' + response);

        },
        error: function (response) {
            console.log('Ajax request failed with response: ' + response.responseText);
            alert('Sorry, the request cannot be processed');

        }
    });
    // Hide form:
    $("#clientForm").css('visibility', 'hidden');
}