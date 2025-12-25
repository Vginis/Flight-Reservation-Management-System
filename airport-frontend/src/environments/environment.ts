export const environment = {
    production: false,
    keycloak: {
        config: {
            url: 'http://localhost:8080',
            realm: 'airport',
            clientId: 'frontend'
        }
    },
    backend: {
        url:  'http://localhost:8010',
        ws_url: 'ws://localhost:8010'
    },
    frontend: {
        url: 'http://localhost:4200'
    }
};