export interface OrderItem {
  itemId: string;
  name: string;
  quantity: number;
  price: number;
}

export interface Address {
  street: string;
  city: string;
  zipCode: string;
}

export interface CreateOrderRequest {
  customerId: string;
  customerName: string;
  restaurantId: string;
  restaurantName: string;
  orderType: 'FOOD' | 'DELIVERY';
  deliveryAddress: Address;
  restaurantAddress: Address;
  items: OrderItem[];
  specialInstructions?: string;
}

export const api = {
  createOrder: async (orderData: CreateOrderRequest) => {
    const response = await fetch('/api/v1/orders', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Idempotency-Key': crypto.randomUUID(), // Simple client-side generation
      },
      body: JSON.stringify(orderData),
    });

    if (!response.ok) {
      throw new Error(`API Error: ${response.statusText}`);
    }

    return response.json();
  },
};
