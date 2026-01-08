import React, { useEffect, useState } from 'react';
import { Card, Badge, Button } from '../components/ui/primitives';
import { orderApi, OrderResponse } from '../api/orderApi';
import { Link } from 'react-router-dom';
import { Eye, Plus, Filter, Download } from 'lucide-react';

export const OrderList = () => {
    const [orders, setOrders] = useState<OrderResponse[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Fetch orders - mocking the search/list endpoint usage
        // Since we don't have a dedicated list endpoint typed in the API client yet properly
        // I will assume orderApi.search or similar exists, or mock it for now if not implemented fully.
        // Wait, I implemented a 'get' by ID but not a search list in orderApi.ts.
        // I should update orderApi.ts to include list fetching.
        // For now, I'll inline a fetch call or update api.
        fetchOrders();
    }, []);

    const fetchOrders = async () => {
        try {
            const res = await fetch('/api/v1/orders?page=0&size=20');
            const data = await res.json();
            // Spring Page response: { content: [], ... }
            if (data.content) {
                setOrders(data.content);
            }
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-7xl mx-auto">
            <div className="flex justify-between items-center mb-6">
                <div>
                    <h1 className="text-2xl font-bold text-slate-800">Orders</h1>
                    <p className="text-slate-500 text-sm">Manage and track customer orders</p>
                </div>
                <div className="flex gap-2">
                    <Button variant="outline"><Filter size={16} className="mr-2"/> Filter</Button>
                    <Button variant="outline"><Download size={16} className="mr-2"/> Export</Button>
                    <Link to="/orders/new">
                        <Button><Plus size={16} className="mr-2"/> Create Order</Button>
                    </Link>
                </div>
            </div>

            <Card className="overflow-hidden">
                <table className="w-full text-sm text-left">
                    <thead className="bg-slate-50 border-b border-slate-200 text-slate-500 font-medium">
                        <tr>
                            <th className="px-6 py-3">Order ID</th>
                            <th className="px-6 py-3">Customer</th>
                            <th className="px-6 py-3">Date</th>
                            <th className="px-6 py-3">Items</th>
                            <th className="px-6 py-3">Total</th>
                            <th className="px-6 py-3">Status</th>
                            <th className="px-6 py-3 text-right">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                        {loading ? (
                            <tr><td colSpan={7} className="text-center py-8 text-slate-500">Loading orders...</td></tr>
                        ) : orders.length === 0 ? (
                            <tr><td colSpan={7} className="text-center py-8 text-slate-500">No orders found.</td></tr>
                        ) : (
                            orders.map((order: any) => (
                                <tr key={order.id} className="hover:bg-slate-50 transition-colors">
                                    <td className="px-6 py-4 font-medium text-slate-900">#{order.id}</td>
                                    <td className="px-6 py-4">
                                        <div className="font-medium text-slate-900">{order.customerName || 'Guest'}</div>
                                        <div className="text-xs text-slate-500">{order.customerId}</div>
                                    </td>
                                    <td className="px-6 py-4 text-slate-500">
                                        {new Date(order.occurredAt || Date.now()).toLocaleDateString()}
                                    </td>
                                    <td className="px-6 py-4 text-slate-500">{order.items?.length || 0} items</td>
                                    <td className="px-6 py-4 font-medium">$ {(order.totalAmount || 0).toFixed(2)}</td>
                                    <td className="px-6 py-4">
                                        <StatusBadge status={order.status} />
                                    </td>
                                    <td className="px-6 py-4 text-right">
                                        <Link to={`/orders/${order.id}`}>
                                            <Button variant="ghost" className="h-8 w-8 p-0"><Eye size={16} /></Button>
                                        </Link>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </Card>
        </div>
    );
};

const StatusBadge = ({ status }: { status: string }) => {
    let variant: 'default' | 'success' | 'warning' | 'destructive' | 'outline' = 'default';
    if (['DELIVERED', 'COMPLETED'].includes(status)) variant = 'success';
    else if (['CANCELLED', 'REJECTED'].includes(status)) variant = 'destructive';
    else if (['VALIDATING', 'PAYMENT_PENDING', 'PROCESSING'].includes(status)) variant = 'warning';
    
    return <Badge variant={variant}>{status}</Badge>;
}
