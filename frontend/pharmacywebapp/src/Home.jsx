import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function Home() {
  const navigate = useNavigate();

  return (
    <div style={{ padding: '40px', maxWidth: '1200px', margin: '0 auto' }}>
      <section className="glass" style={{ padding: '80px 40px', textAlign: 'center', borderRadius: '24px' }}>
        <h2 style={{ fontSize: '3em', margin: '0 0 24px 0', background: 'linear-gradient(to right, #60a5fa, #3b82f6)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
          Your Health, Delivered Seamlessly
        </h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '1.2em', maxWidth: '600px', margin: '0 auto 40px', lineHeight: '1.6' }}>
          Browse our extensive catalog of medicines, securely upload your prescriptions, and get healthcare essentials delivered to your doorstep.
        </p>
        <div style={{ display: 'flex', gap: '20px', justifyContent: 'center' }}>
          <button className="btn-primary" style={{ fontSize: '1.1em', padding: '14px 32px', borderRadius: '12px' }} onClick={() => navigate('/catalog')}>
            Browse Catalog
          </button>
        </div>
      </section>
    </div>
  );
}
