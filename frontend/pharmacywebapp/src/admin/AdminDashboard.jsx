import React from 'react';
import { useOrders } from '../contexts/OrderContext';
import { CheckCircle, XCircle } from 'lucide-react';

export default function AdminDashboard() {
  const { orders, updateOrderStatus } = useOrders();

  return (
    <div style={{ padding: '20px 40px', maxWidth: '1000px', margin: '0 auto' }}>
      <h2 style={{ marginBottom: '32px' }}>Pharmacist Dashboard</h2>
      <p style={{ color: 'var(--text-muted)', marginBottom: '24px' }}>Review and validate incoming orders.</p>

      {orders.length === 0 ? (
        <div className="glass" style={{ padding: '40px', textAlign: 'center' }}>
          <p style={{ color: 'var(--text-muted)' }}>No orders in the system.</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {orders.map(order => (
            <div key={order.id} className="glass" style={{ padding: '24px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', borderBottom: '1px solid var(--border)', paddingBottom: '16px', marginBottom: '16px' }}>
                <div>
                  <h4 style={{ margin: '0 0 8px 0' }}>Order #{order.id}</h4>
                  <span style={{ fontSize: '0.9em', color: 'var(--text-muted)' }}>{new Date(order.date).toLocaleDateString()}</span>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <span style={{ 
                    display: 'inline-block', 
                    padding: '4px 12px', 
                    borderRadius: '16px', 
                    fontSize: '0.8em', 
                    fontWeight: 'bold',
                    background: order.status === 'Validated' ? 'rgba(34, 197, 94, 0.2)' : order.status === 'Rejected' ? 'rgba(239, 68, 68, 0.2)' : 'rgba(234, 179, 8, 0.2)',
                    color: order.status === 'Validated' ? '#4ade80' : order.status === 'Rejected' ? '#ef4444' : '#facc15'
                  }}>
                    {order.status}
                  </span>
                  <div style={{ marginTop: '8px', fontWeight: 'bold' }}>${order.total.toFixed(2)}</div>
                </div>
              </div>
              
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <div>
                  <h5 style={{ margin: '0 0 8px 0', color: 'var(--text-muted)' }}>Items Ordered:</h5>
                  <ul style={{ margin: 0, paddingLeft: '20px', fontSize: '0.9em' }}>
                    {order.items.map((item, idx) => (
                      <li key={idx}>{item.name} x {item.quantity}</li>
                    ))}
                  </ul>
                </div>
                
                {order.status === 'Pending Validation' && (
                  <div style={{ display: 'flex', gap: '12px' }}>
                    <button 
                      onClick={() => updateOrderStatus(order.id, 'Validated')}
                      className="btn-primary" 
                      style={{ background: '#22c55e', display: 'flex', alignItems: 'center', gap: '8px' }}
                    >
                      <CheckCircle size={18} /> Approve
                    </button>
                    <button 
                      onClick={() => updateOrderStatus(order.id, 'Rejected')}
                      className="btn-primary" 
                      style={{ background: '#ef4444', display: 'flex', alignItems: 'center', gap: '8px' }}
                    >
                      <XCircle size={18} /> Reject
                    </button>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
