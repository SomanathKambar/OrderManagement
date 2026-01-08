import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="min-h-screen flex bg-slate-50">
        <!-- Sidebar -->
        <aside class="w-64 bg-slate-900 text-slate-300 hidden md:flex flex-col">
            <div class="p-6 border-b border-slate-800">
                <h1 class="text-xl font-bold text-white flex items-center gap-2">
                    <span class="w-8 h-8 bg-indigo-600 rounded-lg flex items-center justify-center text-white">OMS</span>
                    Angular
                </h1>
            </div>
            <nav class="flex-1 p-4 space-y-1">
                <a routerLink="/" routerLinkActive="bg-indigo-600 text-white" [routerLinkActiveOptions]="{exact: true}" 
                   class="flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium hover:bg-slate-800 transition-colors">
                    Dashboard
                </a>
                <a routerLink="/orders" routerLinkActive="bg-indigo-600 text-white" 
                   class="flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium hover:bg-slate-800 transition-colors">
                    Orders
                </a>
            </nav>
        </aside>

        <!-- Main -->
        <main class="flex-1 flex flex-col">
            <header class="h-16 bg-white border-b border-slate-200 flex items-center justify-between px-6">
                <h2 class="font-semibold text-slate-700">Order Management System</h2>
                <div class="flex items-center gap-4">
                    <span class="text-sm text-slate-500">Admin User</span>
                    <div class="w-8 h-8 rounded-full bg-indigo-100 text-indigo-600 flex items-center justify-center font-bold">A</div>
                </div>
            </header>
            <div class="p-6 overflow-auto flex-1">
                <router-outlet></router-outlet>
            </div>
        </main>
    </div>
  `
})
export class LayoutComponent {}
