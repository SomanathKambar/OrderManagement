import { Injectable, inject, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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
}

@Injectable({ providedIn: 'root' })
export class OrderService {
  private http = inject(HttpClient);
  private zone = inject(NgZone);
  private apiUrl = '/api/v1/orders';

  create(data: CreateOrderRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(this.apiUrl, data, {
        headers: { 'Idempotency-Key': crypto.randomUUID() }
    });
  }

  get(id: string): Observable<OrderResponse> {
    return this.http.get<OrderResponse>(`${this.apiUrl}/${id}`);
  }

  updateStatus(id: string, status: string, reason?: string): Observable<OrderResponse> {
    return this.http.put<OrderResponse>(`${this.apiUrl}/${id}/status`, { targetState: status, reason }, {
        headers: { 'Idempotency-Key': crypto.randomUUID() }
    });
  }

  cancel(id: string, reason: string): Observable<OrderResponse> {
    return this.http.delete<OrderResponse>(`${this.apiUrl}/${id}`, {
        params: { reason },
        headers: { 'Idempotency-Key': crypto.randomUUID() }
    });
  }

  getServerSentEvent(url: string): Observable<any> {
    return new Observable(observer => {
      const eventSource = new EventSource(url);

      eventSource.onopen = () => {
          console.log('Angular SSE Connected');
      };

      eventSource.addEventListener('status-update', (event: any) => {
        this.zone.run(() => {
          observer.next(JSON.parse(event.data));
        });
      });

      eventSource.onerror = error => {
        this.zone.run(() => {
          observer.error(error);
        });
      };

      return () => {
          console.log('Angular SSE Closed');
          eventSource.close();
      };
    });
  }
}