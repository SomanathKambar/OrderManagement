import React from 'react';
import { Outlet, NavLink } from 'react-router-dom';
import { LayoutDashboard, ShoppingCart, Truck, Settings, Bell, Search, Menu } from 'lucide-react';

export const MainLayout = () => {
  return (
    <div className="min-h-screen bg-slate-50 flex font-sans">
      {/* Sidebar */}
      <aside className="w-64 bg-slate-900 text-slate-300 flex-shrink-0 hidden md:flex flex-col">
        <div className="p-6 border-b border-slate-800">
          <h1 className="text-xl font-bold text-white flex items-center gap-2">
            <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center text-white">
                <Truck size={18} />
            </div>
            OMS One
          </h1>
        </div>
        
        <nav className="flex-1 p-4 space-y-1">
          <NavItem to="/" icon={<LayoutDashboard size={20} />} label="Dashboard" />
          <NavItem to="/orders" icon={<ShoppingCart size={20} />} label="Orders" />
          <NavItem to="/shipments" icon={<Truck size={20} />} label="Shipments" />
          <NavItem to="/settings" icon={<Settings size={20} />} label="Settings" />
        </nav>

        <div className="p-4 border-t border-slate-800">
            <div className="bg-slate-800 rounded p-3 flex items-center gap-3">
                <div className="w-8 h-8 rounded-full bg-blue-500 flex items-center justify-center text-white font-bold">
                    AD
                </div>
                <div className="text-sm">
                    <div className="text-white font-medium">Admin User</div>
                    <div className="text-xs text-slate-400">admin@oms.com</div>
                </div>
            </div>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 flex flex-col min-w-0">
        <header className="h-16 bg-white border-b border-slate-200 flex items-center justify-between px-6 sticky top-0 z-10">
            <div className="flex items-center gap-4">
                <button className="md:hidden text-slate-500"><Menu /></button>
                <div className="relative hidden sm:block">
                    <Search className="absolute left-2.5 top-2.5 text-slate-400" size={18} />
                    <input 
                        type="text" 
                        placeholder="Search orders, customers..." 
                        className="pl-9 pr-4 py-2 border border-slate-200 rounded-lg text-sm w-64 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
            </div>
            <div className="flex items-center gap-4">
                <button className="relative p-2 text-slate-500 hover:bg-slate-100 rounded-full">
                    <Bell size={20} />
                    <span className="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full"></span>
                </button>
            </div>
        </header>

        <div className="flex-1 p-6 overflow-auto">
            <Outlet />
        </div>
      </main>
    </div>
  );
};

const NavItem = ({ to, icon, label }: { to: string, icon: React.ReactNode, label: string }) => (
  <NavLink 
    to={to} 
    className={({ isActive }) => 
      `flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors ${
        isActive 
          ? 'bg-blue-600 text-white shadow-md' 
          : 'hover:bg-slate-800 hover:text-white'
      }`
    }
  >
    {icon}
    {label}
  </NavLink>
);
