/**
 * Created by eduarddedu on 21/06/16
 */
function redirect() {
    var location = window.location.origin + '/kontomatik-app/credentials.xhtml';
    window.location.replace(location);
}
// Not used but provided here as example JQuery code
function renderForm() {
   $("#signOutForm").css('visibility', 'visible');
}




