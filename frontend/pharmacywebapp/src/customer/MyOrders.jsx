import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useAuth } from '../contexts/AuthContext';

export default function MyOrders() {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    if (user?.token) {
      axios.get('/api/orders/my-orders', {
        headers: { Authorization: `Bearer ${user.token}` }
      })
      .then(res => setOrders(res.data))
      .catch(err => console.error("Failed to fetch orders", err));
      console.log(orders);
    }
  }, [user]);

  return (
    <div style={{ padding: '20px 40px', maxWidth: '800px', margin: '0 auto' }}>
      <h2 style={{ marginBottom: '32px' }}>My Orders</h2>
      
      {orders.length === 0 ? (
        <div className="glass" style={{ padding: '40px', textAlign: 'center' }}>
          <p style={{ color: 'var(--text-muted)' }}>You haven't placed any orders yet.</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {orders.map(order => (
            <div key={order.orderId} className="glass" style={{ padding: '24px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', borderBottom: '1px solid var(--border)', paddingBottom: '16px', marginBottom: '16px' }}>
                <div>
                  <h4 style={{ margin: '0 0 8px 0' }}>Order #{order.orderId}</h4>
                  <span style={{ fontSize: '0.9em', color: 'var(--text-muted)' }}>{new Date(order.createdAt).toLocaleDateString()}</span>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <span style={{ 
                    display: 'inline-block', 
                    padding: '4px 12px', 
                    borderRadius: '16px', 
                    fontSize: '0.8em', 
                    fontWeight: 'bold',
                    background: order.status === 'APPROVED' ? 'rgba(34, 197, 94, 0.2)' : 'rgba(234, 179, 8, 0.2)',
                    color: order.status === 'APPROVED' ? '#4ade80' : '#fa1515ff'
                  }}>
                    {order.status}
                  </span>
                  <div style={{ marginTop: '8px', fontWeight: 'bold' }}>₹{order.totalAmount.toFixed(2)}</div>
                </div>
              </div>
              <div>
                <h5 style={{ margin: '0 0 8px 0', color: 'var(--text-muted)' }}>Items</h5>
                {order.items ? (
                  <ul style={{ margin: 0, paddingLeft: '20px', fontSize: '0.9em' }}>
                    {order.items.map((item, idx) => (
                      <li key={idx}>{item.medicineName} x {item.quantity}</li>
                    ))}
                  </ul>
                ) : (
                  <p style={{ margin: 0, fontSize: '0.9em', color: 'var(--text-muted)', fontStyle: 'italic' }}>
                    No items
                  </p>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
