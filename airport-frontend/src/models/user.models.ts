export interface UserProfile {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    role: string;
    phoneNumber: string;
    addresses?: Address[] 
}

export interface Address {
    addressName: string;
    country: string;
    city: string;
}

export interface KeycloakUserProfile {
    id: string;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    roles: string[];
}