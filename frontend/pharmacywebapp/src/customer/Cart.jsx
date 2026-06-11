import React from 'react';
import { useCart } from '../contexts/CartContext';
import { Link, useNavigate } from 'react-router-dom';
import { Trash2, Minus, Plus } from 'lucide-react';

export default function Cart() {
  const { cart, removeFromCart, updateQuantity, totalPrice } = useCart();
  const navigate = useNavigate();

  if (cart.length === 0) {
    return (
      <div style={{ padding: '60px 40px', textAlign: 'center' }}>
        <h2>Your Cart is Empty</h2>
        <p style={{ color: 'var(--text-muted)', marginBottom: '24px' }}>Browse our catalog to add medicines.</p>
        <Link to="/catalog" className="btn-primary">Browse Catalog</Link>
      </div>
    );
  }

  return (
    <div style={{ padding: '20px 40px', maxWidth: '800px', margin: '0 auto' }}>
      <h2 style={{ marginBottom: '32px' }}>Shopping Cart</h2>
      
      <div className="glass" style={{ padding: '24px' }}>
        {cart.map(item => (
          <div key={item.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', paddingBottom: '16px', borderBottom: '1px solid var(--border)', marginBottom: '16px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
              <img src={item.image} alt={item.name} style={{ width: '60px', height: '60px', borderRadius: '8px', objectFit: 'cover' }} />
              <div>
                <h4 style={{ margin: '0 0 4px 0' }}>{item.name}</h4>
                <div style={{ fontSize: '0.9em', color: 'var(--text-muted)' }}>${item.price.toFixed(2)}</div>
              </div>
            </div>
            
            <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px', background: 'rgba(0,0,0,0.2)', padding: '4px 8px', borderRadius: '8px' }}>
                <button onClick={() => updateQuantity(item.id, -1)} style={{ background: 'none', border: 'none', color: 'white', cursor: 'pointer' }}><Minus size={16} /></button>
                <span>{item.quantity}</span>
                <button onClick={() => updateQuantity(item.id, 1)} style={{ background: 'none', border: 'none', color: 'white', cursor: 'pointer' }}><Plus size={16} /></button>
              </div>
              
              <div style={{ fontWeight: 'bold', width: '80px', textAlign: 'right' }}>
                ${(item.price * item.quantity).toFixed(2)}
              </div>
              
              <button onClick={() => removeFromCart(item.id)} style={{ background: 'none', border: 'none', color: 'var(--error)', cursor: 'pointer' }}>
                <Trash2 size={20} />
              </button>
            </div>
          </div>
        ))}
        
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '32px' }}>
          <h3>Total</h3>
          <h3 style={{ color: 'var(--primary)' }}>${totalPrice.toFixed(2)}</h3>
        </div>
        
        <div style={{ textAlign: 'right', marginTop: '24px' }}>
          <button className="btn-primary" onClick={() => navigate('/checkout')}>Proceed to Checkout</button>
        </div>
      </div>
    </div>
  );
}
