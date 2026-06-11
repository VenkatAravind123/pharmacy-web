import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  // Check for existing token in cookie on app load
  useEffect(() => {
    const token = Cookies.get('token');
    if (token) {
      try {
        const decoded = jwtDecode(token);
        const userEmail = decoded.sub || decoded.email || '';
        // Try to extract role from JWT claims, fallback to email heuristic if missing
        const roleStr = decoded.role || decoded.roles || (userEmail.includes('admin') ? 'ADMIN' : 'CUSTOMER');
        
        setUser({
          email: userEmail,
          name: userEmail.split('@')[0],
          role: roleStr,
          token: token
        });
      } catch (err) {
        console.error("Invalid token on load", err);
        Cookies.remove('token');
      }
    }
  }, []);

  const login = async (email, password) => {
    try {
      const response = await axios.post('/api/auth/login', { email, password });
      const token = response.data.token;
      
      const decoded = jwtDecode(token);
      const userEmail = decoded.sub || email;
      const roleStr = decoded.role || decoded.roles || (userEmail.includes('admin') ? 'ADMIN' : 'CUSTOMER');

      const loggedUser = {
        email: userEmail,
        name: userEmail.split('@')[0],
        role: roleStr,
        token: token
      };
      
      setUser(loggedUser);
      // Save token in cookie, expires in 7 days
      Cookies.set('token', token, { expires: 7, secure: true, sameSite: 'strict' }); 
      return loggedUser;
    } catch (error) {
      console.error("Login failed", error);
      throw error;
    }
  };

  const registerUser = async (name, email, password, phone) => {
    try {
      await axios.post('/api/auth/register', { name, email, password, phone });
    } catch (error) {
      console.error("Registration failed", error);
      throw error;
    }
  };

  const logout = () => {
    setUser(null);
    Cookies.remove('token');
  };

  return (
    <AuthContext.Provider value={{ user, login, registerUser, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
