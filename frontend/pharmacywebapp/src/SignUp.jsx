import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from './contexts/AuthContext';

export default function SignUp() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');
  const { registerUser } = useAuth();
  const navigate = useNavigate();

  const handleSignUp = async (e) => {
    e.preventDefault();
    try {
      await registerUser(name, email, password, phone);
      alert("Registration successful! Please log in.");
      navigate('/login');
    } catch (err) {
      alert("Registration failed. Please make sure the backend is running and phone is 10 digits.");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card glass">
        <h2>Create Account</h2>
        <p>Join our pharmacy platform</p>
        <form onSubmit={handleSignUp}>
          <input 
            type="text" 
            placeholder="Full Name" 
            className="input-field"
            value={name}
            onChange={e => setName(e.target.value)}
            required
          />
          <input 
            type="email" 
            placeholder="Email address" 
            className="input-field"
            value={email}
            onChange={e => setEmail(e.target.value)}
            required
          />
          <input 
            type="tel" 
            placeholder="Phone Number (10 digits)" 
            className="input-field"
            value={phone}
            onChange={e => setPhone(e.target.value)}
            pattern="[0-9]{10}"
            required
          />
          <input 
            type="password" 
            placeholder="Password (min 6 chars)" 
            className="input-field"
            value={password}
            onChange={e => setPassword(e.target.value)}
            minLength={6}
            required
          />
          <button type="submit" className="btn-primary" style={{width: '100%'}}>
            Create Account
          </button>
        </form>
        <p style={{marginTop: '20px', marginBottom: 0, fontSize: '0.9em'}}>
          Already have an account? <Link to="/login" className="nav-link">Sign in</Link>
        </p>
      </div>
    </div>
  );
}
