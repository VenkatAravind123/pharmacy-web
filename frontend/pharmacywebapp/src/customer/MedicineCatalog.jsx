import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useCart } from '../contexts/CartContext';
import { Plus } from 'lucide-react';

export default function MedicineCatalog() {
  const [selectedCat, setSelectedCat] = useState('All');
  const [search, setSearch] = useState('');
  const [categories, setCategories] = useState([]);
  const [medicines, setMedicines] = useState([]);
  const { addToCart } = useCart();

  useEffect(() => {
    axios.get('/api/categories')
      .then(res => setCategories(res.data))
      .catch(err => console.error("Failed to fetch categories", err));
      
    axios.get('/api/medicines')
      .then(res => setMedicines(res.data))
      .catch(err => console.error("Failed to fetch medicines", err));
  }, []);

  const filtered = medicines.filter(med => {
    const matchCat = selectedCat === 'All' || med.categoryName === selectedCat;
    const matchSearch = med.name.toLowerCase().includes(search.toLowerCase());
    return matchCat && matchSearch;
  });

  return (
    <div style={{ padding: '20px 40px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '32px' }}>
        <h2>Medicine Catalog</h2>
        <input 
          type="text" 
          placeholder="Search medicines..." 
          className="input-field" 
          style={{ width: '300px', marginBottom: 0 }}
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
      </div>

      <div style={{ display: 'flex', gap: '12px', marginBottom: '32px', overflowX: 'auto' }}>
        <button 
          className="btn-primary" 
          style={{ background: selectedCat === 'All' ? 'var(--primary)' : 'rgba(255,255,255,0.1)' }}
          onClick={() => setSelectedCat('All')}
        >
          All
        </button>
        {categories.map(cat => (
          <button 
            key={cat.id} 
            className="btn-primary" 
            style={{ background: selectedCat === cat.name ? 'var(--primary)' : 'rgba(255,255,255,0.1)' }}
            onClick={() => setSelectedCat(cat.name)}
          >
            {cat.name}
          </button>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '24px' }}>
        {filtered.map(med => (
          <div key={med.id} className="glass" style={{ padding: '20px', display: 'flex', flexDirection: 'column' }}>
            <div style={{ width: '100%', height: '180px', backgroundColor: 'var(--primary)', opacity: 0.8, borderRadius: '8px', marginBottom: '16px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'white', fontWeight: 'bold' }}>
              No Image
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <h3 style={{ margin: '0 0 8px 0' }}>{med.name}</h3>
              <span style={{ fontWeight: 'bold', color: 'var(--primary)' }}>₹{med.price.toFixed(2)}</span>
            </div>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9em', margin: '0 0 16px 0', flexGrow: 1 }}>{med.description}</p>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.8em', color: 'var(--text-muted)', marginBottom: '16px' }}>
              <span>{med.dosage}</span>
              <span>{med.manufacturer}</span>
            </div>
            {med.requiresPrescription && (
              <span style={{ fontSize: '0.8em', color: 'var(--error)', marginBottom: '12px', display: 'inline-block' }}>* Prescription Required</span>
            )}
            <button className="btn-primary" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px', width: '100%' }} onClick={() => addToCart(med)}>
              <Plus size={16} /> Add to Cart
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
