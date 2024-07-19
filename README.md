# TireChangeSMIT
Proovitöö SMIT Java Arendaja töökohale

Used frameworks:
  Backend - Spring Boot
  Frontend - Angular 18

Possible backend requests:

    1. Get all free appointment times from all workshops
      - Method: GET
      - url: http://localhost:8080/api/appointments

    2. Get free appointment with filter
      - Method: GET
      - url: http://localhost:8080/api/appointments/filtered
      - params: {
        "from": "yyyy-mm-dd", # Search tire change times from date
        "until":  "yyyy-mm-dd", # Search available times until date,
        "workshopAddresses": [], # Addresses of the workshops that will be searched for appointment times
        "vehicleTypes": [] # All vehicle types that appointment place should service 
        }
        
    3. Book an appointment time
      - Method: PUT
      - url: http://localhost:8080/api/appointments/{appointmentId}
      - params: {
      "workshopAddress": string # Address of the workshop
      }

    4. Get all non testive workshop
      - Method: GET
      - url: http://localhost:8080/api/workshops
  
    5. Get all vehicle types avaiable in the system
      - Method: GET
      - url: http://localhost:8080/api/workshops/vehicle-types
        
