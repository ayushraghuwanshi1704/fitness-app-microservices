const keycloakUrl = import.meta.env.VITE_KEYCLOAK_URL || 'http://localhost:8181';
const realm = import.meta.env.VITE_KEYCLOAK_REALM || 'fitness-oauth2';
const keycloakEndpoint = `${keycloakUrl}/realms/${realm}/protocol/openid-connect`;

export const authConfig = {
    clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID || 'oauth2-pkce-client',
    authorizationEndpoint: `${keycloakEndpoint}/auth`,
    tokenEndpoint: `${keycloakEndpoint}/token`,
    redirectUri: import.meta.env.VITE_REDIRECT_URI || 'http://localhost:5173',
    scope: 'openid profile email offline_access',
    onRefreshTokenExpire: (event) => event.logIn(),
  }
