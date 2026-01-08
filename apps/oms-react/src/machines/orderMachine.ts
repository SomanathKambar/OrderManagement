import { setup, assign, fromPromise, fromCallback } from 'xstate';
import { orderApi } from '../api/orderApi';

// 1. Define Context (Data)
export interface OrderContext {
  orderId: string | null;
  dbId: number | null; // ID from database
  items: Array<{ id: string; name: string; quantity: number }>;
  paymentMethod: string | null;
  shippingAddress: string | null;
  cancellationReason?: string;
  rejectionReason?: string;
  error?: string;
}

// 2. Define Events
export type OrderEvent =
  | { type: 'CREATE_ORDER'; orderId: string; items: any[] }
  | { type: 'SUBMIT_VALIDATION' }
  | { type: 'VALIDATION_PASSED' }
  | { type: 'VALIDATION_FAILED'; reason: string }
  | { type: 'PAYMENT_AUTHORIZED' }
  | { type: 'PAYMENT_REJECTED'; reason: string }
  | { type: 'START_PROCESSING' }
  | { type: 'SHIP_ORDER' }
  | { type: 'DELIVER_ORDER' }
  | { type: 'CANCEL_ORDER'; reason: string }
  | { type: 'SSE_UPDATE'; payload: any }; // New event for SSE

