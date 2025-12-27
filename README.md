# Order Management System

This project is a Spring Boot-based Order Management System, designed as a foundational MVP (Minimum Viable Product) to handle order creation, tracking, and delivery partner assignments. It demonstrates a clean architecture with clear separation of concerns, utilizing modern Java and Spring Boot features.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Use Cases](#use-cases)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Building the Project](#building-the-project)
  - [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Future Enhancements](#future-enhancements)
- [License](#license)

## Features

-   **Order Creation**: Create new orders with details like customer information, order items, and delivery address.
-   **Order Status Management**: Update the status of orders (e.g., PENDING, PROCESSING, DELIVERED, CANCELLED).
-   **Delivery Partner Management**: Register and manage delivery partners, including their availability status.
-   **Order Assignment**: Assign orders to available delivery partners using pluggable strategies (e.g., City-Based, Nearest-First).
-   **Domain-Driven Design**: Clear separation between domain models, entities, DTOs, and services.
-   **RESTful API**: Exposes a comprehensive set of REST endpoints for all core functionalities.
-   **In-memory Database**: Uses H2 database for easy setup and development.

## Tech Stack

-   **Java**: Version 17
-   **Spring Boot**: Version 3.2.0
-   **Maven**: Build automation tool
-   **Spring Data JPA**: For data persistence and repository abstraction.
-   **Hibernate**: JPA implementation.
-   **H2 Database**: In-memory database for development and testing.
-   **Lombok**: To reduce boilerplate code (e.g., getters, setters, constructors).
-   **JUnit 5 & Mockito**: For unit and integration testing.

## Architecture

The project follows a layered architecture, typical for Spring Boot applications, with elements of Domain-Driven Design:

-   **`controller`**: Handles incoming HTTP requests and returns responses. Contains DTOs for request/response bodies.
-   **`service`**: Contains business logic and orchestrates operations between different components.
-   **`domain`**: Core business logic, including models, enums, factories, and domain repositories interfaces.
-   **`data`**: Persistence layer, including JPA entities and concrete repository implementations.
-   **`config`**: Application configuration, e.g., WebConfig.
-   **`exception`**: Custom exceptions and global exception handling.
-   **`util`**: Utility classes and mappers.

## Use Cases

-   **E-commerce Backend**: A core component for managing orders in an online retail system.
-   **Food Delivery Platform**: Handling order placement, assignment to delivery personnel, and status updates.
-   **Logistics and Supply Chain**: Tracking goods movement and managing delivery tasks.
-   **Task Assignment System**: Generic system for assigning tasks to available workers based on various strategies.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

-   Java Development Kit (JDK) 17 or higher
-   Maven 3.6.0 or higher

### Building the Project

Navigate to the project's root directory and run the following Maven command:

```bash
mvn clean install
```

This will compile the code, run tests, and package the application into a JAR file.

### Running the Application

After building, you can run the Spring Boot application using:

```bash
java -jar target/OrderManagement-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run it directly via Maven Spring Boot plugin:

```bash
mvn spring-boot:run
```

The application will start on port `8080` (by default).

## API Endpoints

The API documentation (e.g., Swagger UI) might be available at `/swagger-ui.html` if configured, or you can inspect the `controller` package for available endpoints.

Common endpoints might include:

-   `POST /api/orders`: Create a new order.
-   `GET /api/orders/{id}`: Retrieve order details.
-   `PUT /api/orders/{id}/status`: Update order status.
-   `POST /api/delivery-partners`: Register a new delivery partner.
-   `PUT /api/delivery-partners/{id}/status`: Update delivery partner status.
-   `POST /api/orders/{orderId}/assign/{strategy}`: Assign an order using a specified strategy.

## Future Enhancements

As an MVP, this project lays the groundwork for numerous potential enhancements:

-   **User Authentication & Authorization**: Secure API endpoints with user roles (customer, delivery partner, admin).
-   **Payment Integration**: Incorporate payment gateways for order processing.
-   **Real-time Tracking**: Implement WebSockets for real-time order tracking and delivery partner location updates.
-   **Notifications**: Add email/SMS notifications for order status changes.
-   **Advanced Assignment Strategies**: More sophisticated algorithms considering traffic, weather, load balancing.
-   **External Database**: Migrate from H2 to a persistent database like PostgreSQL or MySQL.
-   **Caching**: Implement caching mechanisms for frequently accessed data.
-   **Monitoring & Logging**: Enhance logging and add monitoring tools (e.g., Prometheus, Grafana).
-   **Microservices Architecture**: Break down the monolithic application into smaller, independent services.
-   **GraphQL API**: Offer a GraphQL interface for more flexible data fetching.

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for more details.

```
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/

   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

   1. Definitions.

      "License" shall mean the terms and conditions for use, reproduction,
      and distribution as defined by Sections 1 through 9 of this document.

      "Licensor" shall mean the copyright owner or entity authorized by
      the copyright owner that is granting the License.

      "Legal Entity" shall mean the union of the acting entity and all
      other entities that control, are controlled by, or are under common
      control with that entity. For the purposes of this definition,
      "control" means (i) the power, direct or indirect, to cause the
      direction or management of such entity, whether by contract or
      otherwise, or (ii) ownership of fifty percent (50%) or more of the
      outstanding shares, or (iii) beneficial ownership of such entity.

      "You" (or "Your") shall mean an individual or Legal Entity
      exercising permissions granted by this License.

      "Source" form shall mean the preferred form for making modifications,
      including but not limited to software source code, documentation
      source, and configuration files.

      "Object" form shall mean any form resulting from mechanical
      transformation or translation of a Source form, including but
      not limited to compiled object code, generated documentation,
      and conversions to other media types.

      "Work" shall mean the work of authorship, whether in Source or
      Object form, made available under the License, as indicated by a
      copyright notice that is included in or attached to the Work
      (an example is provided in the Appendix below).

      "Derivative Works" shall mean any work, whether in Source or Object
      form, that is based on (or derived from) the Work and for which the
      editorial revisions, annotations, elaborations, or other modifications
      represent, as a whole, an original work of authorship. For purposes
      of this License, Derivative Works shall not include works that remain
      separable from, or merely link (or bind by name) to the interfaces of,
      the Work and Derivative Works thereof.

      "Contribution" shall mean any work of authorship, including
      the original version of the Work and any modifications or additions
      to that Work or Derivative Works thereof, that is intentionally
      submitted to Licensor for inclusion in the Work by the copyright owner
      or by an individual or Legal Entity authorized to submit on behalf of
      the copyright owner. For the purposes of this definition, "submitted"
      means any form of electronic, verbal, or written communication sent
      to the Licensor or its representatives, including but not limited to
      communication on electronic mailing lists, source code control systems,
      and issue tracking systems that are managed by, or on behalf of, the
      Licensor for the purpose of discussing and improving the Work, but
      excluding communication that is conspicuously marked or otherwise
      designated in writing by the copyright owner as "Not a Contribution."

      "Contributor" shall mean Licensor and any individual or Legal Entity
      on behalf of whom a Contribution has been received by Licensor and
      subsequently incorporated within the Work.

   2. Grant of Copyright License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      copyright license to reproduce, prepare Derivative Works of,
      publicly display, publicly perform, sublicense, and distribute the
      Work and such Derivative Works in Source or Object form.

   3. Grant of Patent License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      (except as stated in this section) patent license to make, have made,
      use, offer to sell, sell, import, and otherwise transfer the Work,
      where such license applies only to those patent claims licensed
      by such Contributor that are necessarily infringed by their
      Contribution(s) alone or by combination of their Contribution(s)
      with the Work to which such Contribution(s) was submitted. If You
      institute patent litigation against any entity (including a
      cross-claim or counterclaim in a lawsuit) alleging that the Work
      or a Contribution incorporated within the Work constitutes direct
      or contributory patent infringement, then any patent licenses
      granted to You under this License for that Work shall terminate
      as of the date such litigation is filed.

   4. Redistribution. You may reproduce and distribute copies of the
      Work or Derivative Works thereof in any medium, with or without
      modifications, and in Source or Object form, provided that You
      meet the following conditions:

      (a) You must give any other recipients of the Work or
          Derivative Works a copy of this License; and

      (b) You must cause any modified files to carry prominent notices
          stating that You changed the files; and

      (c) You must retain, in the Source form of any Derivative Works
          that You distribute, all copyright, patent, trademark, and
          attribution notices from the Source form of the Work,
          excluding those notices that do not pertain to any part of
          the Derivative Works; and

      (d) If the Work includes a "NOTICE" text file as part of its
          distribution, then any Derivative Works that You distribute must
          include a readable copy of the attribution notices contained
          within such NOTICE file, excluding those notices that do not
          pertain to any part of the Derivative Works, in at least one
          of the following places: within a NOTICE text file distributed
          as part of the Derivative Works; within the Source form or
          documentation, if provided along with the Derivative Works; or,
          within a display generated by the Derivative Works, if and
          wherever such third-party notices normally appear. The contents
          of the NOTICE file are for informational purposes only and
          do not modify the License. You may add Your own attribution
          notices within Derivative Works that You distribute, alongside
          or as an addendum to the NOTICE text from the Work, provided
          that such additional attribution notices cannot be construed
          as modifying the License.

      You may add Your own copyright statement to Your modifications and
      may provide additional or different license terms and conditions
      for use, reproduction, or distribution of Your modifications, or
      for any such Derivative Works as a whole, provided Your use,
      reproduction, and distribution of the Work otherwise complies with
      the conditions stated in this License.

   5. Submission of Contributions. Unless You explicitly state otherwise,
      any Contribution intentionally submitted for inclusion in the Work
      by You to the Licensor shall be under the terms and conditions of
      this License, without any additional terms or conditions.
      Notwithstanding the above, nothing herein shall supersede or modify
      the terms of any separate license agreement you may have executed
      with Licensor regarding such Contributions.

   6. Trademarks. This License does not grant permission to use the trade
      names, trademarks, service marks, or product names of the Licensor,
      except as required for reasonable and customary use in describing the
      origin of the Work and reproducing the content of the NOTICE file.

   7. Disclaimer of Warranty. Unless required by applicable law or
      agreed to in writing, Licensor provides the Work (and each
      Contributor provides its Contributions) on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
      implied, including, without limitation, any warranties or conditions
      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
      PARTICULAR PURPOSE. You are solely responsible for determining the
      appropriateness of using or redistributing the Work and assume any
      risks associated with Your exercise of permissions under this License.

   8. Limitation of Liability. In no event and under no legal theory,
      whether in tort (including negligence), contract, or otherwise,
      unless required by applicable law (such as deliberate and grossly
      negligent acts) or agreed to in writing, shall any Contributor be
      liable to You for damages, including any direct, indirect, special,
      incidental, or consequential damages of any character arising as a
      result of this License or out of the use or inability to use the
      Work (including but not limited to damages for loss of goodwill,
      work stoppage, computer failure or malfunction, or any and all
      other commercial damages or losses), even if such Contributor
      has been advised of the possibility of such damages.

   9. Accepting Warranty or Additional Liability. While redistributing
      the Work or Derivative Works thereof, You may choose to offer,
      and charge a fee for, acceptance of support, warranty, indemnity,
      or other liability obligations and/or rights consistent with this
      License. However, in accepting such obligations, You may act only
      on Your own behalf and on Your sole responsibility, not on behalf
      of any other Contributor, and only if You agree to indemnify,
      defend, and hold each Contributor harmless for any liability
      incurred by, or claims asserted against, such Contributor by reason
      of Your accepting any such warranty or additional liability.

   END OF TERMS AND CONDITIONS

   APPENDIX: How to apply the Apache License to your work.

      To apply the Apache License to your work, attach the following
      boilerplate notice, with the fields enclosed by brackets "{}"
      replaced with your own identifying information. (Don't include the
      brackets!) The text should be enclosed in the appropriate comment
      syntax for the file format. We also recommend that a file named
      NOTICE is accompanied with this work.

      Copyright {yyyy} {name of copyright owner}

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.
```
