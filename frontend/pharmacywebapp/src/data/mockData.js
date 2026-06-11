export const mockMedicines = [
  {
    id: 1,
    name: 'Amoxicillin 500mg',
    category: 'Antibiotics',
    description: 'Used to treat a wide variety of bacterial infections.',
    dosage: '500mg',
    packaging: '10 Capsules / Strip',
    price: 12.99,
    requires_prescription: true,
    image: 'https://images.unsplash.com/photo-1584308666744-24d5e1cc2616?auto=format&fit=crop&w=300&q=80'
  },
  {
    id: 2,
    name: 'Ibuprofen 400mg',
    category: 'Pain Relief',
    description: 'Nonsteroidal anti-inflammatory drug used for treating pain, fever, and inflammation.',
    dosage: '400mg',
    packaging: '15 Tablets / Strip',
    price: 8.50,
    requires_prescription: false,
    image: 'https://images.unsplash.com/photo-1550572017-edb799988220?auto=format&fit=crop&w=300&q=80'
  },
  {
    id: 3,
    name: 'Lisinopril 10mg',
    category: 'Blood Pressure',
    description: 'ACE inhibitor used to treat high blood pressure and heart failure.',
    dosage: '10mg',
    packaging: '30 Tablets / Bottle',
    price: 15.00,
    requires_prescription: true,
    image: 'https://images.unsplash.com/photo-1471864190281-a93a3070b6de?auto=format&fit=crop&w=300&q=80'
  },
  {
    id: 4,
    name: 'Cetirizine 10mg',
    category: 'Allergy',
    description: 'Antihistamine that relieves allergy symptoms.',
    dosage: '10mg',
    packaging: '20 Tablets / Strip',
    price: 6.99,
    requires_prescription: false,
    image: 'https://images.unsplash.com/photo-1628771065518-0d82f1938462?auto=format&fit=crop&w=300&q=80'
  }
];

export const mockCategories = [
  'Antibiotics',
  'Pain Relief',
  'Blood Pressure',
  'Allergy',
  'Vitamins'
];
