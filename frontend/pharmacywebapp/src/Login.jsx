import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from './contexts/AuthContext';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const user = await login(email, password);
      if(user && user.role === 'ADMIN') {
        navigate('/admin/dashboard');
      } else {
        navigate('/catalog');
      }
    } catch (err) {
      alert("Login failed! Please check your credentials and make sure the backend is running.");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card glass">
        <h2>Welcome Back</h2>
        <p>Sign in to your account</p>
        <form onSubmit={handleLogin}>
          <input 
            type="email" 
            placeholder="Email address" 
            className="input-field"
            value={email}
            onChange={e => setEmail(e.target.value)}
            required
          />
          <input 
            type="password" 
            placeholder="Password" 
            className="input-field"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
          />
          <button type="submit" className="btn-primary" style={{width: '100%'}}>
            Sign In
          </button>
        </form>
        <p style={{marginTop: '20px', marginBottom: 0, fontSize: '0.9em'}}>
          Don't have an account? <Link to="/signup" className="nav-link">Sign up</Link>
        </p>
      </div>
    </div>
  );
}
