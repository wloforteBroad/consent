(function () {
    'use strict';


    angular
        .module('cmUserRoles', []);
    angular.module('cmUserRoles')
            .constant('USER_ROLES', {

             admin: 'ADMIN',
             chairperson: 'CHAIRPERSON',
             dacmember: 'DACMEMBER'
           });
    })();