import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OrderDashboardComponent } from './order-dashboard/order-dashboard.component';

@Component({
  standalone: true,
  imports: [RouterModule, OrderDashboardComponent],
  selector: 'app-root',
  template: `
    <app-order-dashboard></app-order-dashboard>
    <router-outlet></router-outlet>
  `,
})
export class AppComponent {
  title = 'oms-angular';
}
