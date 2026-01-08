import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderStore } from '../order.store';

@Component({
  selector: 'app-order-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="p-8 max-w-4xl mx-auto bg-slate-50 min-h-screen font-sans">
      <header class="mb-8">
        <h1 class="text-3xl font-bold text-slate-800">OMS Command Center (Angular)</h1>
        <p class="text-slate-500">Powered by Angular Signals & Native Federation</p>
      </header>

      <!-- 1. State Visualization -->
      <div class="bg-white p-6 rounded-xl shadow-sm border border-slate-200 mb-8">
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-lg font-semibold text-slate-700">Order Lifecycle</h2>
          <span [class]="'px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wide ' + store.statusColor()">
            {{ store.status() }}
          </span>
        </div>

        <!-- Progress Bar / Stepper -->
        <div class="relative flex justify-between items-center w-full">
            <div class="absolute top-1/2 left-0 w-full h-1 bg-gray-200 -z-0"></div>
            <!-- We iterate over a static list of steps for the visual stepper -->
            <ng-container *ngFor="let step of steps">
                <div class="relative z-10 flex flex-col items-center bg-white px-2">
                    <div class="w-8 h-8 rounded-full flex items-center justify-center border-2 transition-all duration-300"
                        [ngClass]="{
                            'border-blue-600 bg-blue-600 text-white': isStepActive(step),
                            'border-green-500 bg-green-500 text-white': isStepCompleted(step),
                            'border-gray-300 text-gray-400': !isStepActive(step) && !isStepCompleted(step)
                        }">
                        <span *ngIf="isStepCompleted(step)">✓</span>
                        <span *ngIf="!isStepCompleted(step)" class="text-xs">{{ getStepIndex(step) }}</span>
                    </div>
                    <span class="text-xs mt-2 font-medium" 
                          [ngClass]="{'text-blue-600': isStepActive(step), 'text-gray-400': !isStepActive(step)}">
                        {{ step }}
                    </span>
                </div>
            </ng-container>
        </div>

        <!-- Error State Display -->
        <div *ngIf="store.isCancelled()" class="mt-8 p-4 bg-red-50 border border-red-200 rounded-lg flex items-start animate-pulse">
            <div class="text-red-500 mr-3">⚠️</div>
            <div>
                <h3 class="font-bold text-red-800">Order Cancelled</h3>
                <p class="text-sm text-red-600">
                    Reason: {{ store.cancellationReason() || store.rejectionReason() || 'Unknown Error' }}
                </p>
            </div>
        </div>
      </div>

      <!-- 2. Dev Controls -->
      <div class="bg-white p-6 rounded-xl shadow-sm border border-slate-200">
        <h2 class="text-lg font-semibold mb-4 text-slate-700">Control Panel</h2>
        
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <!-- Created State Controls -->
            <div *ngIf="store.isCreated()" class="col-span-full flex gap-3">
                <input type="text" [(ngModel)]="customOrderId" class="flex-1 border p-2 rounded" placeholder="Enter Order ID">
                <button (click)="store.initializeOrder(customOrderId())" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                    Reset / Init
                </button>
                <button (click)="store.submitValidation()" class="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700">
                    Submit Validation
                </button>
            </div>

            <!-- Validating Controls -->
            <ng-container *ngIf="store.isValidating()">
                <button (click)="store.validationPassed()" class="btn-success">Pass Validation</button>
                <button (click)="store.validationFailed('Invalid Address')" class="btn-danger">Fail Validation</button>
            </ng-container>

            <!-- Payment Controls -->
            <ng-container *ngIf="store.isPaymentPending()">
                <button (click)="store.authorizePayment()" class="btn-success">Authorize Payment</button>
                <button (click)="store.rejectPayment('Insufficient Funds')" class="btn-danger">Reject Payment</button>
            </ng-container>

            <!-- Processing -->
            <button *ngIf="store.isProcessing()" (click)="store.shipOrder()" class="btn-primary">
                Ship Order
            </button>

            <!-- Shipped -->
            <button *ngIf="store.isShipped()" (click)="store.deliverOrder()" class="btn-success">
                Confirm Delivery
            </button>

            <!-- Global Cancel -->
            <button *ngIf="store.canCancel()" (click)="store.cancelOrder('Manual Admin Cancel')" class="col-span-full mt-4 bg-gray-100 text-gray-700 hover:bg-gray-200 px-4 py-2 rounded border border-gray-300">
                Force Cancel Order
            </button>

            <!-- Reset -->
            <button *ngIf="store.isDelivered() || store.isCancelled()" (click)="store.reset()" class="col-span-full bg-slate-800 text-white px-4 py-2 rounded shadow-lg hover:bg-slate-900 transition-colors">
                Start New Order
            </button>
        </div>

        <div class="mt-8 pt-4 border-t border-gray-100">
            <code class="block bg-gray-50 p-3 rounded text-xs text-gray-500 font-mono">
                State: {{ store.status() }} | ID: {{ store.orderId() }}
            </code>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .btn-primary { @apply bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition-colors; }
    .btn-success { @apply bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition-colors; }
    .btn-danger { @apply bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors; }
  `]
})
export class OrderDashboardComponent {
  store = inject(OrderStore);
  customOrderId = signal('ORD-2026-ANG-001');

  steps = ['Created', 'Validating', 'PaymentPending', 'Processing', 'Shipped', 'Delivered'];

  isStepActive(step: string): boolean {
    return this.store.status() === step;
  }

  isStepCompleted(step: string): boolean {
    const status = this.store.status();
    if (status === 'Cancelled') return false; 
    
    const currentIndex = this.steps.indexOf(status);
    const stepIndex = this.steps.indexOf(step);
    return currentIndex > stepIndex;
  }

  getStepIndex(step: string): number {
    return this.steps.indexOf(step) + 1;
  }
}
