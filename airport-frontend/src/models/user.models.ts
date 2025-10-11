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