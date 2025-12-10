import axios from "axios";

const API_BASE_URL = "/api";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor
api.interceptors.request.use(
  (config) => config,
  (error) => Promise.reject(error)
);

// Response interceptor
api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const message =
      error.response?.data?.message || error.message || "An error occurred";
    try { error.message = message; } catch {}
    return Promise.reject(error);
  }
);

export const productService = {
  getAllProducts: () => api.get("/products"),
  getProductById: (id) => api.get(`/products/${id}`),
  addProduct: (productData) => api.post("/products", productData),
  deleteProduct: (id) => api.delete(`/products/${id}`),
  searchProducts: (searchTerm) =>
    api.get(`/products/search?q=${encodeURIComponent(searchTerm)}`),
  filterByMaterialType: (materialType) =>
    api.get(`/products/filter?materialType=${materialType}`),
  getProductCodeSuggestions: (term) =>
    api.get(`/products/autocomplete/product-code?term=${encodeURIComponent(term)}`),
  getProductNameSuggestions: (term) =>
    api.get(`/products/autocomplete/product-name?term=${encodeURIComponent(term)}`),
  getUnitSuggestions: (term) =>
    api.get(`/products/autocomplete/unit?term=${encodeURIComponent(term)}`),
  getBatchNoSuggestions: (term) =>
    api.get(`/products/autocomplete/batch-no?term=${encodeURIComponent(term)}`),
  getGrnNoSuggestions: (term) =>
    api.get(`/products/autocomplete/grn-no?term=${encodeURIComponent(term)}`),
  getSalesInvoiceNoSuggestions: (term) =>
    api.get(`/products/autocomplete/sales-invoice-no?term=${encodeURIComponent(term)}`),
  getSourceSuggestions: (term) =>
    api.get(`/products/autocomplete/source?term=${encodeURIComponent(term)}`),
  lookupByProductName: (name) =>
    api.get(`/products/lookup/by-name?name=${encodeURIComponent(name)}`),
  lookupByProductCode: (code) =>
    api.get(`/products/lookup/by-code?code=${encodeURIComponent(code)}`),
};

export const homeService = {
  getHealth: () => api.get("/health"),
};

export default api;
