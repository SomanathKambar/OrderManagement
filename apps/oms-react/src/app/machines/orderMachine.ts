import { setup, assign } from 'xstate';

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
  actions: {
    assignData: assign(({ event }) => {
      if (event.type === 'SUBMIT') {
        return {
          customer: event.customer,
          items: event.items,
          id: `ORD-${Date.now()}`
        };
      }
      return {};
    }),
    setError: assign(({ event }) => {
        if (event.type === 'VALIDATION_FAILED' || event.type === 'PAYMENT_FAILED') {
            return { error: event.error };
        }
        return {};
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
      after: {
        2000: { target: 'paymentPending' }, // Mocking successful validation after 2s
      },
      on: {
        VALIDATION_PASSED: 'paymentPending',
        VALIDATION_FAILED: {
          target: 'created',
          actions: 'setError',
        },
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
