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

    public static formatDateForDateTimePattern(date: Date): string {
        const pad = (n: number) => n.toString().padStart(2, '0');
        return `${date.getFullYear()}-${pad(date.getMonth()+1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
    }

    public static formatDateTimeForDateTimePattern(date: Date, time: string): string {
        if (!date || !time) return '';

        const [hours, minutes] = time.split(':').map(Number);
        const combined = new Date(date);

        combined.setHours(hours, minutes, 0, 0);

        const pad = (n: number) => n.toString().padStart(2, '0');

        const yyyy = combined.getFullYear();
        const MM = pad(combined.getMonth() + 1);
        const dd = pad(combined.getDate());
        const HH = pad(combined.getHours());
        const mm = pad(combined.getMinutes());
        const ss = pad(combined.getSeconds());

        return `${yyyy}-${MM}-${dd}T${HH}:${mm}:${ss}`;
    }

    public static disablePastDates = (date: Date | null): boolean => {
        if(!date) return false;
        const today = new Date();
        today.setHours(0,0,0,0);
        return date >= today;
    };

    public static formatFlightTime(timeString: string): string {
    const date = new Date(timeString);

    const day = date.getDate();
    const month = date.toLocaleString('en-US', { month: 'short' });

    const hours = date.getHours();
    const minutes = date.getMinutes().toString().padStart(2, '0');

    const hour12 = hours % 12 || 12;
    const ampm = hours >= 12 ? 'PM' : 'AM';

    return `${day} ${month}, ${hour12}:${minutes} ${ampm}`;
  }

  public static displayTravelDuration(flight: any) : string {
    const departure = new Date(flight.departureTime);
    const arrival = new Date(flight.arrivalTime);
    const diffMs = arrival.getTime() - departure.getTime();
    const diffMinutes = Math.floor(diffMs / (1000 * 60));
    const hours = Math.floor(diffMinutes / 60);
    const minutes = diffMinutes % 60;

    const formattedEndTime = arrival.toLocaleTimeString("en-US", {
      hour: "2-digit",
      minute: "2-digit",
      hour12: true
    });

    return (minutes === 0) ? `${hours}h: ${formattedEndTime}` : `${hours}h ${minutes}m: ${formattedEndTime}` 
  }

    public static readonly seatLetters = ['A', 'B', 'C', 'D', 'E', 'F','H','I','J'];
}