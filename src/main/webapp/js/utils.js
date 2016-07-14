/**
 * Created by eduarddedu on 21/06/16
 */
function redirect() {
    var location = window.location.origin + '/kontomatik-app/credentials.xhtml';
    window.location.replace(location);
}
// Not used, shown here as example JQuery
function showForm1() {
    $("#form1").css('visibility', 'visible');
}
function showForm2() {
    $("#form2").css('visibility', 'visible');
}
function showForm3() {
    $("#form3").css('visibility', 'visible');
}