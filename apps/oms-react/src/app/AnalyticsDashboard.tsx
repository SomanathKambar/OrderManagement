import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line } from 'recharts';

const dataPerformance = [
  { name: 'Mon', accuracy: 94, target: 95 },
  { name: 'Tue', accuracy: 96, target: 95 },
  { name: 'Wed', accuracy: 92, target: 95 },
  { name: 'Thu', accuracy: 98, target: 95 },
  { name: 'Fri', accuracy: 95, target: 95 },
  { name: 'Sat', accuracy: 99, target: 95 },
  { name: 'Sun', accuracy: 97, target: 95 },
];

const dataLeadTime = [
  { time: '09:00', avgMins: 45 },
  { time: '10:00', avgMins: 52 },
  { time: '11:00', avgMins: 48 },
  { time: '12:00', avgMins: 65 }, // Peak
  { time: '13:00', avgMins: 60 },
  { time: '14:00', avgMins: 40 },
  { time: '15:00', avgMins: 35 },
];

export const AnalyticsDashboard: React.FC = () => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8 max-w-4xl mx-auto">
      {/* 1. Picking Accuracy */}
      <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-200">
        <h3 className="text-sm font-bold text-slate-500 uppercase tracking-wide mb-4">Picking Accuracy (%)</h3>
        <div className="h-64">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={dataPerformance}>
              <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e2e8f0" />
              <XAxis dataKey="name" axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 12}} />
              <YAxis axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 12}} domain={[80, 100]} />
              <Tooltip cursor={{fill: '#f1f5f9'}} contentStyle={{borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)'}} />
              <Bar dataKey="accuracy" fill="#3b82f6" radius={[4, 4, 0, 0]} barSize={32} />
              <Bar dataKey="target" fill="#cbd5e1" radius={[4, 4, 0, 0]} barSize={32} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* 2. Lead Time Trend */}
      <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-200">
        <h3 className="text-sm font-bold text-slate-500 uppercase tracking-wide mb-4">Avg Lead Time (Mins)</h3>
        <div className="h-64">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={dataLeadTime}>
              <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e2e8f0" />
              <XAxis dataKey="time" axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 12}} />
              <YAxis axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 12}} />
              <Tooltip contentStyle={{borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)'}} />
              <Line type="monotone" dataKey="avgMins" stroke="#8b5cf6" strokeWidth={3} dot={{r: 4, fill: '#8b5cf6', strokeWidth: 2, stroke: '#fff'}} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};
