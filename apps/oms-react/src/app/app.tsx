import React from 'react';
import { Route, Routes, Navigate } from 'react-router-dom';
import { MainLayout } from '../layouts/MainLayout';
import { OrderDashboardPage } from '../pages/OrderDashboardPage';
import { OrderList } from '../pages/OrderList';
import { OrderWizard } from '../pages/OrderWizard';

export function App() {
  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        <Route index element={<OrderDashboardPage />} />
        <Route path="orders" element={<OrderList />} />
        <Route path="orders/new" element={<OrderWizard />} />
        <Route path="orders/:id" element={<OrderDashboardPage />} /> {/* Reusing Dashboard for detail view for now */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
}

export default App;