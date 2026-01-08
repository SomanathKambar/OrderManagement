import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { computed, inject } from '@angular/core';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap, catchError, of } from 'rxjs';
import { OrderService } from './order.service';

export type OrderStatus = 'Created' | 'Validating' | 'PaymentPending' | 'Processing' | 'Shipped' | 'Delivered' | 'Cancelled';

export interface OrderState {
  orderId: string | null;
  dbId: number | null;
  status: OrderStatus;
  rejectionReason: string | null;
  cancellationReason: string | null;
  items: Array<{ id: string; name: string; quantity: number }>;
}

const initialState: OrderState = {
  orderId: 'ORD-2026-ANG-001',
  dbId: null,
  status: 'Created',
  rejectionReason: null,
  cancellationReason: null,
  items: []
};

export const OrderStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withComputed(({ status }) => ({
    isCreated: computed(() => status() === 'Created'),
    isValidating: computed(() => status() === 'Validating'),
    isPaymentPending: computed(() => status() === 'PaymentPending'),
    isProcessing: computed(() => status() === 'Processing'),
    isShipped: computed(() => status() === 'Shipped'),
    isDelivered: computed(() => status() === 'Delivered'),
    isCancelled: computed(() => status() === 'Cancelled'),
    canCancel: computed(() => !['Delivered', 'Cancelled'].includes(status())),
    statusColor: computed(() => {
        switch (status()) {
            case 'Created': return 'text-gray-500 bg-gray-100';
            case 'Validating': return 'text-yellow-600 bg-yellow-100';
            case 'PaymentPending': return 'text-blue-600 bg-blue-100';
            case 'Processing': return 'text-purple-600 bg-purple-100';
            case 'Shipped': return 'text-indigo-600 bg-indigo-100';
            case 'Delivered': return 'text-green-600 bg-green-100';
            case 'Cancelled': return 'text-red-600 bg-red-100';
            default: return 'text-gray-500';
        }
    })
  })),
  withMethods((store, orderService = inject(OrderService)) => {
    
    const connectStream = rxMethod<string>(pipe(
        switchMap((id) => orderService.getServerSentEvent(`/api/v1/orders/${id}/stream`).pipe(
            tap((event) => {
                console.log('Received SSE:', event);
                // Simple event mapping for demo
                // In a real app, map specific event types to status updates
            }),
            catchError((err) => {
                console.error('SSE Error', err);
                return of(null);
            })
        ))
    ));

    const syncUpdateStatus = rxMethod<{id: string, status: string, reason?: string}>(pipe(
        switchMap(({id, status, reason}) => orderService.updateStatus(id, status, reason).pipe(
            catchError(err => { console.error(err); return of(null); })
        ))
    ));

    const syncCreate = rxMethod<{orderId: string, items: any[]}>(pipe(
      switchMap((req) => orderService.create({
          customerId: 'user-ang-001',
          customerName: 'Angular User',
          restaurantId: 'rest-001',
          restaurantName: 'Pizza Place',
          orderType: 'PICKUP',
          deliveryAddress: 'N/A',
          restaurantAddress: '456 Market St',
          items: [],
          specialInstructions: 'Angular Signal Store'
      }).pipe(
          tap(res => {
              if (res && res.id) {
                  patchState(store, { dbId: res.id, status: 'Created' });
                  connectStream(String(res.id));
              }
          }), 
          catchError((err) => {
              console.error(err);
              return of(null);
          })
      ))
    ));

    return {
        initializeOrder(orderId: string) {
            patchState(store, { orderId, status: 'Created', rejectionReason: null, cancellationReason: null, dbId: null });
        },
        connectStream,
        syncCreate,
        syncUpdateStatus,
        
        submitValidation() {
            if (store.status() === 'Created') {
                patchState(store, { status: 'Validating' });
                if(store.dbId()) syncUpdateStatus({id: String(store.dbId()), status: 'VALIDATING'});
                else syncCreate({orderId: store.orderId()!, items: []}); 
            }
        },
        validationPassed() {
            if (store.status() === 'Validating') {
                patchState(store, { status: 'PaymentPending' });
                if(store.dbId()) syncUpdateStatus({id: String(store.dbId()), status: 'PAYMENT_PENDING'});
            }
        },
        validationFailed(reason: string) {
            if (store.status() === 'Validating') {
                patchState(store, { status: 'Cancelled', rejectionReason: reason });
                if(store.dbId()) syncUpdateStatus({id: String(store.dbId()), status: 'CANCELLED', reason});
            }
        },
        authorizePayment() {
            if (store.status() === 'PaymentPending') {
                patchState(store, { status: 'Processing' });
                if(store.dbId()) syncUpdateStatus({id: String(store.dbId()), status: 'PAID'});
            }
        },
        rejectPayment(reason: string) {
            if (store.status() === 'PaymentPending') {
                patchState(store, { status: 'Cancelled', rejectionReason: reason });
                if(store.dbId()) syncUpdateStatus({id: String(store.dbId()), status: 'CANCELLED', reason});
            }
        },
        shipOrder() {
            if (store.status() === 'Processing') {
                patchState(store, { status: 'Shipped' });
                if(store.dbId()) syncUpdateStatus({id: String(store.dbId()), status: 'OUT_FOR_DELIVERY'});
            }
        },
        deliverOrder() {
            if (store.status() === 'Shipped') {
                patchState(store, { status: 'Delivered' });
                if(store.dbId()) syncUpdateStatus({id: String(store.dbId()), status: 'DELIVERED'});
            }
        },
        cancelOrder(reason: string) {
            if (!['Delivered', 'Cancelled'].includes(store.status())) {
                patchState(store, { status: 'Cancelled', cancellationReason: reason });
                if(store.dbId()) {
                    orderService.cancel(String(store.dbId()), reason).subscribe();
                }
            }
        },
        reset() {
            patchState(store, initialState);
        }
    };
  })
);