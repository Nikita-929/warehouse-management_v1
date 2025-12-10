import React, { useState } from "react";
import api from "../../services/api";

const Upload = ({ showToast }) => {
  const [file, setFile] = useState(null);
  const [isUploading, setIsUploading] = useState(false);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file) {
      showToast("Please select a file to upload", "error");
      return;
    }

    // Check file type
    if (!file.name.match(/\.(xlsx|xls)$/)) {
      showToast("Please select an Excel file (.xlsx or .xls)", "error");
      return;
    }

    // Check file size (5MB limit)
    if (file.size > 5 * 1024 * 1024) {
      showToast("File size must be less than 5MB", "error");
      return;
    }

    setIsUploading(true);

    try {
      const formData = new FormData();
      formData.append("file", file);

      const response = await api.post("/transactions/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      showToast(
        `File uploaded successfully! ${response.data.processed} transactions processed.`,
        "success"
      );
      setFile(null);
      // Reset file input
      document.getElementById("excelFile").value = "";
    } catch (error) {
      console.error("Upload error:", error);
      const errorMessage =
        error.response?.data?.message || "Error uploading file";
      showToast(errorMessage, "error");
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <div>
      <h1 className="mb-4">Upload Excel File</h1>

      <div className="card shadow">
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="excelFile" className="form-label">
                Select Excel File
              </label>
              <input
                type="file"
                className="form-control"
                id="excelFile"
                accept=".xlsx, .xls"
                onChange={handleFileChange}
                disabled={isUploading}
              />
              <div className="form-text">
                Only Excel files (.xlsx, .xls) are allowed. Maximum file size:
                5MB
              </div>
            </div>

            <button
              type="submit"
              className="btn btn-primary"
              disabled={!file || isUploading}
            >
              {isUploading ? (
                <>
                  <span
                    className="spinner-border spinner-border-sm me-2"
                    role="status"
                  ></span>
                  Uploading...
                </>
              ) : (
                <>
                  <i className="fas fa-upload me-2"></i> Upload File
                </>
              )}
            </button>
          </form>
        </div>
      </div>

      <div className="card shadow mt-4">
        <div className="card-header bg-light">
          <h5 className="mb-0">Excel File Format</h5>
        </div>
        <div className="card-body">
          <p>Your Excel file should have the following columns in order:</p>
          <ol>
            <li>Barcode</li>
            <li>Product Code</li>
            <li>Product Name</li>
            <li>Quantity</li>
            <li>Unit</li>
            <li>Batch No</li>
            <li>GRN No</li>
            <li>Material Type (RM/PM/FM)</li>
            <li>Type (IN/OUT)</li>
            <li>Party (Supplier/Client/Floor)</li>
            <li>Date (DD/MM/YYYY format - Indian date system)</li>
          </ol>
          <p className="text-muted">
            Note: The first row should contain headers. Use Indian date format
            (DD/MM/YYYY) in column 11. If no date is provided, the current date
            will be automatically used.
          </p>

          <div className="alert alert-info">
            <h6>Sample Data:</h6>
            <table className="table table-sm table-bordered">
              <thead>
                <tr>
                  <th>Barcode</th>
                  <th>Product Code</th>
                  <th>Product Name</th>
                  <th>Quantity</th>
                  <th>Unit</th>
                  <th>Batch No</th>
                  <th>GRN No</th>
                  <th>Material Type</th>
                  <th>Type</th>
                  <th>Party</th>
                  <th>Date</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>123456789</td>
                  <td>P001</td>
                  <td>Steel Rod</td>
                  <td>10</td>
                  <td>kg</td>
                  <td>B001</td>
                  <td>GRN001</td>
                  <td>RM</td>
                  <td>IN</td>
                  <td>ABC Steel Corp</td>
                  <td>25/10/2024</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Upload;
