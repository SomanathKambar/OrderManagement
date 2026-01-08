import { setup, assign, fromPromise } from 'xstate';
import { api, CreateOrderRequest } from '../services/api';

export interface OrderContext {
  id?: string;
  items: string[];
  customer: {
    name: string;
    email: string;
  };
  error?: string;
}

export type OrderEvent =
  | { type: 'SUBMIT'; customer: { name: string; email: string }; items: string[] }
  | { type: 'VALIDATION_PASSED' }
  | { type: 'VALIDATION_FAILED'; error: string }
  | { type: 'PAYMENT_SUCCESS' }
  | { type: 'PAYMENT_FAILED'; error: string }
  | { type: 'SHIP' }
  | { type: 'DELIVER' }
  | { type: 'CANCEL'; reason: string }
  | { type: 'RESET' };

export const orderMachine = setup({
  types: {
    context: {} as OrderContext,
    events: {} as OrderEvent,
  },
  actors: {
    createOrder: fromPromise(async ({ input }: { input: { customer: { name: string, email: string}, items: string[] } }) => {
      // Constructing a payload that matches backend expectations
      // In a real app, these would come from a more complex form or user profile
      const payload: CreateOrderRequest = {
        customerId: `CUST-${Date.now()}`, // Mock ID
        customerName: input.customer.name,
        restaurantId: 'REST-001',
        restaurantName: 'Pizza Palace',
        orderType: 'FOOD',
        deliveryAddress: {
          street: '123 Main St',
          city: 'Tech City',
          zipCode: '90210'
        },
        restaurantAddress: {
          street: '456 Flavor Blvd',
          city: 'Food Town',
          zipCode: '90211'
        },
        items: input.items.map((item, idx) => ({
          itemId: `ITEM-${idx}`,
          name: item,
          quantity: 1,
          price: 19.99
        })),
        specialInstructions: 'Leave at front door'
      };
      
      return await api.createOrder(payload);
    }),
  },
  actions: {
    assignData: assign(({ event }) => {
      if (event.type === 'SUBMIT') {
        return {
          customer: event.customer,
          items: event.items
        };
      }
      return {};
    }),
    assignOrderId: assign(({ event }) => {
       // @ts-ignore - event.output is typed based on the actor
       return { id: event.output.orderId };
    }),
    setError: assign(({ event }) => {
        // @ts-ignore
        return { error: event.error?.message || 'Unknown error' };
    }),
    clearError: assign({ error: undefined }),
  },
}).createMachine({
  id: 'order',
  initial: 'created',
  context: {
    items: [],
    customer: { name: '', email: '' },
  },
  states: {
    created: {
      on: {
        SUBMIT: {
          target: 'validating',
          actions: 'assignData',
        },
      },
    },
    validating: {
      entry: 'clearError',
      invoke: {
        id: 'createOrder',
        src: 'createOrder',
        input: ({ context }) => ({
          customer: context.customer,
          items: context.items
        }),
        onDone: {
          target: 'paymentPending',
          actions: 'assignOrderId',
        },
        onError: {
          target: 'created',
          actions: 'setError',
        },
      },
      on: {
        CANCEL: 'cancelled',
      },
    },
    paymentPending: {
      on: {
        PAYMENT_SUCCESS: 'processing',
        PAYMENT_FAILED: {
            target: 'created', // Retry payment or edit order
            actions: 'setError'
        },
        CANCEL: 'cancelled',
      },
    },
    processing: {
      after: {
        3000: { target: 'shipped' }, // Auto-ship for demo
      },
      on: {
        SHIP: 'shipped',
        CANCEL: 'cancelled',
      },
    },
    shipped: {
      on: {
        DELIVER: 'delivered',
      },
    },
    delivered: {
      type: 'final',
      on: {
        RESET: 'created',
      },
    },
    cancelled: {
      type: 'final',
      on: {
        RESET: 'created',
      },
    },
  },
});