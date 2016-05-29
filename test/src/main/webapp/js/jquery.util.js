/**
 * Created by allan on 1/8/16.
 */
var util = {
    alertSuccess: function (title, msg) {
        jQuery.gritter.add({
            title: title,
            text: msg,
            class_name: 'growl-success',
            image: 'images/screen.png',
            sticky: false,
            time: 2000
        });
        return false;
    },
    alertError: function (title, msg) {
        jQuery.gritter.add({
            title: title,
            text: msg,
            class_name: 'growl-danger',
            image: 'images/screen.png',
            sticky: false,
            time: 2000
        });
        return false;
    },
    alertWarning: function (title, msg) {
        jQuery.gritter.add({
            title: title,
            text: msg,
            class_name: 'growl-warning',
            image: 'images/screen.png',
            sticky: false,
            time: 2000
        });
        return false;
    },
    alertInfo: function (title, msg) {
        jQuery.gritter.add({
            title: title,
            text: msg,
            class_name: 'growl-info',
            image: 'images/screen.png',
            sticky: false,
            time: 2000
        });
        return false;
    }
};