# Delivery Module Architecture

## Vibe
The **Delivery Module** manages the physical fulfillment aspect of the platform. It handles the fleet of delivery partners, their availability, and the assignment of partners to orders.

## Core Responsibilities
- **Fleet Management**: CRUD operations for delivery partners and tracking their status (AVAILABLE, BUSY, OFFLINE).
- **Assignment Logic**: Logic for matching an order (in `READY_FOR_PICKUP` state) with a nearby or available delivery partner.
- **Real-time Status**: Updates the delivery partner's workload and current city location.

## Key Boundaries
- Depends on the `ordering` module's status but does not own the `Order` entity.
- Uses strategies (e.g., `CITY_BASED`) to optimize partner selection.
