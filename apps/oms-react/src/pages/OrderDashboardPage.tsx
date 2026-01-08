import React, { useState } from 'react';
import { useMachine } from '@xstate/react';
import { orderMachine } from '../machines/orderMachine';
import { CheckCircle, Truck, Package, CreditCard, XCircle, AlertTriangle, RefreshCcw } from 'lucide-react';
import { Button, Card, Badge } from '../components/ui/primitives';
import { AnalyticsDashboard } from '../app/AnalyticsDashboard';

export const OrderDashboardPage: React.FC = () => {
  const [state, send] = useMachine(orderMachine);
  const [orderIdInput, setOrderIdInput] = useState('ORD-2026-001');

  // Helper to check state recursively or simply
  const isMatch = (val: string) => state.matches(val) || (typeof state.value === 'object' && state.value.Active === val);

  return (
    <div className="space-y-6">
      
      {/* Analytics Section */}
      <section>
          <h2 className="text-xl font-bold text-slate-800 mb-4">Performance Overview</h2>
          <AnalyticsDashboard />
      </section>

      {/* State Machine Demo */}
      <section>
          <h2 className="text-xl font-bold text-slate-800 mb-4">Live Order Simulation</h2>
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
              
              {/* 1. Visualization */}
              <Card className="lg:col-span-2 p-6">
                <div className="flex justify-between items-center mb-8">
                    <h3 className="font-semibold text-slate-700">Order Lifecycle</h3>
                    <Badge variant={isMatch('Cancelled') ? 'destructive' : isMatch('Delivered') ? 'success' : 'default'}>
                        {JSON.stringify(state.value).replace(/["{}]/g, '').replace('Active:', '')}
                    </Badge>
                </div>

                <div className="relative flex justify-between items-center px-4">
                  {/* Connector Line */}
                  <div className="absolute left-0 top-1/2 w-full h-1 bg-slate-100 -z-10 transform -translate-y-1/2"></div>
                  
                  {['Created', 'Validating', 'PaymentPending', 'Processing', 'Shipped', 'Delivered'].map((step) => {
                    const active = isMatch(step);
                    // Determine completed state vaguely for demo
                    // If we are at 'Shipped', then 'Created', 'Validating' etc are done.
                    const steps = ['Created', 'Validating', 'PaymentPending', 'Processing', 'Shipped', 'Delivered'];
                    const currentIndex = steps.findIndex(s => isMatch(s));
                    const stepIndex = steps.indexOf(step);
                    const completed = currentIndex > stepIndex;

                    return (
                        <div key={step} className={`flex flex-col items-center bg-white px-2 transition-all duration-500 ${active ? 'scale-110' : 'opacity-70'}`}>
                           <div className={`w-12 h-12 rounded-full flex items-center justify-center border-2 mb-2 shadow-sm ${
                               active ? 'border-blue-500 bg-blue-50 text-blue-600' : 
                               completed ? 'border-green-500 bg-green-50 text-green-600' :
                               'border-slate-200 bg-white text-slate-300' 
                           }`}>
                               {step === 'Created' && <Package size={20} />}
                               {step === 'Validating' && <AlertTriangle size={20} />}
                               {step === 'PaymentPending' && <CreditCard size={20} />}
                               {step === 'Processing' && <Package size={20} />}
                               {step === 'Shipped' && <Truck size={20} />}
                               {step === 'Delivered' && <CheckCircle size={20} />}
                           </div>
                           <span className={`text-xs font-medium ${active ? 'text-blue-800' : 'text-slate-400'}`}>{step}</span>
                        </div>
                    );
                  })}
                </div>
                
                {isMatch('Cancelled') && (
                    <div className="mt-8 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700 flex items-center animate-pulse">
                        <XCircle className="mr-2" />
                        <div>
                            <span className="font-bold">Order Cancelled</span>
                            <p className="text-sm">Reason: {state.context.cancellationReason || state.context.rejectionReason || 'Unknown'}</p>
                        </div>
                    </div>
                )}
              </Card>

              {/* 2. Controls */}
              <Card className="p-6 bg-slate-50/50">
                <h3 className="font-semibold text-slate-700 mb-4 flex items-center gap-2">
                    <SettingsIcon /> Control Panel
                </h3>
                
                <div className="space-y-3">
                    {isMatch('Created') && (
                        <>
                        <div className="flex gap-2">
                            <input 
                                type="text" 
                                value={orderIdInput} 
                                onChange={(e) => setOrderIdInput(e.target.value)}
                                className="border border-slate-300 p-2 rounded text-sm w-full"
                                placeholder="Order ID"
                            />
                        </div>
                        <Button className="w-full" onClick={() => send({ type: 'CREATE_ORDER', orderId: orderIdInput, items: [] })}>
                            Initialize Order
                        </Button>
                        <Button variant="secondary" className="w-full" onClick={() => send({ type: 'SUBMIT_VALIDATION' })}>
                            Submit for Validation
                        </Button>
                        </>
                    )}

                    {isMatch('Validating') && (
                        <div className="grid grid-cols-2 gap-2">
                            <Button className="bg-green-600 hover:bg-green-700" onClick={() => send({ type: 'VALIDATION_PASSED' })}>Pass</Button>
                            <Button variant="destructive" onClick={() => send({ type: 'VALIDATION_FAILED', reason: 'Invalid Address' })}>Fail</Button>
                        </div>
                    )}

                    {isMatch('PaymentPending') && (
                         <div className="grid grid-cols-2 gap-2">
                            <Button className="bg-green-600 hover:bg-green-700" onClick={() => send({ type: 'PAYMENT_AUTHORIZED' })}>Authorize</Button>
                            <Button variant="destructive" onClick={() => send({ type: 'PAYMENT_REJECTED', reason: 'Insufficient Funds' })}>Reject</Button>
                        </div>
                    )}

                    {isMatch('Processing') && (
                        <Button className="w-full bg-purple-600 hover:bg-purple-700" onClick={() => send({ type: 'SHIP_ORDER' })}>Ship Order</Button>
                    )}

                    {isMatch('Shipped') && (
                        <Button className="w-full bg-teal-600 hover:bg-teal-700" onClick={() => send({ type: 'DELIVER_ORDER' })}>Confirm Delivery</Button>
                    )}
                    
                    {!isMatch('Cancelled') && !isMatch('Delivered') && (
                         <Button variant="outline" className="w-full mt-4 text-slate-500 hover:text-red-600 hover:bg-red-50" onClick={() => send({ type: 'CANCEL_ORDER', reason: 'Admin Manual Cancel' })}>
                            Force Cancel
                        </Button>
                    )}

                    {(isMatch('Cancelled') || isMatch('Delivered')) && (
                         <Button variant="secondary" className="w-full mt-4" onClick={() => window.location.reload()}>
                            <RefreshCcw size={16} className="mr-2" /> Reset Demo
                        </Button>
                    )}
                </div>
                
                <div className="mt-6 pt-4 border-t border-slate-200 text-[10px] font-mono text-slate-400 break-all">
                    Context: {JSON.stringify(state.context)}
                </div>
              </Card>
          </div>
      </section>
    </div>
  );
};

const SettingsIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.1a2 2 0 0 1-1-1.72v-.51a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z"/><circle cx="12" cy="12" r="3"/></svg>
)
