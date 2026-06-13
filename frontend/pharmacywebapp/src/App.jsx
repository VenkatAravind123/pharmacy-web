import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './Home';
import Login from './Login';
import SignUp from './SignUp';
import Navbar from './components/Navbar';
import MedicineCatalog from './customer/MedicineCatalog';
import Cart from './customer/Cart';
import Checkout from './customer/Checkout';
import MyOrders from './customer/MyOrders';
import AdminDashboard from './admin/AdminDashboard';
import './index.css';
import Payment from './customer/Payment';


function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/catalog" element={<MedicineCatalog />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/checkout" element={<Checkout />} />
        <Route path="/orders" element={<MyOrders />} />
        <Route path="/payment" element={<Payment/>}/>
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
      </Routes>
    </Router>
  );
}

export default App;
