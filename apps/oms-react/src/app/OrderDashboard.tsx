import React, { useState } from 'react';
import { useMachine } from '@xstate/react';
import { orderMachine } from './machines/orderMachine';
import { 
  CheckCircle, 
  Package, 
  Truck, 
  CreditCard, 
  XCircle, 
  RefreshCw, 
  AlertCircle 
} from 'lucide-react';
import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';

function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

const steps = [
  { id: 'created', label: 'Draft', icon: Package },
  { id: 'validating', label: 'Validating', icon: RefreshCw },
  { id: 'paymentPending', label: 'Payment', icon: CreditCard },
  { id: 'processing', label: 'Processing', icon: RefreshCw },
  { id: 'shipped', label: 'Shipped', icon: Truck },
  { id: 'delivered', label: 'Delivered', icon: CheckCircle },
];

export const OrderDashboard: React.FC = () => {
  const [state, send] = useMachine(orderMachine);
  const [form, setForm] = useState({ name: '', email: '', item: '' });

  const currentStepIndex = steps.findIndex(s => s.id === state.value);
  const isCancelled = state.matches('cancelled');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (form.name && form.item) {
      send({ 
        type: 'SUBMIT', 
        customer: { name: form.name, email: form.email }, 
        items: [form.item] 
      });
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8 font-sans text-gray-900">
      {/* Header / Breadcrumbs */}
      <div className="mb-8">
        <div className="text-sm text-gray-500 mb-2">Home / Orders / New Order</div>
        <h1 className="text-3xl font-bold tracking-tight">Order Management</h1>
        <p className="text-gray-500">Create and track order lifecycle.</p>
      </div>

      {/* Analytics Cards (Mock) */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <div className="text-sm font-medium text-gray-500">Avg Lead Time</div>
          <div className="text-2xl font-bold text-gray-900">12m 30s</div>
        </div>
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <div className="text-sm font-medium text-gray-500">Picking Accuracy</div>
          <div className="text-2xl font-bold text-green-600">99.8%</div>
        </div>
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <div className="text-sm font-medium text-gray-500">Active Orders</div>
          <div className="text-2xl font-bold text-blue-600">24</div>
        </div>
      </div>

      {/* Main Content */}
      <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
        {/* Progress Stepper */}
        <div className="bg-gray-50 p-6 border-b border-gray-100">
          <div className="flex justify-between items-center max-w-4xl mx-auto">
             {steps.map((step, idx) => {
               const Icon = step.icon;
               const isActive = state.matches(step.id);
               const isCompleted = currentStepIndex > idx || state.matches('delivered');
               
               return (
                 <div key={step.id} className="flex flex-col items-center relative z-10">
                   <div className={cn(
                     "w-10 h-10 rounded-full flex items-center justify-center transition-all duration-300",
                     isActive ? "bg-blue-600 text-white shadow-lg scale-110" : 
                     isCompleted ? "bg-green-500 text-white" : "bg-gray-200 text-gray-400"
                   )}>
                     <Icon size={18} />
                   </div>
                   <span className={cn(
                     "text-xs mt-2 font-medium transition-colors",
                     isActive ? "text-blue-600" : 
                     isCompleted ? "text-green-600" : "text-gray-400"
                   )}>{step.label}</span>
                 </div>
               );
             })}
          </div>
          {/* Progress Bar Background (Simple approximation) */}
          <div className="h-1 bg-gray-200 mt-[-30px] mb-[26px] mx-10 relative z-0 hidden md:block" />
        </div>

        <div className="p-8 max-w-2xl mx-auto min-h-[400px]">
          {state.matches('created') && (
            <form onSubmit={handleSubmit} className="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Customer Name</label>
                <input 
                  type="text" 
                  className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all"
                  placeholder="e.g. John Doe"
                  value={form.name}
                  onChange={e => setForm({...form, name: e.target.value})}
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input 
                  type="email" 
                  className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all"
                  placeholder="john@example.com"
                  value={form.email}
                  onChange={e => setForm({...form, email: e.target.value})}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Order Item</label>
                <input 
                  type="text" 
                  className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all"
                  placeholder="e.g. Wireless Headphones"
                  value={form.item}
                  onChange={e => setForm({...form, item: e.target.value})}
                  required
                />
              </div>
              <button 
                type="submit"
                className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 rounded-lg shadow-md transition-all active:scale-[0.98]"
              >
                Create Order
              </button>
            </form>
          )}

          {state.matches('validating') && (
            <div className="flex flex-col items-center justify-center h-full space-y-4">
              <RefreshCw className="animate-spin text-blue-500" size={48} />
              <h3 className="text-xl font-semibold">Validating Order...</h3>
              <p className="text-gray-500">Checking inventory and customer credit.</p>
            </div>
          )}

          {state.matches('paymentPending') && (
            <div className="space-y-6 text-center animate-in zoom-in duration-300">
               <div className="bg-yellow-50 text-yellow-800 p-4 rounded-lg flex items-center justify-center gap-2">
                 <AlertCircle size={20}/> Payment Required
               </div>
               <p>Total: $129.00</p>
               <div className="flex gap-4 justify-center">
                 <button 
                   onClick={() => send({ type: 'PAYMENT_SUCCESS' })}
                   className="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg font-medium transition-colors"
                 >
                   Simulate Success
                 </button>
                 <button 
                   onClick={() => send({ type: 'PAYMENT_FAILED', error: 'Insufficient Funds' })}
                   className="bg-red-100 hover:bg-red-200 text-red-700 px-6 py-2 rounded-lg font-medium transition-colors"
                 >
                   Simulate Failure
                 </button>
               </div>
            </div>
          )}
          
          {state.matches('processing') && (
            <div className="flex flex-col items-center justify-center h-full space-y-4">
              <Package className="animate-bounce text-blue-500" size={48} />
              <h3 className="text-xl font-semibold">Processing Order</h3>
              <p className="text-gray-500">Warehouse is picking your items.</p>
            </div>
          )}

          {state.matches('shipped') && (
            <div className="space-y-6 text-center animate-in slide-in-from-right duration-500">
               <div className="bg-blue-50 text-blue-800 p-6 rounded-xl">
                 <Truck className="mx-auto mb-4" size={48} />
                 <h3 className="text-2xl font-bold mb-2">Order Shipped!</h3>
                 <p className="text-gray-600 mb-4">Tracking ID: 1Z999AA10123456784</p>
                 <button 
                   onClick={() => send({ type: 'DELIVER' })}
                   className="bg-blue-600 text-white px-6 py-2 rounded-lg"
                 >
                   Simulate Delivery
                 </button>
               </div>
            </div>
          )}

          {state.matches('delivered') && (
             <div className="flex flex-col items-center justify-center h-full space-y-6 animate-in zoom-in duration-500">
               <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center">
                 <CheckCircle className="text-green-600" size={40} />
               </div>
               <h2 className="text-3xl font-bold text-gray-900">Delivered</h2>
               <p className="text-gray-500 text-center max-w-sm">
                 Package was left at the front porch. Signed by: J. Doe
               </p>
               <button 
                 onClick={() => send({ type: 'RESET' })}
                 className="text-blue-600 font-medium hover:underline"
               >
                 Start New Order
               </button>
             </div>
          )}

           {isCancelled && (
             <div className="flex flex-col items-center justify-center h-full space-y-6">
               <div className="w-20 h-20 bg-red-100 rounded-full flex items-center justify-center">
                 <XCircle className="text-red-600" size={40} />
               </div>
               <h2 className="text-3xl font-bold text-gray-900">Order Cancelled</h2>
               <p className="text-gray-500 text-center">
                 {state.context.error ? `Error: ${state.context.error}` : 'Request by user.'}
               </p>
               <button 
                 onClick={() => send({ type: 'RESET' })}
                 className="bg-gray-900 text-white px-6 py-2 rounded-lg"
               >
                 Start Over
               </button>
             </div>
          )}
        </div>

        {/* Dev Tools Footer */}
        <div className="bg-gray-100 p-4 text-xs font-mono text-gray-500 border-t border-gray-200">
          <div className="flex justify-between items-center">
             <span>State: {state.value.toString()}</span>
             <button onClick={() => send({ type: 'CANCEL', reason: 'Dev Force' })} className="hover:text-red-600">
               Force Cancel
             </button>
          </div>
        </div>
      </div>
    </div>
  );
};
