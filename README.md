# Car Service Management System

This Spring Boot application provides a backend system for managing car service operations. It includes functionalities for creating and managing customers, job cards, jobs, quotations, technicians, and vehicles. The application uses Redis for caching to improve performance.

## Features

- **Customer Management**: Add, retrieve, and lookup customers.
- **Job Card Management**: Create and manage job cards associated with vehicles.
- **Job Management**: Assign and manage jobs for technicians.
- **Quotation Management**: Generate and manage quotations for job cards.
- **Technician Management**: Add and manage technician details.
- **Vehicle Management**: Lookup vehicles by registration number.

## Technologies Used

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Redis for Caching**
- **PostgreSQL**

## Endpoints

### Customer Controller
- **GET /api/customers/lookup**: Lookup customer by contact.
- **POST /api/customers**: Add a new customer.

### Job Card Controller
- **POST /api/jobcards**: Create a new job card.
- **GET /api/jobcards**: Retrieve all job cards.
- **GET /api/jobcards/{id}**: Get job card by ID.
- **PUT /api/jobcards/{id}**: Update job card details.

### Job Controller
- **POST /api/jobs**: Create a new job.
- **GET /api/jobs**: Retrieve all jobs.
- **GET /api/jobs/{id}**: Get job by ID.
- **PUT /api/jobs/{id}**: Update job details.

### Quotation Controller
- **POST /api/quotations**: Create a new quotation.
- **GET /api/quotations**: Retrieve all quotations.
- **GET /api/quotations/{id}**: Get quotation by ID.
- **GET /api/quotations/jobcard/{jobCardId}**: Get quotations by job card ID.
- **PUT /api/quotations/{id}**: Update quotation details.

### Technician Controller
- **POST /api/technicians**: Add a new technician.
- **GET /api/technicians**: Retrieve all technicians.
- **GET /api/technicians/{id}**: Get technician by ID.
- **PUT /api/technicians/{id}**: Update technician details.
- **DELETE /api/technicians/{id}**: Delete technician.

### Vehicle Controller
- **GET /api/vehicles/lookup**: Lookup vehicle by registration number.

## Running the Application

1. **Clone the repository**:
   ```sh
   git clone https://github.com/lakshayman/CarService.git
   cd CarService
   ```

2. **Update application.properties**:
   Configure your PostgreSQL and Redis settings in the `src/main/resources/application.properties` file.

3. **Build and Run**:
   ```sh
   ./mvnw spring-boot:run
   ```

4. **Access the API**:
   The application will be running at `http://localhost:8080`.

## Postman Collection

You can use the following Postman collection to test the APIs:

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://god.gw.postman.com/run-collection/15788027-38e24b55-e777-4a75-a1e3-84c5e09a6aaa?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D15788027-38e24b55-e777-4a75-a1e3-84c5e09a6aaa%26entityType%3Dcollection%26workspaceId%3D9548aee0-7403-441f-9441-198e34791690)

## Contributing

Feel free to fork the repository and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
