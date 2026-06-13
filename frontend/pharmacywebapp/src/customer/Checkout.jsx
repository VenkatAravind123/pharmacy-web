import React, { useState } from 'react';
import { useCart } from '../contexts/CartContext';
import { useAuth } from '../contexts/AuthContext';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { UploadCloud, CheckCircle } from 'lucide-react';

export default function Checkout() {
  const { cart, totalPrice, requiresPrescription, clearCart } = useCart();
  const { user } = useAuth();
  const [file, setFile] = useState(null);
  const [orderPlaced, setOrderPlaced] = useState(false);
  const navigate = useNavigate();

  const handlePlaceOrder = async () => {
    if (requiresPrescription && !file) {
      alert("Please upload a prescription for your required medicines.");
      return;
    }
    if (!user?.token) return alert("Please log in to place an order.");

    try {
      const res = await axios.post('/api/orders', {
        items: cart.map(i => ({ medicineId: i.id, quantity: i.quantity })),
        total: totalPrice
      }, {
        headers: { Authorization: `Bearer ${user.token}` }
      });
      // 2. If a prescription is required, upload it using the new orderId
      if (requiresPrescription && file) {
        const formData = new FormData();
        formData.append('orderId', res.data.orderId);
        formData.append('file', file);
      
        await axios.post('/api/prescriptions/upload', formData, {
          headers: { 
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${user.token}` 
          }
        });
      }
      
      setOrderPlaced(true);
      setTimeout(() => {
        //clearCart();
        navigate('/payment',{ state: { orderId: res.data.orderId, total: totalPrice } });
      }, 3000);
    } catch (err) {
      console.error("Checkout failed", err);
      alert(err.response?.data?.message || "Checkout failed. Please try again.");
    }

  };

  if (orderPlaced) {
    return (
      <div style={{ padding: '80px 40px', textAlign: 'center' }}>
        <CheckCircle size={64} color="var(--primary)" style={{ marginBottom: '24px' }} />
        <h2>Order Placed Successfully!</h2>
        <p style={{ color: 'var(--text-muted)' }}>Redirecting you to the homepage...</p>
      </div>
    );
  }

  return (
    <div style={{ padding: '20px 40px', maxWidth: '600px', margin: '0 auto' }}>
      <h2 style={{ marginBottom: '32px' }}>Checkout</h2>
      
      <div className="glass" style={{ padding: '32px' }}>
        <h3 style={{ marginTop: 0, marginBottom: '24px' }}>Order Summary</h3>
        {cart.map(item => (
          <div key={item.id} style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px', color: 'var(--text-muted)' }}>
            <span>{item.name} x {item.quantity}</span>
            <span>₹{(item.price * item.quantity).toFixed(2)}</span>
          </div>
        ))}
        <div style={{ borderTop: '1px solid var(--border)', margin: '16px 0', paddingTop: '16px', display: 'flex', justifyContent: 'space-between', fontWeight: 'bold' }}>
          <span>Total</span>
          <span style={{ color: 'var(--primary)' }}>₹{totalPrice.toFixed(2)}</span>
        </div>

        {requiresPrescription && (
          <div style={{ marginTop: '32px', background: 'rgba(239, 68, 68, 0.1)', padding: '20px', borderRadius: '12px', border: '1px dashed var(--error)' }}>
            <h4 style={{ color: 'var(--error)', marginTop: 0 }}>Prescription Required</h4>
            <p style={{ fontSize: '0.9em', color: 'var(--text-muted)', marginBottom: '16px' }}>Some items in your cart require a valid prescription.</p>
            
            <label style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '120px', background: 'rgba(255,255,255,0.05)', borderRadius: '8px', cursor: 'pointer' }}>
              <UploadCloud size={32} color="var(--primary)" style={{ marginBottom: '8px' }} />
              <span style={{ color: 'var(--text-muted)', fontSize: '0.9em' }}>
                {file ? file.name : "Click to upload PDF/Image"}
              </span>
              <input type="file" style={{ display: 'none' }} onChange={e => setFile(e.target.files[0])} />
            </label>
          </div>
        )}

        <button className="btn-primary" style={{ width: '100%', marginTop: '32px', padding: '16px' }} onClick={handlePlaceOrder}>
          Confirm & Place Order
        </button>
      </div>
    </div>
  );
}
