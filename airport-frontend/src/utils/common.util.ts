export class CommonUtils {
    public static mapRoleToLabel(role: string): string {
        const roleToLabelMapping: { [key: string]: string } = {
            'airline_admin': 'Airline Administrator',
            'passenger': 'Passenger',
            'system_admin': 'System Administrator'
        }

        return roleToLabelMapping[role] || role;
    }
}