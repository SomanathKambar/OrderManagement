import React, { useState } from 'react';
import { Card, Button } from '../components/ui/primitives';
import { useNavigate } from 'react-router-dom';
import { orderApi } from '../api/orderApi';
import { Check, ChevronRight, ShoppingBag, User, MapPin } from 'lucide-react';

const steps = [
    { id: 1, name: 'Customer', icon: User },
    { id: 2, name: 'Items', icon: ShoppingBag },
    { id: 3, name: 'Delivery', icon: MapPin },
];

export const OrderWizard = () => {
    const navigate = useNavigate();
    const [currentStep, setCurrentStep] = useState(1);
    const [formData, setFormData] = useState({
        customerId: 'cust-001',
        customerName: 'Alice Smith',
        restaurantId: 'rest-001',
        items: [] as any[],
        deliveryAddress: '123 Tech Park, Innovation Blvd',
    });

    const handleNext = () => {
        if (currentStep < 3) setCurrentStep(c => c + 1);
        else handleSubmit();
    };

    const handleSubmit = async () => {
        try {
            // Mapping form data to API request
            await orderApi.create({
                ...formData,
                restaurantName: 'Pizza Factory',
                orderType: 'DELIVERY',
                restaurantAddress: 'Downtown Kitchen',
                items: [{ menuItemId: 'item-1', name: 'Margherita Pizza', quantity: 2, price: 12.50 }], // Hardcoded item for demo simplicity
                specialInstructions: 'Created via Wizard'
            });
            navigate('/orders');
        } catch (e) {
            alert('Failed to create order');
        }
    };

    return (
        <div className="max-w-3xl mx-auto py-8">
            <h1 className="text-2xl font-bold mb-8 text-center text-slate-800">New Order Wizard</h1>
            
            {/* Stepper */}
            <div className="flex items-center justify-between relative mb-12 px-10">
                <div className="absolute left-0 top-1/2 w-full h-0.5 bg-slate-200 -z-10"></div>
                {steps.map((step) => {
                    const isActive = step.id === currentStep;
                    const isCompleted = step.id < currentStep;
                    return (
                        <div key={step.id} className="flex flex-col items-center bg-slate-50 px-2">
                            <div className={`w-10 h-10 rounded-full flex items-center justify-center border-2 transition-colors ${
                                isActive ? 'border-blue-600 bg-blue-600 text-white' : 
                                isCompleted ? 'border-green-500 bg-green-500 text-white' : 
                                'border-slate-300 bg-white text-slate-400'
                            }`}>
                                {isCompleted ? <Check size={20} /> : <step.icon size={20} />}
                            </div>
                            <span className={`text-sm mt-2 font-medium ${isActive ? 'text-blue-600' : 'text-slate-500'}`}>{step.name}</span>
                        </div>
                    );
                })}
            </div>

            <Card className="p-8 min-h-[400px] flex flex-col">
                <div className="flex-1">
                    {currentStep === 1 && (
                        <div className="space-y-4 animate-in fade-in slide-in-from-right-4 duration-300">
                            <h2 className="text-xl font-semibold mb-4">Customer Details</h2>
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-slate-700 mb-1">Customer ID</label>
                                    <input type="text" className="w-full border p-2 rounded" value={formData.customerId} onChange={e => setFormData({...formData, customerId: e.target.value})} />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-slate-700 mb-1">Name</label>
                                    <input type="text" className="w-full border p-2 rounded" value={formData.customerName} onChange={e => setFormData({...formData, customerName: e.target.value})} />
                                </div>
                            </div>
                        </div>
                    )}
                    
                    {currentStep === 2 && (
                        <div className="space-y-4 animate-in fade-in slide-in-from-right-4 duration-300">
                            <h2 className="text-xl font-semibold mb-4">Add Items</h2>
                            <div className="p-4 border border-dashed rounded-lg text-center text-slate-500 bg-slate-50">
                                <ShoppingBag className="mx-auto mb-2 opacity-50" />
                                <p>Item selection mocked for demo (2x Pizza Margherita)</p>
                            </div>
                        </div>
                    )}

                    {currentStep === 3 && (
                        <div className="space-y-4 animate-in fade-in slide-in-from-right-4 duration-300">
                            <h2 className="text-xl font-semibold mb-4">Delivery Info</h2>
                            <div>
                                <label className="block text-sm font-medium text-slate-700 mb-1">Address</label>
                                <textarea className="w-full border p-2 rounded" rows={3} value={formData.deliveryAddress} onChange={e => setFormData({...formData, deliveryAddress: e.target.value})} />
                            </div>
                        </div>
                    )}
                </div>

                <div className="flex justify-between mt-8 pt-4 border-t border-slate-100">
                    <Button variant="outline" onClick={() => setCurrentStep(c => Math.max(1, c - 1))} disabled={currentStep === 1}>Back</Button>
                    <Button onClick={handleNext}>
                        {currentStep === 3 ? 'Place Order' : 'Next Step'} 
                        {currentStep < 3 && <ChevronRight size={16} className="ml-2" />}
                    </Button>
                </div>
            </Card>
        </div>
    );
};