// 3. Define Machine
export const orderMachine = setup({
  types: {
    context: {} as OrderContext,
    events: {} as OrderEvent,
  },
  actors: {
    createOrderOnServer: fromPromise(async ({ input }: { input: { orderId: string; items: any[] } }) => {
        return await orderApi.create({
            customerId: 'user-123',
            customerName: 'Demo User',
            restaurantId: 'rest-001',
            restaurantName: 'Pizza Place',
            orderType: 'DELIVERY',
            deliveryAddress: '123 Main St',
            restaurantAddress: '456 Market St',
            items: input.items.map(i => ({ menuItemId: i.id, name: i.name, quantity: i.quantity, price: 10 })),
            specialInstructions: 'Created via XState'
        });
    }),
    updateStatusOnServer: fromPromise(async ({ input }: { input: { id: string; status: string; reason?: string } }) => {
        return await orderApi.updateStatus(input.id, input.status, input.reason);
    }),
    cancelOrderOnServer: fromPromise(async ({ input }: { input: { id: string; reason: string } }) => {
        return await orderApi.cancel(input.id, input.reason);
    }),
    listenToUpdates: fromCallback(({ input, sendBack }: { input: { id: string }, sendBack: (event: OrderEvent) => void }) => {
        if (!input.id) return undefined;
        
        console.log(`Connecting SSE for order ${input.id}`);
        const eventSource = new EventSource(`/api/v1/orders/${input.id}/stream`);

        eventSource.onopen = () => console.log('SSE Connected');
        
        eventSource.addEventListener('status-update', (e) => {
             const data = JSON.parse(e.data);
             console.log('SSE Update:', data);
             // We map backend events to XState events
             // Ideally backend events should map 1:1, but here we might need a mapper
             // For simplicity, we just log it or trigger a generic refresh/update
             sendBack({ type: 'SSE_UPDATE', payload: data });
             
             // Simple mapping logic:
             const eventType = data.eventType || data.type; // Adjust based on actual JSON
             if (eventType === 'OrderValidatedEvent') sendBack({ type: 'VALIDATION_PASSED' });
             if (eventType === 'OrderPaidEvent') sendBack({ type: 'PAYMENT_AUTHORIZED' });
             if (eventType === 'OrderShippedEvent') sendBack({ type: 'SHIP_ORDER' });
             if (eventType === 'OrderDeliveredEvent') sendBack({ type: 'DELIVER_ORDER' });
             if (eventType === 'OrderCancelledEvent') sendBack({ type: 'CANCEL_ORDER', reason: data.reason });
        });

        return () => {
            console.log('Closing SSE');
            eventSource.close();
        };
    })
  },
  actions: {
    assignOrderDetails: assign(({ event }) => {
      if (event.type === 'CREATE_ORDER') {
        return {
          orderId: event.orderId,
          items: event.items,
        };
      }
      return {};
    }),
    assignDbId: assign(({ event }) => {
        // @ts-ignore
        if (event.output && event.output.id) {
             // @ts-ignore
            return { dbId: event.output.id };
        }
        return {};
    }),
    setRejectionReason: assign(({ event }) => {
       if (event.type === 'VALIDATION_FAILED' || event.type === 'PAYMENT_REJECTED') {
         return { rejectionReason: event.reason };
       }
       return {};
    }),
    setCancellationReason: assign(({ event }) => {
       if (event.type === 'CANCEL_ORDER') {
         return { cancellationReason: event.reason };
       }
       return {};
    }),
    assignError: assign(({ event }) => {
         // @ts-ignore
        return { error: event.error?.message || 'Unknown error' };
    })
  },
}).createMachine({
  id: 'orderLifecycle',
  initial: 'Created',
  context: {
    orderId: null,
    dbId: null,
    items: [],
    paymentMethod: null,
    shippingAddress: null,
  },
  // Global invoke to listen whenever we have a DB ID?
  // Or invoke per state. 
  // Let's invoke at the root but guard it, or invoke in specific states.
  // Actually, once created, we want to listen regardless of state.
  // So we can have a parallel state or invoke at root level?
  // XState v5 allows invoking at root.
  // But context.dbId is initially null. The actor will be restarted when input changes? 
  // XState doesn't auto-restart invoke on context change unless key changes.
  // So we'll put it in a state that we enter after creation.
  states: {
    Created: {
      on: {
        CREATE_ORDER: {
          target: 'Creating',
          actions: 'assignOrderDetails',
        },
      },
    },
    Creating: {
        invoke: {
            id: 'createOrder',
            src: 'createOrderOnServer',
            input: ({ context }) => ({ orderId: context.orderId!, items: context.items }),
            onDone: {
                target: 'Active', // Move to a parent state 'Active' that manages the listener
                actions: 'assignDbId'
            },
            onError: {
                target: 'Created',
                actions: 'assignError'
            }
        },
        on: {
            SUBMIT_VALIDATION: 'Validating', // Fallback for manual/demo without server
            CANCEL_ORDER: { target: 'Cancelled', actions: 'setCancellationReason' }
        }
    },
    // Active state wraps all "Alive" states and runs the SSE listener
    Active: {
        invoke: {
            id: 'sseListener',
            src: 'listenToUpdates',
            input: ({ context }) => ({ id: String(context.dbId) })
        },
        initial: 'Created', // "Server Created"
        states: {
            Created: {
                on: {
                    SUBMIT_VALIDATION: 'Validating'
                }
            },
            Validating: {
                invoke: {
                    id: 'validate',
                    src: 'updateStatusOnServer',
                    input: ({ context }) => ({ id: String(context.dbId), status: 'VALIDATING' }),
                },
                on: {
                    VALIDATION_PASSED: 'PaymentPending',
                    VALIDATION_FAILED: { target: '#orderLifecycle.Cancelled', actions: 'setRejectionReason' },
                }
            },
            PaymentPending: {
                on: {
                    PAYMENT_AUTHORIZED: 'Processing',
                    PAYMENT_REJECTED: { target: '#orderLifecycle.Cancelled', actions: 'setRejectionReason' },
                }
            },
            Processing: {
                on: {
                    SHIP_ORDER: 'Shipped',
                }
            },
            Shipped: {
                on: {
                    DELIVER_ORDER: { target: '#orderLifecycle.Delivered' }
                }
            }
        },
        on: {
            // Global transitions from SSE events handled by the listener
            CANCEL_ORDER: { target: 'Cancelled', actions: 'setCancellationReason' },
            // If SSE sends VALIDATION_PASSED, the nested state handles it? 
            // Yes, if the event matches the transition name.
            // But we need to make sure the event names match what we sendBack().
            // They do: VALIDATION_PASSED, etc.
        }
    },
    Delivered: {
      type: 'final',
    },
    Cancelled: {
      type: 'final',
    },
  },
});
