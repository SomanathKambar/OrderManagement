export interface CreateOrderRequest {
    customerId: string;
    customerName: string;
    restaurantId: string;
    restaurantName: string;
    orderType: 'DELIVERY' | 'PICKUP';
    deliveryAddress: string;
    restaurantAddress: string;
    items: Array<{
        menuItemId: string;
        name: string;
        quantity: number;
        price: number;
    }>;
    specialInstructions?: string;
}

export interface OrderResponse {
    id: number;
    customerId: string;
    status: string;
    items: any[];
    // Add other fields as needed
}

export interface UpdateStatusRequest {
    targetState: string;
    reason?: string;
}

const API_BASE = '/api/v1/orders';

export const orderApi = {
    create: async (data: CreateOrderRequest): Promise<OrderResponse> => {
        const response = await fetch(API_BASE, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Idempotency-Key': crypto.randomUUID()
            },
            body: JSON.stringify(data)
        });
        if (!response.ok) throw new Error('Failed to create order');
        return response.json();
    },

    get: async (id: string): Promise<OrderResponse> => {
        const response = await fetch(`${API_BASE}/${id}`);
        if (!response.ok) throw new Error('Failed to fetch order');
        return response.json();
    },

    updateStatus: async (id: string, status: string, reason?: string): Promise<OrderResponse> => {
        const response = await fetch(`${API_BASE}/${id}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Idempotency-Key': crypto.randomUUID()
            },
            body: JSON.stringify({ targetState: status, reason })
        });
        if (!response.ok) throw new Error('Failed to update status');
        return response.json();
    },

    cancel: async (id: string, reason: string): Promise<OrderResponse> => {
        const response = await fetch(`${API_BASE}/${id}?reason=${encodeURIComponent(reason)}`, {
            method: 'DELETE',
            headers: {
                'Idempotency-Key': crypto.randomUUID()
            }
        });
        if (!response.ok) throw new Error('Failed to cancel order');
        return response.json();
    }
};
