export class Appointment {
    appointmentId = "";
    workshopName = "";
    workshopAddress = "";
    appointmentDate = "";
    appointmentTime = "";
    vehicleTypes: string[] = [];

    constructor(appointmentId: string, workshopName: string, workshopAddress: string, appointmentDate: string, appointmentTime:string, vehicleTypes: string[]) {
        this.appointmentId = appointmentId;
        this.workshopName = workshopName;
        this.workshopAddress = workshopAddress;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.vehicleTypes = vehicleTypes;
    }
}