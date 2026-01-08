import { Route } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { OrderDashboardComponent } from './order-dashboard/order-dashboard.component';

export const appRoutes: Route[] = [
    {
        path: '',
        component: LayoutComponent,
        children: [
            { path: '', component: OrderDashboardComponent },
            { path: 'orders', component: OrderDashboardComponent }, // Reusing dashboard for now
        ]
    }
];