import React, { createContext, useContext, useState } from 'react';

const OrderContext = createContext();

export function OrderProvider({ children }) {
  const [orders, setOrders] = useState([
    {
      id: 101,
      date: new Date().toISOString(),
      status: 'Validated',
      total: 25.99,
      items: [{ name: 'Amoxicillin 500mg', quantity: 2 }]
    },
    {
      id: 102,
      date: new Date(Date.now() - 86400000).toISOString(),
      status: 'Pending Validation',
      total: 15.00,
      items: [{ name: 'Lisinopril 10mg', quantity: 1 }]
    }
  ]);

  const addOrder = (order) => {
    setOrders(prev => [{ ...order, id: Math.floor(Math.random() * 9000) + 1000 }, ...prev]);
  };

  const updateOrderStatus = (id, newStatus) => {
    setOrders(prev => prev.map(order => order.id === id ? { ...order, status: newStatus } : order));
  };

  return (
    <OrderContext.Provider value={{ orders, addOrder, updateOrderStatus }}>
      {children}
    </OrderContext.Provider>
  );
}

export const useOrders = () => useContext(OrderContext);
