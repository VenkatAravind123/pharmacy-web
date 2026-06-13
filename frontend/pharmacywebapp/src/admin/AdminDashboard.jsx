import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { CheckCircle, XCircle, Package, Layers, Pill, ClipboardList } from 'lucide-react';
import ManageCategories from './ManageCategories';
import ManageMedicines from './ManageMedicines';
import ManageInventory from './ManageInventory';
import axios from 'axios';

export default function AdminDashboard() {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);
  const [activeTab, setActiveTab] = useState('Orders');
  const [prescriptionUrl, setPrescriptionUrl] = useState(null);

  useEffect(() => {
    if (user?.token && activeTab === 'Orders') {
      fetchOrders();
    }
  }, [user, activeTab]);

  const fetchOrders = () => {
    axios.get('/api/orders', { headers: { Authorization: `Bearer ${user.token}` } })
      .then(res => setOrders(res.data))
      .catch(err => console.error("Failed to fetch admin orders", err));
  };

  const updateOrderStatus = async (orderId, newStatus) => {
    try {
      await axios.put(`/api/orders/${orderId}/status?status=${newStatus}`, {}, {
        headers: { Authorization: `Bearer ${user.token}` }
      });
      fetchOrders();
    } catch (err) {
      console.error("Failed to update status", err);
      alert("Failed to update status");
    }
  };

  const viewPrescription = async (prescriptionId) => {
    try {
      const res = await axios.get(`/api/prescriptions/${prescriptionId}/download`, {
        headers: { Authorization: `Bearer ${user.token}` },
        responseType: 'blob'
      });
      const url = URL.createObjectURL(res.data);
      setPrescriptionUrl(url);
    } catch (err) {
      console.error("Failed to load prescription", err);
      alert("Failed to load prescription image");
    }
  };

  const pendingOrders = orders.filter(o => o.status === 'PENDING');

  const renderTabContent = () => {
    switch (activeTab) {
      case 'Categories': return <ManageCategories />;
      case 'Medicines': return <ManageMedicines />;
      case 'Inventory': return <ManageInventory />;
      case 'Orders':
      default:
        return (
          <>
            <h3 style={{ marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <CheckCircle size={20} /> Pending Orders
            </h3>
            {pendingOrders.length === 0 ? (
              <div className="glass" style={{ padding: '40px', textAlign: 'center' }}>
                <p style={{ color: 'var(--text-muted)' }}>No pending orders require validation.</p>
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                {pendingOrders.map(order => (
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
                          background: 'rgba(234, 179, 8, 0.2)',
                          color: '#facc15'
                        }}>
                          {order.status}
                        </span>
                        <div style={{ marginTop: '8px', fontWeight: 'bold' }}>₹{order.totalAmount.toFixed(2)}</div>
                      </div>
                    </div>
                    
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                      <div>
                        <h5 style={{ margin: '0 0 8px 0', color: 'var(--text-muted)' }}>Items Ordered:</h5>
                        {order.items && order.items.length > 0 ? (
                          <ul style={{ margin: 0, paddingLeft: '20px', fontSize: '0.9em' }}>
                            {order.items.map((item, idx) => (
                              <li key={idx}>{item.medicineName} x {item.quantity}</li>
                            ))}
                          </ul>
                        ) : (
                          <p style={{ margin: 0, fontSize: '0.9em', color: 'var(--text-muted)', fontStyle: 'italic' }}>
                            (No items found)
                          </p>
                        )}
                      </div>
                      
                      <div style={{ display: 'flex', gap: '12px' }}>
                        {order.prescriptionId && (
                          <button 
                            onClick={() => viewPrescription(order.prescriptionId)}
                            className="btn-secondary" 
                            style={{ display: 'flex', alignItems: 'center', gap: '8px' }}
                          >
                            <ClipboardList size={18} /> View Prescription
                          </button>
                        )}
                        <button 
                          onClick={() => updateOrderStatus(order.orderId, 'APPROVED')}
                          className="btn-primary" 
                          style={{ background: '#22c55e', display: 'flex', alignItems: 'center', gap: '8px' }}
                        >
                          <CheckCircle size={18} /> Approve
                        </button>
                        <button 
                          onClick={() => updateOrderStatus(order.orderId, 'REJECTED')}
                          className="btn-primary" 
                          style={{ background: '#ef4444', display: 'flex', alignItems: 'center', gap: '8px' }}
                        >
                          <XCircle size={18} /> Reject
                        </button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </>
        );
    }
  };

  return (
    <div style={{ padding: '20px 40px', maxWidth: '1200px', margin: '0 auto' }}>
      {prescriptionUrl && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.8)', zIndex: 1000, display: 'flex', justifyContent: 'center', alignItems: 'center' }} onClick={() => setPrescriptionUrl(null)}>
          <img src={prescriptionUrl} style={{ maxWidth: '90%', maxHeight: '90%', objectFit: 'contain', borderRadius: '8px' }} alt="Prescription" />
        </div>
      )}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px' }}>
        <h2>Pharmacist Dashboard</h2>
        <div style={{ display: 'flex', gap: '12px', background: 'rgba(255,255,255,0.05)', padding: '6px', borderRadius: '12px' }}>
          {[
            { id: 'Orders', icon: ClipboardList },
            { id: 'Categories', icon: Layers },
            { id: 'Medicines', icon: Pill },
            { id: 'Inventory', icon: Package }
          ].map(tab => {
            const Icon = tab.icon;
            return (
              <button
                key={tab.id}
                className="btn-primary"
                style={{ 
                  background: activeTab === tab.id ? 'var(--primary)' : 'transparent',
                  display: 'flex', alignItems: 'center', gap: '8px', padding: '8px 16px'
                }}
                onClick={() => setActiveTab(tab.id)}
              >
                <Icon size={16} /> {tab.id}
              </button>
            )
          })}
        </div>
      </div>
      
      {renderTabContent()}
    </div>
  );
}
