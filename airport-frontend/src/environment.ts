export const environment = {
    production: false,
    keycloak: {
        config: {
        url: 'http://localhost:8080',
        realm: 'airport',
        clientId: 'frontend'
        },
        initOptions: {
        onLoad: 'login-required',
        checkLoginIframe: false
        }
    }
};