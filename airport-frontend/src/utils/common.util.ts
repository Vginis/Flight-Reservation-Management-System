export class CommonUtils {
    public static mapRoleToLabel(role: string): string {
        const roleToLabelMapping: { [key: string]: string } = {
            'airline_admin': 'Airline Administrator',
            'passenger': 'Passenger',
            'system_admin': 'System Administrator'
        }

        return roleToLabelMapping[role] || role;
    }

    public static readonly RoleObjects = [
        { key:"airline_admin", label: "Airline Administrator"},
        { key:"passenger", label: "Passenger"},
        { key:"system_admin", label: "System Administrator"}        
    ];

    public static readonly AIRLINE_ADMIN = "airline_admin";
    public static readonly SYSTEM_ADMIN = "system_admin";
    public static readonly PASSENGER = "passenger";
}