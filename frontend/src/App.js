import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import Header from './components/layout/Header';
import Navigation from './components/layout/Navigation';
import Footer from './components/layout/Footer';
import Dashboard from './components/pages/Dashboard';
import AddProduct from './components/pages/AddProduct';
import ProductList from './components/pages/ProductList';
import Reports from './components/pages/Reports';
import Upload from './components/pages/Upload';
import About from './components/pages/About';

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <Navigation />
        
        <main className="container mt-4">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/products/add" element={<AddProduct />} />
            <Route path="/products/list" element={<ProductList />} />
            <Route path="/reports" element={<Reports showToast={(message, type) => {
              if (type === 'error') {
                window.alert(message);
              } else {
                window.alert(message);
              }
            }} />} />
            <Route path="/upload" element={<Upload showToast={(message, type) => {
              if (type === 'error') {
                window.alert(message);
              } else {
                window.alert(message);
              }
            }} />} />
            <Route path="/about" element={<About />} />
          </Routes>
        </main>
        
        <Footer />
        <ToastContainer
          position="top-right"
          autoClose={3000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
        />
      </div>
    </Router>
  );
}

export default App;
