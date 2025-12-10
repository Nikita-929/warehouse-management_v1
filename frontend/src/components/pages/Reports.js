import React, { useState, useEffect, useRef } from "react";
import api from "../../services/api";

// Helper function to format date in Indian system (DD/MM/YYYY)
const formatIndianDate = (dateStr, withTime = false) => {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  return date.toLocaleString("en-IN", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    ...(withTime && {
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      hour12: false,
    }),
  });
};

export default function Reports({ showToast }) {
  const [transactions, setTransactions] = useState([]);
  const [filteredTransactions, setFilteredTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({
    fromDate: "",
    toDate: "",
    typeFilter: "all",
  });

  // Fetch transactions from backend (guard against React 18 StrictMode double-invoke)
  const hasFetched = useRef(false);
  useEffect(() => {
    if (hasFetched.current) return;
    hasFetched.current = true;
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      setLoading(true);
      console.log("Fetching transactions from API...");
      const response = await api.get("/transactions");
      console.log("API Response:", response); // Debug log

      // Handle ApiResponse wrapper - data is in response.data
      const transactionsData = response.data || response;
      console.log("Transactions Data:", transactionsData); // Debug log
      console.log("Is Array:", Array.isArray(transactionsData)); // Debug log

      // Ensure we have an array
      if (Array.isArray(transactionsData)) {
        console.log("Setting transactions:", transactionsData.length, "items");
        setTransactions(transactionsData);
        setFilteredTransactions(transactionsData);
        showToast(`Loaded ${transactionsData.length} transactions`, "success");
      } else {
        console.error("Transactions data is not an array:", transactionsData);
        showToast("Error: Invalid data format received", "error");
        setTransactions([]);
        setFilteredTransactions([]);
      }
    } catch (error) {
      console.error("Error fetching transactions:", error);
      showToast(`Error loading transactions: ${error.message}`, "error");
      setTransactions([]);
      setFilteredTransactions([]);
    } finally {
      setLoading(false);
    }
  };

  // Handle filter form input
  const handleChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  // Apply filters
  const applyFilters = (e) => {
    e.preventDefault();

    // Ensure transactions is an array
    if (!Array.isArray(transactions)) {
      console.error("Transactions is not an array:", transactions);
      showToast("Error: Invalid data format", "error");
      return;
    }

    const filtered = transactions.filter((t) => {
      const createdAt = new Date(t.createdAt);
      const from = filters.fromDate ? new Date(filters.fromDate) : null;
      const to = filters.toDate ? new Date(filters.toDate) : null;

      const matchDate =
        (!from || createdAt >= from) && (!to || createdAt <= to);
      const matchType =
        filters.typeFilter === "all" ||
        t.type.toLowerCase() === filters.typeFilter;

      return matchDate && matchType;
    });

    setFilteredTransactions(filtered);
  };

  // Export to Excel
  const exportToExcel = () => {
    // Ensure filteredTransactions is an array
    if (!Array.isArray(filteredTransactions)) {
      console.error(
        "FilteredTransactions is not an array:",
        filteredTransactions
      );
      showToast("Error: Invalid data format for export", "error");
      return;
    }

    // Create CSV content
    const headers = [
      "Barcode",
      "Product Code",
      "Product Name",
      "Quantity",
      "Unit",
      "Batch No",
      "GRN No",
      "Material Type",
      "Type",
      "Party",
      "Date",
    ];

    const csvContent = [
      headers.join(","),
      ...filteredTransactions.map((t) =>
        [
          t.barcode || "",
          t.productCode || "",
          t.productName || "",
          t.quantity || "",
          t.unit || "",
          t.batchNo || "",
          t.grnNo || "",
          t.materialType || "",
          t.type || "",
          t.party || "",
          formatIndianDate(t.createdAt, false),
        ].join(",")
      ),
    ].join("\n");

    // Create and download file
    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    const link = document.createElement("a");
    const url = URL.createObjectURL(blob);
    link.setAttribute("href", url);
    link.setAttribute(
      "download",
      `transactions_${new Date().toISOString().split("T")[0]}.csv`
    );
    link.style.visibility = "hidden";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    showToast("Transactions exported successfully", "success");
  };

  return (
    <div className="container-fluid">
      <h1 className="mt-4">Transaction Reports</h1>
      <p className="text-muted mb-4">
        <i className="fas fa-calendar-alt me-1"></i>
        All dates are displayed in Indian format (DD/MM/YYYY)
      </p>

      {/* Filter Form */}
      <div className="card mb-4">
        <div className="card-header">
          <i className="fas fa-filter me-1"></i> Filter Transactions
        </div>
        <div className="card-body">
          <form onSubmit={applyFilters}>
            <div className="row">
              <div className="col-md-3">
                <label htmlFor="fromDate">From Date (DD/MM/YYYY)</label>
                <input
                  type="date"
                  className="form-control"
                  id="fromDate"
                  name="fromDate"
                  value={filters.fromDate}
                  onChange={handleChange}
                />
                {filters.fromDate && (
                  <small className="text-muted">
                    Selected: {formatIndianDate(filters.fromDate)}
                  </small>
                )}
              </div>
              <div className="col-md-3">
                <label htmlFor="toDate">To Date (DD/MM/YYYY)</label>
                <input
                  type="date"
                  className="form-control"
                  id="toDate"
                  name="toDate"
                  value={filters.toDate}
                  onChange={handleChange}
                />
                {filters.toDate && (
                  <small className="text-muted">
                    Selected: {formatIndianDate(filters.toDate)}
                  </small>
                )}
              </div>
              <div className="col-md-4">
                <label htmlFor="typeFilter">Filter Type</label>
                <select
                  className="form-control"
                  id="typeFilter"
                  name="typeFilter"
                  value={filters.typeFilter}
                  onChange={handleChange}
                >
                  <option value="all">All Transactions</option>
                  <option value="in">IN Transactions Only</option>
                  <option value="out">OUT Transactions Only</option>
                </select>
              </div>
              <div className="col-md-2 d-flex align-items-end">
                <button type="submit" className="btn btn-primary w-100">
                  Apply Filters
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>

      {/* Action Buttons */}
      <div className="row mb-4">
        <div className="col-md-12">
          <div className="btn-group" role="group">
            <button
              className="btn btn-success"
              onClick={exportToExcel}
              disabled={filteredTransactions.length === 0}
            >
              <i className="fas fa-file-excel me-1"></i> Export to Excel
            </button>
          </div>
        </div>
      </div>

      {/* Reports Table */}
      <div className="card mb-4">
        <div className="card-header">
          <i className="fas fa-table me-1"></i> Transaction Records
          {loading && <span className="float-end">Loading...</span>}
        </div>
        <div className="card-body">
          <div className="table-responsive">
            <table
              className="table table-bordered"
              id="dataTable"
              width="100%"
              cellSpacing="0"
            >
              <thead>
                <tr>
                  <th>Barcode</th>
                  <th>Product Code</th>
                  <th>Product Name</th>
                  <th>Qty</th>
                  <th>Unit</th>
                  <th>Batch No</th>
                  <th>GRN No</th>
                  <th>RM/PM/FM</th>
                  <th>Type</th>
                  <th>Supplier/Client/Floor</th>
                  <th>Date</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr>
                    <td colSpan="11" className="text-center">
                      <div className="spinner-border" role="status">
                        <span className="visually-hidden">Loading...</span>
                      </div>
                    </td>
                  </tr>
                ) : Array.isArray(filteredTransactions) &&
                  filteredTransactions.length > 0 ? (
                  filteredTransactions.map((t, index) => (
                    <tr key={index}>
                      <td>{t.barcode}</td>
                      <td>{t.productCode}</td>
                      <td>{t.productName}</td>
                      <td>{t.quantity}</td>
                      <td>{t.unit}</td>
                      <td>{t.batchNo}</td>
                      <td>{t.grnNo}</td>
                      <td>{t.materialType}</td>
                      <td>
                        <span
                          className={`badge ${
                            t.type === "IN" ? "bg-success" : "bg-danger"
                          }`}
                        >
                          {t.type}
                        </span>
                      </td>
                      <td>{t.party}</td>
                      <td>{formatIndianDate(t.createdAt, false)}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="11" className="text-center">
                      No transactions found for the selected filters
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
