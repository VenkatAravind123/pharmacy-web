import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ShoppingCart, LogOut, User as UserIcon } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const { totalItems } = useCart();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="glass" style={{ display: 'flex', justifyContent: 'space-between', padding: '16px 40px', margin: '20px', alignItems: 'center' }}>
      <Link to="/" style={{ textDecoration: 'none', color: 'var(--primary)', fontSize: '1.5em', fontWeight: 'bold' }}>
        PharmaCare
      </Link>

      <div style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
        <Link to="/catalog" className="nav-link" style={{textDecoration:'none'}}>Browse Medicines</Link>
        
        {user ? (
          <>
            {user.role === 'ADMIN' && (
              <Link to="/admin/dashboard" className="nav-link" style={{ color: '#facc15',textDecoration:'none' }}>Admin Dashboard</Link>
            )}
            <Link to="/orders" className="nav-link" style={{textDecoration:'none'}}>My Orders</Link>
            <Link to="/cart" style={{ position: 'relative', color: 'white',textDecoration:'none' }}>
              <ShoppingCart size={24} />
              {totalItems > 0 && (
                <span style={{ position: 'absolute', top: '-8px', right: '-8px', background: 'var(--error)', color: 'white', borderRadius: '50%', padding: '2px 6px', fontSize: '0.7em', fontWeight: 'bold' }}>
                  {totalItems}
                </span>
              )}
            </Link>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--text-muted)' }}>
              <UserIcon size={20} />
              <span>{user.name}</span>
            </div>
            <button onClick={handleLogout} style={{ background: 'none', border: 'none', color: 'var(--text-muted)', cursor: 'pointer', display: 'flex', alignItems: 'center' }}>
              <LogOut size={20} />
            </button>
          </>
        ) : (
          <>
            <Link to="/login" className="btn-primary" style={{ background: 'transparent', border: '1px solid var(--border)', color: 'var(--text-main)' }}>Log In</Link>
            <Link to="/signup" className="btn-primary">Sign Up</Link>
          </>
        )}
      </div>
    </nav>
  );
}
